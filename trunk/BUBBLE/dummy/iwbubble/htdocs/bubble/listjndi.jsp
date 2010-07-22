<%@ page import ="java.util.*,javax.naming.*" %> 
<link rel=stylesheet href="/common/css/imiweb.css">
<span class="ntext">

<%
//System.out.println(System.getProperties());
String jndiPath=request.getParameter("jndiPath"); 
try{
	InitialContext initCtx=new InitialContext();
%>
<form>
Domains: 
[<a href="./listjndi.jsp">InitialContext</a>]  - 
[<a href="./listjndi.jsp?jndiPath=java:">java:</a>] - 
[<a href="./listjndi.jsp?jndiPath=bubble:">bubble:</a>]<br>
Jndi url: <input type="text" value="<%=jndiPath==null?"":jndiPath%>" name="jndiPath" size="100"></form><hr>
<%

Object obj=null;
if (jndiPath==null || (jndiPath=jndiPath.trim()).length()==0){
	jndiPath="";
	obj=initCtx;
}else{
	try{
		obj=initCtx.lookup(jndiPath);
	}catch( Exception e ){
		out.println("Invalid object ["+jndiPath+"]: "+e.getMessage());
	}
}

try{
	javax.naming.CompositeName cName=new javax.naming.CompositeName(jndiPath);
	String nav_path="";
	String dummyCurrentPath="";
	int pos;
	if ( cName.size()>0 && (pos=cName.get(0).indexOf(':'))>0){
		String pre=cName.get(0).substring(0, pos+1);
		String post=cName.get(0).substring(pos+1);
		cName.remove(0);
		cName.add(0, post);
		cName.add(0, pre);
	} 
	for ( int i=0; i<cName.size(); i++){
		String dummyEl=cName.get(i);
		
		if ( dummyEl!=null  && dummyEl.length()>0){
			dummyCurrentPath+=dummyEl;
			nav_path+="<a href=\"./listjndi.jsp?jndiPath="+dummyCurrentPath+"\">"+dummyEl+"</a>"+(((i+1)<cName.size())?"/":"");
			dummyCurrentPath+=(((i+1)<cName.size())?"/":"");
		}
	}
	out.println(nav_path);
}catch(Exception e){
	out.println( "Not a compositeName ["+e.getMessage()+"]: "+ jndiPath);
}


if ( obj!=null){
	if ( obj instanceof Context ){
		Context ctx=(Context)obj;
		javax.naming.NamingEnumeration enum=ctx.list("");
		while ( enum.hasMore() ){
			javax.naming.NameClassPair ncp=(javax.naming.NameClassPair)enum.nextElement();
			String href="./listjndi.jsp?jndiPath="+((ncp.isRelative())?jndiPath+"/"+ncp.getName():ncp.getName());
			out.println("<li class=\"lista\"><a href=\""+href+"\">"+ncp.getName() +"</a> [clazz:"+ncp.getClassName()+"][relative:"+ncp.isRelative()+"]");
		}
	}
	out.println("<hr>");
	request.setAttribute("_ck_bean_", obj);
	%>
	<jsp:include page="./listbeans_object_info.jsp" flush="true"/>
	<%
}
%>

<%
}catch(Exception e){
out.println(e);	
}
%>
</span>