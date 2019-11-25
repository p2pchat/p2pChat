package edu.uwstout.p2pchat;

import android.content.Intent;
import android.os.Bundle;


import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import edu.uwstout.p2pchat.FileTransfer.ReceiverAsyncTask;
import edu.uwstout.p2pchat.FileTransfer.SendDataService;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SendDataServiceTest
{

    SendDataService sendDataService;
    TestSocket testSocket;

    @Before
    public void setup()
    {
        testSocket = new TestSocket();
        sendDataService = new TestSendDataService(testSocket);
    }

    @Test
    public void sendData()
    {
        Bundle bundle = new Bundle();
        InMemoryFile imf = new InMemoryFile("This is a test message!");
        bundle.putSerializable(SendDataService.EXTRAS_IN_MEMORY_FILE, imf);
        bundle.putSerializable(SendDataService.EXTRAS_PEER_ADDRESS, null);
        bundle.putSerializable(SendDataService.EXTRAS_PEER_PORT, ReceiverAsyncTask.MAGIC_PORT);
        Intent intent = new TestIntent(SendDataService.ACTION_SEND_DATA, bundle);
        sendDataService.testHandleIntent(intent);
        ArrayList<TestSocket.TestSocketCalls> testSocketCalls = new ArrayList<>();
        testSocketCalls.add(TestSocket.TestSocketCalls.bind);
        testSocketCalls.add(TestSocket.TestSocketCalls.connect);
        testSocketCalls.add(TestSocket.TestSocketCalls.isConnected);
        testSocketCalls.add(TestSocket.TestSocketCalls.getOutputStream);
        testSocketCalls.add(TestSocket.TestSocketCalls.isConnected);
        assertEquals(testSocketCalls, testSocket.getSocketCalls());
    }

}

