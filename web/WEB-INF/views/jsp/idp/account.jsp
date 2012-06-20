<%@ include file="/WEB-INF/views/jsp/common/include.jsp" %>

<tiles:insertDefinition name="center-layout" >
	
	<tiles:putAttribute type="string" name="title" value="ESGF OpenID Login" />
	
	<tiles:putAttribute name="body">
		<tiles:putAttribute type="string" name="pageTitle" value="" />
		
		    <h1>ESGF User Account</h1>
		    
		    <div align="center">
		       <security:authentication property="principal" var="principal"/>
               User Openid:&nbsp;<b><c:out value="${principal.username}"/></b> 
				<div style="width:400px;">
					<div class="panel">
						<table border="0" align="center" width="100%">
						    <caption>Group Membership</caption>
						    <tr>
						        <th>Group</th>
						        <th>Roles</th>
						    </tr>
						  <c:forEach var="entry" items="${attributes}">
						    <tr>
						         <th>${entry.key}</th>
						         <td>
						             <c:forEach var="role" items="${entry.value}">
						               ${role}
						             </c:forEach>
						         <td>
						    </tr>
						  </c:forEach>
						</table>
					</div>
				</div>
			</div>	
			
	</tiles:putAttribute>

</tiles:insertDefinition>