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

<c:if test="<%= useTimeline.equalsIgnoreCase("true") %>">
	<ul class="timeline <%= centerTimeline.equalsIgnoreCase("true") ? "timeline-center timeline-even" : "" %> ">
		<c:forEach items="${apiAiDataList}" var="apiAiDataItem">
		<li class="timeline-item">
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
		</c:forEach>
	</ul>
</c:if>

<c:if test="<%= !useTimeline.equalsIgnoreCase("true") %>">
	<c:forEach items="${apiAiDataList}" var="apiAiDataItem">
		<c:choose>
		    <c:when test="${apiAiDataItem.getType()=='query'}">
				  	<div style="background-color: #000; color: #fff; border-radius: 10px; max-width: 250px; padding: 10px; border: solid 1px #000; margin-bottom: 10px">
		       			<div><b>You said...</b></div>
		       			<div>
		           			${apiAiDataItem.getSpeech()}
		        		</div>
		    	  	</div>
		   	</c:when>
		   	<c:otherwise>
				  	<div style="background-color: #e60000; color: #fff; border-radius: 10px;  max-width: 250px; padding: 10px; border: solid 1px #e60000; margin-bottom: 10px; margin-left: 15px;">
		       			<div><b>Ray said...</b></div>
		       			<div>
		           			${apiAiDataItem.getSpeech()}
		        		</div>
		    	  	</div>
		   	</c:otherwise>
		</c:choose>	
	</c:forEach>
</c:if>

<c:if test="${buttonText != 'blank'}">
	<a href="${buttonUrl}"><div class = "btn btn-primary">${buttonText}</div></a>
</c:if>
	
</aui:container>	
	
 	<aui:form action="${aiPostURL}" name='fm' method='post'>   
 		<aui:input type="hidden" name="voice" value="textCommand" id="voice" />
    	<aui:input type="text" name="query" id="query" label="" inlineLabel="false"/>
    	<div>
    		<div style="float:left;">
    			<aui:button type="submit" id="go" value="Go"/> 
    		</div>
    		<div style="margin-left: 60px;">
    			<aui:button  id = "recordButton" value="Speak" cssClass="btn btn-primary"/>
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
