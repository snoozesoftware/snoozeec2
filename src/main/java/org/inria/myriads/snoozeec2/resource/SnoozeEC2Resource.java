package org.inria.myriads.snoozeec2.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBElement;

import com.amazonaws.ec2.doc._2010_08_31.CreateTagsResponseType;
import com.amazonaws.ec2.doc._2010_08_31.DescribeAddressesResponseInfoType;
import com.amazonaws.ec2.doc._2010_08_31.DescribeAddressesResponseItemType;
import com.amazonaws.ec2.doc._2010_08_31.DescribeAddressesResponseType;
import com.amazonaws.ec2.doc._2010_08_31.DescribeImagesResponseInfoType;
import com.amazonaws.ec2.doc._2010_08_31.DescribeImagesResponseItemType;
import com.amazonaws.ec2.doc._2010_08_31.DescribeImagesResponseType;
import com.amazonaws.ec2.doc._2010_08_31.DescribeInstancesResponseType;
import com.amazonaws.ec2.doc._2010_08_31.InstanceStateChangeSetType;
import com.amazonaws.ec2.doc._2010_08_31.InstanceStateType;
import com.amazonaws.ec2.doc._2010_08_31.ObjectFactory;
import com.amazonaws.ec2.doc._2010_08_31.RebootInstancesResponseType;
import com.amazonaws.ec2.doc._2010_08_31.ReservationInfoType;
import com.amazonaws.ec2.doc._2010_08_31.ReservationSetType;
import com.amazonaws.ec2.doc._2010_08_31.RunInstancesResponseType;
import com.amazonaws.ec2.doc._2010_08_31.RunningInstancesItemType;
import com.amazonaws.ec2.doc._2010_08_31.RunningInstancesSetType;
import com.amazonaws.ec2.doc._2010_08_31.TerminateInstancesResponseType;

import org.apache.commons.lang3.StringUtils;
import org.inria.myriads.snoozecommon.communication.NetworkAddress;
import org.inria.myriads.snoozecommon.communication.virtualcluster.VirtualMachineMetaData;
import org.inria.myriads.snoozecommon.communication.virtualcluster.submission.VirtualClusterSubmissionRequest;
import org.inria.myriads.snoozecommon.communication.virtualcluster.submission.VirtualClusterSubmissionResponse;
import org.inria.myriads.snoozecommon.communication.virtualcluster.submission.VirtualMachineTemplate;
import org.inria.myriads.snoozecommon.globals.Globals;
import org.inria.myriads.snoozecommon.util.TimeUtils;
import org.inria.myriads.snoozecommon.virtualmachineimage.VirtualMachineImage;
import org.inria.myriads.snoozeec2.action.EC2Action;
import org.inria.myriads.snoozeec2.backend.SnoozeEC2Backend;
import org.inria.myriads.snoozeec2.communication.rest.CommunicatorFactory;
import org.inria.myriads.snoozeec2.communication.rest.api.SnoozeEC2API;
import org.inria.myriads.snoozeec2.communication.rest.api.impl.RESTletSnoozeEC2Communicator;
import org.inria.myriads.snoozeec2.instances.EC2Instance;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Snooze EC2 resource.
 * 
 * @author msimonin
 *
 */
public class SnoozeEC2Resource extends ServerResource implements SnoozeEC2API 
{
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(SnoozeEC2Resource.class);
    
    /** context path.*/
    private static String CONTEXTPATH = "com.amazonaws.ec2.doc._2010_08_31";

    /** Snooze EC2 backend. */
    private SnoozeEC2Backend backend_;
    
    /**
     * Constructor. 
     */
    public SnoozeEC2Resource() 
    {
        super();
        log_.debug("Starting image service resource");
        backend_ = (SnoozeEC2Backend) getApplication().getContext().getAttributes().get("backend");
    }

    /**
     * 
     * Routes queries.
     * 
     * @return xmlRepresentation.
     */
    
    public Representation handleRequest()
    {
        Form queryParams = getQuery();
        String stringAction = queryParams.getFirstValue("Action");
        log_.debug(String.format("Received a request for %s", stringAction));
        // Routing given Action query parameters.
        EC2Action action = EC2Action.valueOf(stringAction); 
        switch(action)
        {
        case DescribeInstances:
            return describeInstances();
            
        case DescribeImages:
            return describeImages();
        
        case DescribeAddresses:
            return describeAddresses();
            
        case RunInstances:
            return runInstances();
            
        case TerminateInstances:
            return terminateInstances();
        
        case RebootInstances:
            return rebootInstances();
        
        case CreateTags:
            return createTags();
            
        default: 
            log_.error(String.format("Action %s not supported by the snooze API", stringAction));
            return error();
        }
    }
    
