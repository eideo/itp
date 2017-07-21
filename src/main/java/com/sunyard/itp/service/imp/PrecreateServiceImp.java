package com.sunyard.itp.service.imp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.sunyard.itp.constant.PayConst;
import com.sunyard.itp.entity.PrecreateParams;
import com.sunyard.itp.entity.TransFlow;
import com.sunyard.itp.service.PrecreateService;
import com.sunyard.itp.service.TransFlowService;
import com.sunyard.itp.utils.wxpay.WXPay;
import com.sunyard.itp.utils.wxpay.WXPayConfigImpl;
import com.sunyard.itp.utils.zxing.QRCodeUtil;


/**
 * 被扫预下单
 * @author zhix.huang
 *
 */
@Service
public class PrecreateServiceImp implements PrecreateService{
	@Autowired
	private TransFlowService transFlowService;
	
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public String precreate(PrecreateParams payParams) throws Exception {
		if(payParams.getPayFlag() != null && payParams.getPayFlag().equals("0")){
			logger.debug("zhix.huang:------------进入支付宝预支付接口");
			System.out.println("支付宝支付");
			String out_trade_no = "alipay-pre" + System.currentTimeMillis()
		    + (long) (Math.random() * 10000000L);
			String totalFee = payParams.getTotalFee();
			AlipayClient alipayClient = new DefaultAlipayClient(PayConst.OPEN_API_DOMAIN,
					PayConst.APP_ID,
					PayConst.PRIVATE_KEY,
					"json","UTF-8",
					PayConst.ALIPAY_PUBLIC_KEY,
					PayConst.SIGN_TYPE);
			AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
			request.setBizContent("{" +
			"\"out_trade_no\":\""+out_trade_no+"\"," +
			"\"total_amount\":"+totalFee+"," +
//			"\"seller_id \":\"2088102170223040\"," +
			"\"subject\":\"支付宝被扫订单\"" +	
			"}" 	
			);
			AlipayTradePrecreateResponse response = alipayClient.execute(request);			
			
			
			if(response.isSuccess()){
			System.out.println("调用预下单成功");
			System.out.println(response.getQrCode());
			System.out.println(out_trade_no);
			
			//查询订单     （注：支付宝预下单无法查询,放弃调用）
//			AlipayTradeQueryResponse queryRes = queryAlipayOrder(out_trade_no);
			//如果预下单成功，将该交易流水插入到数据库中
			TransFlow transFlow = new TransFlow();
			transFlow.setTradeNo("待支付下单");
			transFlow.setOutTradeNo(out_trade_no);
			transFlow.setBuyerLogonId("待支付下单");
			transFlow.setTradeStatus("待支付下单");
			transFlow.setTotalAmount(totalFee);
			transFlow.setReceiptAmount(totalFee);
			//转换时间格式
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String date = df.format(new Date());
			transFlow.setSendPayDate(date);
			transFlow.setBuyerUserId("待支付下单");
			transFlow.setTransType("0");
			transFlowService.addTransFlow(transFlow);
			return response.getQrCode();		
			} else {
			System.out.println("调用预下单失败");
			return "fail";
			}	
			//微信支付
		}else if(payParams.getPayFlag() != null && payParams.getPayFlag().equals("1")){
			System.out.println(payParams.getPayFlag());
			System.out.println("微信支付");
			String out_trade_no = "wxpay-pre" + System.currentTimeMillis()
		    + (long) (Math.random() * 10000000L);
	
			//加载微信支付参数
			WXPayConfigImpl config =  WXPayConfigImpl.getInstance();
			WXPay wxpay = new WXPay(config);
			//组装上传参数
			HashMap<String,String> data = new HashMap<String,String>();
			 data.put("body", "微信被扫预下单");
		        data.put("out_trade_no", out_trade_no);
		        data.put("device_info", "");
		        data.put("fee_type", "CNY");
		        data.put("total_fee", payParams.getTotalFee());
		        data.put("spbill_create_ip", "172.16.17.18");
		        data.put("notify_url", "http://test.letiantian.me/wxpay/notify");
		        data.put("trade_type", "NATIVE");
		        data.put("product_id", "12");
	    	
	             Map<String, String> r = wxpay.unifiedOrder(data);
	             System.out.println(r);
	             if(r.get("result_code").equals("SUCCESS")){
	            	 System.out.println("微信预下单成功!");
	                 String qrcode =  r.get("code_url");
//		             System.out.println(r.get("code_url"));
		           //生成不带logo 的二维码  
		             String fileName = out_trade_no;
		 	        String textt = qrcode;  
		 	        QRCodeUtil.encode(textt,"","d:/WPS",true,fileName);  
		 	        //调用查询微信订单接口
		 	      Map<String,String> queryRes = queryWxOrder(out_trade_no);
		 	        //组织流水参数
		 	        TransFlow transFlow = new TransFlow();
		 	        //支付不成功无法查询微信订单号
		 	       transFlow.setTradeNo("微信订单");
		 	       transFlow.setOutTradeNo(out_trade_no);
		 	      //支付不成功无法查询用户信息
		 	       transFlow.setBuyerLogonId("微信用户");
		 	       transFlow.setTradeStatus("预下单成功，支付中");
				   transFlow.setTotalAmount(payParams.getTotalFee());
				   transFlow.setReceiptAmount(payParams.getTotalFee());
				 //转换时间格式
					DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
					String date = df.format(new Date());
				   transFlow.setSendPayDate(date);
				   transFlow.setBuyerUserId(queryRes.get("openid"));
				   transFlow.setTransType("1");
				   transFlowService.addTransFlow(transFlow);
		 	    		 	        
		            return qrcode;
	             }else{
	            	 System.out.println("微信预下单失败！");
	            	 String errCode = r.get("err_code");
	            	 String errCodeDes = r.get("err_code_des");		 
	            	 return "错误代码："+errCode+"    错误描述："+errCodeDes;
	             }
	      
	          
	        
		}else{
			System.out.println(payParams.getPayFlag());
			return "非法支付类型";
		}	
	}
	/**
	 * 查询支付宝订单状态
	 * @param out_trade_no 
	 * @return 
	 * @throws AlipayApiException 
	 */
	public AlipayTradeQueryResponse queryAlipayOrder(String out_trade_no) throws AlipayApiException {
		AlipayClient alipayClient = new DefaultAlipayClient(PayConst.OPEN_API_DOMAIN,
				PayConst.APP_ID,
				PayConst.PRIVATE_KEY,
				"json","UTF-8",
				PayConst.ALIPAY_PUBLIC_KEY,
				PayConst.SIGN_TYPE);
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		System.out.println("-----"+out_trade_no);
		request.setBizContent("{" +
				"\"out_trade_no\":"+out_trade_no+"\"" +
		
		"  }");
		AlipayTradeQueryResponse response = alipayClient.execute(request);
		if(response.isSuccess()){
		System.out.println("调用查询成功");
		System.out.println(response.getBuyerPayAmount());
		System.out.println(response.getBuyerUserId());
		System.out.println(response.getSubMsg());
		System.out.println(response.getTradeStatus());
		return response;
		} else {
			System.out.println("调用查询失败");
			System.out.println(response.getBody());
			return null;
			}		
	}
	//微信查询订单
	public Map<String, String> queryWxOrder(String out_trade_no) throws Exception{
		 System.out.println("查询订单");
		//加载微信支付参数
		WXPayConfigImpl config =  WXPayConfigImpl.getInstance();
		WXPay wxpay = new WXPay(config);
		HashMap<String, String> data = new HashMap<String, String>();
	        data.put("out_trade_no", out_trade_no);
//	        data.put("transaction_id", "4008852001201608221962061594");
	       
	            Map<String, String> r = wxpay.orderQuery(data);
	            System.out.println(r);
	            System.out.println(r.get("result_code"));
	            System.out.println(r.get("out_trade_no"));
	            System.out.println(r.get("settlement_total_fee"));
	            System.out.println(r.get("total_fee"));
	            System.out.println(r.get("time_end"));
	            System.out.println(r.get("cash_fee"));
	            return r;        
	}	
}
