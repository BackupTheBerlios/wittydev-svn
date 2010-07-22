<!--importbean bean="/it/imiweb/app/redux/datastore/IWDataStoreNetServer">
<importbean bean="/it/imiweb/app/redux/datastore/DataStore">


<droplet src="datatable_info_1.jhtml">
	<param name="IWDataStoreNetServer" value="bean:IWDataStoreNetServer">
	<param name="dataStore" value="bean:DataStore">
</droplet-->
<link rel=stylesheet href="/common/css/imiweb.css">
<%
	atg.servlet.DynamoHttpServletRequest dReq=(atg.servlet.DynamoHttpServletRequest)request;
	dReq.setParameter("dataStore", dReq.resolveName("/it/imiweb/app/redux/datastore/DataStore"));
	dReq.setParameter("IWDataStoreNetServer", dReq.resolveName("/it/imiweb/app/redux/datastore/IWDataStoreNetServer"));
%>
<jsp:include page="./datatable_info_1.jsp" flush="true"/>