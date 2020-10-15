package ai.config;



import aQute.bnd.annotation.metatype.Meta;



@Meta.OCD(  
        id = "ai.config.AiConfiguration"
    )
public interface AiConfiguration {
    @Meta.AD(deflt="liferay-onhm",required = false)
    public String dialogflowAgent(); 
    
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


