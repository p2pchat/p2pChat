package edu.uwstout.p2pchat;

import java.net.Socket;

import edu.uwstout.p2pchat.FileTransfer.SendDataService;

public class TestSendDataService extends SendDataService
{
    Socket testSocket;
    public TestSendDataService(Socket testSocket)
    {
        super();
        this.testSocket = testSocket;
    }
    @Override
    public Socket getSocket()
    {
        return testSocket;
    }
}
