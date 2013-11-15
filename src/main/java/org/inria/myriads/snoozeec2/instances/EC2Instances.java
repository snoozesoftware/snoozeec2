package org.inria.myriads.snoozeec2.instances;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author msimonin
 *
 */
public class EC2Instances
{
    
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(EC2Instances.class);
   
    /** Instances. */
    private Map<String, EC2Instance> instances_;
    
    /**
     * Constructor.
     */
    public EC2Instances()
    {
        log_.debug("Initializing EC2 instances");
        instances_ = new HashMap<String, EC2Instance>();
    }
   
    /**
     * @return the instances
     */
    public Map<String, EC2Instance> getInstances()
    {
        return instances_;
    }

    /**
     * @param instances the instances to set
     */
    public void setInstances(Map<String, EC2Instance> instances)
    {
        instances_ = instances;
    }

    /**
     * 
     * Return the instance of id instanceId.
     * return first instance otherwise.
     * 
     * @param instanceId    The instance id.
     * @return  The corresponding instance (null if not found).
     */
    public EC2Instance getInstance(String instanceId)
    {
        EC2Instance instance = instances_.get(instanceId);
        return instance;
    }

}
