<%@ include file="/WEB-INF/views/jsp/common/include.jsp" %>

<tiles:insertDefinition name="center-layout" >
	
	<tiles:putAttribute type="string" name="title" value="ESGF OpenID Login" />
	
	<tiles:putAttribute name="script">
	
		<script type="text/javascript"> 		
			  function init() {
				var submitButton = new YAHOO.widget.Button("submit-button");
			  }
			  YAHOO.util.Event.onDOMReady(init);
		</script>
		
		<style type="text/css">
			.yui-fixed-panel#login-div .bd { width: 40em; text-align: center; margin:0 auto; align: center; }
			.yui-fixed-panel#login-div td { padding: 0.5em; }
		</style>
		
	</tiles:putAttribute>
	
	<tiles:putAttribute name="body">
		<tiles:putAttribute type="string" name="pageTitle" value="OpenID Login" />
			
			<!-- login errors -->
		  	<p>&nbsp;</p>
			<springForm:errors path="loginCommand.*" cssClass="error"/>
			<p>&nbsp;</p>

			<!-- user openid -->
			<c:if test="${not empty sessionScope['esgf.idp.openid']}">
				<div align="center"><img src='<c:url value="/themes/openid_small.gif"/>'/>
				&nbsp;
				Your OpenID: <span class="highlight"><c:out value ="${sessionScope['esgf.idp.openid']}"/></span>
				</div>	
			</c:if>
			
			<!-- password submission form -->
		    <p>&nbsp;</p>
			<springForm:form method="post" commandName="loginCommand" name="loginForm">
				<div class="yui-fixed-panel" id="login-div">
					<div class="bd">
						<table border="0" align="center">
							<tr>
								<td align="right" class="required">Password</td>
								<td align="left"><springForm:password path="password"/></td>
								<td><input type="submit" value="GO" id="submit-button"/></td>
							</tr>
						</table>
					</div>
				</div>
			</springForm:form>
			
	</tiles:putAttribute>

</tiles:insertDefinition>