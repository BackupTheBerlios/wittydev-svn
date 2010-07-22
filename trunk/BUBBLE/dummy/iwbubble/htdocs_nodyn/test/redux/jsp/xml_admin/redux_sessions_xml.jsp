<%@ page contentType="text/xml" import ="java.util.*,
		it.imiweb.app.redux.server.city.CityMonitor,
		it.imiweb.app.redux.server.city.ServerInfosHome,
		it.imiweb.app.redux.server.city.CityTripSession" %><%response.setContentType("text/xml");%>

<%!
String adminUrl;
CityMonitor cityMonitor;
ServerInfosHome serverInfosHome;
String sessionsManagerPath="/it/imiweb/bubble/SessionsManager";

	String[][] xmlReserved=
	{
		{"<", "&lt;"},
		{">", "&gt;"},
		{"\"", "&quot;"},
		{"\'", "&#039;"},
		{"\\", "&#092;"},
		{"&", "&amp;"},
	};	
	public String cleanForXml(String str){
		return replace	( str, xmlReserved );
	}
	public static String replace(String dummy, String[][] repl){
	
		String result= dummy;
		for (int i=0; i<repl.length; i++){
		    int pos=0;
		    while( (pos=result.indexOf( repl[i][0], pos) ) >=0 ){
		        result=result.substring(0, pos)+repl[i][1]+result.substring(pos+repl[i][0].length() );
		        pos+=repl[i][1].length();
		    }
		}
		return result;
	}
	
%>

<%
	
	atg.servlet.DynamoHttpServletRequest dRequest = (atg.servlet.DynamoHttpServletRequest)request;
	if ( cityMonitor == null )
		cityMonitor=(CityMonitor)dRequest.resolveName("/it/imiweb/app/redux/city/CityMonitor");
	if ( cityMonitor != null )
		dRequest.setParameter( "cityMonitor", cityMonitor );
	if ( serverInfosHome == null )
		serverInfosHome=(ServerInfosHome)dRequest.resolveName("/it/imiweb/app/redux/city/ServerInfosHome");

	if (adminUrl==null){
		String adminPort=""+request.getServerPort(); //request.getParameter("adminPort");
		String adminHost=request.getServerName(); //request.getParameter("adminHostName");
		adminUrl="http://"+adminHost+":"+adminPort+"/bubble/listbeans.jsp?path=";
		
	}	

	System.out.println ( "#@#@#@#@#@#@ redux_sessions_xml ["+new Date()+"] ==> "+request.getRemoteAddr());          
%>

