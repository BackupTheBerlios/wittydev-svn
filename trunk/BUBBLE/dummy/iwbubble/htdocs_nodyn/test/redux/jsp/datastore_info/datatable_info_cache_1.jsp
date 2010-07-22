<%@ page import="
	java.util.*,
	it.imiweb.util.datastore.ColumnMetaData,
	it.imiweb.util.datastore.DataTable,
	it.imiweb.util.datastore.InternalDataRow"%>


<script>


<%!
	static java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yy hh:mm:ss");
	static java.text.SimpleDateFormat df_short = new java.text.SimpleDateFormat("hh:mm:ss");

	String formatTipDate(String dateLong ){
		return formatTipDate(dateLong, false);
	}
	String formatTipDate(long time ){
		return formatTipDate(time, false);
	}
	String formatTipDate(long time, boolean shortFormat ){
		try{
			if ( shortFormat )
				return df_short.format(new Date(time));
			else	
				return df.format(new Date(time));//+" ["+time+"]";
		}catch(Exception e){
			e.printStackTrace();
			return ""+time;
		}
	
	}
	String formatTipDate(String dateLong, boolean shortFormat ){
		if (dateLong==null) return "[null]";
		try{
			long time=new Long (dateLong).longValue();
			return  formatTipDate(time, shortFormat);
			
		}catch(Exception e){
			e.printStackTrace();
			return dateLong;
		}
		
	}	
%>








