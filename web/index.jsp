<%@ include file="/WEB-INF/views/jsp/common/include.jsp" %>

<tiles:insertDefinition name="center-layout" >
	
	<tiles:putAttribute type="string" name="title" value="ESGF Identity Provider" />
	
	<tiles:putAttribute name="script">
	
		<script type="text/javascript"> 		
			  function init() {
				var submitButton = new YAHOO.widget.Button("submit-button");
			  }
			  YAHOO.util.Event.onDOMReady(init);
		</script>
		
		<style type="text/css">
			.yui-fixed-panel#login-div .bd { width: 45em; text-align: center; margin:0 auto; align: center; }
			.yui-fixed-panel#login-div td { padding: 0.5em; }
		</style>
		
	</tiles:putAttribute>
	
	<tiles:putAttribute name="body">
		
		<tiles:putAttribute type="string" name="pageTitle" value="ESGF Identity Provider" />
		
		<!-- page scope variables -->
		<c:set var="openid_cookie" value="<%= esg.idp.server.web.OpenidPars.OPENID_COOKIE_NAME %>"/>
		<security:authentication property="principal" var="principal"/>
		
		<!-- Errors -->
		<c:if test="${param['failed']==true}">
			<span class="error">Error: unable to resolve OpenID identifier.</span>
		</c:if>		
		
		<p>&nbsp;</p>
		<c:choose>
		
			<c:when test="${principal=='anonymousUser'}">
				
				<!-- User is not authenticated -->
				<table  border="0" align="center">
					<tr>
						<td align="right" valign="top">
							<img src='<c:url value="/themes/openid-logo-300x300.png"/>' width="80" hspace="10px"/>
						</td>
						<td align="left" valign="top">
							<form name="openidForm" action='<c:url value="/j_spring_openid_security_check.jsp"/>' >					
								<div class="yui-fixed-panel" id="login-div">
									<div class="bd">
										<table border="0" align="center">
											<tr>
												<td align="right" class="required">Openid:</td>
												<td align="left"><input type="text" name="openid_identifier" size="50" value="${cookie[openid_cookie].value}"/></td>
												<td><input type="submit" value="GO" id="submit-button"/></td>
											</tr>
											<tr>
												<td align="center" colspan="3">
													<input type="checkbox" name="remember_openid" checked="checked" /> <span class="highlight">Remember my OpenID</span> on this computer
												</td>
											</tr>				
										</table>
									</div>
								</div>
							</form>
							<p>&nbsp;</p>Please enter your OpenID. You will be redirected to your registration web site to login .
						</td>
					</tr>
				</table>
			</c:when>
			
			<c:otherwise>
				<!-- User is authenticated -->
				<p>&nbsp;</p>
				<div align="center">
					<p>Thanks, you are now logged in.</p>
					<p>&nbsp;</p>
					<img src='<c:url value="/themes/openid_small.gif"/>' align="top" />
					Your OpenID: <b><c:out value="${principal.username}"/></b>
				</div>
			</c:otherwise>
			
	    </c:choose>
	    
			
	</tiles:putAttribute>

</tiles:insertDefinition>