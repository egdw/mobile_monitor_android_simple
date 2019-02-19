package monitor.mobie.hdy.im.model;

/**
 * Created by hdy on 2019/2/19.
 * 用于企业wx发送信息
 */

public class Send {
    private String touser = "@all";
    private String msgtype = "markdown";
    private String agentid;
    private MarkDown markdown;


    public Send(String agentid, String message) {
        this.agentid = agentid;
        this.markdown = new MarkDown(message);
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public MarkDown getMarkdown() {
        return markdown;
    }

    public void setMarkdown(MarkDown markdown) {
        this.markdown = markdown;
    }

    private class MarkDown {
        private String content;

        public MarkDown(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
