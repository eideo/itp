package com.sunyard.itp.service.imp;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.sunyard.itp.constant.PayConst;
import com.sunyard.itp.service.QueryOrderService;
import com.sunyard.itp.utils.wxpay.WXPay;
import com.sunyard.itp.utils.wxpay.WXPayConfigImpl;
/**
 * 查询订单业务类
 * @author zhix.huang
 *
 */
@Service
public class QueryOrderServiceImp implements QueryOrderService{
	/**
	 * 查询支付宝订单
	 * @param outTradeNo
	 * @throws Exception 
	 */
	public AlipayTradeQueryResponse queryAlipayOrder(String out_trade_no) throws Exception {
		// TODO Auto-generated method stub
		AlipayClient alipayClient = new DefaultAlipayClient(PayConst.OPEN_API_DOMAIN,
				PayConst.APP_ID,
				PayConst.PRIVATE_KEY,
				"json","UTF-8",
				PayConst.ALIPAY_PUBLIC_KEY,
				PayConst.SIGN_TYPE);
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		System.out.println("-----"+out_trade_no);
		request.setBizContent("{" +
				"\"out_trade_no\":\""+out_trade_no+"\"" +
		
		"  }");
		AlipayTradeQueryResponse response = alipayClient.execute(request);
		if(response.isSuccess()){
		System.out.println("调用查询成功");
		
		return response;
		} else {
			System.out.println("调用查询失败");
			System.out.println(response.getBody());
			return null;
			}		
	}
	/**
	 * 查询微信订单
	 * @param outTradeNo
	 * @throws Exception 
	 */
	public  Map<String, String> queryWxOrder(String out_trade_no) throws Exception {
		 System.out.println("查询订单");
			//加载微信支付参数
			WXPayConfigImpl config =  WXPayConfigImpl.getInstance();
			WXPay wxpay = new WXPay(config);
			HashMap<String, String> data = new HashMap<String, String>();
		        data.put("out_trade_no", out_trade_no);
//		        data.put("transaction_id", "4008852001201608221962061594");		       
		            Map<String, String> r = wxpay.orderQuery(data);
		            System.out.println(r);
//		            System.out.println(r.get("result_code"));
//		            System.out.println(r.get("out_trade_no"));
//		            System.out.println(r.get("settlement_total_fee"));
//		            System.out.println(r.get("total_fee"));
//		            System.out.println(r.get("time_end"));
//		            System.out.println(r.get("cash_fee"));
		            return r;        
		
	}

}
