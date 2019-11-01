package edu.uwstout.p2pchat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.io.ByteStreams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;

/**
 * Represents a file that has been loaded into memory.
 */
public class InMemoryFile
{
    /**
     * Byte array containing contents of file.
     */
    private byte[] data;
    /**
     * Filename of the file loaded into memory.
     */
    private String filename;
    /**
     * Mime type of the file loaded into memory.
     */
    private String mimeType;

    /**
     * Conversion factor between milliseconds since epoch to second since epoch
     */
    private final int oneThousand = 1000;

    /**
     * Constructs based on raw data
     *
     * @param filenameStr
     *         Name of file
     * @param dataRef
     *         Contents of file
     * @param mimeTypeStr
     *         Mime type of file
     */
    public InMemoryFile(final String filenameStr, final byte[] dataRef, final String mimeTypeStr)
    {
        filename = filenameStr;
        data = dataRef;
        mimeType = mimeTypeStr;
    }

    /**
     * Constructs based on InputStream
     *
     * @param filenameStr
     *         Name of file
     * @param streamRef
     *         InputStream to load data from
     * @param mimeTypeStr
     *         Mime type of file
     */
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

    /**
     * Saves this file to storage.
     *
     * @param context
     *         MainActivity context
     * @param date
     *         Current date and time
     * @return ExternalFile reference if successful, otherwise null
     */
    public ExternalFile saveToStorage(final Context context, final Date date)
    {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MainActivity.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
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

    /**
     * @return Mime type getter
     */
    public final String getMimeType()
    {
        return mimeType;
    }

    /**
     * @return File size getter
     */
    public final int getSize()
    {
        return data.length;
    }

    /**
     * Compares two InMemoryFiles
     *
     * @param other
     *         Object to compare to
     * @return true if same value
     */
    public final boolean equals(final InMemoryFile other)
    {
        return Arrays.equals(data, other.data) &&
                filename.equals(other.filename);
    }
}
