package com.sunyard.itp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sunyard.itp.entity.PrecreateParams;
import com.sunyard.itp.entity.TradePayParams;
import com.sunyard.itp.service.PrecreateService;
import com.sunyard.itp.service.TradePayService;

@Controller
public class PayController {
	@Autowired
	private PrecreateService precreateService;
	@Autowired
	private TradePayService tradePayService;
	
	
	/**
	 * 跳转到测试页面
	 * @return
	 */
	@RequestMapping("/toTest")
	public String toTest(){
		return "test";
	}
	/**
	 * 跳转到支付页面
	 * @return
	 */
	@RequestMapping("/toPay")
	public String toPay(){
		return "precreatePay";
	}
	/**
	 *被扫预下单 生成二维码
	 * @throws Exception 
	 */
	@RequestMapping("/precreate")
	public String Precreate(PrecreateParams precreateParams,ModelMap modelMap) throws Exception{
//		PrecreateParams precreateParams = new PrecreateParams();
//		precreateParams.setPayFlag("0");
//		precreateParams.setTotalFee("45");
		
		System.out.println(precreateParams);
		//System.out.println(precreateService.precreate(precreateParams));
		String url = precreateService.precreate(precreateParams);
//	System.out.println(url);
		modelMap.addAttribute("qrcode",url);
		if(url.indexOf("alipay") != -1){
			return "alipay";
		}
		else if(url.indexOf("weixin") != -1){
			return "weixin";
		}else{
			return "error";
		}
	}
	/**
	 * 跳转到主扫支付界面
	 * @return
	 */
	@RequestMapping("/toTradePay")
	public String toTradePay(){
		
		return "tradePay";
	}
	@RequestMapping("/tradePay")
	public String tradePay(TradePayParams tradePayParams,ModelMap modelMap) throws Exception{
		String res = tradePayService.tradePay(tradePayParams);
		modelMap.addAttribute("resultCode", res);
//		if(res.indexOf("成功") != -1){
//			return "payResult";
//		}else{
//			return "error";
//		}
		return "payResult";
	}
	
}
