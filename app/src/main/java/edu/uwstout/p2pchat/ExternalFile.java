package edu.uwstout.p2pchat;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Represents a file stored on a device.
 */
public class ExternalFile
{

    /**
     * Reference to file on device.
     */
    private File path;
    /**
     * Mime type of file.
     */
    private String mimeType;

    /**
     * @return Mime type getter
     */
    public final String getMimeType()
    {
        return mimeType;
    }

    /**
     * @param mimeTypeStr Mime type setter
     */
    public final void setMimeType(final String mimeTypeStr)
    {
        mimeType = mimeTypeStr;
    }

    /**
     * External File constructor
     * @param pathRef Path to file
     * @param mimeTypeStr Mime type of file
     */
    public ExternalFile(final File pathRef, final String mimeTypeStr)
    {
        path = pathRef;
        mimeType = mimeTypeStr;
    }

    /**
     * Loads file from storage into memory
     * @return InMemoryFile of loaded file or null if failed.
     */
    public final InMemoryFile loadIntoMemory()
    {
        byte[] data = new byte[(int) path.length()];
        try
        {
            DataInputStream dis = new DataInputStream(new FileInputStream(path));
            dis.readFully(data);
            return new InMemoryFile(path.getName(), data, mimeType);
        }
        catch (Exception exc)
        {
            return null;
        }
    }

    /**
     * Deletes this file in storage.
     * @return true if successful
     */
    public final boolean delete()
    {
        return path.delete();
    }

    /**
     * @return Returns true if this file exists.
     */
    public final boolean exists()
    {
        return path.exists();
    }

    /**
     * @return Returns the string representation of the path to this file.
     */
    public final String getPath()
    {
        return path.getPath();
    }
}
