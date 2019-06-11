package ai.portlet;

import com.liferay.kris.apiai.model.ApiAiData;
import com.liferay.kris.apiai.service.ApiAiDataLocalService;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.PortletPreferences;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.sun.jersey.api.client.Client;

import ai.config.AiConfiguration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;


@Component(
		configurationPid = "ai.config.AiConfiguration",
		immediate = true,
		property = {
			"com.liferay.portlet.display-category=category.sample",
			"com.liferay.portlet.instanceable=true",
			"javax.portlet.display-name=Ask Ray?",
			"javax.portlet.init-param.template-path=/",
			"javax.portlet.init-param.view-template=/view.jsp",
			"javax.portlet.resource-bundle=content.Language",
			"javax.portlet.security-role-ref=power-user,user",
			"javax.portlet.name=aiportlet",
		},
		service = Portlet.class
	)


public class AiPortlet extends MVCPortlet {
	
	
	
	//View renderer for default view.jsp
    @Override
	public void doView(
			RenderRequest renderRequest, RenderResponse renderResponse)
				throws IOException, PortletException {
    	
    	String normalString = "HelloWorld";
    	char[] normalStringCharArray = normalString.toCharArray();
    	
    	String reverseString = "";
    	for(int i = normalStringCharArray.length - 1; i >= 0; i--)
    	{
    		reverseString = reverseString + normalStringCharArray[i];
    	}
    	System.out.println("The reverse string is " + reverseString);
    	ServiceContext serviceContext;
    	String numberOfRecordsDisplayed = "";
    	String sortOrder = "";
    	if (renderRequest.getPreferences().getValue("numberOfRecordsDisplayed", "") == null) {
			numberOfRecordsDisplayed = _aiConfiguration.numberOfRecordsDisplayed();
		}
		else {
			numberOfRecordsDisplayed = renderRequest.getPreferences().getValue("numberOfRecordsDisplayed", "6");
			if (numberOfRecordsDisplayed.isEmpty()) numberOfRecordsDisplayed = "6";
		}
    	
    	if (renderRequest.getPreferences().getValue("sortOrder", "") == null) {
			sortOrder = _aiConfiguration.sortOrder();
		}
		else {
			sortOrder = renderRequest.getPreferences().getValue("sortOrder", "asc");
		}
		
		int records = Integer.valueOf(numberOfRecordsDisplayed);
		try {
			serviceContext = ServiceContextFactory.getInstance(renderRequest);
			System.out.println("Default Authorisation token = " + _aiConfiguration.authorisationToken());
			List<ApiAiData> apiAiDataList = _apiAiDataLocalService.getRecentConversation(serviceContext, records, sortOrder);
			System.out.println("The size of the data list is " + apiAiDataList.size());
			//List<ApiAiData> apiAiDataList = _apiAiDataLocalService.getApiAiDatas(0, 1000);
			renderRequest.setAttribute("apiAiDataList", apiAiDataList);
			String speechTextValue = (String) renderRequest.getAttribute("speechText");
			if (speechTextValue == null) {
				renderRequest.setAttribute("speechText", "");
			}
			String buttonTextValue = (String) renderRequest.getAttribute("buttonText");
			if (buttonTextValue == null) {
			  renderRequest.setAttribute("buttonText", "blank");
			}
			renderRequest.setAttribute("useTimeline", _aiConfiguration.useTimeline());
			renderRequest.setAttribute("centerTimeline", _aiConfiguration.centerTimeline());
			
			super.doView(renderRequest, renderResponse);
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//System.out.println("My unique portlet authorisation token = " + renderRequest.getPortalContext().getProperty("authorisationToken"));
	}
	
    @Activate
    @Modified
    protected void activate(Map<Object, Object> properties) {
                
        _aiConfiguration = ConfigurableUtil.createConfigurable(AiConfiguration.class, properties);
    }
    
    private volatile AiConfiguration _aiConfiguration;
    
    @Reference(unbind = "-")
	 protected void setApiAiDataLocalService(ApiAiDataLocalService apiAiDataLocalService) {
		_apiAiDataLocalService = apiAiDataLocalService;
	 }
	 private ApiAiDataLocalService _apiAiDataLocalService;
}