    @Post
    public Representation handlePost(Representation entity)
    {
        NetworkAddress address = new NetworkAddress();
        address.setAddress("localhost");
        address.setPort(4001);
        SnoozeEC2API snoozeEC2Communicator = CommunicatorFactory.newSnoozeEC2Communicator(address);
        return snoozeEC2Communicator.handlePost(entity);
      
    }
    
    @Get
    public Representation handleGet()
    {
        return handleRequest();
    }

    /**
     * 
     * Creates tags (stub).
     * 
     * @return xml CreateTagesResponse type.
     */
    private Representation createTags()
    {   
        log_.debug("Creating tags");
        CreateTagsResponseType response = new CreateTagsResponseType();
        response.setRequestId(UUID.randomUUID().toString());
        response.setReturn(true);
        
        //set representation
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<CreateTagsResponseType> elementResponse =
                objectFactory.createCreateTagsResponse(response);
        
        JaxbRepresentation<JAXBElement<CreateTagsResponseType>> responseRepresentation =
                new JaxbRepresentation<JAXBElement<CreateTagsResponseType>>(elementResponse);
        responseRepresentation.setContextPath(CONTEXTPATH);
        return responseRepresentation;
    }

    /**
     * 
     * Reboot instances.
     * 
     * @return xml RebootInstanceResponse type.
     */
    private Representation rebootInstances()
    {
        log_.debug("Rebooting instances.");
        List<String> listInstances = getParamList("InstanceId");
        boolean isRebooted = backend_.rebootInstances(listInstances);
        
        RebootInstancesResponseType response = new RebootInstancesResponseType();
        response.setRequestId(UUID.randomUUID().toString());
        response.setReturn(isRebooted);
        
        // set representation
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<RebootInstancesResponseType> elementResponse =
                objectFactory.createRebootInstancesResponse(response);
        
        JaxbRepresentation<JAXBElement<RebootInstancesResponseType>> responseRepresentation =
                new JaxbRepresentation<JAXBElement<RebootInstancesResponseType>>(elementResponse);
        responseRepresentation.setContextPath(CONTEXTPATH);
        return responseRepresentation;
    }

    /**
     * 
     * Gets the param list from the pattern.
     * For instance if pattern=InstanceId, it will fetch all parameters InstanceId.n
     * 
     * @param pattern       Pattern to fecth
     * @return              List of corresponding values.
     */
    private List<String> getParamList(String pattern)
    {
        Form queryParams = getQuery();
        List<String> paramList = new ArrayList<String>();
        for (Parameter  queryParam : queryParams)
        {
            if (queryParam.getName().contains(pattern))
            {
                log_.debug("Adding instance " + queryParam.getValue() + "to the param list");
                paramList.add(queryParam.getValue());
            }
        }
        return paramList;
    }

    /**
     * 
     * Terminate instances.
     * In snooze it means destroy.
     * 
     * @return  TerminateInstancesResponse.
     */
    private Representation terminateInstances()
    {
        log_.debug("Terminating instances.");
        List<String> listInstances = getParamList("InstanceId");
        InstanceStateChangeSetType instanceSet = backend_.terminateInstances(listInstances);
        
        TerminateInstancesResponseType response = new TerminateInstancesResponseType();
        response.setRequestId(UUID.randomUUID().toString());
        response.setInstancesSet(instanceSet);
        
        // set representation
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<TerminateInstancesResponseType> elementResponse =
                objectFactory.createTerminateInstancesResponse(response);
        
        JaxbRepresentation<JAXBElement<TerminateInstancesResponseType>> responseRepresentation =
                new JaxbRepresentation<JAXBElement<TerminateInstancesResponseType>>(elementResponse);
        responseRepresentation.setContextPath(CONTEXTPATH);
        return responseRepresentation;
    }

