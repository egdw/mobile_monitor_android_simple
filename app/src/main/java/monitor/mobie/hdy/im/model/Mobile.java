package monitor.mobie.hdy.im.model;

/**
 * Created by hdy on 2017/9/8.
 * 手机的实体类
 */
public class Mobile {

    private String id;
    private String mobileNumber;
    private String password;
    private Message message;
    private ContactMessage contactMessage;

    public Mobile() {
    }

    public Mobile(String id, String mobileNumber, String password, String publicKey, Message message, ContactMessage contactMessage, String privateKey) {
        this.id = id;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.message = message;
        this.contactMessage = contactMessage;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public ContactMessage getContactMessage() {
        return contactMessage;
    }

    public void setContactMessage(ContactMessage contactMessage) {
        this.contactMessage = contactMessage;
    }


    @Override
    public String toString() {
        return "Mobile{" +
                "id=" + id +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", password='" + password + '\'' +
                ", message=" + message +
                ", contactMessage=" + contactMessage +
                '}';
    }
}
