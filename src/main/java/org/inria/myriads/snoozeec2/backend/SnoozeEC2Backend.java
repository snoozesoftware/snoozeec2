package org.inria.myriads.snoozeec2.backend;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.ec2.doc._2010_08_31.InstanceStateChangeSetType;
import com.amazonaws.ec2.doc._2010_08_31.InstanceStateChangeType;
import com.amazonaws.ec2.doc._2010_08_31.InstanceStateType;

import org.inria.myriads.snoozecommon.communication.NetworkAddress;
import org.inria.myriads.snoozecommon.communication.groupmanager.GroupManagerDescription;
import org.inria.myriads.snoozecommon.communication.rest.api.BootstrapAPI;
import org.inria.myriads.snoozecommon.communication.rest.api.GroupManagerAPI;
import org.inria.myriads.snoozecommon.communication.virtualcluster.VirtualMachineMetaData;
import org.inria.myriads.snoozecommon.communication.virtualcluster.submission.VirtualClusterSubmissionRequest;
import org.inria.myriads.snoozecommon.communication.virtualcluster.submission.VirtualClusterSubmissionResponse;
import org.inria.myriads.snoozecommon.communication.virtualmachine.VirtualMachinesList;
import org.inria.myriads.snoozecommon.request.HostListRequest;
import org.inria.myriads.snoozecommon.virtualmachineimage.VirtualMachineImage;
import org.inria.myriads.snoozecommon.virtualmachineimage.VirtualMachineImageList;
import org.inria.myriads.snoozeec2.configurator.api.EC2Configuration;
import org.inria.myriads.snoozeec2.instances.EC2Instances;
import org.inria.myriads.snoozeec2.resource.EC2InstanceState;
import org.inria.myriads.snoozeimages.communication.rest.api.ImagesRepositoryAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





/**
 * 
 * Image service backend.
 * 
 * @author msimonin
 *
 */
public class SnoozeEC2Backend 
{    
    
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(SnoozeEC2Backend.class);
    
    /** Image repository settings. */
    private EC2Configuration EC2Configuration_;
    
    /** Instances.*/
    private EC2Instances EC2Instances_;
    
    /**
     * 
     * Constructor.
     * 
     * @param imageServiceConfiguration     Configuration.
     * @param ec2Instances                  Instances mapping.
     */
    public SnoozeEC2Backend(EC2Configuration imageServiceConfiguration, EC2Instances ec2Instances) 
    {
        EC2Configuration_ = imageServiceConfiguration;
        EC2Instances_ = ec2Instances;
    }

    /**
     * 
     * Return the list of all the instances.
     * 
     * @return list of virtual machines.
     */
    public List<VirtualMachineMetaData> describeInstances()
    {
        log_.debug("Describe instances");
        NetworkAddress bootstrapAddress = EC2Configuration_.getBootstrapSettings();
        BootstrapAPI bootstrapCommunicator =
                org.inria.myriads.snoozecommon.communication.rest.CommunicatorFactory
                .newBootstrapCommunicator(bootstrapAddress);
        HostListRequest hostListRequest = new HostListRequest();
        // infinite lookup ...
        hostListRequest.setLimit(-1);
        VirtualMachinesList virtualMachines = bootstrapCommunicator.getVirtualMachineDescriptions(hostListRequest);
        
        return virtualMachines.getVirtualMachines();
    }

    /**
     * 
     * Get the list of all the images.
     * 
     * @return the liste of images.
     */
    public ArrayList<VirtualMachineImage> describeImages()
    {
        log_.debug("Getting the images from the image repository");
        NetworkAddress imageRepositoryAddress = EC2Configuration_.getImageRepositorySettings();
        
        ImagesRepositoryAPI imagesRepositoryCommunicator = 
                org.inria.myriads.snoozeimages.communication.rest.CommunicatorFactory
                .newImagesRepositoryCommunicator(imageRepositoryAddress);
        
        VirtualMachineImageList images = imagesRepositoryCommunicator.getImagesList();
        log_.debug("Returning the images list from the repository" + images.getImages().size());
        for (VirtualMachineImage image : images.getImages())
        {
            log_.debug(image.getName());
        }
        
        return images.getImages();
    }

    /**
     * 
     * Run Instances.
     * 
     * 
     * @param submissionRequest     The submission resquest.
     * @return  the taskIdentifier. 
     */
    public String runInstances(VirtualClusterSubmissionRequest submissionRequest)
    {
        log_.debug("Starting instances");
        NetworkAddress bootstrapAddress = EC2Configuration_.getBootstrapSettings();
        BootstrapAPI bootstrapCommunicator = 
                org.inria.myriads.snoozecommon.communication.rest.CommunicatorFactory
                .newBootstrapCommunicator(bootstrapAddress);
        String taskIdentifier = bootstrapCommunicator.startVirtualCluster(submissionRequest);
        return taskIdentifier;
    }

