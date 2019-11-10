package edu.uwstout.p2pchat.testing;

import java.util.Date;

import edu.uwstout.p2pchat.room.Message;
import edu.uwstout.p2pchat.room.Peer;

public class MockMessages {
    private static Date now = new Date();
    private static int currentID = 0;
    public static int getCurrentID() {
        return currentID++;
    }
    private static Date createTimestamp(int minutesAgo) {
        return new Date(now.getTime() - (minutesAgo * 60 * 1000));
    }
    private static Message textSent(Peer to, String content, int minutesAgo) {
        Message message = new Message(to.macAddress);
        message.id = getCurrentID();
        message.content = content;
        message.mimeType = "text/message";
        message.sent = true;
        message.timestamp = createTimestamp(minutesAgo);
        return message;
    }
    private static Message textRec(Peer from, String content, int minutesAgo) {
        Message message = new Message(from.macAddress);
        message.id = getCurrentID();
        message.content = content;
        message.mimeType = "text/message";
        message.sent = false;
        message.timestamp = createTimestamp(minutesAgo);
        return message;
    }
    public static final Message austin1 = textSent(MockPeers.austin, "This is a test sent message", 60);
    public static final Message austin2 = textRec(MockPeers.austin, "This is a test received message", 50);
    public static final Message austin3 = textSent(MockPeers.austin, "Here is another test sent message", 30);
    public static final Message austin4 = textRec(MockPeers.austin, "Here is another test received message", 15);

}
