<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>信雅达微信支付</title>
</head>

<body>

	<a href=${requestScope.qrcode} title="聚合支付" target="_blank">Welcome</a>
	<input type="hidden" id="url" name="url" value="${requestScope.qrcode}"/>

<!-- 跳转到支付宝的支付url -->
	<script type="text/javascript">
	 var a=document.getElementById("url").value;  
  	location.href=a;
 //	open(a); 
	</script>
</body>

</html>