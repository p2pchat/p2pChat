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
    byte[] data;
    String filename;
    String mimeType;

    public InMemoryFile(String filename, byte[] data, String mimeType)
    {
        this.filename = filename;
        this.data = data;
        this.mimeType = mimeType;
    }

    public InMemoryFile(String filename, InputStream stream, String mimeType)
    {
        try
        {
            data = ByteStreams.toByteArray(stream);
            this.filename = filename;
            this.mimeType = mimeType;
        }
        catch (Exception e)
        {
            data = new byte[0];
            filename = "Error";
        }
    }

    public ExternalFile saveToStorage(Context context, Date date)
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
                    String.valueOf(date.getTime() / 1000));
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

    public String getMimeType()
    {
        return mimeType;
    }

    public int getSize()
    {
        return data.length;
    }

    public boolean equals(InMemoryFile other)
    {
        return Arrays.equals(data, other.data) &&
                filename.equals(other.filename);
    }
}
