package com.delav.util;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

@Component
public class DeferredCache {
	private static final Logger logger = LoggerFactory.getLogger(DeferredCache.class);
	// 缓存Map对象
	private Map<String, DeferredResult<String>> resultMap;
	// 创建一个定长为5的线程池，支持定时及周期性任务执行
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
  
	public DeferredCache() {
		this(2000);
	}
  
	public DeferredCache(int capcity) {
		// 创建ConcurrentHashMap对象，ConcurrentHashMap允许多个修改操作并发进行
		this.resultMap = new ConcurrentHashMap<String, DeferredResult<String>>(capcity);
	}
  
	public void put(String key, DeferredResult<String> result) {
		result.onCompletion(new CompletionCallBack(key, this));
		this.resultMap.put(key, result);
		logger.info("结果存入Map完成");
		this.executor.schedule(new TimeoutCleanTask(key, this), 40L, TimeUnit.SECONDS);
		logger.info("缓存 ");
	}
  
	public DeferredResult<String> get(String key) {
		DeferredResult<String> deferredResult = this.resultMap.remove(key);
		if (deferredResult == null) {
			return null;
		}
		return deferredResult;
	}
  
	@PreDestroy
	public void destory() {
		this.resultMap.clear();
		this.resultMap = null;
		this.executor.shutdown();
	}
  
	public Map<String, DeferredResult<String>> getResultMap() {
		return this.resultMap;
	}
  
private class CompletionCallBack implements Runnable {
	String key;
	DeferredCache deferredResultMiniCache;
//	private LogHandler logger = LogHandler.getLogger(CompletionCallBack.class);
    
    public CompletionCallBack(String key, DeferredCache deferredResultMiniCache) {
    	this.key = key;
    	this.deferredResultMiniCache = deferredResultMiniCache;
    }
    
    public void run() {
    	DeferredResult<String> deferredResult = this.deferredResultMiniCache.get(this.key);
    	if (null != deferredResult) {
    		logger.info(String.format("删除无效的deferredResult,Key为：%s", new Object[] { this.key }));
    	}
    }
}	
  
class TimeoutCleanTask implements Callable<String> {
    String key;
    DeferredCache deferredResultMiniCache;
//    private LogHandler logger = LogHandler.getLogger(TimeoutCleanTask.class);
    
    public TimeoutCleanTask(String key, DeferredCache deferredResultMiniCache)
    {
      this.key = key;
      this.deferredResultMiniCache = deferredResultMiniCache;
    }
    
    public String call()
      throws Exception
    {
      DeferredResult<String> deferredResult = this.deferredResultMiniCache.get(this.key);
      if (null != deferredResult) {
        logger.info(String.format("TimeoutCleanTask:删除无效的deferredResult,Key为：%s", new Object[] { this.key }));
      }
      return "Ready!";
    }
  }
}

