package monitor.mobie.hdy.im.config;

import java.text.SimpleDateFormat;

public interface Constant {
     public static final String BARK_ENABLE = "bark_enable";
     public static final String BARK_URL = "bark_url";
     public static final String COUSTOM_ENABLE = "coustom_enable";
     public static final String COUSTOM_METHOD = "coustom_method";
     public static final String COUSTOM_REMARK = "coustom_remark";
     public static final String COUSTOM_URL = "coustom_url";
     public static final String DATA = "data";
     public static final String DELETE = "DELETE";
     public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
     public static final String GET = "GET";
     public static final String POST = "POST";
     public static final String PUT = "PUT";
     public static final String SCKEY = "SCKEY";
     public static final String SCKEY_ENABLE = "SCKEY_enable";
     public static final String WX_AGENTID = "wx_agentid";
     public static final String WX_CORPID = "wx_corpid";
     public static final String WX_CORPSECRET = "wx_corpsecret";
     public static final String WX_ENABLE = "wx_enable";



     public static final String EMAIL_ENABLE = "email_enable";
     public static final String EMAIL_ACCOUNT = "email_account";
     public static final String EMAIL_PASSWORD = "email_password";
     public static final String EMAIL_TYPE = "email_type";
     public static final String EMAIL_RECEIVER = "email_receiver";


     /**
      * 预设常用的软件包名，一键启用对对应软件的通知接收
      */
     //中国常用社交软件
     public static final String[] ChinaTalkAPP = {
             "com.tencent.mobileqq",
             "com.tencent.mm",
             "com.alibaba.android.rimet",
             "com.tencent.qqlite",
             "com.tencent.minihd.qq",
             "com.tencent.mobileqqi",
             "com.tencent.tim",
             "com.bullet.messenger",
             "im.yixin",
             "com.juphoon.justalk",
             "com.tencent.eim",
             "com.tencent.wework",
             "com.sina.weibo",
             "com.p1.mobile.putong",
             "com.immomo.momo"
     };

     //中国常用购物消费软件
     public static final String[] ChinaShopAPP = {
             "com.taobao.taobao",
             "com.eg.android.AlipayGphone",
             "com.jingdong.app.mall",
             "com.sankuai.meituan",
             "com.taobao.idlefish",
             "com.xunmeng.pinduoduo",
             "com.xiaomi.shop",
             "com.tmall.wireless",
             "com.suning.mobile.ebuy",
             "com.alibaba.wireless",
             "com.xingin.xhs",
             "com.nuomi",
             "com.dangdang.buy2",
             "com.netease.yanxuan",
             "com.koudai.weidian.buyer",
             "com.thestore.main",
             "com.gome.eshopnew",
             "com.huodao.hdphone",
             "com.fenqile.fenqile",
             "com.taobao.litetao",
             "com.jm.android.jumei",
             "com.meilishuo",
             "com.jd.jdlite",
             "com.meituan.retail.v.android",
             "cn.missfresh.application",
             "com.baidu.wallet",
             "com.wangyin.payment"
     };

     //国外常用社交软件
     public static final String[] OtherTalkAPP = {
             "com.twitter.android",
             "jp.naver.line.android",
             "com.skype.raider",
             "com.facebook.katana",
             "com.android.chrome",
             "com.whatsapp",
             "com.facebook.orca"
     };

     //新闻类软件
     public static final String[] NewsAPP = {
             "com.ss.android.article.news",
             "com.sina.weibo",
             "com.netease.newsreader.activity",
             "com.sohu.newsclient"
     };

     //短视频类
     public static final String[] ShortVideos = {
             "com.ss.android.ugc.aweme",
             "com.smile.gifmaker",
             "com.ss.android.ugc.live",
             "com.yixia.videoeditor",
             "com.youku.phone",
             "com.qiyi.video",
             "com.tencent.qqlive",
             "air.tv.douyu.android",
             "com.panda.videoliveplatform"
     };


     //外卖类
     public static final String[] TakeOut = {
             "me.ele",
             "com.sankuai.meituan.takeoutnew",
             "com.dianping.v1"
     };


     //音乐类
     public static final String[] Music = {
             "com.tencent.qqmusic",
             "com.kugou.android",
             "com.tencent.karaoke",
             "cn.kuwo.player",
             "fm.xiami.main",
             "com.changba",
             "com.netease.cloudmusic",
             "com.ximalaya.ting.android"
     };

     // 短信和电话
     public static final String[] PhoneAndMessage = {
             "com.android.contacts",
             "com.android.dialer",
             "com.android.mms",
             "com.android.providers.telephony",
             "com.android.phone",
             "com.android.incallui"
     };

}
