package monitor.mobie.hdy.im.model;

/**
 * Created by hdy on 2019/2/19.
 */

public class Token {
    private int errcode;
    private String errmsg;
    private String access_token;
    private int expires_in;

    public Token() {
    }

    public Token(int errcode, String errmsg, String access_token, int expires_in) {
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.access_token = access_token;
        this.expires_in = expires_in;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    //判断是否没有错误
    public boolean isOk() {
        return "ok".equals(errmsg);
    }

    @Override
    public String toString() {
        return "Token{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                '}';
    }
}
