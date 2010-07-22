<%@ page import="it.imiweb.util.datastore.ColumnMetaData"%>
<table>
<form onSubmit="this.action='./datatable_info.jsp'">
<tr class="text">

<%! String filterMatchType; %>
<% 
	atg.servlet.DynamoHttpServletRequest dReq=(atg.servlet.DynamoHttpServletRequest)request;
	filterMatchType=request.getParameter("filterMatchType"); 
	String showDescriptions=request.getParameter("showDescriptions"); 
	String filterValue=request.getParameter("filterValue"); 
	String filterFieldId= request.getParameter("filterFieldId");
	String showUpdatesCount= request.getParameter("showUpdatesCount");
	String showResubscriber= request.getParameter("showResubscriber");

	if (showDescriptions==null)showDescriptions=""; 
	if (filterValue==null)filterValue=""; 
	if (filterFieldId==null)filterFieldId=""; 
	if (showUpdatesCount==null)showUpdatesCount=""; 
	if (showResubscriber==null)showResubscriber=""; 
	

	
%>
<td>ID:</td><td><select name="filterFieldId" class="text">
	<option value="filterCacheId">Cache Id
	<% 
	ColumnMetaData[] cmds=(ColumnMetaData[])dReq.getObjectParameter("dataTable.tableMetaData.columnsMetaData");
	for ( int i=0;i<cmds.length; i++){
		if ( filterFieldId.equals(cmds[i].getId()) ) {
			if ( showDescriptions.length()==0){%>
				<option value="<%=cmds[i].getId()%>" selected><%=cmds[i].getId()%>
			<%}else{%>
				<option value="<%=cmds[i].getId()%>" selected><%=cmds[i].getDescription()%>
			<%}%>
		<%}else if ( showDescriptions.length()!=0){%>
				<option value="<%=cmds[i].getId()%>"><%=cmds[i].getId()%>
			
		<%}%>
	<%}%>
	</select>
</td>
<td>Match:</td><td>
	<select name="filterMatchType" class="text">
		<option value="0" <%=filterMatchType!=null && filterMatchType.equals("0")?"selected":""%> >Exact
		<option value="1" <%=filterMatchType!=null && filterMatchType.equals("1")?"selected":""%> >Begins With
		<option value="2" <%=filterMatchType!=null && filterMatchType.equals("2")?"selected":""%> >Ends With
		<option value="3" <%=filterMatchType!=null && filterMatchType.equals("3")?"selected":""%> >Contains
	</select>
</td>

<td>Value:</td><td><input name="filterValue" type="text" class="text" value="<%=filterValue%>" > </td>
<td><input type="submit" class="text" value="GO" name="doFilter"> </td>
<td><input type="submit" class="text" value="ALL" name="filterGetAll"> </td>


</tr>
<input type="hidden" name="tableDepth" value="<%=request.getParameter("tableDepth")%>">
<input type="hidden" name="infoType" value="<%=request.getParameter("infoType")%>">
<input type="hidden" name="tableId" value="<%=request.getParameter("tableId")%>">

<%if ( showDescriptions.length()>0){%>
	<input type="hidden" name="showDescriptions" value="<%=showDescriptions%>">
<%}%>
<%if ( showUpdatesCount.length()>0){%>
	<input type="hidden" name="showUpdatesCount" value="<%=showUpdatesCount%>">
<%}%>
<%if ( showResubscriber.length()>0){%>
	<input type="hidden" name="showResubscriber" value="<%=showResubscriber%>">
<%}%>

</form>



</table>
