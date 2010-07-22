<%@ page import="java.util.*" %>
<table bgcolor="#555555" border=0 cellpadding=0 cellspacing=0>
<tr bgcolor="#EEEEEE" class=text-b style="font-size:12pt;"><th>TABLE METADATA</th></tr>
<tr class=text><td>
<table >
<tr class=text >
	<td bgcolor="#EEEEEE"><b>Id</b></td>
	<td bgcolor="#FFFFEE"><%= request.getParameter("dataTable.tableMetaData.id")%></td>
</tr>
<tr class=text>
	<td bgcolor="#EEEEEE"><b>Description</b></td>
	<td bgcolor="#FFFFEE"><%= request.getParameter("dataTable.tableMetaData.description")%></td>
</tr>
<tr class=text>
	<td bgcolor="#EEEEEE"><b>Transactional Get Available:</b></td>
	<td bgcolor="#FFFFEE"><%= request.getParameter("dataTable.tableMetaData.transactionalGetAvailable")%></td>
</tr>
<tr class=text>
	<td bgcolor="#EEEEEE"><b>Subscriptions Available:</b></td>
	<td bgcolor="#FFFFEE"><%= request.getParameter("dataTable.tableMetaData.subscriptionsAvailable")%></td>
</tr>
<tr class=text>
	<td bgcolor="#EEEEEE"><b>Transactional Filter Available:</b></td>
	<td bgcolor="#FFFFEE"><%= request.getParameter("dataTable.tableMetaData.transactionalFilterAvailable")%></td>
</tr>
<tr class=text>
	<td bgcolor="#EEEEEE"><b>Filter Subscriptions Available:</b></td>
	<td bgcolor="#FFFFEE"><%= request.getParameter("dataTable.tableMetaData.filterSubscriptionsAvailable")%></td>
</tr>
<tr class=text>
	<td bgcolor="#EEEEEE"><b>Transactional Search Available:</b></td>
	<td bgcolor="#FFFFEE"><%= request.getParameter("dataTable.tableMetaData.transactionalSearchAvailable")%></td>
</tr>
<tr class=text>
	<td bgcolor="#EEEEEE"><b>Search Subscriptions Available:</b></td>
	<td bgcolor="#FFFFEE"><%= request.getParameter("dataTable.tableMetaData.searchSubscriptionsAvailable")%></td>
</tr>
<tr class=text>
	<td bgcolor="#EEEEEE"><b>Search Subscriptions Available:</b></td>
	<td bgcolor="#FFFFEE"><%= request.getParameter("dataTable.tableMetaData.searchSubscriptionsAvailable")%></td>
</tr>
	
<tr class=text valign=top><td bgcolor="#EEEEEE"><b>Attributes:</b></td><td bgcolor="#FFFFEE"><table>

<% 
atg.servlet.DynamoHttpServletRequest dReq=(atg.servlet.DynamoHttpServletRequest)request;
Dictionary dict=(Dictionary)dReq.getObjectParameter("dsTrojan.attributes");
for ( Enumeration keys=dict.keys(); keys.hasMoreElements(); ){
	String key=(String)keys.nextElement();
	Object value=dict.get(key);

%>	
	<tr style="font-size:9pt;" class=text><td ><b><i><%= key%></i></b></td><td bgcolor="#FFEEFF"><%= value%></td></tr>
<%}%>
	</table></td></tr>
</table>

</td></tr></table>



<br>



<table bgcolor="#555555" border=0 cellpadding=0 cellspacing=0>
<tr bgcolor="#EEEEEE" class=text-b style="font-size:12pt;"><th>COLUMNS METADATA</th></tr>
<tr class=text><td>

<table >
	<tr bgcolor="#EEEEEE" class=text>
		<th>ID</th>
		<th>Key</th>
		<th>Description</th>
		<th>Type</th>
		<th>Attributes</th>
	</tr>
<!--droplet bean="ForEach">
<param name="array" value="param:dataTable.tableMetaData.columnsMetaData">
<oparam name="output"-->
<% 
Object[] cmds=(Object[])dReq.getObjectParameter("dataTable.tableMetaData.columnsMetaData");

for( int j=0; j<cmds.length; j++){
	dReq.setParameter("elementA", cmds[j]);
	Dictionary attributes=
		((it.imiweb.util.datastore.DataStoreTrojan)dReq.getObjectParameter("dsTrojan"))
			.getColumnAttributes(j);
		//dReq.setParameter("attributes", attributes);
%>
	<tr bgcolor="#FEFEEE" class=text>
		<td bgcolor="#EEEEEE"><%= request.getParameter("elementA.id")%></td>
		<td><%= request.getParameter("elementA.key")%></td>
		<td><%= request.getParameter("elementA.description")%></td>
		<td><%= request.getParameter("elementA.type")%></td>
		<td style="font-size:8pt;">

		<% for ( Enumeration attrKeys=attributes.keys(); attributes!=null  && attrKeys.hasMoreElements(); ){
			String attrKey=(String)attrKeys.nextElement();
			Object attrVal=attributes.get(attrKey);
		%>
		[<%= attrKey %>=<%= attrVal%>]
		<%}%>
		</td>
	</tr>
<% }%>

</table>
</td></tr></table>

<%
	if ( dReq.getObjectParameter("dataTable") instanceof it.imiweb.util.datastore.ListDataTable ){
%>

<table border=1>
	<tr><th>INDEX</th>
		<th>List Length</td></th>

	<% 
	int[] listsLength=(int[])dReq.getObjectParameter("dataTable.listsLengthCache");
	for ( int h=0; h<listsLength.length; h++){%>
	<tr><td><%= h %></td>
		<td><%= listsLength[h]%></td></tr>
	
	<%}%>
<table>
<%}%>