    /**
     * 
     * Run an instance.
     * 
     * 
     * @return runInstancesResponse.
     */
    private Representation runInstances()
    {
        Form queryParams = getQuery();
        String imageId = queryParams.getFirstValue("ImageId");
        if (StringUtils.isBlank(imageId))
        {
            log_.debug("Image Id is blank");
            return error();
        }
        String instanceType = queryParams.getFirstValue("InstanceType");
        if (StringUtils.isBlank(instanceType))
        {
            log_.debug("Instance id is blank");
            return error();
        }
        int minCount = Integer.valueOf(queryParams.getFirstValue("MinCount"));

        EC2Instance instance = backend_.getEC2Instances().getInstance(instanceType);
        if (instance == null)
        {
            log_.debug("Instance type not found");
            return error();
        }
        
        VirtualClusterSubmissionRequest submissionRequest = new VirtualClusterSubmissionRequest();
        ArrayList<VirtualMachineTemplate> templates = new ArrayList<VirtualMachineTemplate>();
        submissionRequest.setVirtualMachineTemplates(templates);
        for (int i = 0; i < minCount; i++)
        {
            String imageName = UUID.randomUUID().toString();    
            
            VirtualMachineTemplate template = new VirtualMachineTemplate();
            template.setImageId(imageId);
            template.setName(imageName);
            template.setVcpus(instance.getVcpus());
            template.setMemory(instance.getRam());
            templates.add(template);
        }
        
        String taskIdentifier = backend_.runInstances(submissionRequest);
        //polling
        int pollingInterval = backend_.getPollingInterval();
        int numberOfRetries = backend_.getNumberOfRetries();
        VirtualClusterSubmissionResponse virtualClusterResponse = null;
        int i = 0;
        while (i < numberOfRetries)
        {
            try 
            {              
                log_.debug("Waiting for virtual cluster response retrieval");
                Thread.sleep(TimeUtils.convertSecondsToMilliseconds(pollingInterval));
                virtualClusterResponse = backend_.getVirtualClusterResponse(taskIdentifier); 
                if (virtualClusterResponse != null)
                {
                    log_.debug("Received valid virtual cluster response!");
                    break;
                }
            } 
            catch (InterruptedException exception) 
            {
                log_.error("Interrupted exception", exception);
                break;
            }
            i = i + 1;
        }
        RunInstancesResponseType response = new RunInstancesResponseType();
        response.setRequestId(UUID.randomUUID().toString());
        response.setOwnerId(Globals.DEFAULT_INITIALIZATION);
        response.setReservationId(Globals.DEFAULT_INITIALIZATION);
        RunningInstancesSetType instanceSet = buildInstanceSet(virtualClusterResponse.getVirtualMachineMetaData());
        response.setInstancesSet(instanceSet);
        
        // set representation
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<RunInstancesResponseType> elementResponse =
                objectFactory.createRunInstancesResponse(response);
        
        JaxbRepresentation<JAXBElement<RunInstancesResponseType>> responseRepresentation =
                new JaxbRepresentation<JAXBElement<RunInstancesResponseType>>(elementResponse);
        responseRepresentation.setContextPath(CONTEXTPATH);
        return responseRepresentation;
    }

    /**
     * 
     * Describes addresses.
     * (needed by libcloud)
     * 
     * @return  describeAddressesResponse (xml)
     */
    private Representation describeAddresses()
    {
        DescribeAddressesResponseType addresses = new DescribeAddressesResponseType();
        
        DescribeAddressesResponseInfoType addressesResponseInfo = new DescribeAddressesResponseInfoType();
        List<DescribeAddressesResponseItemType> addressesInfo = addressesResponseInfo.getItem();
        DescribeAddressesResponseItemType addressItem = new DescribeAddressesResponseItemType();
        addressesInfo.add(addressItem);
        addresses.setAddressesSet(addressesResponseInfo);
        
        addressItem.setInstanceId("123");
        addressItem.setPublicIp("1.2.3.4");
        
        // set representation
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<DescribeAddressesResponseType> elementResponse =
                objectFactory.createDescribeAddressesResponse(addresses);
        
        
        
        JaxbRepresentation<JAXBElement<DescribeAddressesResponseType>> responseRepresentation =
                new JaxbRepresentation<JAXBElement<DescribeAddressesResponseType>>(elementResponse);
        responseRepresentation.setContextPath(CONTEXTPATH);        
        return responseRepresentation;
        
    }

