<%@ include file="/WEB-INF/views/jsp/common/include.jsp" %>

<tiles:insertDefinition name="center-layout" >
	
	<tiles:putAttribute type="string" name="title" value="ESGF OpenID Login" />
	
	<tiles:putAttribute name="body">
		<tiles:putAttribute type="string" name="pageTitle" value="" />
		
		    <h1>ESGF OpenID Login</h1>
			
			<!-- login errors -->
			<div align="center">
				<springForm:errors path="loginCommand.*" cssClass="error"/>
			</div>

			<!-- user openid -->
			<c:set var="openid_attribute" value="<%= esg.idp.server.web.OpenidPars.SESSION_ATTRIBUTE_OPENID %>"/>
			
			<!-- password submission form -->
			<table  border="0" align="center">
					<tr>
						<td>
							<div class="panel">
								<table>
									<caption>Status: not logged-in</caption>
									<tr>
										<td>
											<img src='<c:url value="/themes/openid.png"/>' width="80"/>
										</td>
										<td>
											<springForm:form method="post" commandName="loginCommand" name="loginForm">
													<table border="0" align="center" class="required">
														<!--  <c:if test="${not empty sessionScope[openid_attribute]}">-->
															<tr>
																<td align="left">
																<!-- Your OpenID: <b><c:out value ="${sessionScope[openid_attribute]}"/></b>-->
																Username:&nbsp;
																<!--<label for="login_form_username">&nbsp;Username</label>:-->
                                                                <input id="login_form_username" name="login_form_username" type="text"/>
																</td>
															</tr>
														<!-- </c:if>  --> 
														<tr>
															<td align="center" class="required">
															    Password:&nbsp; 
															    <springForm:password path="password"/>
																&nbsp;<input type="submit" value="SUBMIT" class="button" /><!---->
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