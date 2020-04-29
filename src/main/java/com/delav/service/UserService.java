package com.delav.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListeners;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

import com.delav.entity.UserBean;
import com.delav.mapper.UserMapper;
import com.delav.util.JsonUtil;
import com.delav.util.DeferredCache;

/**
 * @author Delav
 * @date   2019/06/27
 */
@Service
public class UserService {
	
	// 日志器
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	UserMapper userMapper;
	@Autowired
	private DeferredCache deferredResultCache;
	
	public UserBean Sel(UserBean params) {
		logger.info("开始查询数据");
		UserBean result = userMapper.select(params);
		return result;
	}
	
	public List<UserBean> All(UserBean params) {
		logger.info("开始查询数据");
		List<UserBean> resultList = userMapper.selectList(params);
		return resultList;
	}
	
	public void Ins(UserBean params) {
		logger.info("开始提交数据");
		userMapper.insert(params);
		//能获取插入的id是因为UserMapper.xml的insert语句新增了useGeneratedKeys和keyProperty参数
		Integer insertId = params.getId();
		logger.info("插入数据的ID: {}", insertId);
		/*
		 * logger.info("insert结果: {}", result); 
		 * // insert返回结果为 1，表示插入了一条数据 
		 * if (result ==) { 
		 * 		return insertId; 
		 * } else 
		 * 		{ return 0; }
		 */
	}
	
	@JmsListeners({@org.springframework.jms.annotation.JmsListener(destination="mq-1")})
	public void doService(String jsonStr) throws InterruptedException {
		TimeUnit.SECONDS.sleep(10);
		UserBean user = null;
		try {
			user = JsonUtil.jsonStr2Obj(jsonStr, UserBean.class);
			logger.info("数据转为实体bean完成");
			Ins(user);
			logger.info("数据入库成功");
	        DeferredResult<String> deferredResult = this.deferredResultCache.get(user.getUserName());
	        if (null == deferredResult) {
	        	logger.info("找不到缓存对象");
	        	return;
	        }
	        logger.info("找到缓存对象");
	        String rspMsg = "提交成功";
	        deferredResult.setResult(rspMsg);
	        logger.info("发送响应完成");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("响应异常");
			e.printStackTrace();
		}
		
	}
}






















