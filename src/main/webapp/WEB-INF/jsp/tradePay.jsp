 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, minimal-ui">
<title>信雅达主扫发起页面</title>
</head>
<style>
	form{
		position:absolute;
		top:20%;
		left:20%;
	}
</style>
<body>
	<form action="tradePay.action" method="post">
        <div style="margin-left:2%;color:#f00">信雅达聚合支付</div><br/>
         <div style="margin-left:2%;">买家支付授权码：</div><br/>
        <input type="text" style="height:35px;margin-left:2%;" name="authCode" id="authCode" /><br /><br />
        <div style="margin-left:2%;">输入金额：</div><br/>
        <input type="text" style="height:35px;margin-left:2%;" name="totalFee" id="totalFee" /><br /><br />
		<div align="center">
			<input type="submit" value="结算" style="width:210px; height:40px; border-radius: 15px;background-color:#FE6714; border:0px #FE6714 solid; cursor: pointer;  color:white;  font-size:16px;" type="button" />
		</div>
	</form>
	
</body>
</html>