package monitor.mobie.hdy.im.model;

/**
 * Created by hdy on 2017/9/8.
 */
public class ContactMessageDetail {
    /**
     * Call log type for incoming calls.
     */
    public static final int INCOMING_TYPE = 1;
    /**
     * Call log type for outgoing calls.
     */
    public static final int OUTGOING_TYPE = 2;
    /**
     * Call log type for missed calls.
     */
    public static final int MISSED_TYPE = 3;
    /**
     * Call log type for voicemails.
     */
    public static final int VOICEMAIL_TYPE = 4;
    /**
     * Call log type for calls rejected by direct user action.
     */
    public static final int REJECTED_TYPE = 5;
    /**
     * Call log type for calls blocked automatically.
     */
    public static final int BLOCKED_TYPE = 6;


    //电话号码
    private String phoneNumber;
    //备注
    private String name;
    //日期
    private String date;
    //时长
    private String duration;
    //类型
    private Integer type;

    public ContactMessageDetail(String phoneNumber, String name, String date, String duration, Integer type) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.type = type;
    }

    public ContactMessageDetail() {
    }

    public static int getIncomingType() {
        return INCOMING_TYPE;
    }

    public static int getOutgoingType() {
        return OUTGOING_TYPE;
    }

    public static int getMissedType() {
        return MISSED_TYPE;
    }

    public static int getVoicemailType() {
        return VOICEMAIL_TYPE;
    }

    public static int getRejectedType() {
        return REJECTED_TYPE;
    }

    public static int getBlockedType() {
        return BLOCKED_TYPE;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ContactMessageDetail{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", duration='" + duration + '\'' +
                ", type=" + type +
                '}';
    }
}
