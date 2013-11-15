package org.inria.myriads.snoozeec2.instances.generator.api;

import org.inria.myriads.snoozeec2.instances.EC2Instances;

/**
 * 
 * EC2 Instances Generator.
 * 
 * @author msimonin
 *
 */
public interface EC2InstancesGenerator
{
    
    /**
     * 
     * Generates the instance list.
     * 
     * @return EC2Instances.
     */
    EC2Instances generate();
}
