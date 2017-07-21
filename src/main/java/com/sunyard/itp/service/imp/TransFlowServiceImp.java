package com.sunyard.itp.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.itp.entity.TransFlow;
import com.sunyard.itp.mapper.TransFlowMapper;
import com.sunyard.itp.service.TransFlowService;



/**
 * 流水业务实现类
 * @author zhix.huang
 *
 */
@Service
public class TransFlowServiceImp implements TransFlowService {
	@Autowired
	private TransFlowMapper transFlowMapper;
	
	@Transactional
	public void addTransFlow(TransFlow transFlow) {
		transFlowMapper.addTransFlow(transFlow);
		
	}

}
