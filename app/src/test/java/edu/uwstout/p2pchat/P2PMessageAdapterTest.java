package edu.uwstout.p2pchat;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import edu.uwstout.p2pchat.room.Message;

public class P2PMessageAdapterTest
{

    private Message test_message;
    private ArrayList<Message> test_messageList;
    private P2PMessageAdapter test_messageAdapter;











    @Before
    public void setUp() throws Exception {




        test_message = new Message("test");
        test_messageList = new ArrayList<>();
        test_messageList.add(test_message);
        test_messageAdapter = new P2PMessageAdapter(test_messageList);
    }







    @Test
    public void testGetItemCount()
    {
        assertEquals(test_messageList.size(),test_messageAdapter.getItemCount());
    }



}