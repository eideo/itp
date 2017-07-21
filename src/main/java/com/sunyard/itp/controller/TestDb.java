/**
 * 
 */
package com.sunyard.itp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sunyard.itp.entity.Test;
import com.sunyard.itp.service.TestService;

/**
 * @Description:test
 * @author:huam.zhou
 * @date:2017年7月17日 下午3:08:26 
 */
@Controller
public class TestDb {
	@Autowired
	private TestService TestService;
	
	@RequestMapping("/test")
	public String test(){
		Test t = new Test();
		t.setId("125");
		t.setName("黄志鑫");
		t.setBirthday("2016-01-01");
		
		
		TestService.add(t);
		System.out.println("dd");
		return "success";
	}

}
