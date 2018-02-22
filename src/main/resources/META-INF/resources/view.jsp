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
	
	<div class="popover bs-popover-right">
    <div class="arrow"></div>
    <div class="inline-scroller">
        <div class="popover-header">Popover right</div>
        <div class="popover-body">
            Viennese flavour cup eu, percolator froth ristretto mazagran caffeine. White roast seasonal, mocha trifecta, dripper caffeine spoon acerbic to go macchiato strong.
        </div>
    </div>
</div>

	

</aui:container>	
	
 	<aui:form action="${aiPostURL}" name='fm' method='post'>   
    	<aui:input type="text" name="query" label="Your query:" inlineLabel="true"/>
    	<aui:button type="submit" value="Go"/> 	   
	</aui:form>
	
</p>