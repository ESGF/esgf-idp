<%@ include file="/WEB-INF/views/jsp/common/include.jsp" %>

<tiles:insertDefinition name="center-layout" >
	
	<tiles:putAttribute type="string" name="title" value="ESGF OpenID Login" />
	
	<tiles:putAttribute name="body">
		<tiles:putAttribute type="string" name="pageTitle" value="" />
		
		    <h1>ESGF OpenID Login</h1>
			
			<!-- login errors -->
			<div align="center">
				<springForm:errors path="loginCommand_ids.*" cssClass="error"/>
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
											<springForm:form method="post" commandName="loginCommand_ids" name="loginForm">
													<table border="0" align="center" class="required">
														<!--  <c:if test="${not empty sessionScope[openid_attribute]}">-->
															<tr>
																<td align="left">
																<!-- Your OpenID: <b><c:out value ="${sessionScope[openid_attribute]}"/></b>-->
																Username:&nbsp;&nbsp;
																</td>
																<td align="left">
																<!--<label for="login_form_username">&nbsp;Username</label>:-->
                                                                <input id="username" name="username" type="text" /><!-- size=27 -->
																&nbsp;&nbsp;
																</td>
															</tr>
														<!-- </c:if>  --> 
														<tr>
															<td align="left" class="required">
															    Password:&nbsp;&nbsp; 
															</td>
															<td align="left" class="required">    
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