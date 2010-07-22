<jsp:include page="./admin_login_check.jsp"/>


<link rel=stylesheet href="/common/css/imiweb.css">

<!--jsp:include page="./redux_servers_list.jsp" flush="true"/-->
<table border="0" cellpadding=0 cellspacing=0 bgcolor="#eeeeff" width="80%">
<form method="post">
<%@ include file="./redux_servers_list.jsp" %> 
<tr><td style="text-align:right;" class="ntext">
	<input type="checkbox" name="newWindow" value="true"> Apri in una nuova finestra |
	<input type="checkbox" name="showAllSessionsInfo" value="true" <%=request.getParameter("showAllSessionsInfo")!=null && request.getParameter("showAllSessionsInfo").equals("true")?"checked":""%>> Mostrare il dettaglio delle sessioni |
	<input type="checkbox" name="noSearch" value="true" <%=request.getParameter("noSearch")!=null && request.getParameter("noSearch").equals("true")?"checked":""%>> Non mostrare la lista dei server |
	
	<input type="reset" value="Reset" style="width:50; text-align:center;" class="ntext"> 
	<input type="button" value="Go" style="width:50; text-align:center;" class="ntext" onClick="if(this.form['newWindow'].checked)this.form.target='_blank'; this.form.submit();"></td></tr>
</form></table>
<hr>


<table border="0" cellpadding=0 cellspacing=0 bgcolor="gray"><tr><td>
<table border="0" cellpadding=1 cellspacing=1>
<tr class=text bgcolor="#ccccff" >				
				<td align=center ><b>Session ID</b></td>
				<td align=center bgcolor="#ffffcc"><b>First In</b></td>
				<td align=center bgcolor="#ffffcc"><b>Last In</b></td>
				<td align=center bgcolor="#ffffcc"><b>User</b></td>
				<td align=center bgcolor="#ffffcc" title="Blocked Markets count"><b>BMC</b></td>
				<td align=center bgcolor="#ffffcc"><b>Customer ID</b></td>
				<td align=center bgcolor="#ffffcc"><b>External ID</b></td>
				<td align=center bgcolor="#ffffcc"><b>CC ID</b></td>
				<td align=center bgcolor="#ffffcc"><b>Client Addr.</b></td>
			
		
				<td align=center bgcolor="#ccffcc" ><b>Last Out</b></td>
				<td align=center bgcolor="#ccffcc" ><b>Bytes Out</b></td>
				<td align=center bgcolor="#ccffcc" ><b>Obj Out</b></td>
				<td align=center bgcolor="#ccffcc" ><b>OAL</b></td>
			
			
		
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
</tr>

<%
	//for ( java.util.Enumeration e= request.getParameterNames(); e.hasMoreElements(); ){
	//	String paramName=(String)e.nextElement();
	//	if ( paramName.indexOf("ckBau_")==0){
	//		String sessHost=request.getParameter(paramName);
			//out.println("<li>"+request.getParameter(paramName));
	for ( java.util.Enumeration e= servers.keys(); e.hasMoreElements(); ){
		String serverType=(String)e.nextElement();
		String[][] serversArr=(String[][])servers.get(serverType);
		for ( int i=0; i<serversArr.length; i++){
			String id="ckBau_"+serversArr[i][0];
			if ( request.getParameter(id) !=null && request.getParameter(id).length()>0){
				String sessHost=request.getParameter(id);
				((atg.servlet.DynamoHttpServletRequest)request).setParameter("xmlSessAdminPage", sessHost+"/test/redux/jsp/xml_admin/redux_sessions_xml.jsp?showAllSessionsInfo="+request.getParameter("showAllSessionsInfo"));
			%>
				<jsp:include page="./redux_sessions_monitor_0.jsp" flush="true"/>
			
			<% 	out.flush();
				
			}
		}
	}
%>



</table></td></tr></table>		
