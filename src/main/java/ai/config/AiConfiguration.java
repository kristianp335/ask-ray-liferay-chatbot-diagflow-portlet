package ai.config;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import aQute.bnd.annotation.metatype.Meta;


@ExtendedObjectClassDefinition(category = "Artificial Intelligence", scope = ExtendedObjectClassDefinition.Scope.GROUP)
@Meta.OCD(  
        id = "ai.config.AiConfiguration"
    )
public interface AiConfiguration {
    @Meta.AD(required = false)
    public String authorisationToken(); 
    
    @Meta.AD(required = false)
    public String numberOfRecordsDisplayed();
    
    @Meta.AD(required = false)
    public String sortOrder();  
}
