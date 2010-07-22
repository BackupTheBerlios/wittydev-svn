<%@ page import ="
			it.imiweb.util.datastore.DataStore,
			it.imiweb.util.datastore.DataTable,
			it.imiweb.util.datastore.DataTableImpl,
			it.imiweb.util.datastore.PipedDataTable,
			it.imiweb.util.datastore.CachedDataTable,
			it.imiweb.util.datastore.DataTableListener,
			it.imiweb.util.datastore.InternalDataRow,
			it.imiweb.util.datastore.DataStoreTrojan,
			it.imiweb.util.datastore.impl.IWDataStoreNetServer,
			it.imiweb.util.datastore.impl.reuters.wrappers.IWReutersBookDataTableImpl,
			it.imiweb.util.datastore.ChannelsGroups,
			it.imiweb.util.datastore.DataFilter,
			it.imiweb.util.DataTools,
			
			it.imiweb.util.SpecializedQueue,
			it.imiweb.util.Enqueuer,
			it.imiweb.util.SpecializedPublisher,
			it.imiweb.util.datastore.DataRow,
			it.imiweb.util.datastore.ColumnMetaData"
%>





<%
	atg.servlet.DynamoHttpServletRequest dReq=(atg.servlet.DynamoHttpServletRequest)request;
	
	DataStore dataStore = (DataStore)dReq.getObjectParameter("dataStore");
	String tableId= request.getParameter("tableId");
	String dummyTableDepth= request.getParameter("tableDepth");
	
	int tableDepth=0;
	if (dummyTableDepth!=null) tableDepth=new Integer(dummyTableDepth).intValue();
	
	DataTable dataTable=null;
	try {
	 dataTable = dataStore.getDataTable(tableId);
	}catch(it.imiweb.util.IWException iwe){}
	for ( int i =0;  i<tableDepth; i++){
		if (dataTable instanceof PipedDataTable ){
			dataTable=((PipedDataTable)dataTable).getDataTable();
			if (i==tableDepth) break;
		}else{
			out.println("R u crazy???");
			return;
		}
	}	
	DataStoreTrojan dsTrojan = new DataStoreTrojan();
	dsTrojan.setDataTable(dataTable);
	dReq.setParameter("dataTable", dataTable);
	dReq.setParameter("dsTrojan", dsTrojan);

%>

<% 
	String infoType=request.getParameter("infoType");
	if ( infoType == null )infoType="";
	if ( infoType.equals("listeners") ){
%>
	<jsp:include page="./datatable_info_listeners.jsp" flush="true"/>
<%}else if ( infoType.equals("cache") ){%>
	<jsp:include page="./datatable_info_cache.jsp" flush="true"/>
<%}else if ( infoType.equals("switch") ){%>
	booh
<%}else {%>
	<jsp:include page="./datatable_info_metadata.jsp" flush="true"/>
<%}%>

<!--droplet bean="Switch">
<param name="value" value="param:infoType">
<oparam name="listeners">
	<droplet src="datatable_info_listeners.jhtml">
	<param name="dataStore" value="param:dataStore">
	<param name="dataTable" value="param:dataTable">
	<param name="dsTrojan" value="param:dsTrojan">
	</droplet>
</oparam>
<oparam name="cache">
	<droplet src="datatable_info_cache.jhtml">
	<param name="dataStore" value="param:dataStore">
	<param name="dataTable" value="param:dataTable">
	<param name="dsTrojan" value="param:dsTrojan">
	</droplet>
</oparam>
<oparam name="switch">
	<droplet src="datatable_switch.jhtml">
	<param name="dataStore" value="param:dataStore">
	<param name="dataTable" value="param:dataTable">
	</droplet>
</oparam>
<oparam name="default">
	<droplet src="datatable_info_metadata.jhtml">
	<param name="dataStore" value="param:dataStore">
	<param name="dataTable" value="param:dataTable">
	<param name="dsTrojan" value="param:dsTrojan">
	</droplet>
</oparam>
</droplet--->
