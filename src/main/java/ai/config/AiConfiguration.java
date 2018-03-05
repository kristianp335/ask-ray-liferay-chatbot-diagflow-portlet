package ai.config;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import aQute.bnd.annotation.metatype.Meta;


@ExtendedObjectClassDefinition(category = "Artificial Intelligence", scope = ExtendedObjectClassDefinition.Scope.GROUP)
@Meta.OCD(  
        id = "ai.config.AiConfiguration"
    )
public interface AiConfiguration {
    @Meta.AD(deflt="4304414ee84640ef8267ea82c383d6e9",required = false)
    public String authorisationToken(); 
    
    @Meta.AD(deflt="6",required = false)
    public String numberOfRecordsDisplayed();
    
    @Meta.AD(deflt="asc",required = false)
    public String sortOrder();  
    
    @Meta.AD(deflt="panel-success",required = false)
    public String youClass();
    
    @Meta.AD(deflt="panel-primary", required = false)
    public String rayClass();
    
    @Meta.AD(deflt="true", required = false)
    public String useTimeline();
    
    @Meta.AD(deflt="true", required = false)
    public String centerTimeline();
    
}


