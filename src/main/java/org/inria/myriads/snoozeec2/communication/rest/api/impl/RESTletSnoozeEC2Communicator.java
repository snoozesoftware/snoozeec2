package org.inria.myriads.snoozeec2.communication.rest.api.impl;

import org.inria.myriads.snoozecommon.communication.NetworkAddress;
import org.inria.myriads.snoozecommon.virtualmachineimage.VirtualMachineImageList;
import org.inria.myriads.snoozeec2.communication.rest.api.SnoozeEC2API;
import org.inria.myriads.snoozeimages.communication.rest.api.ImagesRepositoryAPI;
import org.inria.myriads.snoozeimages.communication.rest.api.impl.RESTletImagesRepositoryCommunicator;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RESTletSnoozeEC2Communicator implements SnoozeEC2API
{
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(RESTletSnoozeEC2Communicator.class);
    
    /** Address.*/
    private NetworkAddress address_;
    
    /**
     * Constructor.
     * 
     * @param address  The image repository address.
     */
    public RESTletSnoozeEC2Communicator(NetworkAddress address)
    {
        log_.debug("Initializing REST image repository communicator");
        address_ = address;
    }

    /**
     * Creates a client resource.
     * 
     * @return     The client resource
     */
    private ClientResource createClientResource(String queryParams)
    {
        log_.debug("Creating client resource with queryparams : " + queryParams);
        String address = address_.getAddress();
        String port = String.valueOf(address_.getPort());
        ClientResource clientResource = new ClientResource("http://" + address + ":" + port + "/?" + queryParams); 
        
        return clientResource;
    }
    /**
     * Creates a client resource.
     * 
     * @return     The client resource
     */
    private ClientResource createClientResource()
    {
        log_.debug("Creating client resource");
        String address = address_.getAddress();
        String port = String.valueOf(address_.getPort());
        ClientResource clientResource = new ClientResource("http://" + address + ":" + port); 
        return clientResource;
    }    

    
    public Representation handleGet()
    {
        log_.debug("handle a get request");
        ClientResource clientResource = null;
        
        try 
        {
            clientResource = createClientResource();
            SnoozeEC2API snoozeEc2 = clientResource.wrap(SnoozeEC2API.class); 
            Representation result = snoozeEc2.handleGet();
            return result;
        } 
        catch (Exception exception)
        {
            log_.debug("Errort while retrieving the image list");
        }
        finally
        {
            if (clientResource != null)
            {
                clientResource.release();
            }
        }
        return null;
    }
    

    @Override
    public Representation handlePost(Representation body)
    {
        log_.debug("handle a post request");
        ClientResource clientResource = null;
        
        try 
        {
            clientResource = createClientResource(body.getText());
            SnoozeEC2API snoozeEc2 = clientResource.wrap(SnoozeEC2API.class); 
            Representation result = snoozeEc2.handleGet();
            return result;
        } 
        catch (Exception exception)
        {
            log_.debug("Error while handling post request");
            exception.printStackTrace();
        }
        finally
        {
            if (clientResource != null)
            {
                clientResource.release();
            }
        }
        return null;
    }
    
}
