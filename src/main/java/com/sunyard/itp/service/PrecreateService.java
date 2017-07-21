package com.sunyard.itp.service;

import com.sunyard.itp.entity.PrecreateParams;

/**
 * 支付业务类
 * @author zhix.huang
 *
 */
public interface PrecreateService {
	String precreate(PrecreateParams payParams) throws Exception;
}
