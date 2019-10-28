package edu.uwstout.p2pchat;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class ExternalFile
{
    private File path;
    private String mimeType;

    public ExternalFile(final File pathRef, final String mimeTypeStr)
    {
        path = pathRef;
        mimeType = mimeTypeStr;
    }

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

    public final boolean delete()
    {
        return path.delete();
    }

    public final boolean exists()
    {
        return path.exists();
    }

    public final String getPath()
    {
        return path.getPath();
    }
}
