<%@ page isELIgnored ="false" %> 
<%@ page import ="java.util.*,
		it.imiweb.app.redux.server.city.CityMonitor,
		it.imiweb.app.redux.server.city.ServerInfosHome,
		it.imiweb.app.redux.server.city.CityTripSession" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

    <%-- c:if test="${param.p != null}">
        aaaa<c:out value="${param.p}" />
    </c:if --%>
    


<%!

static java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
static java.text.SimpleDateFormat df_short = new java.text.SimpleDateFormat("hh:mm:ss");
CityMonitor cityMonitor;
ServerInfosHome serverInfosHome;
String adminUrl; String target; boolean messagingEnabled;
String sessionManagerPath="/it/imiweb/bubble/SessionsManager";

String formatTipDate(String dateLong ){
	return formatTipDate(dateLong, false);
}
String formatTipDate(long time ){
	return formatTipDate(time, false);
}
String formatTipDate(long time, boolean shortFormat ){
	try{
		if ( shortFormat )
			return df_short.format(new Date(time));
		else	
			return df.format(new Date(time))+" ["+time+"]";
	}catch(Exception e){
		e.printStackTrace();
		return ""+time;
	}

}
String formatTipDate(String dateLong, boolean shortFormat ){
	if (dateLong==null) return "[null]";
	try{
		long time=new Long (dateLong).longValue();
		return  formatTipDate(time, shortFormat);
		
	}catch(Exception e){
		e.printStackTrace();
		return dateLong;
	}
	
}	

%>



<%-- c:set var="adminPort" value="9980" scope="application"/>
<c:set var="adminHostName" value="http://localhost" scope="application"/ --%>

<%
	
	atg.servlet.DynamoHttpServletRequest dRequest = (atg.servlet.DynamoHttpServletRequest)request;
	if ( cityMonitor == null )
		cityMonitor=(CityMonitor)dRequest.resolveName("/it/imiweb/app/redux/city/CityMonitor");
	if ( cityMonitor != null )
		dRequest.setParameter( "cityMonitor", cityMonitor );
	if ( serverInfosHome == null )
		serverInfosHome=(ServerInfosHome)dRequest.resolveName("/it/imiweb/app/redux/city/ServerInfosHome");
	if (adminUrl==null){
		String port="9980"; //request.getParameter("adminPort");
		String host="iwbtest"; //request.getParameter("adminHostName");
		adminUrl="http://"+host+":"+port+"/bubble/listbeans.jsp?path=";
		target=port;
	}	
	String dummy=request.getParameter("enableMessaging");
	
	if (dummy!=null && dummy.equals("true"))
	messagingEnabled=true;
	else
	messagingEnabled=false;
	
	
	System.out.println ( "#@#@#@#@#@#@ redux_sessions ["+new Date()+"] ==> "+request.getRemoteAddr());          
%>





<%
	if (request.getParameter("sessionsForm")==null){
		dRequest.setParameter("showSessionInfos" ,"show" );
		dRequest.setParameter("showWideCommInfos" ,"show" );
		dRequest.setParameter("showWideCommQueueInfos" ,"show" );

	
%>
	<c:set var="aashowSessionInfos" value="show" scope="request"/>
	<c:set var="aashowWideCommInfos" value="show" scope="request"/>
	<c:set var="aashowWideCommQueueInfos" value="show" scope="request"/>
<%}%>











<html><head>
<script>
function openMessageWindow(anchor){
	dummy=window.open(anchor.href,"MessagingWindow", "scrollbars=no,toolbar=no,resizable=no,menubar=no,width=400,height=400,topmargin=0,leftmargin=0");
	dummy.focus();
	
	return false;
}
function openUserInfoWindow(anchor){
	dummy=window.open(anchor.href,"UserInfoWindow", "scrollbars=yes,toolbar=no,resizable=yes,menubar=no,width=350,height=350,topmargin=0,leftmargin=0");
	dummy.focus();
	
	return false;
}
function openProfileWindow(anchor){
	dummy=window.open(anchor.href,"ProfileWindow", "scrollbars=yes,toolbar=no,resizable=yes,menubar=no,width=700,height=500,topmargin=0,leftmargin=0");
	dummy.focus();
	
	return false;
}

</script>
</head><body style="margin-left:10px;" >


