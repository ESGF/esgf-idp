<%@ include file="/WEB-INF/views/jsp/common/include.jsp" %>

<tiles:insertDefinition name="center-layout" >
	
	<tiles:putAttribute type="string" name="title" value="ESGF OpenID Login" />
	
	<tiles:putAttribute name="body">
		<tiles:putAttribute type="string" name="pageTitle" value="OpenID Login" />
		
		   
			
			<!-- login errors -->
		  	<p>&nbsp;</p>
			<springForm:errors path="loginCommand.*" cssClass="error"/>
			<p>&nbsp;</p>

			<!-- user openid -->
			<c:set var="openid_attribute" value="<%= esg.idp.server.web.OpenidPars.SESSION_ATTRIBUTE_OPENID %>"/>
			
			<!-- password submission form -->
		    <p>&nbsp;</p>
			
			<table  border="0" align="center">
					<tr>
						<td>
							<div class="panel">
								<table>
									<tr>
										<td>
											<img src='<c:url value="/themes/openid.png"/>' width="80"/>
										</td>
										<td>
											<springForm:form method="post" commandName="loginCommand" name="loginForm">
													<table border="0" align="center">
														<c:if test="${not empty sessionScope[openid_attribute]}">
															<tr>
																<td align="center">
																	 Your OpenID: <b><c:out value ="${sessionScope[openid_attribute]}"/></b>
																</td>
															</tr>
														</c:if>
														<tr>
															<td align="center" class="required">
															    Password:&nbsp; 
															    <springForm:password path="password"/>
																&nbsp;<input type="submit" value="SUBMIT" class="button" />
														    </td>
														</tr>
													</table>
											</springForm:form>
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
				</table>
			
	</tiles:putAttribute>

</tiles:insertDefinition>