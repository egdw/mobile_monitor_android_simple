package monitor.mobie.hdy.im.config;

import java.text.SimpleDateFormat;

public interface Constant {
     String DATA = "data";
     String SCKEY = "SCKEY";
     String WX_CORPID = "wx_corpid";
     String WX_CORPSECRET = "wx_corpsecret";
     String WX_AGENTID = "wx_agentid";

     String SCKEY_ENABLE = "SCKEY_enable";
     String WX_ENABLE = "wx_enable";

     String COUSTOM_ENABLE="coustom_enable";
     String COUSTOM_URL="coustom_url";
     String COUSTOM_METHOD="coustom_method";
     String COUSTOM_REMARK= "coustom_remark";


     String GET = "GET";
     String POST = "POST";
     String PUT  = "PUT";
     String DELETE  = "DELETE";

     SimpleDateFormat FORMATTER = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
}
