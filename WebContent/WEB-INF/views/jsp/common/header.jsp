<%@ include file="/WEB-INF/views/jsp/common/include.jsp" %>

<img src='<c:url value="/images/esgf.png"/>' height="76px" style="margin:2px; float:left"/>
<c:set var="ilogo"><spring:message code="esgf.homepage.institutionLogo" /></c:set>
<c:if test="${ilogo != ''}">
	<img src='<c:url value="${ilogo}" />' height="76px" style="margin:2px; float:right" />
</c:if>
