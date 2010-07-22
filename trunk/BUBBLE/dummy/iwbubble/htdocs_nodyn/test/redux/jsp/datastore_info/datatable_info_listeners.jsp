<%@ page import="java.util.*" %>
<table border=0 cellspacing=0 cellpadding=0><tr><td>
<table border=0 cellspacing=0 cellpadding=0 style="font-size:11pt;" bgcolor="#EEFFEE">
<tr valign=top class=text><td colspan=2>
	<table 	border=0 cellspacing=0 cellpadding=0 style="font-size:11pt;">
	<tr><td><li> <b>DataTable Id:</b></td><td> &nbsp;&nbsp;<i><%= request.getParameter("dataTable.id")%></i></td></tr>
	<tr><td><li> <b>DataTable Class Name:</b></td><td> &nbsp;&nbsp;<i><%= request.getParameter("dataTable.class.name")%></i></td></tr>
	<tr><td><li> <b>DataTable Depth:</b></td><td> &nbsp;&nbsp;<i><%= request.getParameter("tableDepth") %></i></td></tr>
	</table>
</td></tr>
</table>
<br>
<% 
	int tot;
	tot=0;
 %>	

<table border=0 cellspacing=0 cellpadding=0 style="font-size:11pt;" bgcolor="#FFFFEE" width=100%>
<tr valign=top bgcolor="#AAAAAA" class=menu-main><th>Registration Type</th><th>Registered keys</th></tr>
<% 
	atg.servlet.DynamoHttpServletRequest dReq=(atg.servlet.DynamoHttpServletRequest)request;
	Object[] v = (Object[])dReq.getObjectParameter("dsTrojan.channelsGroups.forStats_InternalChannelsGroups");
	for ( int i=0; v!=null && i<v.length; i++ ){
		dReq.setParameter("elementA", v[i]);
	%>
	
		<tr valign=top><td bgcolor=#EEEEEE class=text><%= request.getParameter("elementA.id") %></td>
		<td>
		<table border=1 cellspacing=0 cellpadding=0 width=100% style="font-size:11pt;">
		<tr class=text-b><th></th><th>Key</th><th>Listeners count</th></tr>
		
		<%	
		Dictionary dict = (Dictionary)dReq.getObjectParameter("elementA.forStats_Channels");
		int count=0;
		for ( Enumeration e=dict.elements(); dict!=null && e.hasMoreElements(); count++){
			dReq.setParameter("elementB", e.nextElement() );
		%>
		
		<!--droplet bean="ForEach">
		<param name="array" value="param:element.forStats_Channels">
		<oparam name="output"-->
			<% tot+= (( Vector )dReq.getObjectParameter("elementB.forStats_Listeners")).size(); %>
			<tr class=text style="text-align:center;">
			<td ><%= count%></td>
			<td ><%= request.getParameter("elementB.forStats_ChannelKeys")%></td>
			<td align=center ><%= request.getParameter("elementB.forStats_ListenersCount")%></td>
			</tr>
		<%}%>
		<tr class=text ><td colspan=2 ><b>Total subscriptions</b></td><td align=center><%= tot %></td></tr>
		</table>
<%
	}
%>	
	</td></tr>

</td></tr></table>