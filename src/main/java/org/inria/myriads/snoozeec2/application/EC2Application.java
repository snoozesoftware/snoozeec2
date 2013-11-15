package org.inria.myriads.snoozeec2.application;


import org.inria.myriads.snoozeec2.resource.SnoozeEC2Resource;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

/**
 * 
 * Image service application.
 * 
 * @author msimonin
 *
 */
public class EC2Application extends Application 
{

    /**
     * 
     * Constructor.
     * 
     * @param context   the context.
     */
    public EC2Application(Context context) 
    {
        super(context);
    }
    
    /**
     * Creates the root restlet.
     * 
     * @return  The restlet router
     */
    public Restlet createInboundRoot() 
    {  
         Router router = new Router(getContext());  
         router.attach("/", SnoozeEC2Resource.class);
         return router;  
    }

}
