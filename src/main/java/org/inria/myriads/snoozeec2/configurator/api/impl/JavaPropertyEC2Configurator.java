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
package org.inria.myriads.snoozeec2.configurator.api.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.inria.myriads.snoozecommon.communication.NetworkAddress;
import org.inria.myriads.snoozecommon.guard.Guard;
import org.inria.myriads.snoozecommon.util.NetworkUtils;
import org.inria.myriads.snoozeec2.configurator.api.EC2Configuration;
import org.inria.myriads.snoozeec2.configurator.api.EC2Configurator;
import org.inria.myriads.snoozeec2.configurator.networking.NetworkingSettings;
import org.inria.myriads.snoozeec2.exception.EC2ConfiguratorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Node configurator.
 * 
 * @author Matthieu Simonin
 */
public final class JavaPropertyEC2Configurator 
    implements EC2Configurator
{
    
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(JavaPropertyEC2Configurator.class);
    
    /** NodeParams. */
    private EC2Configuration EC2Configuration_;
        
    /** Properties. */
    private Properties properties_;
    
    /**
     * Initialize parameters.
     *  
     * @param configurationFile             The configuration file
     * @throws EC2ConfiguratorException 
     * @throws IOException 
     */
    public JavaPropertyEC2Configurator(String configurationFile) 
        throws EC2ConfiguratorException, IOException
    {
        Guard.check(configurationFile);
        EC2Configuration_ = new EC2Configuration();
          
        properties_ = new Properties();    
        FileInputStream fileInput = new FileInputStream(configurationFile);
        properties_.load(fileInput); 
        
        setNetworkingSettings();
        setImageRepositorySettings();
        setBootstrapSettings();
        setContextPath();
        
        fileInput.close();
    }


    /**
     * 
     * Sets the context path.
     * 
     * @throws EC2ConfiguratorException Exception.
     */
    private void setContextPath() throws EC2ConfiguratorException
    {
        String contextPath = properties_.getProperty("config.ec2.package.contextpath");
        if (!StringUtils.isBlank(contextPath))
        {
            EC2Configuration_.setContextPath(contextPath);
        }
        else
        {
            log_.debug("Unable to set the context path: ");
            throw new EC2ConfiguratorException("Blank context path");
        }
        
    }



    /**
     * 
     * Sets the bootstrap settings.
     * 
     * @throws EC2ConfiguratorException Exception
     */
    private void setBootstrapSettings() throws EC2ConfiguratorException
    {
        try
        {
            
            String listenAddress = properties_.getProperty("bootstrap.address");
            log_.debug("listenAddress " + listenAddress);
            if (listenAddress != null)
            {
                listenAddress = listenAddress.trim();
            } 
            else
            {
                listenAddress = InetAddress.getLocalHost().getHostAddress();
            }
            log_.debug("Getting port number");
            int controlDataPort = Integer.valueOf(getProperty("bootstrap.port"));  
            NetworkAddress controlDataAddress = 
                    NetworkUtils.createNetworkAddress(listenAddress, controlDataPort);
            EC2Configuration_.setBootstrapSettings(controlDataAddress);
            log_.debug("bootstrap setting set");
        }
        catch (Exception exception)
        {
            log_.debug("Unable to set the bootstrap settings : " + exception.getMessage());
            throw new EC2ConfiguratorException(exception.getMessage());
        }
        
    }

    /**
     * 
     * Sets the image repositoiry settings.
     * 
     * @throws EC2ConfiguratorException Exception
     */
    private void setImageRepositorySettings() throws EC2ConfiguratorException
    {
        
        try
        {
            String listenAddress = properties_.getProperty("imageRepository.address");
            if (listenAddress != null)
            {
                listenAddress = listenAddress.trim();
            } else
            {
                listenAddress = InetAddress.getLocalHost().getHostAddress();
            }
              
            int controlDataPort = Integer.valueOf(getProperty("imageRepository.port"));  
            NetworkAddress controlDataAddress = 
                    NetworkUtils.createNetworkAddress(listenAddress, controlDataPort);
            EC2Configuration_.setImageRepositorySettings(controlDataAddress);
        }
        catch( Exception exception)
        {
            log_.debug("Unable to set the image reposirory settings : " + exception.getMessage());
            throw new EC2ConfiguratorException(exception.getMessage());
        }
        
    }



    @Override
    public EC2Configuration getImageServiceConfiguration() 
    {
        return EC2Configuration_;
    }
    
    /**
     * Set the network settings.
     * 
     * @throws EC2ConfiguratorException    The configuration exception
     * @throws UnknownHostException         The unknown host exception
     */
    private void setNetworkingSettings() 
        throws EC2ConfiguratorException, UnknownHostException 
    {
        try
        {
            String listenAddress = properties_.getProperty("network.listen.address");
            if (listenAddress != null)
            {
                listenAddress = listenAddress.trim();
            } else
            {
                listenAddress = InetAddress.getLocalHost().getHostAddress();
            }
              
            int controlDataPort = Integer.valueOf(getProperty("network.listen.port"));  
            NetworkAddress controlDataAddress = 
                    NetworkUtils.createNetworkAddress(listenAddress, controlDataPort);
            EC2Configuration_.setNetworkingSettings(controlDataAddress);
        }
        catch (Exception exception)
        {
            log_.debug("Unable to set the networking settings : " + exception.getMessage());
            throw new EC2ConfiguratorException(exception.getMessage());
        }
        
        
    }

    
    /**
     * Returns the content of a properties.
     * 
     * @param tag                           The tag
     * @return                              The content string
     * @throws EC2ConfiguratorException    The configuration exception
     */
    private String getProperty(String tag) 
        throws EC2ConfiguratorException
    {
        String content = properties_.getProperty(tag);
        if (content == null) 
        {
            throw new EC2ConfiguratorException(String.format("%s entry is missing", tag));
        }
        
        content = content.trim();
        return content;             
    }

}
