package com.delav.controller;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Delav
 * @date   2019/06/27
 */
@RestController
@RequestMapping("/File")
public class FileController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(value= {"uploadFile"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public String uploadFile(@RequestParam("fileName") MultipartFile file) {
		
		if (file.isEmpty()) {
			return "文件为空";
		}
		String fileName = file.getOriginalFilename();
		logger.info("上传文件名为: {}", fileName);
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
	    logger.info("文件类型为: {}", suffixName);
		Integer fileSize = (int) file.getSize();
		logger.info("文件大小为: {}", fileSize);
		
		Date dateTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		File path = null;
		try {
			path = new File(ResourceUtils.getURL("classpath:").getPath());
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
        if (path == null || !path.exists()) {
            path = new File("");
        }
		
		String filePath = path.getParentFile().getParent() + File.separator + "file" + 
														File.separator + sdf.format(dateTime) + File.separator;
		logger.info("文件上传目录为: {}", filePath);
		File destFile = new File(filePath + fileName);
		System.out.println("00000: " + destFile.getParentFile());
		if (!destFile.getParentFile().exists()) {
			destFile.getParentFile().mkdirs();
			logger.info("创建目录成功", filePath);
	    }
		if (destFile.exists()) {
			String first = fileName.substring(0,fileName.indexOf("."));
			destFile = new File(filePath + first + "(1)" + suffixName);
			logger.info("文件已存在，更改文件名成功");
		}
		try {
			file.transferTo(destFile);
			logger.info("文件上传成功");
			return "File Upload Successful";
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("文件上传失败");
		return "File Upload Fail";
	}
	
	@RequestMapping(value= {"downloadFile/{name}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public String downloadFile(HttpServletResponse response, @PathVariable String name) throws Exception {
		Date dateTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		File path = null;
		try {
			path = new File(ResourceUtils.getURL("classpath:").getPath());
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
        if (path == null || !path.exists()) {
            path = new File("");
        }
		
		String filePath = path.getParentFile().getParent() + File.separator + "file" + 
														File.separator + sdf.format(dateTime) + File.separator;
		logger.info("文件下载目录为: {}", filePath);
		logger.info("下载的文件名为: {}", name);
		File file = new File(filePath + name);
		if (file.exists()) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;fileName="+java.net.URLEncoder.encode(name,"UTF-8"));
			logger.info("开始下载文件");
			byte[] buffer = new byte[1024];
			OutputStream out = null;
			FileInputStream in = null;
			int i = 0;
			try {
				out = response.getOutputStream();
				in = new FileInputStream(file);
				while ((i=in.read(buffer)) > 0) {
					out.write(buffer, 0, i);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				in.close();
				// 这个不关闭的话，下载的txt文本内容不全，不知为啥
				out.close();
			}
			logger.info("文件下载成功");
			return "File Download Successful";
		}
		logger.info("文件不存在");
		return "File Download Fail";
	}
}

















