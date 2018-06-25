<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>

<link rel="stylesheet" type="text/css"
	href="webjars/bootstrap/3.3.7/css/bootstrap.min.css" />

<!-- 
	<spring:url value="/css/main.css" var="springCss" />
	<link href="${springCss}" rel="stylesheet" />
	 -->
<c:url value="/css/main.css" var="jstlCss" />
<link href="${jstlCss}" rel="stylesheet" />

</head>
<body>

<!-- This is the Products Page, which shows the list of all Available Products -->

<a href="product">product items are here</a>

<img src="/images/1.jpg"/>
<img src="/images/2.jpg"/>
<img src="/images/3.jpg"/>
<img src="/images/4.jpg"/>
<img src="/images/5.jpg"/>
<img src="/images/6.jpg"/>

<form action="shopping-cart" method="post">

    <input type="submit" name="submit" value="submit">
    
</form>
</body>

</html>
