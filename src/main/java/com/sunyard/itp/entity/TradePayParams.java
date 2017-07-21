package com.sunyard.itp.entity;

import java.math.BigDecimal;

/**
 * 主扫条码支付参数
 * @author zhix.huang
 *
 */
public class TradePayParams {
	//授权码
	private String authCode;
	//订单金额
	private BigDecimal totalFee;
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public BigDecimal getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}
	public TradePayParams(String authCode, BigDecimal totalFee) {
		super();
		this.authCode = authCode;
		this.totalFee = totalFee;
	}
	public TradePayParams() {
		super();
	}
	@Override
	public String toString() {
		return "TradePayParams [authCode=" + authCode + ", totalFee=" + totalFee + "]";
	}
	
	
}
