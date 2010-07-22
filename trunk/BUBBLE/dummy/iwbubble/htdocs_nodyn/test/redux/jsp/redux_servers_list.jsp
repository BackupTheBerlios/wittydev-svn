<%!
	it.imiweb.util.OrderedDictionary servers=new it.imiweb.util.OrderedDictionary();
	{
		servers.put("Sviluppo",new String[][]
						{{"sviluppo-8548", "http://sviluppo:8548", "1"}} );
	
		servers.put("Test",new String[][]
						{
							{"iwbtest-8545", "http://iwbtest:8545", "1"},
							{"tomcat-iwbtest-9980", "http://iwbtest:9980", "1"},
							{"jboss-iwbtest-4480", "http://iwbtest:4480", "1"}
						} );
	
		servers.put("Prod_Old",new String[][]
						{
							{"d1-8548", "http://dynamo1:8548", "1"},
							{"d1-8549", "http://dynamo1:8549", "1"},
							{"d1-8648", "http://dynamo1:8648", "1"},
							{"d2-8548", "http://dynamo2:8548", "0"},
							{"d2-8549", "http://dynamo2:8549", "0"},
							{"d3-8548", "http://dynamo3:8548", "1"},
							{"d3-8549", "http://dynamo3:8549", "1"},
							{"d4-8548", "http://dynamo4:8548", "1"},
							{"d4-8549", "http://dynamo4:8549", "1"},
							{"d5-8548", "http://dynamo5:8548", "1"},
							{"d5-8549", "http://dynamo5:8549", "1"},
							{"d5-8648", "http://dynamo5:8648", "1"}
						} );

		servers.put("Bl_Ext_Tr",new String[][]
						{
							{"bt01-8548", "http://bl-tr01:8548", "1"},
							{"bt01-8549", "http://bl-tr01:8549", "1"},
							{"bt02-8548", "http://bl-tr02:8548", "1"},
							{"bt02-8549", "http://bl-tr02:8549", "1"},
							{"bt03-8548", "http://bl-tr03:8548", "1"},
							{"bt03-8549", "http://bl-tr03:8549", "1"},
							{"bt04-8548", "http://bl-tr04:8548", "1"},
							{"bt04-8549", "http://bl-tr04:8549", "1"}
						} );

		servers.put("Bl_Ext_Info",new String[][]
						{
							{"bi01-8648", "http://bl-info01:8648", "1"},
							{"bi01-8649", "http://bl-info01:8649", "1"},
							{"bi02-8648", "http://bl-info02:8648", "1"},
							{"bi02-8649", "http://bl-info02:8649", "1"},
							{"bi03-8648", "http://bl-info03:8648", "1"},
							{"bi03-8649", "http://bl-info03:8649", "1"},
							{"bi04-8648", "http://bl-info04:8648", "1"},
							{"bi04-8649", "http://bl-info04:8649", "1"},
							{"bi05-8648", "http://bl-info05:8648", "1"},
							{"bi05-8649", "http://bl-info05:8649", "1"},
							{"bi06-8648", "http://bl-info06:8648", "1"},
							{"bi06-8649", "http://bl-info06:8649", "1"},
							{"bi07-8648", "http://bl-info07:8648", "1"},
							{"bi07-8649", "http://bl-info07:8649", "1"},
							{"bi08-8648", "http://bl-info08:8648", "1"},
							{"bi08-8649", "http://bl-info08:8649", "1"},
							{"bi09-8648", "http://bl-info09:8648", "1"},
							{"bi09-8649", "http://bl-info09:8649", "1"},
							{"bi10-8648", "http://bl-info10:8648", "1"},
							{"bi10-8649", "http://bl-info10:8649", "1"},
							{"bi11-8648", "http://bl-info11:8648", "1"},
							{"bi11-8649", "http://bl-info11:8649", "1"},
							{"bi12-8648", "http://bl-info12:8648", "1"},
							{"bi12-8649", "http://bl-info12:8649", "1"},
							{"bi13-8648", "http://bl-info13:8648", "1"},
							{"bi13-8649", "http://bl-info13:8649", "1"},
							{"bi14-8648", "http://bl-info14:8648", "1"},
							{"bi14-8649", "http://bl-info14:8649", "1"},
						} );           


		servers.put("Bl_Int_Tr",new String[][]
						{
							{"biwt01-8548", "http://bl-iwtr01:8548", "1"},
							{"biwt01-8549", "http://bl-iwtr01:8549", "0"},
							{"biwi01-8548", "http://bl-iwinfo01:8548", "1"},
							{"biwi01-8549", "http://bl-iwinfo01:8549", "0"}
						} );

		servers.put("Bl_Int_Info",new String[][]
						{
							{"biwt01-8648", "http://bl-iwtr01:8648", "1"},
							{"biwt01-8649", "http://bl-iwtr01:8649", "0"},
							{"biwi01-8648", "http://bl-iwinfo01:8648", "1"},
							{"biwi01-8649", "http://bl-iwinfo01:8649", "0"}
						} );
	}				

%>


<script>
<%
	
	for ( java.util.Enumeration e= servers.keys(); e.hasMoreElements(); ){
		String serverType=(String)e.nextElement();
		String[][] serversArr=(String[][])servers.get(serverType);
		out.println( "function selectAll_"+serverType+"(yoCheck){");
		for (int i=0; i<serversArr.length; i++){
			if ( serversArr[i].length>2 && serversArr[i][2].length()>0 && serversArr[i][2].equals("1")){
				out.println( "yoCheck.form[\"ckBau_"+serversArr[i][0]+"\"].checked=yoCheck.checked;");
			}
		}
		out.println( "}" );
	}
%>
</script>



<% if ( request.getParameter("noSearch")==null || !request.getParameter("noSearch").equals("true") ){%>
<tr><td>
<%
	String serverTypeListToShow=request.getParameter("serverTypeToShow");
	for ( java.util.Enumeration e=servers.keys(); e.hasMoreElements(); ){
		String serverType=(String)e.nextElement();
		String [][] serversArr=(String [][])servers.get(serverType);
		if ( serverTypeListToShow==null || serverTypeListToShow.length()==0 || serverTypeListToShow.equalsIgnoreCase(serverType )){
			%>
			<table border="0" cellpadding=0 cellspacing=1 width="100%">
			<tr bgcolor="#ccccff"><td class="ntext" width="100" style="vertical-align:top;"><input type="checkbox" name="true" onClick="selectAll_<%=serverType%>(this);"><b><%=serverType%></b></td>
			<td class="ntext" style="text-align:left;">
			<%
			for ( int j=0; j<serversArr.length; j++){
				String id="ckBau_"+serversArr[j][0];
				boolean checked=false;
				if ( request.getParameter(id)!=null && request.getParameter(id).length()>0)checked=true;	
			%>
				<font style="white-space:nowrap;"><input type="checkBox" name="<%=id%>" value="<%=serversArr[j][1]%>" <%=checked?"checked":""%>><%=serversArr[j][0]%></font>
			<%}
			%></td></tr></table><%
		}		
	}
%>
</td></tr>
<%}else{
	for ( java.util.Enumeration e= request.getParameterNames(); e.hasMoreElements(); ){
		String paramName=(String)e.nextElement();
		if ( paramName.indexOf("ckBau_")==0){
			String sessHost=request.getParameter(paramName);
			out.println("<input type=\"hidden\" name=\""+paramName+"\" value=\""+sessHost+"\">");
		}
	}
}%>
