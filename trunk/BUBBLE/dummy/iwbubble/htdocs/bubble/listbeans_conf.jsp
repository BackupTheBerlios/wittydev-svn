<link rel=stylesheet href="/common/css/imiweb.css">
<%
	it.imiweb.beanworld.config.ConfigEntry configEntry;
	it.imiweb.beanworld.config.ConfigPropertiesFile[] configFiles;
	String beanPath= request.getParameter("path");
	it.imiweb.beanworld.bubble.servlet.http.BubbleHttpServletRequest bReq= 
		(it.imiweb.beanworld.bubble.servlet.http.BubbleHttpServletRequest)request;

	if ( beanPath==null ) beanPath="";
	if ( beanPath==null || beanPath.length()==0 || beanPath.equals("/") ) {
		configEntry=bReq.getWebArchitect().getConfiguration();
	}else{
		try{
			configEntry=bReq.getWebArchitect().getConfigEntry(beanPath);
		}catch(Exception e ){
			e.printStackTrace();
			configEntry=null;
		}
	}
	if ( configEntry==null ){
		out.println("Configuration not found!");
	}else {
		configFiles=configEntry.getConfigFiles();

%>
<a class="title-ora">Bean Infos:</a><br><br>
<table cellpadding="0" cellspacing="0" bgcolor="#5555aa"><tr><td>
<table cellpadding="1" cellspacing="1"  >
<tr bgcolor="#CCCCFE"><td class="ntext-b">Name</td><td class="ntext" bgcolor="#ffffee"><%= configEntry.getName() %></td></tr>
<tr bgcolor="#CCCCFE"><td class="ntext-b">Scope</td><td class="ntext" bgcolor="#ffffee"><%= configEntry.getScope() %></td></tr>
<tr bgcolor="#CCCCFE"><td class="ntext-b">Type</td><td class="ntext" bgcolor="#ffffee"><%= configEntry.getType() %></td></tr>
<tr bgcolor="#CCCCFE"><td class="ntext-b">Loaded Time</td><td class="ntext" bgcolor="#ffffee"><%= new java.util.Date(configEntry.getLoadedTime()) %></td></tr>
</table></td><tr></table>

<br><hr width=20%><a class="title-ora">Final Configuration:</a><br><br>
<table cellpadding="0" cellspacing="0" bgcolor="#5555aa"><tr><td>
<table cellpadding="1" cellspacing="1" >
<tr  class="ntext-b" style="text-align:center;" bgcolor="#CCCCFE">
	<td>Property</td><td>Type</td><td>String value</td>
</tr>
<%
	for ( java.util.Enumeration e=configEntry.getPropertiesConf().elements(); e.hasMoreElements(); ){
		it.imiweb.beanworld.config.ConfigProperty conf= (it.imiweb.beanworld.config.ConfigProperty)e.nextElement();
%>
<tr bgcolor="#ffffff">
	<td class=ntext-b><%= conf.getName() %></td>
	<td class=ntext style="text-align:center;"><%= conf.isReference()?"REF":(conf.isJNDIReference()?"JNDI":"VAL") %></td>
	<td class=ntext><%= conf.getStringValue() %></td>
</tr>
<%
	}
%></table></td></tr></table><%

%><br><hr width=20%><a class="title-ora">Configuration files:</a><br><br><%
		for ( int i=0; configFiles!=null && i<configFiles.length; i++ ){
			String publish="";	
			
			%>


<table border=1 bgcolor="#CCCCFE" cellspacing=0 cellpadding=0><tr>
<td>
	<table width=100% cellspacing=0 cellpadding=0 bgcolor="#AAAAFE"><tr><td>
	<table width=100% cellspacing=1 cellpadding=1>
	<tr bgcolor="#CCCCFE">
		<td class=ntext-b width=100>File Path:</td>
		<td class=ntext bgcolor="#ffffee" ><%= configFiles[i].getRootContextPath() + ((configFiles[i].getRootContextPath()!=null && !configFiles[i].getRootContextPath().endsWith("/"))?"/":"") + configFiles[i].getPath() %></td>
	</tr>
	<tr bgcolor="#CCCCFE">
		<td class=ntext-b width=100>Last Modified:</td>
		<td class=ntext bgcolor="#ffffee"><%= new java.util.Date(configFiles[i].getRefFileTime()) %></td>
	</tr>
	</table></td></tr></table>
</td></tr>
<tr><td class=ntext bgcolor="#ffffff"><pre><%= configFiles[i].getContent() %></pre></tr></td></tr>
</table>
<br>
			<%
		}
		
	}
	
%>
