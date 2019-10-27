package edu.uwstout.p2pchat;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class InMemoryFile {
    byte[] data;
    String filename;
    public InMemoryFile(String filename, byte[] data) {
        this.filename = filename;
        this.data = data;
    }
    public ExternalFile saveToStorage(Context context) {
        File path = new File(context.getFilesDir(), filename);
        try {
            if(!path.exists()) {
                path.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(path);
            stream.write(data);
            stream.close();
            return new ExternalFile(path);
        } catch(Exception exception) {
           return null;
        }
    }
}
