/**
 * Copyright (C) 2010-2013 Eugen Feller, INRIA <eugen.feller@inria.fr>
 *
 * This file is part of Snooze, a scalable, autonomic, and
 * energy-aware virtual machine (VM) management framework.
 *
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 */
package org.inria.myriads.snoozeec2.configurator.api;

import java.io.Serializable;

import org.inria.myriads.snoozecommon.communication.NetworkAddress;
import org.inria.myriads.snoozeec2.configurator.networking.NetworkingSettings;



/**
 * Node parameters class.
 * 
 * @author Eugen Feller
 */
public class EC2Configuration 
    implements Serializable
{
    /** Default serial version. */
    private static final long serialVersionUID = 1L;
    
    /** Networking settings.*/
    private NetworkAddress networkingSettings_;
    
    /** Image repository address */;
    private NetworkAddress imageRepositorySettings_;

    /** bootstrap repository address */;
    private NetworkAddress bootstrapSettings_;
    
    
    /** context path.*/
    private String contextPath_;
    
    
    /** Empty constructor. */
    public EC2Configuration()
    {
      networkingSettings_ = new NetworkAddress();  
      imageRepositorySettings_ = new NetworkAddress();
      bootstrapSettings_ = new NetworkAddress();
    }

    /**
     * @return the networkingSettings_
     */
    public NetworkAddress getNetworkingSettings()
    {
        return networkingSettings_;
    }

    /**
     * 
     * Sets the networking settings.
     * 
     * @param networkingSettings the networkingSettings to set
     */
    public void setNetworkingSettings(NetworkAddress networkingSettings)
    {
        networkingSettings_ = networkingSettings;
    }

    /**
     * @return the contextPath
     */
    public String getContextPath()
    {
        return contextPath_;
    }

    /**
     * @param contextPath the contextPath to set
     */
    public void setContextPath(String contextPath)
    {
        contextPath_ = contextPath;
    }

    /**
     * @return the imageRepositorySettings
     */
    public NetworkAddress getImageRepositorySettings()
    {
        return imageRepositorySettings_;
    }

    /**
     * @param imageRepositorySettings the imageRepositorySettings to set
     */
    public void setImageRepositorySettings(NetworkAddress imageRepositorySettings)
    {
        imageRepositorySettings_ = imageRepositorySettings;
    }

    /**
     * @return the bootstrapSettings
     */
    public NetworkAddress getBootstrapSettings()
    {
        return bootstrapSettings_;
    }

    /**
     * @param bootstrapSettings the bootstrapSettings to set
     */
    public void setBootstrapSettings(NetworkAddress bootstrapSettings)
    {
        bootstrapSettings_ = bootstrapSettings;
    }    
    
}
