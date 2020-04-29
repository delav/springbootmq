package com.delav;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages="com.delav.mapper")
public class springbootmqApplication {

	public static void main(String[] args) {
		SpringApplication.run(springbootmqApplication.class, args);
	}

}