<ReduxInfo id="<%="sess_"+request.getServerName()+"_"+request.getServerPort()%>">
	<ServerInfo host="<%=request.getServerName()%>" httpPort="<%=request.getServerPort()%>" adminUrl="<%=adminUrl%>" 
			sessionsManagerPath="<%=sessionsManagerPath%>" 
			serverTime="<%=serverInfosHome.getCurrentTimeMillis()%>" />
			
			
	<CityMonitor 	monitorOn="<%=cityMonitor.isMonitorOn()%>"
			sessionsCount="<%=cityMonitor.getRegisteredSessionsCount()%>"
			monitorJobInterval="<%=cityMonitor.getMonitorJobInterval()%>"
			>
			
		<CityTripSessions>
			<%
			String dummy=request.getParameter("showAllSessionsInfo");
			boolean showAllSessions=( dummy!=null && dummy.equalsIgnoreCase("true" ) );
			if (showAllSessions)
			for ( int i=0; i<cityMonitor.getRegisteredSessionsList().size(); i++){
				CityTripSession element=(CityTripSession)cityMonitor.getRegisteredSessionsList().elementAt(i);
				it.imiweb.net.widecomm.app.WideCommDynamoHttpServer wideCommHttpServer=
				(it.imiweb.net.widecomm.app.WideCommDynamoHttpServer)element.getWideCommDynamoHttpServer();

			%>

				<CityTripSession>
				<SessionInfo	id="<%=element.getDynamoSession().getId()%>"
						creationTime="<%=element.getDynamoSession().getCreationTime()%>"
						lastAccessedTime="<%=element.getDynamoSession().getLastAccessedTime()%>"
						userName="<%= cleanForXml(element.getUserHome().getUserNameForDebugOnly())%>"
						blockedMarketsCount="<% String[] dummyArr=(String[])element.getUserHome().getBlockedMarkets(); out.print((dummyArr==null)?"-":""+dummyArr.length); %>"
						customerId="<%=element.getUserHome().getCustomerId()%>"
						externalId="<%=element.getUserHome().getExternalId()%>"
						callCenterId="<%=(element.getUserHome().getCallCenterId()==null)?"":element.getUserHome().getCallCenterId()%>"
						clientAddress="<%=wideCommHttpServer==null?"":element.getWideCommDynamoHttpServer().getClientAddress()%>"
						clientIWAddress="<%=(wideCommHttpServer==null ||wideCommHttpServer.getIWClientAddress()==null)?"":wideCommHttpServer.getIWClientAddress()%>"
						>
				</SessionInfo>
				<%if ( wideCommHttpServer!=null){%>
					<WideCommInfo	
							lastWriteTime="<%=wideCommHttpServer.getLastWriteTime()%>"
							writtenBytesCount="<%=wideCommHttpServer.getWrittenBytesCount()%>"
							writtenObjectsCount="<%=wideCommHttpServer.getWrittenObjectsCount()%>"
							objectsAveradgeLength="<%=(wideCommHttpServer==null || wideCommHttpServer.getWrittenObjectsCount()<=0)?0:wideCommHttpServer.getWrittenBytesCount()/wideCommHttpServer.getWrittenObjectsCount()%>"
							>

						<WideCommStats>
							<% 
							for ( Enumeration enum=element.getWideCommDynamoHttpServer().getWideCommStats().getSentStats().keys(); enum.hasMoreElements();){
								String key=(String)enum.nextElement();
								long[][] values=(long[][])element.getWideCommDynamoHttpServer().getWideCommStats().getSentStats().get(key);
							%>
								<MessageGroup type="<%=key%>">
									<% for ( int j=0; j<values.length; j++ ){%>
										<Message 
											channel="<%=values[j][0]%>"
											sentObject="<%=values[j][1]%>"
											realSize="<%=values[j][2]%>"
											sentSize="<%=values[j][3]%>"
											/>
									 	
									 <%}%>
								</MessageGroup>
							<%}%>
						</WideCommStats>
					
					</WideCommInfo>
					<%it.imiweb.net.widecomm.WideCommQueue wideCommQueue=
						(it.imiweb.net.widecomm.WideCommQueue)element.getWideCommDynamoHttpServer().getWideCommQueue(); 
					if (wideCommQueue!=null){%>
						<WideCommQueueInfo	
							enqueuedObjectsCount="<%=wideCommQueue.getEnqueuedObjectsCount() %>"
							enqueueExceptionsCount="<%=wideCommQueue.getEnqueueExceptionsCount()%>"
							mergedObjectsCount="<%=wideCommQueue.getMergedObjectsCount()%>"
							mergedRatio="<%=(wideCommQueue==null || wideCommQueue.getWrittenMessagesCount()<=0)?0:(wideCommQueue.getMergedObjectsCount()*100/wideCommQueue.getWrittenMessagesCount())%>"
							writtenObjectsCount="<%=wideCommQueue.getWrittenObjectsCount()%>"
							writtenMessagesCount="<%=wideCommQueue.getWrittenMessagesCount()%>"
							packedRatio="<%=(wideCommQueue==null || wideCommQueue.getWrittenMessagesCount()<=0)?0:(100-(wideCommQueue.getWrittenObjectsCount()*100/wideCommQueue.getWrittenMessagesCount()))%>"
							publishingErrorsCount="<%=wideCommQueue.getPublishingErrorsCount()%>"
							firstQueueSize="<%=wideCommQueue.getFirstQueueSize()%>"
							secondQueueSize="<%=wideCommQueue.getSecondQueueSize()%>"
							>
						</WideCommQueueInfo>
					<%}%>
				
				
				<%}%>
				</CityTripSession>
			<%}%>
		</CityTripSessions>
	</CityMonitor>

</ReduxInfo>
