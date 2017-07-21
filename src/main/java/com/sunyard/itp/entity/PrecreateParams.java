package com.sunyard.itp.entity;

/**
 * 被扫预下单所需参数
 * @author zhix.huang
 *
 */
public class PrecreateParams {
	private String payFlag;//支付标记  0-支付宝    1-微信
	private String totalFee;//支付金额
	

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getPayFlag() {
		return payFlag;
	}

	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}

	@Override
	public String toString() {
		return "PrecreateParams [payFlag=" + payFlag + ", totalFee=" + totalFee + "]";
	}
	
	
	
}
