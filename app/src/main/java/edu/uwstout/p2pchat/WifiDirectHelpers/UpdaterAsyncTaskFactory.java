package edu.uwstout.p2pchat.WifiDirectHelpers;

/**
 * A modified factory pattern to provide functionality
 * which builds new instances of UpdaterAsyncTasks.
 * Having a factory permits us to use the dependency
 * injection pattern to make testing possible.
 */
public interface UpdaterAsyncTaskFactory
{
    /**
     * Creates a fresh UpdaterAsyncTask for
     * Notifying host our our INetAddress.
     * @return A fresh UpdaterAsyncTask where
     * execute has not been called yet.
     */
    default UpdaterAsyncTask newUpdateTask()
    {
        return new UpdaterAsyncTask();
    }
}