<table border="0" cellpadding=0 cellspacing=0 bgcolor=gray><tr><td>
<table border="0" cellpadding=1 cellspacing=1 width=300>

<tr class=text bgcolor="#ccccff"><td colspan=2 align=center><a href="<%= adminUrl+"/it/imiweb/app/redux/city/CityMonitor" %>" target="<%= target %>"><b>CITY MONITOR INFOS<b></a></td></tr>


<tr class=text>
	<td bgcolor="#ccccff"><b>Monitor On<b></td>
	<td align=center bgcolor="#eeeeff"><%= cityMonitor.isMonitorOn() %></td>
</tr>
<tr class=text>
	<td bgcolor="#ccccff"><b>Sessions Count<b></td>
	<td align=center bgcolor="#eeeeff"><%= cityMonitor.getRegisteredSessionsCount() %></td>
</tr>
<tr class=text>
	<td bgcolor="#ccccff"><b>Monitor Interval<b></td>
	<td align=center bgcolor="#eeeeff"><%= cityMonitor.getMonitorJobInterval() %></td>
</tr>
<tr class=text>
	<td bgcolor="#ccccff"><b>Server Time<b></td>
	<td align=center bgcolor="#eeeeff"><%= new Date(serverInfosHome.getCurrentTimeMillis()) %></td>
</tr>
<form method=get>
<input type="hidden" name="sessionsForm" value="ok">

<c:choose>
	<c:when test="${empty param.showFullStats}" > 
		<tr class=text><td bgcolor="#ccccff"><b>Show Full Stats:</b></td><td bgcolor="#eeeeff" align=center><input type="checkbox" name="showFullStats" value="true" onClick="this.form.submit();"></td></tr>
	</c:when>
	<c:otherwise> 
		<tr class=text><td bgcolor="#ccccff"><b>Show Full Stats:</b></td><td bgcolor="#eeeeff" align=center><input type="checkbox" name="showFullStats" value="true" onClick="this.form.submit();" checked></td></tr>
	</c:otherwise> 
</c:choose>	


<c:choose>
	<c:when test="${empty param.enableMessaging}" > 
		<tr class=text><td bgcolor="#ccccff"><b>Enable Messaging:</b></td><td bgcolor="#eeeeff" align=center><input type="checkbox" name="enableMessaging" value="true" onClick="this.form.submit();"></td></tr>
	</c:when>
	<c:otherwise> 
		<tr class=text><td bgcolor="#ccccff"><b>Enable Messaging:</b></td><td bgcolor="#eeeeff" align=center><input type="checkbox" name="enableMessaging" value="true" onClick="this.form.submit();" checked></td></tr>
	</c:otherwise> 
</c:choose>	



</table>
</td></tr></table>
<br>





<table border="0" cellpadding=0 cellspacing=0 bgcolor="gray"><tr><td>
<table border="0" cellpadding=1 cellspacing=1>
<tr class=text bgcolor="#bbbbff">
	<td bgcolor="white" colspan="<%= (messagingEnabled)?2:1 %>">
		<table border=0 cellspacing=0 cellPadding=0 width=100%><tr align=center>
		<c:choose>
		<c:when test="${param.showSessionInfos == 'show'}">
			<td bgcolor="#ffffcc"><input type="checkbox" name="showSessionInfos" value="show" onClick="this.form.submit();" checked></td>
		</c:when>
		<c:otherwise>
			<td bgcolor="#ffffcc"><input type="checkbox" name="showSessionInfos" value="show" onClick="this.form.submit();"></td>
		</c:otherwise>
		</c:choose>


		<c:choose>
		<c:when test="${param.showWideCommInfos == 'show'}">
			<td bgcolor="#bbffbb"><input type="checkbox" name="showWideCommInfos" value="show" onClick="this.form.submit();" checked></td>
		</c:when>
		<c:otherwise>
			<td bgcolor="#bbffbb"><input type="checkbox" name="showWideCommInfos" value="show" onClick="this.form.submit();" ></td>
		</c:otherwise>
		</c:choose>

		<c:choose>
		<c:when test="${param.showWideCommQueueInfos == 'show'}">
			<td bgcolor="#ccccff"><input type="checkbox" name="showWideCommQueueInfos" onClick="this.form.submit();" value="show" checked></td>
		</c:when>
		<c:otherwise>
			<td bgcolor="#ccccff"><input type="checkbox" name="showWideCommQueueInfos" onClick="this.form.submit();" value="show" ></td>
		</c:otherwise>
		</c:choose>

		</tr></table>
	
	</td>
	
		<c:choose>
		<c:when test="${param.showSessionInfos == 'show'}">
			<td colspan="<%=(messagingEnabled)?10:8%>" align=center bgcolor="#ffffbb"><b>Session Infos<b></td>
		</c:when>
		<c:otherwise></c:otherwise>
		</c:choose>


		<c:choose>
		<c:when test="${param.showWideCommInfos == 'show'}">
			<td colspan=4 align=center bgcolor="#bbffbb" ><b>WideComm<b></td>
		</c:when>
		<c:otherwise></c:otherwise>
		</c:choose>
		<c:choose>
		<c:when test="${param.showWideCommQueueInfos == 'show'}">
			<td colspan=11 align=center ><b>WideComm Queue<b></td>
		</c:when>
		<c:otherwise></c:otherwise>
		</c:choose>
