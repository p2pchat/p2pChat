package edu.uwstout.p2pchat.WifiDirectHelpers;

/**
 * A modified factory pattern to provide functionality
 * which builds new instances of LocalHostAsyncTasks.
 * Having a factory permits us to use the dependency
 * injection pattern to make testing possible.
 */
public interface LocalhostAsyncTaskFactory
{
    /**
     * Creates a fresh LocalhostAsyncTask for
     * resolving the localhost.
     * @return A fresh LocalhostAsyncTask where execute
     * has not been called yet.
     */
    default LocalhostAsyncTask newLocalhostTask()
    {
        return new LocalhostAsyncTask();
    }
}
