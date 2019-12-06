package edu.uwstout.p2pchat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.uwstout.p2pchat.room.Message;

/**
 * Testing class for MessageViewHolder
 */
public class P2PMessageViewHolderTest
{

    private Message test_message;

    /**
     * Creates a test Message instance
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception
    {

        test_message = new Message("test");

    }

    /**
     * Tests binding data.
     */
    @Test
    public void bindData()
    {


    }


    /**
     * Cleans up after running a test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception
    {

    }
}