package edu.uwstout.p2pchat.WifiDirectHelpers;

/**
 * A modified factory pattern to provide functionality
 * which builds new instances of ReceiverAsyncTasks.
 * Having a factory permits us to use the dependency
 * injection pattern to make testing possible.
 */
public interface ReceiverAsyncTaskFactory
{
    /**
     * Creates a fresh ReceiverAsyncTask for
     * receiving messages.
     * @return A fresh ReceiverAsyncTask where execute
     * has not been called yet.
     */
    default ReceiverAsyncTask newReceiverTask()
    {
        return new ReceiverAsyncTask();
    }
}