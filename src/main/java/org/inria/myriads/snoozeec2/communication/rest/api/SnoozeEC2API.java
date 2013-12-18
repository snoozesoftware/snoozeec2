package org.inria.myriads.snoozeec2.communication.rest.api;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

/**
 * @author msimonin
 *
 */
public interface SnoozeEC2API
{

    
    /**
     * 
     * handle a get request on the api.
     * 
     * @return representation of the result.
     */
    @Get
    Representation handleGet();
    
    
    /**
     * 
     * handle a post request.
     * 
     * @param body      The body representation.
     * @return representation of the result.
     */
    @Post
    Representation handlePost(Representation body);

}
