<jsp:include page="./admin_login_check.jsp"/>
<%!
	boolean isUnset( String str){
		return str==null || (str.length()==0);
	}
%>
<table border=0 cellspacing=0 cellpadding=0 style="font-size:11pt;" bgcolor="#EEEEFF">
<tr valign=top><td colspan=2 class="text">
	<table 	border=0 cellspacing=0 cellpadding=0 style="font-size:11pt;">
	<tr><td><li> <b>DataTable Id:</b></td><td> &nbsp;&nbsp;<i><%= request.getParameter("dataTable.id") %></i></td></tr>
	<tr><td><li> <b>DataTable Class Name:</b></td><td> &nbsp;&nbsp;<i><%= request.getParameter("dataTable.class.name") %></i></td></tr>
	<tr><td><li> <b>DataTable Depth:</b></td><td> &nbsp;&nbsp;<i><%= request.getParameter("tableDepth")%></i></td></tr>
	</table>
</td></tr>

<form method=get onSubmit="this.action='/test/redux/datastore_info/datatable_info_cache.jsp'">

<tr><td>
	<table 	border=0 cellspacing=0 cellpadding=0 style="font-size:11pt;">

	<input type=hidden name="tableDepth" value="<%= request.getParameter("tableDepth")%>">
	<input type=hidden name="infoType" value="<%= request.getParameter("infoType")%>">
	<input type=hidden name="tableId" value="<%= request.getParameter("tableId")%>">
	<% if ( isUnset(request.getParameter("showUpdatesCount"))  ) {%>
		<tr><td><b>Show Updates Count:</b></td><td><input type="checkbox" name="showUpdatesCount" value="true" onClick="this.form.submit();"></td></tr>
	<%}else{%>
		<tr><td><b>Show Updates Count:</b></td><td><input type="checkbox" name="showUpdatesCount" value="true" onClick="this.form.submit();" checked></td></tr>
	<%}%>
	<% if ( isUnset(request.getParameter("showDescriptions"))  ) {%>
		<tr><td><b>Show Descriptions:</b></td><td><input type="checkbox" name="showDescriptions" value="true" onClick="this.form.submit();"></td></tr>
	<%}else{%>
		<tr><td><b>Show Descriptions:</b></td><td><input type="checkbox" name="showDescriptions" value="true" onClick="this.form.submit();" checked></td></tr>
	<%}%>
	<% if ( isUnset(request.getParameter("showClassTypes"))  ) {%>
		<tr><td><b>Show Class Types:</b></td><td><input type="checkbox" name="showClassTypes" value="true" onClick="this.form.submit();"></td></tr>
	<%}else{%>
		<tr><td><b>Show Class Types:</b></td><td><input type="checkbox" name="showClassTypes" value="true" onClick="this.form.submit();" checked></td></tr>
	<%}%>
	<% if ( isUnset(request.getParameter("showResubscriber"))  ) {%>
		<tr><td><b>Show Resubscribers:</b></td><td align=left><input type="checkbox" name="showResubscriber" value="true" onClick="this.form.submit();"></td></tr>
	<%}else{%>
		<tr><td><b>Show Resubscribers:</b></td><td><input type="checkbox" name="showResubscriber" value="true" onClick="this.form.submit();" checked></td></tr>
	<%}%>
	</table>
</td></tr>

<%String dummy;%>
<% dummy=request.getParameter("filterFieldId"); if (dummy!=null && dummy.length()>0  ){%><input type="hidden" value="<%=dummy%>" name="filterFieldId"><%}%>
<% dummy=request.getParameter("filterMatchType"); if (dummy!=null && dummy.length()>0  ){%><input type="hidden" value="<%=dummy%>" name="filterMatchType"><%}%>
<% dummy=request.getParameter("filterValue"); if (dummy!=null && dummy.length()>0  ){%><input type="hidden" value="<%=dummy%>" name="filterValue"><%}%>
<% dummy=request.getParameter("doFilter"); if (dummy!=null && dummy.length()>0  ){%><input type="hidden" value="<%=dummy%>" name="doFilter"><%}%>
<% dummy=request.getParameter("filterGetAll"); if (dummy!=null && dummy.length()>0  ){%><input type="hidden" value="<%=dummy%>" name="filterGetAll"><%}%>


</form>
	
</table>

<jsp:include page="./datatable_info_cache_filter.jsp" flush="true"/>
<!--droplet src="datatable_info_cache_filter.jhtml"-->
</droplet>



<br>
<font class=text-b style="font-size:12pt; color:#000000;">Live Cache </font>

<%
atg.servlet.DynamoHttpServletRequest dReq=(atg.servlet.DynamoHttpServletRequest)request;
dReq.setParameter("cacheType", "LIVE");
dReq.setParameter("cacheElement", dReq.getObjectParameter("dsTrojan.liveCache"));
%>
<jsp:include page="./datatable_info_cache_1.jsp" flush="true"/>

<br>
<font class=text-b style="font-size:12pt; color:#000000;" >Timed Cache </font>
<%
dReq.setParameter("cacheType", "TIMED");
dReq.setParameter("cacheElement", dReq.getObjectParameter("dsTrojan.timedCache"));
%>
<jsp:include page="./datatable_info_cache_1.jsp" flush="true"/>





<!--droplet src="datatable_info_cache_1.jhtml">
	<param name="cacheType" value="LIVE">
	<param name="cacheElement" value="param:dsTrojan.liveCache">
</droplet>
<br>
<font class=text-b style="font-size:12pt; color:#000000;" >Timed Cache </font>
<droplet src="datatable_info_cache_1.jhtml">
	<param name="showResubscriber" value="false">
	<param name="cacheType" value="TIMED">
	<param name="cacheElement" value="param:dsTrojan.timedCache">
</droplet-->
