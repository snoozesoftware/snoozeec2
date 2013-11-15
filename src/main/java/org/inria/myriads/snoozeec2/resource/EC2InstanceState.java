package org.inria.myriads.snoozeec2.resource;

import org.inria.myriads.snoozecommon.communication.virtualcluster.status.VirtualMachineStatus;

/**
 * @author msimonin
 *
 */
public enum EC2InstanceState
{
    /** pending.*/
    PENDING(0, "pending"),
    /** runnning.*/
    RUNNING(16, "running"),
    /** shutting-down.*/
    SHUTTING_DOWN(32, "shutting-down"),
    /** terminated.*/
    TERMINATED(48, "terminated"),
    /** stopping.*/
    STOPPING(64, "stopping"),
    /** stopped.*/
    STOPPED(80, "stopped");
    
    /** internal code.*/
    private int code_;
    
    /** internal message.*/
    private String message_;
    
    /**
     * 
     * Constructor.
     * 
     * @param code      state code (according to EC2)
     * @param message   message.
     */
    private EC2InstanceState(int code, String message)
    {
        code_ = code;
        message_ = message;
    }

    /**
     * @return the code
     */
    public int getCode()
    {
        return code_;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code)
    {
        code_ = code;
    }

    /**
     * @return the message
     */
    public String getMessage()
    {
        return message_;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message)
    {
        message_ = message;
    }
    
    /**
     * 
     * translate a snooze vm status to EC2 instance state.
     * 
     * @param status    the snooze vm status.
     * @return  The EC2 instance state.
     */
    public static EC2InstanceState translateState(VirtualMachineStatus status)
    {
        switch(status)
        {
        case RUNNING: 
            return EC2InstanceState.RUNNING;
        case OFFLINE:
            return EC2InstanceState.TERMINATED;
        case SHUTDOWN_PENDING:
            return EC2InstanceState.SHUTTING_DOWN;
        default:
            return EC2InstanceState.RUNNING;
        }
    }
}
