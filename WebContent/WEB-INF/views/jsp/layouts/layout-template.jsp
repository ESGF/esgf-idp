<%@ include file="/WEB-INF/views/jsp/common/include.jsp"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		
		<!-- page title -->
		<title><tiles:getAsString name="title" /></title>
		
		<!-- common <head> content -->
		<tiles:insertAttribute name="head" />
		
		<!-- page specific javascript -->
		<tiles:insertAttribute name="script" />
		    
	</head>
	
	<body>
		
		<!-- common header -->
		<p><tiles:insertAttribute name="header" /></p>
				
		<!-- page-specific body -->
		<p><tiles:insertAttribute name="body" /></p>
		
		<!-- common footer -->
		<p><tiles:insertAttribute name="footer" /></p>
		
	</body>
	
</html>