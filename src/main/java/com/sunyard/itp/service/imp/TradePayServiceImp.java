package com.sunyard.itp.service.imp;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.sunyard.itp.constant.PayConst;
import com.sunyard.itp.entity.TradePayParams;
import com.sunyard.itp.entity.TransFlow;
import com.sunyard.itp.service.QueryOrderService;
import com.sunyard.itp.service.TradePayService;
import com.sunyard.itp.service.TransFlowService;
import com.sunyard.itp.utils.wxpay.WXPay;
import com.sunyard.itp.utils.wxpay.WXPayConfigImpl;



/**
 * 主扫业务类
 * @author zhix.huang
 *
 */
@Service
public class TradePayServiceImp implements TradePayService{
	@Autowired
	private TransFlowService transFlowService;
	@Autowired
	private QueryOrderService queryOrderService;
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String tradePay(TradePayParams tradePayparams) throws Exception {
		String auth_code = tradePayparams.getAuthCode();
		BigDecimal totalFee = tradePayparams.getTotalFee();
		
		if(auth_code.startsWith("25") || auth_code.startsWith("26") || auth_code.startsWith("27") || auth_code.startsWith("28") || auth_code.startsWith("29") || auth_code.startsWith("30")){
			System.out.println("支付宝");
			String out_trade_no = "alipay-tra" + System.currentTimeMillis()
		    + (long) (Math.random() * 10000000L);
			AlipayClient alipayClient = new DefaultAlipayClient(PayConst.OPEN_API_DOMAIN,
					PayConst.APP_ID,
					PayConst.PRIVATE_KEY,
					"json","UTF-8",
					PayConst.ALIPAY_PUBLIC_KEY,
					PayConst.SIGN_TYPE);
			AlipayTradePayRequest request = new AlipayTradePayRequest();
			request.setBizContent("{" +
					"\"out_trade_no\":\""+out_trade_no+"\"," +
					"\"scene\":\""+PayConst.SCENE+"\"," +
					"\"auth_code\":\""+auth_code+"\"," +
					"\"subject\":\"支付宝主扫测试\"," +
					"\"total_amount\":"+totalFee+"" +
					"  }");
			System.out.println(	"{" +
					"\"out_trade_no\":\""+out_trade_no+"\"," +
					"\"auth_code\":\""+auth_code+"\"," +
					"\"subject\":\"支付宝主扫测试\"," +
					"\"total_amount\":"+totalFee+"," +
					"  }" );
			System.err.println(request.getBizContent());
			AlipayTradePayResponse response = alipayClient.execute(request);
			System.out.println(response.getBody());
			if(response.isSuccess()){
			System.out.println("调用成功");
			//交易成功，插入流水
			AlipayTradeQueryResponse order = queryOrderService.queryAlipayOrder(out_trade_no);
//			//如果预下单成功，将该交易流水插入到数据库中
			TransFlow transFlow = new TransFlow();
			transFlow.setTradeNo(order.getTradeNo());
			transFlow.setOutTradeNo(out_trade_no);
			transFlow.setBuyerLogonId(order.getBuyerLogonId());
			transFlow.setTradeStatus(order.getTradeStatus());
			transFlow.setTotalAmount(order.getTotalAmount());
			transFlow.setReceiptAmount(order.getReceiptAmount());
			//转换时间格式
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String date = df.format(order.getSendPayDate());
			transFlow.setSendPayDate(date);
			transFlow.setBuyerUserId(order.getBuyerUserId());
			transFlow.setTransType("0");
			//插入流水
			transFlowService.addTransFlow(transFlow);
			return "支付成功！";
			} else {
			System.out.println("调用失败");
			return "支付失败！";
			}
		}else if(auth_code.startsWith("10") || auth_code.startsWith("11") || auth_code.startsWith("12") || auth_code.startsWith("13") || auth_code.startsWith("14") || auth_code.startsWith("15")){
			System.out.println("微信支付");
			String statu = "微信支付成功！";
			String out_trade_no = "weixin-tra" + System.currentTimeMillis()
		    + (long) (Math.random() * 10000000L);
			WXPayConfigImpl config =  WXPayConfigImpl.getInstance();
			WXPay wxpay = new WXPay(config);
			HashMap<String,String> data = new HashMap<String,String>();
	    	data.put("body","微信主扫测试");
	    	data.put("out_trade_no", out_trade_no);
	    	data.put("total_fee", totalFee.toString());
	    	data.put("spbill_create_ip", "172.16.17.18");
	    	data.put("auth_code", auth_code);
	    	 try {
	             Map<String, String> r = wxpay.microPay(data);
	             System.out.println(r);
	        //如果成功 插入流水
	             if(r.get("result_code").equals("SUCCESS")){
		             Map<String, String> order = queryOrderService.queryWxOrder(out_trade_no);
		           //如果预下单成功，将该交易流水插入到数据库中   组织数据
		 			TransFlow transFlow = new TransFlow();
		 			transFlow.setTradeNo(order.get("transaction_id"));
		 			transFlow.setOutTradeNo(out_trade_no);
		 			transFlow.setBuyerLogonId(order.get("openid"));
		 			transFlow.setTradeStatus(order.get("trade_state"));
		 			transFlow.setTotalAmount(order.get("total_fee"));
		 			transFlow.setReceiptAmount(order.get("settlement_total_fee"));
		 			transFlow.setSendPayDate(order.get("time_end"));
		 			transFlow.setBuyerUserId(order.get("openid"));
		 			transFlow.setTransType("1");
		 			//插入流水
		 			transFlowService.addTransFlow(transFlow);
	 			
	 			return statu;
	             }else{
	            	 System.out.println("微信刷卡支付失败");
	            	 statu = "微信支付失败";
	            	 String errCode = r.get("err_code");
	            	 String errCodeDes = r.get("err_code_des");		
	            	 System.out.println(errCodeDes);
	            	 TransFlow transFlow = new TransFlow();
	            	 transFlow.setOutTradeNo(out_trade_no);
	            	 transFlow.setTradeStatus(errCode);
	            	 transFlow.setTransType("1");
	            	 transFlowService.addTransFlow(transFlow);
	            	 return statu +"错误代码："+errCode+"    错误描述："+errCodeDes;
	             }
	         } catch (Exception e) {
	             e.printStackTrace();
	             return "微信支付系统异常";
	         }
		}else{
			return "非法的授权码！";
		}
		
	}
}
