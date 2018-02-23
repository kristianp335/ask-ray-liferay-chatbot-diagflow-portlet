package ai.portlet;

import java.util.Date;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import com.liferay.bnd.util.ConfigurableUtil;
import com.liferay.kris.apiai.service.ApiAiDataLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import ai.config.AiConfiguration;



@Component(
		configurationPid = "ai.config.AiConfiguration",
	    immediate = true,
		property = {
		    	"javax.portlet.name=aiportlet",
		    	"mvc.command.name=aiPost"
		},
	    service = MVCActionCommand.class
	)

public class AiMVCActionCommand  extends BaseMVCActionCommand {
		
	@Reference(unbind = "-")
	 protected void setApiAiDataLocalService(ApiAiDataLocalService apiAiDataLocalService) {
		_apiAiDataLocalService = apiAiDataLocalService;
	 }
	private ApiAiDataLocalService _apiAiDataLocalService;
	
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
			_handleActionCommand(actionRequest, actionResponse);
	}

	private void _handleActionCommand(ActionRequest actionRequest, ActionResponse actionResponse) throws PortalException  {
		String authtoken = "";
		String numberOfRecordsDisplayed = "";
		//my APIAI Diagflow key is 4304414ee84640ef8267ea82c383d6e9
		if (actionRequest.getPreferences().getValue("authorisationToken", "") == null) {
			authtoken = _aiConfiguration.authorisationToken();
		}
		else {
			authtoken = actionRequest.getPreferences().getValue("authorisationToken", "4304414ee84640ef8267ea82c383d6e9");
		}
		
		String yourQuery = ParamUtil.getString(actionRequest, "query");
		ServiceContext serviceContext = ServiceContextFactory.getInstance(actionRequest);
	    //suppress session messages to make it more conversational
	    SessionMessages.add(actionRequest, SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
		Client client = new Client(); 
		WebResource webResource = client.resource("https://api.api.ai/v1/query?v=20150910");
	    String mySessionId = actionRequest.getPortletSession().getId();	
      	String input = "{\"query\":\"" + yourQuery + "\", \"lang\": \"en\", \"sessionId\": \"" + mySessionId + "\"}";
        ClientResponse response = webResource.header("Content-Type", "application/json").header("Authorization", "Bearer " + authtoken).post(ClientResponse.class, input);  
        //parse the JSON response
        JSONParser parser = new JSONParser();   
    	//get the root JSON object
		JSONObject json;
		String speech = "";
		String fulfillment = "";
		String action = "";
		String result = "";			
		try {
			json = (JSONObject) parser.parse(response.getEntity(String.class));
			JSONObject jsonRes = (JSONObject) json.get("result");
			result = jsonRes.toString();	
			//get the action sometimes known as the intent
			action = (String) jsonRes.get("action");
			//get the speech, the response string of the robot from the API.AI fulfillment JSON object
			JSONObject jsonResFulfillment = (JSONObject)  jsonRes.get("fulfillment");
			fulfillment = jsonResFulfillment.toString();
			speech = (String) jsonResFulfillment.get("speech");
			_apiAiDataLocalService.addApiAiData(serviceContext, yourQuery, authtoken, speech, action, fulfillment, result);	
			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			actionRequest.setAttribute("buttonText", "blank");
			actionRequest.setAttribute("buttonUrl", "");
			if (action.contains("blog") == true)
			{
			long plid = PortalUtil.getPlidFromPortletId(themeDisplay.getScopeGroupId(), false, "com_liferay_blogs_web_portlet_BlogsPortlet");
			LiferayPortletURL portletUrl = PortletURLFactoryUtil.create(actionRequest,"com_liferay_blogs_web_portlet_BlogsPortlet" , plid, PortletRequest.RENDER_PHASE);
			actionRequest.setAttribute("buttonText", "Open Blogs");
			actionRequest.setAttribute("buttonUrl", portletUrl.toString());
			}
		} catch (ClientHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UniformInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	@Activate
    @Modified
    protected void activate(Map<Object, Object> properties) {
                
        _aiConfiguration = ConfigurableUtil.createConfigurable(AiConfiguration.class, properties);
    }
	
	private volatile AiConfiguration _aiConfiguration;
	
	
	
}
	

