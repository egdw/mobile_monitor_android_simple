package monitor.mobie.hdy.im.model;

import java.util.LinkedList;

/**
 * Created by hdy on 2017/9/8.
 * 通话记录
 */
public class ContactMessage {
    private String id;
    private LinkedList<ContactMessageDetail> messages;

    public ContactMessage(String id, LinkedList<ContactMessageDetail> messages) {
        this.id = id;
        this.messages = messages;
    }

    public ContactMessage() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LinkedList<ContactMessageDetail> getMessages() {
        return messages;
    }

    public void setMessages(LinkedList<ContactMessageDetail> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "ContactMessage{" +
                "id='" + id + '\'' +
                ", messages=" + messages +
                '}';
    }
}
