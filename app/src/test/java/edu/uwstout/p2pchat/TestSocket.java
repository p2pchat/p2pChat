package edu.uwstout.p2pchat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

public class TestSocket extends Socket
{
    public enum TestSocketCalls { bind, connect, isConnected, getOutputStream }

    boolean isConnected = false;

    public TestSocket() {
        socketCalls = new ArrayList<>();
    }

    public ArrayList<TestSocketCalls> getSocketCalls()
    {
        return socketCalls;
    }

    ArrayList<TestSocketCalls> socketCalls;

    @Override
    public void bind(SocketAddress bindpoint) throws IOException
    {
        socketCalls.add(TestSocketCalls.bind);
    }

    @Override
    public void connect(SocketAddress endpoint) throws IOException
    {
        isConnected = true;
        socketCalls.add(TestSocketCalls.connect);
    }

    @Override
    public boolean isConnected()
    {
        socketCalls.add(TestSocketCalls.isConnected);
        return isConnected;
    }

    @Override
    public OutputStream getOutputStream() throws IOException
    {
        socketCalls.add(TestSocketCalls.getOutputStream);
        return null;
    }
}
