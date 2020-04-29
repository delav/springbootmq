package com.delav;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootDemoApplicationTests {
	
	@Autowired
	private WebApplicationContext wac;
	// 模拟对Controller发起请求的对象
	private MockMvc mvc;
	
	@Before
	public void Setup() throws Exception {
		// mvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
		mvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	public void getTest() throws Exception {
		
		  HashMap<String, String> result = new HashMap<String, String>(); 
		  // 请求ID和对应的预期结果
		  result.put("1", "{id=1, userName='aaa', passWord='111', realName='admin'}");
		  result.put("2", "{id=2, userName='bbb', passWord='222', realName='admin'}");
		  result.put("3", "{id=3, userName='ccc', passWord='333', realName='admin'}");
		  result.put("5", "{id=5, userName='tom', passWord='999', realName='delav'}");
		  
		  for (String key: result.keySet()) { 
			  // 执行请求 
			  String path ="/User/getUser/" + key; 
			  System.out.println("请求URL: " + path);
			  mvc.perform(MockMvcRequestBuilders.get(path)
					  		.contentType(MediaType.APPLICATION_JSON_UTF8)
					  		.accept(MediaType.APPLICATION_JSON)
					  ) 
					  // 响应状态 
					  .andExpect(status().isOk()) 
					  // 响应内容
					  .andExpect(content().string(equalTo(result.get(key)))); 
			  }	 
	}
	
	@Test
	public void postTest() throws Exception {
		
		String params = "{\"userName\":\"tom\", \"passWord\":\"999\", \"realName\":\"333\"}";
		
		mvc.perform(MockMvcRequestBuilders.post("/User/postUser")
						.accept(MediaType.APPLICATION_JSON_UTF8)
						.content(params)
				)
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("Commit Success")));
	}
		
}



