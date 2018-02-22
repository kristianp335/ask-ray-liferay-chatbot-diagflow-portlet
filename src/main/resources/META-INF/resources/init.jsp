<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@page import="ai.config.AiConfiguration"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.kris.apiai.model.ApiAiData"%>
<%@page import="java.util.List"%>




<liferay-theme:defineObjects />

<portlet:defineObjects />

<%
AiConfiguration aiConfiguration = (AiConfiguration) 	renderRequest.getAttribute(AiConfiguration.class.getName());

	String authorisationToken = StringPool.BLANK;

	

	if (Validator.isNotNull(aiConfiguration)) {
		authorisationToken =
			portletPreferences.getValue(
				"authorisationToken", aiConfiguration.authorisationToken());

		
	}else{
		authorisationToken = portletPreferences.getValue("authorisationToken", "");

		
	}
%>

