package org.inria.myriads.snoozeec2.action;

/**
 * 
 * EC2 Actions.
 * 
 * @author msimonin
 *
 */
public enum EC2Action 
{
    /** Describe instances. */
    DescribeInstances,
    /** Describe images. */
    DescribeImages, 
    /** Describe Addresses. */
    DescribeAddresses,
    /** Run instances.*/
    RunInstances, 
    /** Terminate instances.*/
    TerminateInstances,
    /** Reboot instances.*/
    RebootInstances,
    /** Create Tags.*/
    CreateTags,
}

