package edu.uwstout.p2pchat;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class P2PMessageAdapterTest
{
    private String test_text;
    private String test_timestamp;
    private String test_status;
    private String test_messageType;
    private P2PMessage test_message;
    private ArrayList<P2PMessage> test_messageList;
    private P2PMessageAdapter test_messageAdapter;











    @Before
    public void setUp() throws Exception {



        test_text = "Hello";
        test_timestamp = "11-09-1997 11:50:23.453";
        test_status = "end";
        test_messageType = "text";
        test_message = new P2PMessage(test_text,test_timestamp,test_status,
                test_messageType);
        test_messageList = new ArrayList<>();
        test_messageList.add(test_message);
        test_messageAdapter = new P2PMessageAdapter(test_messageList);
    }







    @Test
    public void testGetItemCount()
    {
        assertEquals(test_messageList.size(),test_messageAdapter.getItemCount());
    }

    @Test
    public void testGetText()
    {
        assertEquals(test_text,test_message.getText());
    }

    @Test
    public void testSetText()
    {
        test_message.setText("apple");
        assertEquals("apple",test_message.getText());
    }

    @Test
    public void testGetTimestamp()
    {
        assertEquals(test_timestamp,test_message.getTimestamp());
    }

    @Test
    public void testSetTimestamp()
    {
        test_message.setTimestamp("time");
        assertEquals("time",test_message.getTimestamp());
    }

    @Test
    public void testGetStatus()
    {
        assertEquals(test_status,test_message.getStatus());
    }

    @Test
    public void testSetStatus()
    {
        test_message.setStatus("start");
        assertEquals("start",test_message.getStatus());
    }

    @Test
    public void testGetMessageType()
    {
        assertEquals(test_messageType,test_message.getMessageType());
    }

    @Test
    public void testSetMessageType()
    {
        test_message.setMessageType("Image");
        assertEquals("Image",test_message.getMessageType());
    }


}