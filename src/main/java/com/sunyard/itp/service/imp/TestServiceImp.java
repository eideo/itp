/**
 * 
 */
package com.sunyard.itp.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunyard.itp.entity.Test;
import com.sunyard.itp.mapper.TestMapper;
import com.sunyard.itp.service.TestService;

/**
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author:huam.zhou
 * @date:2017年7月17日 下午4:45:05 
 */
@Service
public class TestServiceImp implements TestService{
	
	@Autowired
	private TestMapper TestMapper;
	@Override
	public int add(Test t) {
		return TestMapper.addTest(t);
	}

}
