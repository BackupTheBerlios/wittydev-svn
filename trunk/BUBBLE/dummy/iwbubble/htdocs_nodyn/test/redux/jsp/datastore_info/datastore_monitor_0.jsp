<%@ page import ="java.util.*,java.io.*,java.net.*,it.imiweb.util.xml.XmlLoader,it.imiweb.util.xml.XmlNode" %> 
<%!
//out.println(request.getParameter("xmlDatastoreAdminPage")+"<br>");
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
	String xmlDatastorePage=request.getParameter("xmlDatastoreAdminPage");
	String adminUrl=null;
	boolean showHierarchy=(request.getParameter("showHierarchy")!=null && request.getParameter("showHierarchy").trim().equalsIgnoreCase("true"));
	if ( xmlDatastorePage!=null)
	try{

		XmlLoader loader = new XmlLoader();
		loader.setXmlPaths( new String[]{xmlDatastorePage});
		loader.load();
		XmlNode rootNode=loader.getXmlRoot();
		Vector rootChildren=rootNode.getChildren();
		//out.println("==>"+rootNode);
		
		XmlNode serverInfoNode=null;
		XmlNode datastoreNode=null;
		
		for ( int i=0; rootChildren!=null && i<rootChildren.size(); i++){
			XmlNode dummyNode=(XmlNode)rootChildren.elementAt(i);
			if ( dummyNode.getType().equalsIgnoreCase("ServerInfo")){
				serverInfoNode=dummyNode;
			}else if ( dummyNode.getType().equalsIgnoreCase("DataStore")){
				datastoreNode=dummyNode;			
			}
		}
		if ( serverInfoNode!=null )adminUrl=(String)serverInfoNode.getAttributes().get("ADMINURL");
		String hostUrl=serverInfoNode==null?null:"http://"+serverInfoNode.getAttributes().get("HOST")+":"+serverInfoNode.getAttributes().get("HTTPPORT");
%>
		
		<tr><td colspan="16" bgcolor="#efefef"><table border="0" cellpacing="1" cellpadding="1" >
			<tr class="ntext" bgcolor="#cdcdcd">
				<td width="180">Srv ID:&nbsp;<%= rootNode.getAttributes().get("ID")%></td>
				<td width="100">DT Count:&nbsp;<%= (datastoreNode==null)?"?":datastoreNode.getAttributes().get("DATATABLESCOUNT")%></td>

				<%if(serverInfoNode!=null){%>
				<td>Time:&nbsp;<%= formatTipDate((String)serverInfoNode.getAttributes().get("SERVERTIME"))%></td>
				<td>Host:&nbsp;<%= serverInfoNode.getAttributes().get("HOST")%></td>
				<td>Port:&nbsp;<%= serverInfoNode.getAttributes().get("HTTPPORT")%></td>
				<%}%>
				<%if(datastoreNode!=null){%>
				<td>DataStore&nbsp;On:&nbsp;<%= datastoreNode.getAttributes().get("DATASTOREON")%></td>
				<%}%>
			</tr>
		</table></td></tr>
<%		
		Vector datastoreChildren=(datastoreNode==null)?null:datastoreNode.getChildren();
		for ( int i=0; datastoreChildren!=null && i<datastoreChildren.size(); i++ ){
			XmlNode dummyNode=(XmlNode)datastoreChildren.elementAt(i);
			
			
			int k=-1;
			while(dummyNode!=null){
				k++;
				String level=(String)dummyNode.getAttributes().get("LEVEL");
				boolean isLevelZero=(level!=null && level.trim().equals("0"));
				String dummy=(String)dummyNode.getAttributes().get("ISCACHEDDATATABLE");
				boolean isCache=(dummy!=null && dummy.equalsIgnoreCase("true"));
				
				dummy=(String)dummyNode.getAttributes().get("ISPULLMODE");
				boolean isPullMode=(dummy!=null && dummy.equalsIgnoreCase("true"));
				
				dummy=(String)dummyNode.getAttributes().get("ISSWITCHABLE");
				boolean isSwitch=(dummy!=null && dummy.equalsIgnoreCase("true"));
				
				dummy=(String)dummyNode.getAttributes().get("ISSTARTED");
				boolean isStarted=(dummy!=null && dummy.equalsIgnoreCase("true"));
	
				dummy=(String)dummyNode.getAttributes().get("ISMERGINGDISABLED");
				boolean isMergingDisabled=(dummy!=null && dummy.equalsIgnoreCase("true"));
	
				
				String className=(String)dummyNode.getAttributes().get("TYPE");
				int pos=className.lastIndexOf('.');
				if (pos>0)className=className.substring(pos+1);

				String serverToshow=request.getParameter("serverType");
				if (serverToshow==null) serverToshow="";
				
				String currentServerId=request.getParameter("currentServerId");
				if ( currentServerId==null ) currentServerId="";
				String sessHost=request.getParameter(currentServerId);
				
				String tableId=(String)dummyNode.getAttributes().get("ID");
				String href=hostUrl+"/test/redux/jsp/datastore_info/datatable_info.jsp?tableId="+tableId+"&tableDepth="+k;
				String hrefCache=hostUrl+"/test/redux/jsp/datastore_info/datatable_info.jsp?tableId="+tableId+"&tableDepth="+k+"&infoType=cache";
				String hrefListeners=hostUrl+"/test/redux/jsp/datastore_info/datatable_info.jsp?tableId="+tableId+"&tableDepth="+k+"&infoType=listeners";
				String hrefSwitch="/test/redux/jsp/datastore_info/datatable_switch_monitor.jsp?tableId="+tableId+"&tableDepth="+k+"&infoType=switch&serverTypeToShow="+serverToshow+"&"+currentServerId+"="+sessHost;
				
				String onClick_0="yoWind=window.open(this.id, 'DataTableInfo_"+tableId+"_"+k+"_info', "+
						"'scrollbars=yes,toolbar=yes,resizable=yes,menubar=no,width=700,height=482,topmargin=0,leftmargin=0');"+
						" yoWind.focus();"+
						" return false;";
				String onClick_1="yoWind=window.open(this.id, 'DataTableInfo_"+tableId+"_"+k+"_listeners', "+
						"'scrollbars=yes,toolbar=yes,resizable=yes,menubar=no,width=700,height=482,topmargin=0,leftmargin=0');"+
						" yoWind.focus();"+
						" return false;";
				String onClick_2="yoWind=window.open(this.id, 'DataTableInfo_"+tableId+"_"+k+"_cache', "+
						"'scrollbars=yes,toolbar=yes,resizable=yes,menubar=no,width=700,height=482,topmargin=0,leftmargin=0');"+
						" yoWind.focus();"+
						" return false;";
	
				String onClick_3="yoWind=window.open(this.id, 'DataTableInfo_"+tableId+"_"+k+"switch', "+
						"'scrollbars=yes,toolbar=no,resizable=yes,menubar=no,width=800,height=500,topmargin=0,leftmargin=0');"+
						" yoWind.focus();"+
						" return false;";


			%>
			
			<tr valign=middle style="font-size:10pt; font-face:arial;" bgcolor="<%=(isLevelZero)?"#CCCCEE":"#EEEEFF"%>" class="ntext">
							<td align=center bgcolor=#AAAAFF title="Index"><font color="#EEEEEE"><%=(isLevelZero)?""+i:""%></font></td>
							<td align=left title="DataTable ID"><a href="<%=href%>" id="<%=href%>" onClick="<%=onClick_0%>"><%=(!isLevelZero)?"":dummyNode.getAttributes().get("ID")%></a></td>
							
							<td align=left title="Class name"><%=className%></td>
							
							<td align=center title="Listeners"><a href="<%=hrefListeners%>" id="<%=hrefListeners%>" onClick="<%=onClick_1%>"><img src="./images/ear_1.gif" border=0 height="20" width="14"></a></td>
	
							<%if (isCache){%>
							<td align=center title="Cache"><a href="<%=hrefCache%>" id="<%=hrefCache%>" onClick="<%=onClick_2%>"><img src="./images/cache_ico.gif" border=0 height="20" widtha="27"></a></td>
							<%}else{%>
							<td align=center title="Cache">-</td>
							<%}%>
							
							<%if (isSwitch){%>
							<td align=center title="Switch"><a href="<%=hrefSwitch%>" id="<%=hrefSwitch%>" onClick="<%=onClick_3%>"><img src="./images/switch_off_lt.gif" border=0  height="20" width="32"></a></td>
							<%}else{%>
							<td align=center title="Switch">-</td>
							<%}%>
	
							
							<td align=center title="Channel"><%=(!isLevelZero)?"":dummyNode.getAttributes().get("CHANNEL")%></td>
							<td align=left ><%=dummyNode.getAttributes().get("DESCRIPTION")%></td>
			
							<td align=center  title="Level"><%=level%></td>
							<td align=center  title="Is Started?"><%=isStarted?"yes":"no"%></td>
					<td align=center title="Start Time:<%=formatTipDate((String)dummyNode.getAttributes().get("STARTTIME"))%>"><%= formatTipDate((String)dummyNode.getAttributes().get("STARTTIME"), true)%></td>
			
							<td align=center title="Is Pull Mode?"><%=isPullMode?"yes":"no"%></td>
							<td align=center title="Current Switch Index"><%=!isSwitch?"-":dummyNode.getAttributes().get("CURRENTSWITCHINDEX")%></td>
							<td align=center title="Updates Count"><%=dummyNode.getAttributes().get("UPDATESCOUNT")%></td>
							<td align=center title="Updates Without Listeners Count"><%=dummyNode.getAttributes().get("UPDATESWITHOUTLISTENERSCOUNT")%></td>
							<td align=center title="Is Merging Disabled?"><%=isMergingDisabled?"yes":"no"%></td>
			</tr>
			<%	
			if ( showHierarchy && dummyNode.getChildren()!=null && dummyNode.getChildren().size()>0)
				dummyNode=(XmlNode)dummyNode.getChildren().elementAt(0);
			else
				dummyNode=null;
			}
		
		}

	}catch(Exception e){
		e.printStackTrace();
		
		out.println("<tr><td colspan=\"16\" bgcolor=\"#efefef\"><font color=red><b>Error: </b>"+e.getMessage()+"</font> in [<a href=\""+xmlDatastorePage+"\" target=\"_blank\">"+xmlDatastorePage+"]</a></td></tr>");
	}
%>















