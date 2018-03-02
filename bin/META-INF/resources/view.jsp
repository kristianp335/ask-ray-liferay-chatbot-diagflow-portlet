<%@ include file="/init.jsp" %>
<% 
List<ApiAiData> apiAiDataList = (List) renderRequest.getAttribute("apiAiDataList");
String buttonText = (String) renderRequest.getAttribute("buttonText");
String buttonUrl = (String) renderRequest.getAttribute("buttonUrl");
String speechText = (String) renderRequest.getAttribute("speechText");
%>

<p>
<portlet:resourceURL id="krisview" var="krisViewURL" />
<portlet:actionURL name="aiPost" var="aiPostURL">
</portlet:actionURL>

<aui:container markupView="lexicon">

<%-- <c:if test="${something == 0}"><c:set var="className" value="zero_something"/></c:if> --%>

<ul class="timeline">
	<c:forEach items="${apiAiDataList}" var="apiAiDataItem">
	<li class="timeline-item">
		<%-- <div class="panel ${apiAiDataItem.getType()=='query' ? 'panel-primary' : '${rayClass}' }"> --%>
		<c:choose>
 			<c:when test="${apiAiDataItem.getType()=='query'}">
  				<div class="panel  <%=youClass %>">
 			</c:when>
	 		<c:otherwise>
	  			<div class="panel <%=rayClass %>">
	 		</c:otherwise>
		</c:choose>
			<div class="timeline-increment-icon">
                <span class="timeline-icon"></span>
            </div>
			<div class="panel-footer">
				<strong>${apiAiDataItem.getType()=='query' ? 'You' : 'Ray'}: </strong>
				${apiAiDataItem.getSpeech()}
			</div>
		</div>
    </li>
	<%-- <c:choose>
    <c:when test="${apiAiDataItem.getType()=='query'}">
		  	<div style="background-color: #4bb2ee; color: #fff; border-radius: 10px; max-width: 250px; padding: 10px; border: solid 1px #1bd096; margin-bottom: 10px">
       			<div><b>You said...</b></div>
       			<div>
           			${apiAiDataItem.getSpeech()}
        		</div>
    	  	</div>
   	</c:when>
   	<c:otherwise>
		  	<div style="background-color: #1bd096; color: #fff; border-radius: 10px;  max-width: 250px; padding: 10px; border: solid 1px #0ea877; margin-bottom: 10px; margin-left: 15px;">
       			<div><b>Ray said...</b></div>
       			<div>
           			${apiAiDataItem.getSpeech()}
        		</div>
    	  	</div>
   	</c:otherwise>
   	</c:choose> --%>
   	
	</c:forEach>
	
	<c:if test="${buttonText != 'blank'}">
		<a href="${buttonUrl}"><div class = "btn btn-primary">${buttonText}</div></a>
	</c:if>

</ul>	
	
</aui:container>	
	
 	<aui:form action="${aiPostURL}" name='fm' method='post'>   
 		<aui:input type="hidden" name="voice" value="textCommand" id="voice" />
    	<aui:input type="text" name="query" id="query" label="Your query:" inlineLabel="true"/>
    	<div>
    		<div style="float:left;">
    			<aui:button type="submit" id="go" value="Go"/> 
    		</div>
    		<div style="margin-left: 60px;">
    			<aui:button  id = "recordButton" value="" cssClass="icon-microphone"/>   
    		</div> 
    	</div>
    	<div style="clear:both;"></div>
	</aui:form>
		
</p>

<aui:script>
var SpeechRecognition = SpeechRecognition || webkitSpeechRecognition
var SpeechRecognitionEvent = SpeechRecognitionEvent || webkitSpeechRecognitionEvent
var recognition = new SpeechRecognition();
recognition.lang = 'en-UK';

var synth = window.speechSynthesis;
var msg = new SpeechSynthesisUtterance();
msg.text = "${speechText}";
synth.speak(msg);	

$("#<portlet:namespace/>recordButton").on("click", function() {
	recognition.start();
	$("#<portlet:namespace/>query").children().text("Listening...");
});

recognition.onresult = function(event) {	
	var speechResult = event.results["0"]["0"].transcript
	$("#<portlet:namespace/>query").val(speechResult);
	$("#<portlet:namespace/>voice").val("voiceCommand");
	$("#<portlet:namespace/>go").click();	
}


</aui:script>
