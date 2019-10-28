package edu.uwstout.p2pchat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.io.ByteStreams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class InMemoryFile
{
    private byte[] data;
    private String filename;
    private String mimeType;

    private final int oneThousand = 1000;

    public InMemoryFile(final String filenameStr, final byte[] dataRef, final String mimeTypeStr)
    {
        filename = filenameStr;
        data = dataRef;
        mimeType = mimeTypeStr;
    }

    public InMemoryFile(final String filenameStr, final InputStream streamRef,
            final String mimeTypeStr)
    {
        try
        {
            data = ByteStreams.toByteArray(streamRef);
            filename = filenameStr;
            mimeType = mimeTypeStr;
        }
        catch (Exception e)
        {
            data = new byte[0];
            filename = "Error";
        }
    }

    public ExternalFile saveToStorage(final Context context, final Date date)
    {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            return null;
        }
        else
        {
            File pathToDir = new File(context.getExternalFilesDir(null),
                    String.valueOf(date.getTime() / oneThousand));
            pathToDir.mkdir();
            File path = new File(pathToDir, filename);

            try
            {
                if (!path.exists())
                {
                    path.createNewFile();
                }
                FileOutputStream stream = new FileOutputStream(path);
                stream.write(data);
                stream.close();
                return new ExternalFile(path, mimeType);
            }
            catch (Exception exception)
            {
                return null;
            }
        }
    }

    public final String getMimeType()
    {
        return mimeType;
    }

    public final int getSize()
    {
        return data.length;
    }

    public final boolean equals(final InMemoryFile other)
    {
        return Arrays.equals(data, other.data) &&
                filename.equals(other.filename);
    }
}