</tr>



<tr class=text bgcolor="#ccccff" >
	<td align=center colspan="<%= (messagingEnabled)?2:1 %>"><b>Session ID<b></td>

	<c:choose>
	<c:when test="${param.showSessionInfos == 'show'}">
		<td align=center bgcolor="#ffffcc"><b>First In<b></td>
		<td align=center bgcolor="#ffffcc"><b>Last In<b></td>
		<td align=center bgcolor="#ffffcc" colspan="<%= (messagingEnabled)?2:1 %>"><b>User<b></td>
		<td align=center bgcolor="#ffffcc" title="Blocked Markets count"><b>BMC<b></td>
		<td align=center bgcolor="#ffffcc"><b>Customer ID<b></td>
		<td align=center bgcolor="#ffffcc" colspan="<%= (messagingEnabled)?2:1 %>"><b>External ID<b></td>
		<td align=center bgcolor="#ffffcc"><b>CC ID<b></td>
		<td align=center bgcolor="#ffffcc"><b>Client Addr.<b></td>
	</c:when>
	<c:otherwise></c:otherwise>
	</c:choose>
	

	<c:choose>
	<c:when test="${param.showWideCommInfos == 'show'}">
		<td align=center bgcolor="#ccffcc" ><b>Last Out<b></td>
		<td align=center bgcolor="#ccffcc" ><b>Bytes Out<b></td>
		<td align=center bgcolor="#ccffcc" ><b>Obj Out<b></td>
		<td align=center bgcolor="#ccffcc" ><b>OAL<b></td>
	</c:when>
	<c:otherwise></c:otherwise>
	</c:choose>
	
	

	<c:choose>
	<c:when test="${param.showWideCommQueueInfos == 'show'}">
		<td align=center title="queue.enqueuedObjectsCount"><b>q.enqObj</b></td>
		<td align=center title="queue.enqueueExceptionsCount"><b>q.enqExc</b></td>
		<td align=center title="queue.mergedObjectsCount"><b>q.mergObj</b></td>
		<td align=center title="Merged Ratio"><b>MR</b></td>
		<td align=center title="queue.writtenObjectsCount"><b>q.wrObj</b></td>
		<td align=center title="queue.writtenMessagesCount"><b>q.wrMsg</b></td>
		<td align=center title="Packed Ratio"><b>PR</b></td>
		<td align=center title="queue.publishingErrorsCount"><b>q.pubErr</b></td>
		<td align=center title="queue.firstQueueSize"><b>q.frstQ</b></td>
		<td align=center title="queue.secondQueueSize"><b>q.scndQ</b></td>
		<td align=center title="Full Stats"> </td>
	</c:when>
	<c:otherwise></c:otherwise>
	</c:choose>

</tr>





