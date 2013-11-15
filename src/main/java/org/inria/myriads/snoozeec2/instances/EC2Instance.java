package org.inria.myriads.snoozeec2.instances;

/**
 * 
 * EC2Instance.
 * 
 * @author msimonin
 *
 */
public class EC2Instance
{
    /** id.*/
    private String id_; 
    
    /** name.*/
    private String name_;
    
    /** mem.*/
    private int ram_;
    
    /** disk.*/
    private int disk_;
    
    /** bandwidth.*/
    private double bandwidth_;
    
    /** vcpus.*/
    private int vcpus_;

    /**
     * @return the id
     */
    public String getId()
    {
        return id_;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id)
    {
        id_ = id;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name_;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        name_ = name;
    }

    /**
     * @return the ram
     */
    public int getRam()
    {
        return ram_;
    }

    /**
     * @param ram the ram to set
     */
    public void setRam(int ram)
    {
        ram_ = ram;
    }

    /**
     * @return the disk
     */
    public int getDisk()
    {
        return disk_;
    }

    /**
     * @param disk the disk to set
     */
    public void setDisk(int disk)
    {
        disk_ = disk;
    }

    /**
     * @return the bandwidth
     */
    public double getBandwidth()
    {
        return bandwidth_;
    }

    /**
     * @param bandwidth the bandwidth to set
     */
    public void setBandwidth(double bandwidth)
    {
        bandwidth_ = bandwidth;
    }

    /**
     * @return the vcpus
     */
    public int getVcpus()
    {
        return vcpus_;
    }

    /**
     * @param vcpus the vcpus to set
     */
    public void setVcpus(int vcpus)
    {
        vcpus_ = vcpus;
    }
    
}
