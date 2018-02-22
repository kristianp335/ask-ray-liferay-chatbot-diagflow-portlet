<%@ include file="/init.jsp" %>
<% 
List<ApiAiData> apiAiDataList = (List) renderRequest.getAttribute("apiAiDataList");
%>

<p>
<portlet:resourceURL id="krisview" var="krisViewURL" />
<portlet:actionURL name="aiPost" var="aiPostURL">
</portlet:actionURL>

<aui:container markupView="lexicon">

	<c:forEach items="${apiAiDataList}" var="apiAiDataItem">
	<c:choose>
    <c:when test="${apiAiDataItem.getType()=='query'}">
		  	<div style="background-color: #4bb2ee; color: #fff; max-width: 250px; padding: 8px; border: solid 1px #1bd096; margin-bottom: 10px">
       			<div><b>You said...</b></div>
       			<div>
           			${apiAiDataItem.getSpeech()}
        		</div>
    	  	</div>
   	</c:when>
   	<c:otherwise>
		  	<div style="background-color: #1bd096; color: #fff;  max-width: 250px; padding: 8px; border: solid 1px #0ea877; margin-bottom: 10px; margin-left: 15px;">
       			<div><b>Ray said...</b></div>
       			<div>
           			${apiAiDataItem.getSpeech()}
        		</div>
    	  	</div>
   	</c:otherwise>
   	</c:choose>
   	
	</c:forEach>
	
</aui:container>	
	
 	<aui:form action="${aiPostURL}" name='fm' method='post'>   
    	<aui:input type="text" name="query" label="Your query:" inlineLabel="true"/>
    	<aui:button type="submit" value="Go"/> 	   
	</aui:form>
	
</p>