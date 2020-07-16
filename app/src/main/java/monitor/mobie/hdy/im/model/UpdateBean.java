package monitor.mobie.hdy.im.model;

/**
 * @author hdy
 * 更新的实体类
 */
public class UpdateBean {
    private String download_url;
    private boolean force;
    private String update_content;
    private int v_code;
    private String v_name;

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public String getUpdate_content() {
        return update_content;
    }

    public void setUpdate_content(String update_content) {
        this.update_content = update_content;
    }

    public int getV_code() {
        return v_code;
    }

    public void setV_code(int v_code) {
        this.v_code = v_code;
    }

    public String getV_name() {
        return v_name;
    }

    public void setV_name(String v_name) {
        this.v_name = v_name;
    }

    @Override
    public String toString() {
        return "UpdateBean{" +
                ", download_url='" + download_url + '\'' +
                ", force=" + force +
                ", update_content='" + update_content + '\'' +
                ", v_code='" + v_code + '\'' +
                ", v_name='" + v_name + '\'' +
                '}';
    }
}
