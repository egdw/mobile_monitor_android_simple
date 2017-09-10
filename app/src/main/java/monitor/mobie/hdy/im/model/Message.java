package monitor.mobie.hdy.im.model;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by hdy on 2017/9/8.
 * 存放短信消息
 */
public class Message {
    private String id;
    private LinkedList<MessageDetail> messages;

    public Message() {
    }

    public Message(String id, LinkedList<MessageDetail> messages) {
        this.id = id;
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LinkedList<MessageDetail> getMessages() {
        return messages;
    }

    public void setMessages(LinkedList<MessageDetail> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", messages=" + messages +
                '}';
    }
}