<%try{%>
<!--
	function checkMe(checkVal, checkType){
		dummy = checkType.toLowerCase();
		//alert(checkVal.length);
		yoForm=checkVal.form;
		for ( i=0; i<yoForm.length; i++){
			//alert(yoForm[0].name);
			if ( yoForm[i].name.toLowerCase().indexOf(dummy) == 0 ){
				yoForm[i].checked=checkVal.checked;
			}
		}
	}
-->
</script>

<form name="dudetteForm" action="/test/redux/datastore_info/direct_subscribe.jsp" target="_blank" method="post">


<% 

	atg.servlet.DynamoHttpServletRequest dReq=(atg.servlet.DynamoHttpServletRequest)request;
 	String tableId=request.getParameter("tableId");
	String showResubscriber=request.getParameter("showResubscriber");
	String showClassTypes=request.getParameter("showClassTypes");
	String showDescriptions=request.getParameter("showDescriptions");
	String showUpdatesCount=request.getParameter("showUpdatesCount");
	if ( showResubscriber==null) showResubscriber="";
	if ( tableId==null) tableId="";
	if ( showClassTypes==null ) showClassTypes="";
	if ( showDescriptions==null)showDescriptions="";
	if ( showUpdatesCount==null)showUpdatesCount="";
	
	if ( showResubscriber.equals("true") ){
		if ( tableId.equals("BOOK") ){%>
			<input type="submit" name="doBook" value="Risottoscrivi Book" class="ntext">
		<%}else if ( tableId.equals("STOCK_PRICE") ){%>
			<input type="submit" name="doPrice" value="Risottoscrivi Prezzi" class="ntext">
		<%}else {%>
			<b>Resubscription available only for Book And Stock_Price!</b>
		<%}
	}%>
	

<table border=0 width =100% cellspacing=0 cellpadding=0 bgcolor=black>
<tr><td>
<table border=0 width =100% cellspacing=1 cellpadding=0>
	
	<% if (showClassTypes.equals("true")){%>
		<tr class="menu-main" bgcolor=#FFFFFF>
		<% if ( !showResubscriber.equals("true") ){%>
			<td colspan=3></td >
		<%}else{%>
			<td colspan=4></td >
		<%}%>
		
		<% 
		ColumnMetaData[] cmds=(ColumnMetaData[])dReq.getObjectParameter("dataTable.tableMetaData.columnsMetaData");
		for ( int i=0; i<cmds.length; i++ ){
			ColumnMetaData elementA=cmds[i];
		%>
			<td bgcolor="#AAAAAA" title="<%= elementA.getType()%>" align=center>&nbsp;<b><%= elementA.getType()%></valueof></b>&nbsp;</td>
		<%}%>
		</tr>		
	<%}%>




<tr class="menu-main" bgcolor=#FFFFFF>
	<% if (showResubscriber.equals("true")) {%>
		<td title="Seleziona tutti"><input type="checkbox" onClick="checkMe(this, 'instrument_id_');"></td >
	<%}%>
	<td title="row #" style="color:#000000;"><b>#</b></td>
	<td title="Cache ID" style="color:#000000;"><b>Cache ID</b></td>
	<td title="Last update Time" style="color:#000000;"><b>Time</b></td>

	<% 		
	ColumnMetaData[] cmds=(ColumnMetaData[])dReq.getObjectParameter("dataTable.tableMetaData.columnsMetaData");
		
	for ( int i=0; i<cmds.length; i++ ){
		if ( showDescriptions.length()==0 ){%>
		<td bgcolor="#AAAAAA" title="<%=cmds[i].getDescription()%>" align=center>&nbsp;<b><%=cmds[i].getId()%></b>&nbsp;</td>
		<%}else{%>
		<td bgcolor="#AAAAAA" title="<%=cmds[i].getId()%>" align=center>&nbsp;<b><%=cmds[i].getDescription()%></b>&nbsp;</td>
		<%}
	}%>
	
</tr>
<%
	String filter=(String)request.getParameter("filter");
	String fieldId=request.getParameter("filterFieldId");
	String matchType=request.getParameter("filterMatchType");
	String dummyFilterValue=request.getParameter("filterValue");
	String[] filterValues; 
	

	Dictionary hash1=(Dictionary)dReq.getObjectParameter("cacheElement");
	if  ( request.getParameter("filterGetAll")!=null ){
		// do nothing
	} else if  ( request.getParameter("doFilter")==null || fieldId==null || dummyFilterValue==null || 
			dummyFilterValue.length()==0 || fieldId.length()==0){
		dReq.setParameter("cacheElement", new Hashtable());
	} else if (hash1!=null ){
		if ( dummyFilterValue.endsWith("||") )dummyFilterValue=dummyFilterValue.substring(0, dummyFilterValue.length()-2);
		StringTokenizer tokenizer = new StringTokenizer(dummyFilterValue, "||");
		filterValues= new String[tokenizer.countTokens()];
		int kk=0;
		while ( tokenizer.hasMoreTokens() )
			filterValues[kk++]=tokenizer.nextToken();
		Hashtable hasht= new Hashtable();
		for (int pp=0; pp<filterValues.length; pp++){
			String filterValue=filterValues[pp];
			
			
			if ( fieldId.equals("filterCacheId") ){
				//non facio niente
				Object dummy;
				if ( hash1!=null && (dummy=hash1.get(filterValue))!=null){
					hasht.put(filterValue, dummy );
				}		
			}else{
				
				DataTable dt=(DataTable)dReq.getObjectParameter("dataTable");
				if (dt==null)
					dReq.setParameter("ERROR", "Error: No datatable set!");
				else
					try{
						int index=dt.getTableMetaData().getColumnIndex(fieldId);
						if (index<0){
							dReq.setParameter("ERROR", "Error: field ["+filterValue+"] not found!");
						}
						
						for  (Enumeration e=hash1.keys(); e.hasMoreElements(); ){
							
							String key=e.nextElement().toString();
							//InternalDataRow row=(InternalDataRow)e.nextElement();
							Object dummy=hash1.get(key);
							if (dummy instanceof InternalDataRow ){
								InternalDataRow row=(InternalDataRow)hash1.get(key);
								
								
								Object value=row.getRowValues()[index];
								if (  value!=null ) {
									
									if ( matchType.equals("1") && value.toString().startsWith(filterValue))
										hasht.put(key, row );
									else if ( matchType.equals("2") && value.toString().endsWith(filterValue))
										hasht.put(key, row );
									else if ( matchType.equals("3") && value.toString().indexOf(filterValue)>=0)
										hasht.put(key, row );
									else if ( value.equals(filterValue))
										hasht.put(key, row );
								}else if ( value == null &&  filterValue.trim().length()==0 ){
									hasht.put(key, row );
								}
							 }else if ( dummy instanceof Vector ){
							 	if ( filterValue.trim().length()==0 )
							 		hasht.put(key, dummy );
							 } 
						}		
					
						
					}catch(Exception e){
						dReq.setParameter("ERROR", "Error: "+e.getMessage());
						e.printStackTrace();
					}
			}
			dReq.setParameter("cacheElement", hasht);
		}		
	}
	

	
%>

<%

Dictionary cacheElement=(Dictionary)dReq.getObjectParameter("cacheElement");
int index=-1;
for (Enumeration e=cacheElement.keys(); cacheElement!=null && e.hasMoreElements(); ){
	index++;
	String key=(String)e.nextElement();
	Object element=cacheElement.get(key);
	if ( showUpdatesCount.length()>0 ){
	%>
		<tr class=text style="font-size=7pt; text-align:center;" bgcolor=#FFFFFF>
		<%if (!showResubscriber.equals("true") ){%>
			<td colspan=3></td >
		<%}else{%>
			<td colspan=4></td >
		<%}
		
		//Object element = dReq.getObjectParameter("element");
		if ( element instanceof InternalDataRow ){
			int[] updatesCount=((InternalDataRow)element).getUpdatesCount();
			for ( int k=0; k< updatesCount.length; k++ ){ 
			%>
			<td bgcolor=#AAAAAA><nobr><%= updatesCount[k] %></nobr></td>
			<%}
		}else if (element instanceof Vector){%>
			<td colspan="30" bgcolor=#BBBBBB style="color:red;">&nbsp;N.A.</td>
		<%}%>		
		</tr>
	<%}%>


	<%
	if ( showClassTypes.length()>0 ){%>

		<tr class=text style="font-size=7pt; text-align:center;" bgcolor=#FFFFFF>
		<%if (!showResubscriber.equals("true") ){%>
			<td colspan=3></td >
		<%}else{%>
			<td colspan=4></td >
		<%}

		
		// Object element = dReq.getObjectParameter("element");
		if ( element instanceof InternalDataRow ){
			Object[] rowValues=((InternalDataRow)element).getRowValues();
			for ( int k=0; k< rowValues.length; k++ ){ 
				ColumnMetaData cMd= 
				(ColumnMetaData)dReq.getObjectParameter("dataTable.tableMetaData.columnsMetaData["+k+"]");
	
				Object obj=rowValues[k];
				if ( obj==null  ){%>
					<td bgcolor=#AAAAAA style="color:gray;"><nobr>null</nobr></td>
				<%}else if ( cMd!=null && cMd.getClassType()!=null && cMd.getClassType().isAssignableFrom(obj.getClass()) ){%>
				<td bgcolor=#AAAAAA style="color:green;"><nobr><%= obj.getClass().getName() %></nobr></td>
				<%}else{%>
				<td bgcolor=#AAAAAA style="color:red;"><nobr><%= obj.getClass().getName() %></nobr></td>
				<%}
			
			}
			
		}else if (element instanceof Vector){%>
			<td colspan="30" bgcolor=#BBBBBB style="color:red;">&nbsp;N.A.</td>
		<%}%>		

		</tr>
	<%}%>

	
	<script>
		var0=<java type=print>"\""+request.getParameter("tableId")+"\";"</java>
		cacheType=<java type=print>"\""+request.getParameter("cacheType")+"\";"</java>
	<!--
		
		function yodiLaCacheUpdate(anch){
			
			var1="./cache_update.jsp?cacheType="+cacheType+"&tableId=" + var0 + "&cacheId=" + anch.id+"&time="+(new Date()).getTime();
			//alert (var1);
			
			bau=window.open( var1, 'UPDATE_WINDOW', 'scrollbars=yes,toolbar=no,resizable=yes,menubar=no,width=600,height=550,topmargin=0,leftmargin=0' );
			bau.focus();
			return false;
		}
	-->
	</script>
	<% 
	//Object element = dReq.getObjectParameter("element");
	if ( element instanceof InternalDataRow ){
		%>
		
		
		
		<tr class=text bgcolor=#FFFFFF>
		
		
		<%if ( showResubscriber.equals("true") ){ %>
			<td><input type="checkbox" name="instrument_id_<%=index%>" value="<%=((InternalDataRow)element).getRowValues()[0]%>"></td >
		<%}%>
		
		<td ><%=index%></td>
		<td ><a href="<%=key%>" id="<%=key%>" onClick="return yodiLaCacheUpdate(this);" style="text-decoration: none;"><%=key%></a></td>
		<td ><%=formatTipDate(""+((InternalDataRow)element).getLastUpdateTime() )%>  <!--converter="date"  date="dd/MM/yy HH:mm:ss"--></td>
		
		<%
		Object[] rowValues=((InternalDataRow)element).getRowValues();
		for ( int k=0; k< rowValues.length; k++ ){ 
			ColumnMetaData cMd= 
				(ColumnMetaData)dReq.getObjectParameter("dataTable.tableMetaData.columnsMetaData["+k+"]");
			String description=cMd.getDescription();
			String presentationType=(String)(cMd.getAttribute("PRESENTATIONTYPE"));
			boolean isList=(cMd.getAttribute("UPDATEASLIST")!=null);
			
			
			if ( isList ){%>

				<td bgcolor=#EEEEEE  title="<%=description%>">
				<nobr>
				<% 
				Object[] listArr=(Object[])rowValues[k];
				for ( int p=0; p<listArr.length; p++ ){%>
					[<%=listArr[p]%>]<br>
				<%}%>
					
				</nobr></td>
			
			<%}else if(
				(presentationType==null || !presentationType.equals("java.util.Date"))
				&&								
				(description==null || 
				 (description.toLowerCase().indexOf("time")<0 && description.toLowerCase().indexOf("date")<0))
			){%>
				<td bgcolor=#EEEEEE  title="<%=description%>">
				<nobr><%=rowValues[k]==null?"":rowValues[k]%></nobr></td>
			<%}else{%>
				<td bgcolor=#EEEEEE  title="<%=description+"["+rowValues[k]+"]"%>">
				<!--valueof param="element" converter="date"  date="dd/MM/yy HH:mm:ss"></valueof></td-->
				<%=rowValues[k]==null?"":formatTipDate(""+rowValues[k])%>
			<%}
		}
		%>
		
		</tr>
	<%}else if (element instanceof Vector){%>
	
		<tr class=text bgcolor=#FFFFFF>
		<% if( showResubscriber.equals("true") ){%>
			<td>nbps;</td >	
		<%}%>
		
		<td class=text-b style="color:red;"><%= index %></td>
		<td class=text-b style="color:red;"><%=key%></td>
		<td style="text-align:center;">-</td>
		<td colspan="30" bgcolor=#EEEEEE ><valueof param="element"></valueof></td>
		</tr>
		
	<!--tr class=text bgcolor=#FFFFFF>
	<Droplet bean="Switch">
	<param name="value" value="param:showResubscriber">
	<oparam name="true">
		<td>nbps;</td >
	</oparam>
	</droplet>
	<td class=text-b style="color:red;"><valueof param="index"></valueof></td>
	<td class=text-b style="color:red;"><valueof param="key"></valueof></td>
	<td style="text-align:center;">-</td>
	<td colspan="30" bgcolor=#EEEEEE ><valueof param="element"></valueof></td>
	</tr-->
	
	<%}%>


<%}%>
</table></td></tr></form></table>
<%}catch(Exception e){
e.printStackTrace();
}%>