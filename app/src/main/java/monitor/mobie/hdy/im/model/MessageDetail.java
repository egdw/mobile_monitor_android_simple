package monitor.mobie.hdy.im.model;

/**
 * Created by hdy on 2017/9/8.
 */
public class MessageDetail {

    /** Message type: all messages. */
    public static final int MESSAGE_TYPE_ALL    = 0;

    /** Message type: inbox. */
    public static final int MESSAGE_TYPE_INBOX  = 1;

    /** Message type: sent messages. */
    public static final int MESSAGE_TYPE_SENT   = 2;

    /** Message type: drafts. */
    public static final int MESSAGE_TYPE_DRAFT  = 3;

    /** Message type: outbox. */
    public static final int MESSAGE_TYPE_OUTBOX = 4;

    /** Message type: failed outgoing message. */
    public static final int MESSAGE_TYPE_FAILED = 5;

    /** Message type: queued to send later. */
    public static final int MESSAGE_TYPE_QUEUED = 6;

    //短信备注名
    private String messageName;
    private String messageNum;
    private String messageTime;
    private String messgaeText;
    private Integer type;

    public MessageDetail() {
    }

    public MessageDetail(String messageName, String messageNum, String messageTime, String messgaeText, Integer type) {
        this.messageName = messageName;
        this.messageNum = messageNum;
        this.messageTime = messageTime;
        this.messgaeText = messgaeText;
        this.type = type;
    }

    public static int getMessageTypeAll() {
        return MESSAGE_TYPE_ALL;
    }

    public static int getMessageTypeInbox() {
        return MESSAGE_TYPE_INBOX;
    }

    public static int getMessageTypeSent() {
        return MESSAGE_TYPE_SENT;
    }

    public static int getMessageTypeDraft() {
        return MESSAGE_TYPE_DRAFT;
    }

    public static int getMessageTypeOutbox() {
        return MESSAGE_TYPE_OUTBOX;
    }

    public static int getMessageTypeFailed() {
        return MESSAGE_TYPE_FAILED;
    }

    public static int getMessageTypeQueued() {
        return MESSAGE_TYPE_QUEUED;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public String getMessageNum() {
        return messageNum;
    }

    public void setMessageNum(String messageNum) {
        this.messageNum = messageNum;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessgaeText() {
        return messgaeText;
    }

    public void setMessgaeText(String messgaeText) {
        this.messgaeText = messgaeText;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MessageDetail{" +
                "messageName='" + messageName + '\'' +
                ", messageNum='" + messageNum + '\'' +
                ", messageTime='" + messageTime + '\'' +
                ", messgaeText='" + messgaeText + '\'' +
                ", type=" + type +
                '}';
    }
}
