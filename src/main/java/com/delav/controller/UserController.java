package com.delav.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.delav.entity.UserBean;
import com.delav.service.UserService;
import com.delav.activemq.ActiveMQSender;
import com.delav.util.DeferredCache;
import com.delav.util.JsonUtil;


/**
 * @author Delav
 * @date   2019/06/27
 */
@RestController
@RequestMapping("/User")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private ActiveMQSender jmsTemplate;
	@Autowired
	private DeferredCache deferredCache;
	
	@RequestMapping(value= {"getUser/{id}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public String getUser(@PathVariable int id) throws JsonProcessingException {
		String resultString = null;
		List<String> ubStringList = new ArrayList<String>();
		logger.info("查询数据ID为: {} ", id);
		UserBean user = new UserBean();
		logger.info("数据转为实体bean成功");
		user.setId(id); 
		if (id == 0) {
			List<UserBean> resultAll = userService.All(user);
			if (!resultAll.isEmpty()) {
				for (UserBean ub : resultAll) {
					ubStringList.add(JsonUtil.object2JsonStr(ub));
				}
				resultString = ubStringList.toString();
			}

		} else {
			UserBean result = (UserBean) userService.Sel(user);
			resultString = JsonUtil.object2JsonStr(result);
		}
		logger.info("查询数据成功, 查询结果为: {}", resultString);
		return resultString;
	}

	@RequestMapping(value= {"postUser"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public DeferredResult<String> postUser(@RequestBody String params) throws IOException {
		logger.info("接收到的数据为: {}", params);
		DeferredResult<String> deferredResult = new DeferredResult<String>(3000L);
		UserBean user = new UserBean();
		user = JsonUtil.jsonStr2Obj(params, UserBean.class);
		logger.info("数据转为实体bean成功");
		this.jmsTemplate.sendMsg("mq-1", params);
		logger.info("发送消息到队列成功");
		deferredResult.onTimeout(new Runnable() {
			@Override
			public void run() {
				DeferredResult<String> deferredResult = new DeferredResult<String>();
				String timeOutMsg = "{\"resultCode\": \"408\", \"resultDesc\": \"超时\"}";
				logger.info("响应超时");
				deferredResult.setResult(timeOutMsg);
				logger.info("返回超时成功");
			}
		});
		this.deferredCache.put(user.getUserName(), deferredResult);
		logger.info("缓存deferredResult完成");
		return deferredResult;
		/*  
		 * Integer result = userService.Ins(user); 
		 * if (result != 0) {
		 * logger.info("数据入库成功"); 
		 * 		return "Commit Success"; 
		 * } else {
		 * 		logger.info("数据入库失败"); 
		 * 		return "Commit Fail"; 
		 * }
		 */
		
	}
}
