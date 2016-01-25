<%@ include file="/WEB-INF/views/jsp/common/include.jsp"%>

<tiles:insertDefinition name="center-layout">

	<tiles:putAttribute type="string" name="title" value="ESGF Identity Provider" />

	<tiles:putAttribute name="body">

		<h1>ESGF Identity Provider</h1>

		<table align="center">
			<tr>
				<td align="center">
					<div class="panel">
						<table>
							<tr>
								<td>
									<img src='<c:url value="/themes/openid.png"/>' width="80" hspace="10px" />
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>

	</tiles:putAttribute>

</tiles:insertDefinition>