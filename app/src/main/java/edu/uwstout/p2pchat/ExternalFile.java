package edu.uwstout.p2pchat;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class ExternalFile {
    File path;
    public ExternalFile(File path) {
        this.path = path;
    }
    public InMemoryFile loadIntoMemory() {
        byte[] data = new byte[(int) path.length()];
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(path));
            dis.readFully(data);
            return new InMemoryFile(path.getName(), data);
        } catch(Exception exc) {
            return null;
        }
    }
}
