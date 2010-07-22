<%@ page contentType="text/xml"  import ="
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
%><%response.setContentType("text/xml");%>





<%
	atg.servlet.DynamoHttpServletRequest dReq=(atg.servlet.DynamoHttpServletRequest)request;
	

	
	DataStore dataStore = (DataStore)dReq.resolveName("/it/imiweb/app/redux/datastore/DataStore");
	//(DataStore)dReq.getObjectParameter("dataStore");
	String tableId= request.getParameter("tableId");
	String dummyTableDepth= request.getParameter("tableDepth");
	
	int tableDepth=0;
	
	if (dummyTableDepth!=null && dummyTableDepth.length()>0 ) 
		tableDepth=new Integer(dummyTableDepth).intValue();
	
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
	

%>

<%
	atg.servlet.DynamoHttpServletRequest dRequest = (atg.servlet.DynamoHttpServletRequest)request;
	boolean switchOk=false;
	String message;
	if ( dataTable==null || !(dataTable instanceof it.imiweb.util.datastore.SwitchDataTable)){
		switchOk=false;
		message="DataTable non trovato o non e' di tipo SwitchDataTable";
				
	}else if (request.getParameter("newSwitchIndex")==null || request.getParameter("newSwitchIndex").trim().length()==0) {
		switchOk=false;
		message="Invalid switch, given index is not valid";
	}else{ 
		try{
			int newIndex=Integer.parseInt(request.getParameter("newSwitchIndex"));
			((it.imiweb.util.datastore.SwitchDataTable)dataTable).setCurrentDataTableIndex(newIndex);
			switchOk=true;
			message="DataTable switched: "+((it.imiweb.util.datastore.SwitchDataTable)dataTable).getDescription();
		}catch(Exception e){
			e.printStackTrace();
			message =e.getMessage();
		}
	}
	if ( dataTable!=null)dReq.setParameter("dataTable", dataTable);
	
%>

<SwitchOperation dataTableId="<%=dReq.getParameter("dataTable.id")%>" 
	depth="<%=dReq.getParameter("tableDepth")%>" 
	clazz="<%=dReq.getParameter("dataTable.class.name")%>" 
	newIndex="<%=dReq.getParameter("newSwitchIndex")%>" 
	switchSucceeded="<%=switchOk%>"><%=message%>
	<%if ( dataTable!=null && dataTable instanceof it.imiweb.util.datastore.SwitchDataTable) {
		it.imiweb.util.datastore.SwitchDataTable sdt=(it.imiweb.util.datastore.SwitchDataTable)dataTable;
		for ( int i=0; i<sdt.getDataTablesList().length; i++){
			DataTableImpl dt=(DataTableImpl)sdt.getDataTablesList()[i];
		%>
			<ListedDataTable clazz="<%=dt.getClass().getName()%>" index="<%=i%>" description="<%=dt.getDescription()%>" />
		<%}%>
	<%}%>
	
</SwitchOperation>