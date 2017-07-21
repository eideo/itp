package com.sunyard.itp.service;
import com.alipay.api.AlipayApiException;
import com.sunyard.itp.entity.TradePayParams;
/**
 * 主扫业务类
 * @author zhix.huang
 *
 */
public interface TradePayService {
	String tradePay(TradePayParams tradePayparams) throws AlipayApiException, Exception;
}