    /**
     * 
     * Describe Images.
     * 
     * @return describeImageResponse (xml).
     */
    private Representation describeImages() 
    {
        ArrayList<VirtualMachineImage> images = backend_.describeImages();
        if (images == null)
        {
            return null;
        }
        DescribeImagesResponseType response = new DescribeImagesResponseType();
        DescribeImagesResponseInfoType imageSet = new DescribeImagesResponseInfoType();
        response.setImagesSet(imageSet);
        List<DescribeImagesResponseItemType> imageItems = imageSet.getItem();
        for (VirtualMachineImage image : images)
        {
            DescribeImagesResponseItemType imageResponse = new DescribeImagesResponseItemType();
            imageResponse.setImageId(image.getName());
            imageResponse.setImageLocation(image.getName());
            imageResponse.setName(image.getName());
            imageItems.add(imageResponse);    
        }
        
        // set representation
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<DescribeImagesResponseType> elementResponse =
                objectFactory.createDescribeImagesResponse(response);
        
        JaxbRepresentation<JAXBElement<DescribeImagesResponseType>> responseRepresentation =
                new JaxbRepresentation<JAXBElement<DescribeImagesResponseType>>(elementResponse);
        responseRepresentation.setContextPath(CONTEXTPATH);        
        return responseRepresentation;
        
    }

    
    /**
     * 
     * Describe instances.
     * 
     * @return DescribeInstancesResponse (xml)
     */
    private Representation describeInstances() 
    {
        log_.debug("Describing instances");
        List<VirtualMachineMetaData> virtualMachines = backend_.describeInstances();
        log_.debug("found instances : " + virtualMachines.size());

        DescribeInstancesResponseType response = new DescribeInstancesResponseType();
        response.setRequestId(UUID.randomUUID().toString());
        ReservationSetType reservationSet = new ReservationSetType();
        ReservationInfoType reservation = new ReservationInfoType();
        reservationSet.getItem().add(reservation); 
        response.setReservationSet(reservationSet);
        reservation.setOwnerId(Globals.DEFAULT_INITIALIZATION);
        reservation.setReservationId(Globals.DEFAULT_INITIALIZATION);
        RunningInstancesSetType instanceSet = new RunningInstancesSetType();
        reservation.setInstancesSet(instanceSet);
        
        instanceSet = buildInstanceSet(virtualMachines);
        
        
        reservation.setInstancesSet(instanceSet);
        //set representation
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<DescribeInstancesResponseType> elementResponse =
                objectFactory.createDescribeInstancesResponse(response);
        
        JaxbRepresentation<JAXBElement<DescribeInstancesResponseType>> responseRepresentation =
                new JaxbRepresentation<JAXBElement<DescribeInstancesResponseType>>(elementResponse);
        responseRepresentation.setContextPath(CONTEXTPATH);
        return responseRepresentation;
    }
    
    /**
     * 
     * Converts a list of virtual machine meta data in runningInstanceSetType.
     * 
     * @param virtualMachines   The virtualMachine meta data list.
     * @return  RunningInstanceSetType.
     */
    private RunningInstancesSetType buildInstanceSet(List<VirtualMachineMetaData> virtualMachines)
    {
        log_.debug("Building Instance set");
        RunningInstancesSetType instanceSet = new RunningInstancesSetType();
        List<RunningInstancesItemType> runnningInstances = instanceSet.getItem();
        for (VirtualMachineMetaData virtualMachine : virtualMachines)
        {
            RunningInstancesItemType runningInstance = new RunningInstancesItemType();
            // state
            InstanceStateType state = new InstanceStateType();
            EC2InstanceState translatedState = 
                    EC2InstanceState.translateState(virtualMachine.getStatus());
            state.setCode(translatedState.getCode());
            state.setName(translatedState.getMessage());
            
            runningInstance.setInstanceState(state);   
            runningInstance.setImageId(virtualMachine.getImage().getName());
            runningInstance.setInstanceId(virtualMachine.getVirtualMachineLocation().getVirtualMachineId());
            runningInstance.setIpAddress(virtualMachine.getIpAddress());
            runningInstance.setDnsName(virtualMachine.getVirtualMachineLocation().getVirtualMachineId());
            runnningInstances.add(runningInstance);
        }
        log_.debug("InstanceSet built");
        return instanceSet;
    }

    /**
     * 
     * Error.
     * 
     * @return errror Representation.
     */
    private Representation error() 
    {
        return null;
    }

    
}

