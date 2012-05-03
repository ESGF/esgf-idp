<%@ include file="/WEB-INF/views/jsp/common/include.jsp" %>

<tiles:insertDefinition name="center-layout" >
	
	<tiles:putAttribute type="string" name="title" value="ESGF Identity Provider" />
		
	<tiles:putAttribute name="body">
		
		
		<!-- page scope variables -->
		<c:set var="openid_cookie" value="<%= esg.idp.server.web.OpenidPars.OPENID_COOKIE_NAME %>"/>
		<security:authentication property="principal" var="principal"/>
		
		<h1>ESGF Identity Provider</h1>
		
		<!-- Errors -->
		<c:if test="${param['failed']==true}">
			<div align="center">
				<span class="error">Error: unable to resolve OpenID identifier.</span>
			</div>
		</c:if>	
		
		<c:choose>
		
			<c:when test="${principal=='anonymousUser'}">
				
				<!-- User is not authenticated -->
				<table  border="0" align="center">
					<tr>
						<td>
							<div class="panel">
								<table>
									<caption>Status: not logged-in</caption>
									<tr>
										<td>
											<img src='<c:url value="/themes/openid.png"/>' width="80" />
										</td>
										<td>
											<form name="loginForm" action='<c:url value="/j_spring_openid_security_check.jsp"/>' >	
												<script language="javascript">
													function sanitize() {
														openidElement = document.getElementById("openid_identifier");
														openid = openidElement.value;
														openid = openid.replace("http:","https:")
														               .replace(/^\s\s*/, '').replace(/\s\s*$/, '');
														openidElement.value = openid;
													}
												</script>															    				
												<table border="0" cellpadding="10px" cellspacing="10px" align="center">
													<tr>
														<td align="right" class="required">OpenID:</td>
														<td align="left"><input type="text" name="openid_identifier" size="80" value="${cookie[openid_cookie].value}" id="openid_identifier" /></td>
														<td><input type="submit" value="LOGIN" class="button" onclick="javascript:sanitize()" /></td>
													</tr>
													<tr>
														<td align="center" colspan="3">
															<input type="checkbox" name="remember_openid" checked="checked" /> <span class="highlight">Remember my OpenID</span> on this computer
														</td>
													</tr>				
												</table>
											</form>
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
				</table>

			</c:when>
			
			<c:otherwise>
				<!-- User is authenticated -->
				<table align="center">
					<tr>
						<td align="center">
							<div class="panel">
								
								<table>
									<caption>Status: logged-in</caption>
									<tr>
										<td>
											<img src='<c:url value="/themes/openid.png"/>' width="80" hspace="10px"/>
										</td>
										<td>
											<form name="logoutForm" action='<c:url value="/j_spring_security_logout"/>' >					
												<table border="0" cellpadding="10px" cellspacing="10px" align="center">
													<tr>
														<td align="center">Thank you, you are now logged in.</td>
													</tr>
													<tr>
														<td align="center">Your Openid:&nbsp;<b><c:out value="${principal.username}"/></b></td>
													</tr>
													<tr>
														<td>&nbsp;<input type="submit" value="LOGOUT" class="button" /></td>
													</tr>
												</table>
											</form>
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
				</table>
				
			</c:otherwise>
			
	    </c:choose>
	    
			
	</tiles:putAttribute>

</tiles:insertDefinition>