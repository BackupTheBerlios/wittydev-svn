<jsp:include page="./admin_login_check.jsp"/>
<link rel=stylesheet href="/common/css/imiweb.css">

<table border="0" cellpadding=0 cellspacing=0 bgcolor="#eeeeff" width="80%">
<form method="post">
<%@ include file="../redux_servers_list.jsp" %> 

<tr><td style="text-align:right;" class="ntext">
	<input type="checkbox" name="newWindow" value="true"> Apri in una nuova finestra |
	<input type="checkbox" name="showHierarchy" value="true" <%=request.getParameter("showHierarchy")!=null && request.getParameter("showHierarchy").equals("true")?"checked":""%>> Mostra la gerarchia completa |
	<input type="checkbox" name="noSearch" value="true" <%=request.getParameter("noSearch")!=null && request.getParameter("noSearch").equals("true")?"checked":""%>> Non mostrare la lista dei server |
	
	<input type="reset" value="Reset" style="width:50;  text-align=center;" class="ntext"> 
	<input type="button" value="Go" style="width:50; text-align=center;" class="ntext" onClick="if(this.form['newWindow'].checked)this.form.target='_blank'; this.form.submit();"></td></tr>
</form></table>
<hr>



<table border="0" cellpadding=1 cellspacing=1 ><tr>
<!--td width=10></td-->
<td bgcolor="#AAAAFF">
<table border="0" cellpadding=1 cellspacing=1>
<tr class=text>	
				<td align=center bgcolor="#224499"><font color="#EEEEEE"><b>&nbsp;&nbsp;</b></font></td>
				<td align=center bgcolor="#224499"  title="DataTable ID"><font color="#EEEEEE"><b>&nbsp;&nbsp;ID&nbsp;&nbsp;</b></font></td>
				<td align=center bgcolor="#224499"  title="Class name"><font color="#EEEEEE"><b>&nbsp;&nbsp;TYPE&nbsp;&nbsp;</b></font></td>
				<td align=center bgcolor="#224499"  title="Listeners"><font color="#EEEEEE"><b>&nbsp;&nbsp;LISTN&nbsp;&nbsp;</b></font></td>
				<td align=center bgcolor="#224499"  title="Cache"><font color="#EEEEEE"><b>&nbsp;&nbsp;CACHE&nbsp;&nbsp;</b></font></td>
				<td align=center bgcolor="#224499" title="Switch"><font color="#EEEEEE"><b>&nbsp;&nbsp;SWITCH&nbsp;&nbsp;</b></font></td>
				<td align=center bgcolor="#224499" title="Channel"><font color="#EEEEEE"><b>&nbsp;&nbsp;CHANNEL&nbsp;&nbsp;</b></font></td>
				<td align=center bgcolor="#224499" title="Description"><font color="#EEEEEE"><b>&nbsp;&nbsp;DESCRIPTION&nbsp;&nbsp;</b></font></td>

				<td align=center bgcolor="#224499" title="Level"><font color="#EEEEEE" title="Level"><b>LV</b></font></td>
				<td align=center bgcolor="#224499" title="is Started"><font color="#EEEEEE"><b>ON</b></font></td>
				<td align=center bgcolor="#224499" title="Start Time"><font color="#EEEEEE"><b>TIME</b></font></td>

				<td align=center bgcolor="#224499" title="Is Pull Mode?"><font color="#EEEEEE"><b>PULL</b></font></td>
				<td align=center bgcolor="#224499" title="Switch Current Index"><font color="#EEEEEE"><b>SW&nbsp;IDX</b></font></td>
				<td align=center bgcolor="#224499" title="Updates Count"><font color="#EEEEEE"><b>UPDATES</b></font></td>
				<td align=center bgcolor="#224499" title="Updates Without Listeners Count"><font color="#EEEEEE"><b>UWLC</b></font></td>
				<td align=center bgcolor="#224499" title="Is Merging Disabled?"><font color="#EEEEEE"><b>MD</b></font></td>
</tr>

<%
	atg.servlet.DynamoHttpServletRequest dReq=(atg.servlet.DynamoHttpServletRequest)request;
	for ( java.util.Enumeration e= servers.keys(); e.hasMoreElements(); ){
		String serverType=(String)e.nextElement();
		dReq.setParameter("serverType", serverType);
		
		String[][] serversArr=(String[][])servers.get(serverType);
		for ( int i=0; i<serversArr.length; i++){
			String id="ckBau_"+serversArr[i][0];
			String dummy=request.getParameter(id);
			if ( dummy !=null && dummy.length()>0){
				String sessHost=request.getParameter(id);
				dReq.setParameter( "currentServerId", id);
				dReq.setParameter("xmlDatastoreAdminPage", sessHost+"/test/redux/jsp/datastore_info/xml/datastore_info_xml.jsp");
				//out.println(request.getParameter("xmlDatastoreAdminPage")+"<br>");
			%>
				<jsp:include page="./datastore_monitor_0.jsp" flush="true"/>
			<% 	out.flush();
				
			}
		}
	}
%>



</table></td></tr></table>		
