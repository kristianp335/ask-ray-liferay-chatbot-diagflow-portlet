package ai.config;
 

import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;
 
import java.util.Map;
 
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
 
import aQute.bnd.annotation.metatype.Configurable;
@Component(
        configurationPid = "ai.config.AiConfiguration",
        configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true,
        property = {
            "javax.portlet.name=aiportlet"
        },
        service = ConfigurationAction.class
    )
/**
 * 
 * (1) configurationPolicy optional means that the component is created regardless of whether or not the configuration was set
 * (2) The property javax.portlet.name indicates that this configuration is for com_proliferay_portlet_DemoPortlet
 * (3 )This component should be registered as a configuration action class so it should specify service = ConfigurationAction.class
 *  in the @Component annotation.
 *
 */
public class AiConfigurationAction extends DefaultConfigurationAction {
 
    @Override
    public void include(PortletConfig portletConfig, HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws Exception {
 
        httpServletRequest.setAttribute(AiConfiguration.class.getName(), _aiConfiguration);
 
        super.include(portletConfig, httpServletRequest, httpServletResponse);
    }
 
    @Override
    public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse)
            throws Exception {
 
        String authorisationToken = ParamUtil.getString(actionRequest, "authorisationToken");        
        String numberOfRecordsDisplayed = ParamUtil.getString(actionRequest, "numberOfRecordsDisplayed");        
        String sortOrder = ParamUtil.getString(actionRequest, "sortOrder");        
        String youClass = ParamUtil.getString(actionRequest, "youClass");        
        String rayClass = ParamUtil.getString(actionRequest, "rayClass");
        String useTimeline = ParamUtil.getString(actionRequest, "useTimeline");
        String centerTimeline = ParamUtil.getString(actionRequest, "centerTimeline");
        
        setPreference(actionRequest, "authorisationToken", authorisationToken);
        setPreference(actionRequest, "numberOfRecordsDisplayed", numberOfRecordsDisplayed);
        setPreference(actionRequest, "sortOrder", sortOrder);
        setPreference(actionRequest, "youClass", youClass);
        setPreference(actionRequest, "rayClass", rayClass);
        setPreference(actionRequest, "useTimeline", useTimeline);
        setPreference(actionRequest, "centerTimeline", centerTimeline);
    
 
        super.processAction(portletConfig, actionRequest, actionResponse);
    }
 
    /**
     * 
     * (1)If a method is annoted with @Activate then the method will be called at the time of activation of the component
     *  so that we can perform initialization task
     *  
     * (2) This class is annoted with @Component where we have used configurationPid with id com.proliferay.configuration.DemoConfiguration
     * So if we modify any configuration then this method will be called. 
     */
    @Activate
    @Modified
    protected void activate(Map<Object, Object> properties) {
       
        
        _aiConfiguration = Configurable.createConfigurable(AiConfiguration.class, properties);
        //_demoConfiguration = ConfigurableUtil.createConfigurable(DemoConfiguration.class, properties);
    }
 
    private volatile AiConfiguration _aiConfiguration;  
 
}