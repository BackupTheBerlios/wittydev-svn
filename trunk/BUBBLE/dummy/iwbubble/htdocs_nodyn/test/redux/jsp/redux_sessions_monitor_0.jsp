<%@ page import ="java.util.*,java.io.*,java.net.*,it.imiweb.util.xml.XmlLoader,it.imiweb.util.xml.XmlNode" %> 
<%!
	static java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	static java.text.SimpleDateFormat df_short = new java.text.SimpleDateFormat("hh:mm:ss");
	String getPage(String urlPage)throws Exception{
		URL url=new URL(urlPage);
		URLConnection conn=url.openConnection();

	        BufferedReader bufR=new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String allLines="";
	        String line;
		/*
	        int count=-1;
	        char[] b;
		while ( (count=conn.getInputStream().available())>=0 ){
			if (b==null || b.length<count)b=new byte[count];
			count=conn.getInputStream().read(b);
			allLines+=new String(b, 0, count);
	        
	        }*/
	        while((line=bufR.readLine())!=null){
	            allLines+=line+"\n";
	            
	        }
	        conn.getInputStream().close();
	        return allLines;
		
	}

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
				return df.format(new Date(time));//+" ["+time+"]";
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
<%	
	String xmlSessPage=request.getParameter("xmlSessAdminPage");
	String adminUrl=null;
	String sessionsManagerPath=null;
	if ( xmlSessPage!=null)
	try{
		XmlLoader loader = new XmlLoader();
		loader.setXmlPaths( new String[]{xmlSessPage});
		loader.load();
		XmlNode rootNode=loader.getXmlRoot();
		Vector rootChildren=rootNode.getChildren();
		XmlNode serverInfoNode=null;
		XmlNode cityMonitorNode=null;
		for ( int i=0; rootChildren!=null && i<rootChildren.size(); i++){
			XmlNode dummyNode=(XmlNode)rootChildren.elementAt(i);
			if ( dummyNode.getType().equalsIgnoreCase("ServerInfo")){
				serverInfoNode=dummyNode;
			}else if ( dummyNode.getType().equalsIgnoreCase("CityMonitor")){
				cityMonitorNode=dummyNode;			
			}
		}
		if ( serverInfoNode!=null )adminUrl=(String)serverInfoNode.getAttributes().get("ADMINURL");
		if ( serverInfoNode!=null )sessionsManagerPath=(String)serverInfoNode.getAttributes().get("SESSIONSMANAGERPATH");
%>

		<tr><td colspan="24" bgcolor="#efefef"><table border="0" cellpacing="1" cellpadding="1" >
			<tr class="ntext" bgcolor="#cdcdcd">
				<td width="180">Srv ID:&nbsp;<%= rootNode.getAttributes().get("ID")%></td>
				<td width="100">Sess.&nbsp;Count:&nbsp;<%= (cityMonitorNode==null)?"?":cityMonitorNode.getAttributes().get("SESSIONSCOUNT")%></td>

				<%if(serverInfoNode!=null){%>
				<td>Time:&nbsp;<%= formatTipDate((String)serverInfoNode.getAttributes().get("SERVERTIME"))%></td>
				<td>Host:&nbsp;<%= serverInfoNode.getAttributes().get("HOST")%></td>
				<td>Port:&nbsp;<%= serverInfoNode.getAttributes().get("HTTPPORT")%></td>
				<%}%>
				<%if(cityMonitorNode!=null){%>
				<td>Monitor&nbsp;On:&nbsp;<%= cityMonitorNode.getAttributes().get("MONITORON")%></td>
				<td>Interval:&nbsp;<%= cityMonitorNode.getAttributes().get("MONITORJOBINTERVAL")%></td>
				<%}%>
			</tr>
			
		
		</table></td></tr>
		<%//if(cityMonitorNode!=null && (request.getParameter("serverInfoOnly")==null || !request.getParameter("serverInfoOnly").equals("true"))  ){
		   if(cityMonitorNode!=null && request.getParameter("showAllSessionsInfo")!=null &&  request.getParameter("showAllSessionsInfo").equals("true")  ){
			Vector monitorChildren=cityMonitorNode.getChildren();
			XmlNode cityTripSessions=null;
			for ( int i=0; monitorChildren!=null && i<monitorChildren.size(); i++){
				XmlNode dummyNode=(XmlNode)monitorChildren.elementAt(i);
				if ( dummyNode.getType().equalsIgnoreCase("cityTripSessions")){
					cityTripSessions=dummyNode;
					break;
				}
			}

			if ( cityTripSessions!=null ){
				Vector sessChildren=cityTripSessions.getChildren();
				for ( int i=0; sessChildren!=null && i<sessChildren.size(); i++){
					Vector cityTripSessionChildren=((XmlNode)sessChildren.elementAt(i)).getChildren();
					XmlNode sessionInfo=null;
					XmlNode wideCommInfo=null;
					XmlNode wideCommQueueInfo=null;
					for ( int j=0; cityTripSessionChildren!=null && j<cityTripSessionChildren.size(); j++){
						XmlNode dummyNode=(XmlNode)cityTripSessionChildren.elementAt(j);
						if ( dummyNode.getType().equalsIgnoreCase("sessionInfo")){
							sessionInfo=dummyNode;
						}else if ( dummyNode.getType().equalsIgnoreCase("wideCommInfo")){
							 wideCommInfo=dummyNode;
						}else if ( dummyNode.getType().equalsIgnoreCase("wideCommQueueInfo")){
							wideCommQueueInfo=dummyNode;
						}
					
					}
					
					
				%>



				
	<tr class=text bgcolor="#eeeeff">
		<td ><a 
			href="<%= adminUrl+sessionsManagerPath+"/"+sessionInfo.getAttributes().get("ID") %>"
			target="<%= sessionInfo.getAttributes().get("ID") %>"><%= sessionInfo.getAttributes().get("ID") %></a></td>




			<td bgcolor="#ffffee" title00="Session First Access" align=center title="<%=(String)sessionInfo.getAttributes().get("CREATIONTIME")%>"><%=formatTipDate((String)sessionInfo.getAttributes().get("CREATIONTIME"), true)%></valueof></td>
			<td bgcolor="#ffffee" title00="Session Last Access" align=center title="<%=(String)sessionInfo.getAttributes().get("LASTACCESSEDTIME")%>"><%=formatTipDate((String)sessionInfo.getAttributes().get("LASTACCESSEDTIME"), true)%></td>
	
			<td bgcolor="#ffffee" title="Username"	align=left ><a class="text" style="font-weight:normal;" href="./userInfo.jhtml?id=<%=sessionInfo.getAttributes().get("ID")%>" onClick="return openUserInfoWindow(this);"><%= sessionInfo.getAttributes().get("USERNAME") %></a></td>
			<td bgcolor="#ffffee" title="Blocked Markets Count" align=center><%= sessionInfo.getAttributes().get("BLOCKEDMARKETSCOUNT") %></td>
			<td bgcolor="#ffffee" title="customer Id" align=right><%= sessionInfo.getAttributes().get("CUSTOMERID") %></td>
			<td bgcolor="#ffffee" title="external Id" align=right><a style="font-weight:normal;" href="/internal/redux/conf/redux_conf.jhtml?externalId=<%=sessionInfo.getAttributes().get("EXTERNALID")%>" onClick="return openProfileWindow(this);"><%=sessionInfo.getAttributes().get("EXTERNALID")%></a></td>
			<td bgcolor="#ffffee" title="Call Center Id" align=right><%=sessionInfo.getAttributes().get("CALLCENTERID")%></td>
			
			
			<td bgcolor="#ffffee" align=right title="<%=sessionInfo.getAttributes().get("CLIENTIWADDRESS")%>"><%= sessionInfo.getAttributes().get("CLIENTADDRESS")%></td>
		
	
		
			<td bgcolor="#eeffee" title="WC Last Write Time" align=center title="<%=formatTipDate((String)wideCommInfo.getAttributes().get("LASTWRITETIME"))%>"><%= formatTipDate((String)wideCommInfo.getAttributes().get("LASTWRITETIME"), true) %></td>
			<td bgcolor="#eeffee" title="WC Bytes Out count" align=right><%= wideCommInfo.getAttributes().get("WRITTENBYTESCOUNT")%></td>
			<td bgcolor="#eeffee" title="WC Objects Out count" align=right ><%= wideCommInfo.getAttributes().get("WRITTENOBJECTSCOUNT")%></td>
			<td bgcolor="#eeffee" title="Objects Average Lenght" align=right ><%= wideCommInfo.getAttributes().get("OBJECTSAVERADGELENGTH") %></td>


		
			<td title="queue.enqueuedObjectsCount" align=right><%= wideCommQueueInfo.getAttributes().get("ENQUEUEDOBJECTSCOUNT") %></td>
			<td title="queue.enqueueExceptionsCount" align=right><%= wideCommQueueInfo.getAttributes().get("ENQUEUEEXCEPTIONSCOUNT")%></td>
			<td title="queue.mergedObjectsCount" align=right><%= wideCommQueueInfo.getAttributes().get("MERGEDOBJECTSCOUNT")%></td>
			<td title="Merged Ratio" align=right><%= wideCommQueueInfo.getAttributes().get("MERGEDRATIO") %>%</td>			

			<td title="queue.writtenObjectsCount" align=right><%= wideCommQueueInfo.getAttributes().get("WRITTENOBJECTSCOUNT")%></td>
			<td title="queue.writtenMessagesCount" align=right><%= wideCommQueueInfo.getAttributes().get("WRITTENMESSAGESCOUNT")%></td>
			
			<td title="Packed Ratio" align=right><%= wideCommQueueInfo.getAttributes().get("PACKEDRATIO")%>%</td>			
			
			<td title="queue.publishingErrorsCount" align=right><%= wideCommQueueInfo.getAttributes().get("PUBLISHINGERRORSCOUNT")%></td>
			<td title="queue.firstQueueSize" align=right><%= wideCommQueueInfo.getAttributes().get("FIRSTQUEUESIZE")%></td>
			<td title="queue.secondQueueSize" align=right><%= wideCommQueueInfo.getAttributes().get("SECONDQUEUESIZE")%></td>
		
			<td title="Sent Stats">
				<%--c:choose>
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
				</c:choose --%>
	
			</td>

	</tr>





				
				
				<%
				
			}}
	}%>
		
		
<%
	}catch(Exception e){
		e.printStackTrace();
		
		out.println("<tr><td colspan=\"24\" bgcolor=\"#efefef\"><font color=red><b>Error: </b>"+e.getMessage()+"</font> in [<a href=\""+xmlSessPage+"\" target=\"_blank\">"+xmlSessPage+"]</a></td></tr>");
	}
	//out.println(getPage("http://iwbtest.lan.iw:9980/test/redux/jsp/xml_admin/redux_sessions_xml.jsp"));
%>
