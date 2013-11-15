package org.inria.myriads.snoozeec2.instances.generator.api.impl;

import java.io.File;

import org.codehaus.jackson.map.ObjectMapper;
import org.inria.myriads.snoozeec2.exception.EC2ConfiguratorException;
import org.inria.myriads.snoozeec2.instances.EC2Instances;
import org.inria.myriads.snoozeec2.instances.generator.api.EC2InstancesGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Jackson Instances Generator.
 * Generates instance from a json file.
 * 
 * @author msimonin
 *
 */
public class JacksonInstancesGenerator implements EC2InstancesGenerator
{
    
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(EC2InstancesGenerator.class);
    

    /** instances.*/
    private EC2Instances instances_;
    
    
    
    /**
     * 
     * Constructor.
     * 
     * @param instancesFile                 instanceFile.
     * @throws EC2ConfiguratorException     EC2ConfiguratorException.
     */ 
    public JacksonInstancesGenerator(String instancesFile) 
            throws EC2ConfiguratorException
    {
        log_.debug("Building the instances list");
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            instances_ = mapper.readValue(
                    new File(instancesFile), EC2Instances.class);
        }
        catch (Exception exception)
        {
            log_.debug("Error while building the instances list");
            throw new EC2ConfiguratorException(exception.getMessage());
        }
        
    }


    @Override
    public EC2Instances generate()
    {
        return instances_;
    }

}
