<%@ page contentType="text/xml" import =" java.util.*, 
it.imiweb.app.redux.server.city.ServerInfosHome, 
it.imiweb.util.datastore.DataStore, it.imiweb.util.datastore.DataTable, 
it.imiweb.util.datastore.DataTableImpl, it.imiweb.util.datastore.PipedDataTable, 
it.imiweb.util.datastore.CachedDataTable, 
it.imiweb.util.datastore.impl.IWDataStoreNetServer, 
it.imiweb.util.datastore.SwitchDataTable" 
%><%response.setContentType("text/xml");%>






<%! 
	String adminUrl; String target; 
	ServerInfosHome serverInfosHome;
	DataStore dataStore;
	Dictionary channelsMap;

	String doDataTableXmlInfo(String id, DataTable dataTable){
		return doDataTableXmlInfo(id, dataTable, 0);
	}
	String doDataTableXmlInfo(String id, DataTable dataTable, int level){
		if (level>100) return "<Error level=\""+level+"\"/>";
		boolean switchable=( dataTable!=null && dataTable instanceof SwitchDataTable && 
					((SwitchDataTable)dataTable).getDataTablesList()!=null &&
					((SwitchDataTable)dataTable).getDataTablesList().length>0);

		
		String result="\n<DataTable 	id=\""+id+"\""+ 
				" startTime=\""+dataTable.getStartTime()+"\""+
				" isStarted=\""+dataTable.isStarted()+"\""+
				" isPullMode=\""+dataTable.isPullMode()+"\""+
				" level=\""+level+"\""+
				" channel=\""+channelsMap.get(id)+"\""+
				" type=\""+dataTable.getClass().getName()+"\""+
				" isCachedDataTable=\""+(dataTable instanceof CachedDataTable)+"\""+
				" isSwitchDataTable=\""+(dataTable instanceof SwitchDataTable)+"\""+
				" isSwitchable=\""+switchable +"\""+
				((dataTable instanceof SwitchDataTable)?" currentSwitchIndex=\""+((SwitchDataTable)dataTable).getCurrentDataTableIndex()+"\"":"")+
				((dataTable instanceof SwitchDataTable && ((SwitchDataTable)dataTable).getDataTablesList()!=null)?" switchDataTablesCount=\""+((SwitchDataTable)dataTable).getDataTablesList().length+"\"":"")+
				" description=\""+(dataTable instanceof DataTableImpl?((DataTableImpl)dataTable).getDescription():dataTable.getTableMetaData().getDescription())+"\""+
				((dataTable instanceof DataTableImpl)?" updatesWithoutListenersCount=\""+((DataTableImpl)dataTable).getUpdatesWithoutListenersCount()+"\"":"")+
				((dataTable instanceof DataTableImpl)?" updatesCount=\""+((DataTableImpl)dataTable).getUpdatesCount()+"\"":"")+
				((dataTable instanceof DataTableImpl)?" isMergingDisabled=\""+((DataTableImpl)dataTable).isMergingDisabled()+"\"":"")+
				">\n";
				
			if ( dataTable instanceof PipedDataTable && ((PipedDataTable)dataTable).getDataTable()!=null) 
				result+=doDataTableXmlInfo( id, ((PipedDataTable)dataTable).getDataTable(), ++level);
		
		result+="\n</DataTable>";
		return result;
	
	}

%>
<%
	atg.servlet.DynamoHttpServletRequest dRequest = (atg.servlet.DynamoHttpServletRequest)request;
	if ( serverInfosHome == null )
		serverInfosHome=(ServerInfosHome)dRequest.resolveName("/it/imiweb/app/redux/city/ServerInfosHome");
	if ( dataStore == null )
		dataStore=(DataStore)dRequest.resolveName("/it/imiweb/app/redux/datastore/DataStore");

	if (channelsMap==null)
		channelsMap=((IWDataStoreNetServer)dRequest.resolveName("/it/imiweb/app/redux/datastore/IWDataStoreNetServer")).getDataTablesIds2ChannelsMap();

		
	if (adminUrl==null){
		String adminPort=""+request.getServerPort(); 	//request.getParameter("adminPort");
		String adminHost=request.getServerName(); 	//request.getParameter("adminHostName");
		adminUrl="http://"+adminHost+":"+adminPort+"/bubble/listbeans.jsp?path=";
		target=adminPort;
	}	
%>

<DataStoreInfo id="<%="ds_"+request.getServerName()+"_"+request.getServerPort()%>">
	<ServerInfo host="<%=request.getServerName()%>" httpPort="<%=request.getServerPort()%>" adminUrl="<%=adminUrl%>" 
			serverTime="<%=serverInfosHome.getCurrentTimeMillis()%>" />

	<DataStore  	datastoreOn="<%=(dataStore==null)?"":""+dataStore.isStarted()%>" 
			datatablesCount="<%=(dataStore==null && dataStore.getDataTablesIds()!=null )?"":""+dataStore.getDataTablesIds().length%>">
		<% for ( int i=0; dataStore!=null && i<dataStore.getDataTablesIds().length; i++) { 
			DataTable dataTable=dataStore.getDataTable(dataStore.getDataTablesIds()[i]);		
		%>
		<%=doDataTableXmlInfo(dataStore.getDataTablesIds()[i], dataTable)%>
		
		<%}%>
		
				
	</DataStore>

</DataStoreInfo>