    /**
     * @return the eC2Configuration
     */
    public EC2Configuration getEC2Configuration()
    {
        return EC2Configuration_;
    }

    /**
     * @param eC2Configuration the eC2Configuration to set
     */
    public void setEC2Configuration(EC2Configuration eC2Configuration)
    {
        EC2Configuration_ = eC2Configuration;
    }

    /**
     * @return the eC2Instances
     */
    public EC2Instances getEC2Instances()
    {
        return EC2Instances_;
    }

    /**
     * @param eC2Instances the eC2Instances to set
     */
    public void setEC2Instances(EC2Instances eC2Instances)
    {
        EC2Instances_ = eC2Instances;
    }

    /**
     * 
     * Reboot the instances.
     * 
     * @param listInstances     the instances to reboot.
     * @return  true iff everything is ok.
     */
    public boolean rebootInstances(List<String> listInstances)
    {
        log_.debug("Rebooting instances");
        NetworkAddress bootstrapAddress = EC2Configuration_.getBootstrapSettings();
        BootstrapAPI bootstrapCommunicator = 
                org.inria.myriads.snoozecommon.communication.rest.CommunicatorFactory
                .newBootstrapCommunicator(bootstrapAddress);
        boolean isRebooted = true;
        for (String instance : listInstances)
        {
            
            isRebooted = isRebooted & bootstrapCommunicator.rebootVirtualMachine(instance);
        }
        return isRebooted;
    }

    /**
     * 
     * Terminates instances.
     * 
     * @param listInstances         The list of instances to terminate.
     * @return  The instanceset change state.
     */
    public InstanceStateChangeSetType terminateInstances(List<String> listInstances)
    {
        log_.debug("Terminating instances");
        NetworkAddress bootstrapAddress = EC2Configuration_.getBootstrapSettings();
        BootstrapAPI bootstrapCommunicator = 
                org.inria.myriads.snoozecommon.communication.rest.CommunicatorFactory
                .newBootstrapCommunicator(bootstrapAddress);
        InstanceStateChangeSetType instanceStateChange = new InstanceStateChangeSetType();
        List<InstanceStateChangeType> instanceSet = instanceStateChange.getItem();
        InstanceStateType previousState = new InstanceStateType();
        previousState.setCode(EC2InstanceState.RUNNING.getCode());
        previousState.setName(EC2InstanceState.RUNNING.getMessage());
        
        for (String instance : listInstances)
        {
            InstanceStateChangeType instanceState = new InstanceStateChangeType();
            boolean isTerminated = bootstrapCommunicator.destroyVirtualMachine(instance);
            InstanceStateType currentState = new InstanceStateType();
            if (isTerminated)
            {
                currentState.setCode(EC2InstanceState.TERMINATED.getCode());
                currentState.setName(EC2InstanceState.TERMINATED.getMessage());
            }
            else
            {
                // need to be more precise here.
                currentState.setCode(EC2InstanceState.RUNNING.getCode());
                currentState.setName(EC2InstanceState.RUNNING.getMessage());
            }
            instanceState.setPreviousState(previousState);
            instanceState.setPreviousState(currentState);
            instanceSet.add(instanceState);
        }
        return instanceStateChange;
        
    }

    /**
     * 
     * Polling interval.
     * @todo get it from cfg.
     * 
     * @return  the polling interval.
     */
    public int getPollingInterval()
    {
        return 3;
    }

    /**
     * 
     * Gets the virtual cluster submission response.
     * 
     * @param taskIdentifier    The taskidentifier.
     * @return  The Virtual Cluster submission response.
     */
    public VirtualClusterSubmissionResponse getVirtualClusterResponse(String taskIdentifier)
    {
        log_.debug("Describe instances");
        NetworkAddress bootstrapAddress = EC2Configuration_.getBootstrapSettings();
        BootstrapAPI bootstrapCommunicator = 
                org.inria.myriads.snoozecommon.communication.rest.CommunicatorFactory
                .newBootstrapCommunicator(bootstrapAddress);
        
        GroupManagerDescription leaderDescription = bootstrapCommunicator.getGroupLeaderDescription();
        GroupManagerAPI groupLeaderCommunicator = 
                org.inria.myriads.snoozecommon.communication.rest.CommunicatorFactory
                .newGroupManagerCommunicator(leaderDescription.getListenSettings().getControlDataAddress());
    
        VirtualClusterSubmissionResponse response = groupLeaderCommunicator.getVirtualClusterResponse(taskIdentifier);
        return response;
    }

    /**
     * 
     * Gets the number of retries.
     * @todo get it form cfg.
     * 
     * 
     * @return the number of retries.
     */
    public int getNumberOfRetries()
    {
        return 100;
    }

    
}
