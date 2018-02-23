<%@ include file="/init.jsp"%>
 
<liferay-portlet:actionURL portletConfiguration="<%=true%>"
    var="configurationActionURL" />
 
<liferay-portlet:renderURL portletConfiguration="<%=true%>"
    var="configurationRenderURL" />
 
<aui:form action="<%=configurationActionURL%>" method="post" name="fm">
    <aui:input name="<%=Constants.CMD%>" type="hidden"
        value="<%=Constants.UPDATE%>" />
 
    <aui:input name="redirect" type="hidden"
        value="<%=configurationRenderURL%>" />
 
    <aui:fieldset>
        <aui:input name="authorisationToken" label="Authorisation Token" value="<%=authorisationToken%>">
        </aui:input>
        <aui:input name="numberOfRecordsDisplayed" label="Number of records to display" value="<%=numberOfRecordsDisplayed%>">
        </aui:input>
    </aui:fieldset>
    
    
 
    <aui:button-row>
        <aui:button type="submit"></aui:button>
    </aui:button-row>
</aui:form>