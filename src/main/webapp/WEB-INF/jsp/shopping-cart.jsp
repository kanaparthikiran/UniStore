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

<c:url value="/css/buttonStyles.css" var="buttonStyles" />
<link href="${buttonStyles}" rel="stylesheet" />

</head>
<body>
    This is Shopping Cart Page
    
<!--     <a href="address">address page is here</a>
 -->
<form action="address" method="get">

    <input type="submit" name="submit" value="submit">
    
</form>
    
</body>

</html>
