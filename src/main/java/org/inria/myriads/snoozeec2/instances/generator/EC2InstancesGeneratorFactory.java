package org.inria.myriads.snoozeec2.instances.generator;

import org.inria.myriads.snoozeec2.exception.EC2ConfiguratorException;
import org.inria.myriads.snoozeec2.instances.generator.api.EC2InstancesGenerator;
import org.inria.myriads.snoozeec2.instances.generator.api.impl.JacksonInstancesGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * EC2 Instances Generator Factory.
 * 
 * @author msimonin
 *
 */
public final class EC2InstancesGeneratorFactory
{

    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(EC2InstancesGeneratorFactory.class);
        
    /**
     * 
     * Constructor.
     * 
     * @throws Exception 
     * 
     */
    private EC2InstancesGeneratorFactory() throws Exception
    {
        throw new Exception("Unsupported operation");
    }

    /**
     * 
     * Creates a instance generator.
     * 
     * @param instancesFile     The file to read
     * @return  The list of Instances
     * @throws EC2ConfiguratorException The Exception.
     */
    public static EC2InstancesGenerator newInstancesGenerator(String instancesFile) throws EC2ConfiguratorException
    {
        return new JacksonInstancesGenerator(instancesFile);
    }
    
}
