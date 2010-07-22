<jsp:include page="./admin_login_check.jsp"/>
<%@ page import ="java.util.*,java.io.*,java.net.*,it.imiweb.util.xml.XmlLoader,it.imiweb.util.xml.XmlNode" %> 
<link rel=stylesheet href="/common/css/imiweb.css">
<%
	String switchIntervalVar = request.getParameter("switchInterval");
	long switchInterval=0;
	if ( switchIntervalVar==null ) switchIntervalVar="0";
	try{
		switchInterval=Integer.parseInt(switchIntervalVar);
	}catch(Exception e){e.printStackTrace();}
%>

<table border="0" cellpadding=0 cellspacing=0 bgcolor="#eeeeff" width="80%">
<form method="get">
<%@ include file="../redux_servers_list.jsp" %> 

<tr><td style="text-align:right;" class="ntext">
	Intervallo frà 2 Switch (in sec.):
	<input type="test" class="ntext" style="text-align:right;width=30;"  size="2" name="switchInterval" value="<%=""+switchInterval%>">  | 
	
	<input type="checkbox" name="noSearch" value="true" <%=request.getParameter("noSearch")!=null && request.getParameter("noSearch").equals("true")?"checked":""%>> Non mostrare la lista dei server |
	<input type="reset" value="Reset" style="width:50;  text-align=center;" class="ntext"> 
	<input type="button" value="Go" style="width:50; text-align=center;" class="ntext" onClick="this.form.submit();"></td></tr>
<input type ="hidden" name="tableId" value="<%=request.getParameter("tableId")==null?"":request.getParameter("tableId")%>">
<input type ="hidden" name="tableDepth" value="<%=request.getParameter("tableDepth")==null?"":request.getParameter("tableDepth")%>">
<input type ="hidden" name="serverTypeToShow" value="<%=request.getParameter("serverTypeToShow")==null?"":request.getParameter("serverTypeToShow")%>">
</table>
<hr>

<br>

<table border=0 cellspacing=0 cellpadding=0 style="font-size:11pt;" bgcolor="#EEFFEE">
<tr valign=top class=text><td colspan=2>
	<table 	border=0 cellspacing=0 cellpadding=0 style="font-size:11pt;">
	<tr><td><li> <b>DataTable Id:</b></td><td> &nbsp;&nbsp;<i><%= request.getParameter("tableId")%></i></td></tr>
	<tr><td><li> <b>DataTable Depth:</b></td><td> &nbsp;&nbsp;<i><%= request.getParameter("tableDepth")%></i></td></tr>
	</table>
</td></tr>
</table>


<%
	String tableId=request.getParameter("tableId");
	String tableDepth=request.getParameter("tableDepth");
	if ( tableDepth==null)tableDepth="0";
	if ( tableId==null || tableId.length()==0 ){%>
		<hr>Nessun DataTable Specificato	<hr>
	<%}else{
		String newSwitchIndex =request.getParameter("newSwitchIndex");
		
		if ( newSwitchIndex!=null && newSwitchIndex.length()>0 ){
			for ( java.util.Enumeration e= servers.keys(); e.hasMoreElements(); ){
				String serverType=(String)e.nextElement();
				String[][] serversArr=(String[][])servers.get(serverType);
				for ( int i=0; i<serversArr.length; i++){
					String id="ckBau_"+serversArr[i][0];
					String dummy=request.getParameter(id);
					if ( dummy !=null && dummy.length()>0){
						String sessHost=request.getParameter(id);
						String xmlSessPage=sessHost+"/test/redux/jsp/datastore_info/xml/datatable_switch_xml.jsp?tableId="+tableId+"&tableDepth="+tableDepth+"&newSwitchIndex="+newSwitchIndex;
						//out.println(request.getParameter("xmlDatastoreAdminPage")+"<br>");
						try{
							XmlLoader loader = new XmlLoader();
							loader.setXmlPaths( new String[]{xmlSessPage});
							loader.load();
							XmlNode rootNode=loader.getXmlRoot();
							String dummySucc = (String)rootNode.getAttributes().get("SWITCHSUCCEEDED");
							if ( dummySucc!=null && dummySucc.equals("true") ){%>
								<li class="ntext" style="color: green;"> <a href="<%=xmlSessPage%>" target="_blank"><%=sessHost%></a> [OK]: <%=rootNode.getText()%><br>
							<%}else{%>
								<li class="ntext" style="color: red;"> <a href="<%=xmlSessPage%>" target="_blank"><%=sessHost%></a> [ERROR]: <%=rootNode.getText()%><br>
							<%}
			
						}catch(Exception exc){
							exc.printStackTrace();%>
							<li class="ntext" style="color: red;"> [<a href="<%=xmlSessPage%>" target="_blank"><%=sessHost%></a>]: [EXCEPTION]: <%=exc.getMessage()%><br>
							<%
						}
						out.flush(); 
						try{	
							//if ( switchInterval>0 )Thread.currentThread().sleep(switchInterval*1000);
							for ( int k=0; k<switchInterval; k++){
								out.println(".");
								out.flush();
								if ( switchInterval>0 )Thread.currentThread().sleep(1000);
							}
						}catch(Exception e2){}


						%>
						
							
						
					<% 	
					}
				}
			}
		}	

		%>
		<br>
		<select name="newSwitchIndex">
		<option name="">
		<%
		boolean found=false;
		for ( java.util.Enumeration e= request.getParameterNames(); e.hasMoreElements(); ){
			String paramName=(String)e.nextElement();
			if ( paramName.indexOf("ckBau_")==0){
				String sessHost=request.getParameter(paramName);
	
				String xmlSessPage=sessHost+"/test/redux/jsp/datastore_info/xml/datatable_switch_xml.jsp?tableId="+tableId+"&tableDepth="+tableDepth;
				try{
					XmlLoader loader = new XmlLoader();
					loader.setXmlPaths( new String[]{xmlSessPage});
					loader.load();
					XmlNode rootNode=loader.getXmlRoot();
					Vector rootChildren=rootNode.getChildren();
					for ( int i=0; rootChildren!=null && i<rootChildren.size(); i++){
						
						XmlNode dummyNode=(XmlNode)rootChildren.elementAt(i);
						System.out.println(dummyNode.getType());
						if ( dummyNode.getType().equalsIgnoreCase("ListedDataTable")){%>
							
							<option value="<%=dummyNode.getAttributes().get("INDEX")%>"><%=dummyNode.getAttributes().get("DESCRIPTION")%>
						<%}
					}
	
				}catch(Exception exc){exc.printStackTrace(); }
				found=true;
				break;
				
			}
				
		}%>
		</select>

		<%if (!found){%>
				Scegliere almeno un server attivo !
		<%}

	}
%>

</form>