<%
for ( int i=0; i<cityMonitor.getRegisteredSessionsList().size(); i++){
	CityTripSession element=(CityTripSession)cityMonitor.getRegisteredSessionsList().elementAt(i);
%>
	<tr class=text bgcolor="#eeeeff">
		<td ><a 
			href="<%= adminUrl+sessionManagerPath+"/"+element.getDynamoSession().getId() %>"
			target="<%= element.getDynamoSession().getId() %>"><%= element.getDynamoSession().getId() %></a></td>
		<% if(messagingEnabled){%>
		<td title="Message Session" align=center><a href="./reduxPTT.jhtml?RECIPIENT_TYPE=SESSION_ID_RECIPIENT&RECIPIENT_ID=<%= element.getDynamoSession().getId() %>" onClick="return openMessageWindow(this);" class="menu-footer">@</a></td>
		<%}%>




		<c:choose>
		<c:when test="${param.showSessionInfos == 'show'}">
			<td bgcolor="#ffffee" title00="Session First Access" align=center title="<%=formatTipDate(""+element.getDynamoSession().getCreationTime())%>"><%=formatTipDate(""+element.getDynamoSession().getCreationTime(), true)%></valueof></td>
			<td bgcolor="#ffffee" title00="Session Last Access" align=center title="<%=formatTipDate(element.getDynamoSession().getLastAccessedTime())%>"><%=formatTipDate(element.getDynamoSession().getLastAccessedTime(), true)%></td>
	
			<td bgcolor="#ffffee" title="Username"	align=left ><a class="text" style="font-weight:normal;" href="./userInfo.jhtml?id=<%=element.getDynamoSession().getId()%>" onClick="return openUserInfoWindow(this);"><%= element.getUserHome().getUserNameForDebugOnly() %></a></td>
			<% if(messagingEnabled){ %>
			<td bgcolor="#ffffee" title="Message User" align=center><a href="./reduxPTT.jhtml?RECIPIENT_TYPE=USER_NAME_RECIPIENT&RECIPIENT_ID=<%=element.getUserHome().getUserNameForDebugOnly() %>" onClick="return openMessageWindow(this);" class="menu-footer">@</a></td>
			<%}%>
			<td bgcolor="#ffffee" title="Blocked Markets Count" align=center><% String[] dummyArr=(String[])element.getUserHome().getBlockedMarkets(); out.println((dummyArr==null)?"-":""+dummyArr.length); %></td>
			<td bgcolor="#ffffee" title="customer Id" align=right><%= element.getUserHome().getCustomerId() %></td>
			<td bgcolor="#ffffee" title="external Id" align=right><a style="font-weight:normal;" href="/internal/redux/conf/redux_conf.jhtml?externalId=<%= element.getUserHome().getExternalId() %>" onClick="return openProfileWindow(this);"><%=element.getUserHome().getExternalId()%></a></td>
			<%if(messagingEnabled){%>
			<td bgcolor="#ffffee" title="Message External ID" align=center><a href="./reduxPTT.jhtml?RECIPIENT_TYPE=EXTERNAL_ID_RECIPIENT&RECIPIENT_ID=<%=element.getUserHome().getExternalId()%>" onClick="return openMessageWindow(this);" class="menu-footer">@</a></td>
			<%}%>
			<td bgcolor="#ffffee" title="Call Center Id" align=right><%=element.getUserHome().getCallCenterId()%></td>
			
			
			<td bgcolor="#ffffee" align=right title="<%=element.getWideCommDynamoHttpServer().getIWClientAddress()%>"><%= element.getWideCommDynamoHttpServer().getClientAddress()%></td>
		</c:when>
		<c:otherwise></c:otherwise>
		</c:choose>
		
	
		<c:choose>
		<c:when test="${param.showWideCommInfos == 'show'}">
			<%it.imiweb.net.widecomm.app.WideCommDynamoHttpServer wideCommHttpServer=
				(it.imiweb.net.widecomm.app.WideCommDynamoHttpServer)element.getWideCommDynamoHttpServer();%>
		
			<!--td align=center title="param:element.wideCommDynamoHttpServer.lastReadTime"><valueof param="element.wideCommDynamoHttpServer.lastReadTime" converter="date"  date="HH:mm:ss"></valueof></td-->
			<td bgcolor="#eeffee" title="WC Last Write Time" align=center title="<%=formatTipDate( element.getWideCommDynamoHttpServer().getLastWriteTime())%>"><%= formatTipDate(element.getWideCommDynamoHttpServer().getLastWriteTime(), true) %></td>
			<td bgcolor="#eeffee" title="WC Bytes Out count" align=right><%= element.getWideCommDynamoHttpServer().getWrittenBytesCount()%></td>
			<td bgcolor="#eeffee" title="WC Objects Out count" align=right ><%= element.getWideCommDynamoHttpServer().getWrittenObjectsCount()%></td>
			<td bgcolor="#eeffee" title="Objects Average Lenght" align=right ><%= (wideCommHttpServer==null || wideCommHttpServer.getWrittenObjectsCount()<=0)?0:wideCommHttpServer.getWrittenBytesCount()/wideCommHttpServer.getWrittenObjectsCount() %></td>
		</c:when>
		<c:otherwise></c:otherwise>
		</c:choose>


		<c:choose>
		<c:when test="${param.showWideCommQueueInfos == 'show'}">
			<%it.imiweb.net.widecomm.WideCommQueue wideCommQueue=
				(it.imiweb.net.widecomm.WideCommQueue)element.getWideCommDynamoHttpServer().getWideCommQueue(); %>
		
			<td title="queue.enqueuedObjectsCount" align=right><%= element.getWideCommDynamoHttpServer().getWideCommQueue().getEnqueuedObjectsCount() %></td>
			<td title="queue.enqueueExceptionsCount" align=right><%= element.getWideCommDynamoHttpServer().getWideCommQueue().getEnqueueExceptionsCount()%></td>
			<td title="queue.mergedObjectsCount" align=right><%= element.getWideCommDynamoHttpServer().getWideCommQueue().getMergedObjectsCount()%></td>
			<td title="Merged Ratio" align=right><%= (wideCommQueue==null || wideCommQueue.getWrittenMessagesCount()<=0)?0:(wideCommQueue.getMergedObjectsCount()*100/wideCommQueue.getWrittenMessagesCount()) %>%</td>			

			<td title="queue.writtenObjectsCount" align=right><%= element.getWideCommDynamoHttpServer().getWideCommQueue().getWrittenObjectsCount()%></td>
			<td title="queue.writtenMessagesCount" align=right><%= element.getWideCommDynamoHttpServer().getWideCommQueue().getWrittenMessagesCount()%></td>
			
			<td title="Packed Ratio" align=right><%= (wideCommQueue==null || wideCommQueue.getWrittenMessagesCount()<=0)?0:(100-(wideCommQueue.getWrittenObjectsCount()*100/wideCommQueue.getWrittenMessagesCount()))%>%</td>			
			
			<td title="queue.publishingErrorsCount" align=right><%= element.getWideCommDynamoHttpServer().getWideCommQueue().getPublishingErrorsCount()%></td>
			<td title="queue.firstQueueSize" align=right><%= element.getWideCommDynamoHttpServer().getWideCommQueue().getFirstQueueSize()%></td>
			<td title="queue.secondQueueSize" align=right><%= element.getWideCommDynamoHttpServer().getWideCommQueue().getSecondQueueSize()%></td>
		
			<td title="Sent Stats">
				<c:choose>
				<c:when test="${param.showFullStats == 'true'}">
					<table border=1 cellspacing=0 cellpadding=0 width=100%>
					
					<% 
					
					for ( Enumeration enum=element.getWideCommDynamoHttpServer().getWideCommStats().getSentStats().keys(); enum.hasMoreElements();){
						String key=(String)enum.nextElement();
						long[][] values=(long[][])element.getWideCommDynamoHttpServer().getWideCommStats().getSentStats().get(key);
					%>
						<tr align=center class=text><td colspan=4><b><%= key %></b></td></tr>
						
						<% for ( int j=0; j<values.length; j++ ){%>
						 	<tr align=right class=text>
						 	<td title="Channel" align=right><%=values[j][0]%></td>
						 	<td title="Sent Objects Count" align=right><%=values[j][1]%></td>
						 	<td title="Real Size" align=right><%=values[j][2]%></td>
						 	<td title="Sent Size" align=right><%=values[j][3]%></td>
						 	</tr>
						 <%}%>
					<%}%>
					</table>
				</c:when>
				<c:otherwise>*</c:otherwise>
				</c:choose>
	
			</td>
		</c:when>
		<c:otherwise></c:otherwise>
		</c:choose>












	</tr>
<%}%>













	
















<%--











</table>
</td></tr></table>
<hr>
<br>
<br>

<!--droplet src="/test/tools/site_dispatcher.jhtml"></droplet-->

</body>
</html>

--%>