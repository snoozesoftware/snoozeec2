package org.inria.myriads.snoozeec2.configurator.networking;

import org.inria.myriads.snoozecommon.communication.groupmanager.ListenSettings;

/**
 * 
 * Network Settings.
 * 
 * @author msimonin
 *
 */
public class NetworkingSettings 
{
   
    /** Listen parameters. */
    private ListenSettings listen_;

    
    /** Constructor. */
    public NetworkingSettings()
    {
        listen_ = new ListenSettings();

    }
    
    /**
     * Returns the listen parameters.
     * 
     * @return  The listen parameters
     */
    public ListenSettings getListen()
    {
        return listen_;
    }
    

}
