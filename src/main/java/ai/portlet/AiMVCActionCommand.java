package ai.portlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.liferay.kris.dialogflow.service.ApiAiDataLocalService;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;


import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
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
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.impl.provider.entity.StringProvider;

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
	
	public String getAccessToken() throws IOException {
		String accessToken =  null;
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream stream = classloader.getResourceAsStream("liferay-onhm-1c5c15d292d4.json");
		List<String> scopes = Arrays.asList("https://www.googleapis.com/auth/cloud-platform", "https://www.googleapis.com/auth/dialogflow"); 
		GoogleCredential credentials = GoogleCredential.fromStream(stream).createScoped(scopes);
		credentials.refreshToken();
		accessToken = credentials.getAccessToken();
		System.out.println("Access token for dialog flow standard portlet is " + accessToken);
		return accessToken;	
	}

	private void _handleActionCommand(ActionRequest actionRequest, ActionResponse actionResponse) throws PortalException, IOException   {
		String authtoken = "";
		String dialogflowAgent;
		if (actionRequest.getPreferences().getValue("dialogflowAgent", "") == null) {
			dialogflowAgent = _aiConfiguration.dialogflowAgent();
		}
		else {
			dialogflowAgent = actionRequest.getPreferences().getValue("dialogflowAgent", "");
		}
		String numberOfRecordsDisplayed = "";
		authtoken = this.getAccessToken();		
		String yourQuery = ParamUtil.getString(actionRequest, "query");
		String voice = ParamUtil.getString(actionRequest, "voice");
		ServiceContext serviceContext = ServiceContextFactory.getInstance(actionRequest);
	    //suppress session messages to make it more conversational
	    SessionMessages.add(actionRequest, SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
	    DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
		defaultClientConfig.getClasses().add(StringProvider.class);
		Client client = Client.create(defaultClientConfig);
		String conversationSession = actionRequest.getPortletSession().getId();
		System.out.println("We are good 1");
		WebResource webResource = client.resource("https://dialogflow.googleapis.com/v2/projects/" + dialogflowAgent + "/agent/sessions/" + conversationSession + ":detectIntent");
       	String input = "{\"query_input\": {\"text\": {\"text\":\"" + yourQuery + "\",  \"language_code\": \"en-US\" }}}";
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
			JSONObject jsonRes = (JSONObject) json.get("queryResult");
			result = jsonRes.toString();	
			//get the action sometimes known as the intent
			action = (String) jsonRes.get("action");
			fulfillment = (String) jsonRes.get("action");
			result = (String) jsonRes.get("action");			
			
			JSONArray jsonResFulfillment = (JSONArray)  jsonRes.get("fulfillmentMessages");
			JSONObject firstObject = (JSONObject) jsonResFulfillment.get(0);
			JSONObject text1Object = (JSONObject) firstObject.get("text");
			JSONArray textArray = (JSONArray) text1Object.get("text");
			speech = textArray.get(0).toString();
			
			_apiAiDataLocalService.addApiAiData(serviceContext, yourQuery, authtoken, speech, action, fulfillment, result);	
			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			actionRequest.setAttribute("buttonText", "blank");
			actionRequest.setAttribute("buttonUrl", "");
			System.out.println("The voice command value is " + voice);
			if(voice.contains("voice")) {
				actionRequest.setAttribute("speechText", speech);	
			}
			if (action.contains("blog") == true)
			{
				long plid = PortalUtil.getPlidFromPortletId(themeDisplay.getScopeGroupId(), false, "com_liferay_blogs_web_portlet_BlogsPortlet");
				LiferayPortletURL portletUrl = PortletURLFactoryUtil.create(actionRequest,"com_liferay_blogs_web_portlet_BlogsPortlet" , plid, PortletRequest.RENDER_PHASE);
				actionRequest.setAttribute("buttonText", "Open Blogs");
				actionRequest.setAttribute("buttonUrl", portletUrl.toString());
			}
			if( action.contains("setup-email")) {
				long androidPlid = themeDisplay.getPlid();
				LiferayPortletURL androidPortletUrl = PortletURLFactoryUtil.create(actionRequest,"com_liferay_blogs_web_portlet_BlogsPortlet" , androidPlid, PortletRequest.RENDER_PHASE);
				androidPortletUrl.setParameter("categoryId", "63160");	
				actionResponse.sendRedirect(androidPortletUrl.toString());
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
		} catch (IOException e) {
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
	

