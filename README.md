# mobile_monitor_android

恶搞大王手机通知转发器安卓端

> 开发目的：将不同设备安装的通知信息（短信、电话、应用）统一转发到一个手机上。

# 最新下载地址

[点击这里查看最新版本](https://github.com/egdw/mobile_monitor_android_simple/releases/)

# 后续更新
1. 添加自动转发关键词通知的功能。
2. 只接受关键词通知转发
3. 提供配置的快速导出和导出,不需要每次换设备或重装费力的输入数据

## 最新更新
1.2021/7/15
更新了bark推送

2. 2020/7/16
目前已经支持自定义接口推送,支持两种请求方式GET和POST方式
支持多接口推送,包括微信推送,Server酱推送,自定义接口推送
支持检测更新

3. 2019
优化了企业微信推送，现在已经可以支持企业微信通知转发的微信上。（我知道很多人可能担心server酱的安全问题）
推荐大家使用企业微信转微信的方式。具体的方法基本和下面的企业微信设置方法差不多，唯一的区别就是需要扫描一个企业微信的二维码就可以把企业微信的推送
转发到微信上了。

## 如何将企业微信的通知转发到微信上步骤
1. 首先先按照[企业微信设置方法](https://github.com/egdw/mobile_monitor_android_simple/wiki/%E4%BC%81%E4%B8%9A%E5%BE%AE%E4%BF%A1%E8%AE%BE%E7%BD%AE%E6%96%B9%E6%B3%95)
2. [点击这个链接](https://work.weixin.qq.com/wework_admin/frame#profile/wxPlugin)
3. 找到【邀请关注】旁边的二维码，用你的微信扫描一下即可。以后所有的企业微信通知都会转发到你的微信上，不需要再下载企业微信了。

## 使用方法
* [Bark设置方法](https://github.com/egdw/mobile_monitor_android_simple/wiki/Bark-%E8%AE%BE%E7%BD%AE)
* [多接口同时推送设置方法](https://github.com/egdw/mobile_monitor_android_simple/wiki/%E5%A4%9A%E6%8E%A5%E5%8F%A3%E8%AF%B7%E6%B1%82%E7%94%A8%E6%B3%95)
* [自定义推送设置方法](https://github.com/egdw/mobile_monitor_android_simple/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89%E6%8E%A8%E9%80%81)
* [Server酱设置方法](https://github.com/egdw/mobile_monitor_android_simple/wiki/Server%E9%85%B1%E6%8E%A8%E9%80%81%E8%AE%BE%E7%BD%AE%E6%96%B9%E6%B3%95)
* [企业微信设置方法](https://github.com/egdw/mobile_monitor_android_simple/wiki/%E4%BC%81%E4%B8%9A%E5%BE%AE%E4%BF%A1%E8%AE%BE%E7%BD%AE%E6%96%B9%E6%B3%95)

## 目前功能

* 所有通知推送
* 部分应用通知推送
* 关键词屏蔽
* 亮屏推送
* 自定义地址推送
* 多地址多链接同时推送


## 安卓端界面

![界面图1](https://upload-images.jianshu.io/upload_images/9127053-00a40f2bf42567ec.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/9127053-4d98fd3b0385f0ac.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![界面图2](https://upload-images.jianshu.io/upload_images/9127053-d7228d03c48e3828.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 企业微信效果图

![](https://upload-images.jianshu.io/upload_images/9127053-12795ddf1657a2ac.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



## 微信推送效果图

![微信图1](https://github.com/egdw/temp_pic_upload/blob/master/353DF1E0E25623A8BF6D2416C3A9C8DD.jpg?raw=true)
![微信图2](https://github.com/egdw/temp_pic_upload/blob/master/51F0D251CD73DE1BFB04B7F3A9AAA61D.jpg?raw=true)


# Server酱具体步骤

## 注册Server酱

注册一个Server酱,并绑定好微信.[点击这里进行注册](http://sc.ftqq.com/3.version)

## 复制SCKEY

![](https://upload-images.jianshu.io/upload_images/9127053-0bbf0f40d67fa3c1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 安装并给予读取通知栏权限

![](https://upload-images.jianshu.io/upload_images/9127053-a769182f93214694.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 输入SCKEY码

![](https://upload-images.jianshu.io/upload_images/9127053-8574913042e289ca.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 加入白名单

防止被系统杀掉.

## 实际效果

![](https://upload-images.jianshu.io/upload_images/9127053-e314aad7129a502a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/9127053-fa5def5886b68d34.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


# 企业微信具体步骤

## 注册一个企业微信

也可以直接在手机上下载企业微信app

[企业微信官网](https://work.weixin.qq.com/wework_admin/loginpage_wx?etype=expired#index)

## 获取企业编号(corpid)

![](https://upload-images.jianshu.io/upload_images/9127053-ccf4d591f2372a86.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 获取应用编号(agentid) & 获取应用密钥(corpsecret)

![](https://upload-images.jianshu.io/upload_images/9127053-b158c310a83f08d8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/9127053-f8dd0c033041a882.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 输入到软件中

![](https://upload-images.jianshu.io/upload_images/9127053-1168c2511fb397f4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)'

## 实际效果

![](https://upload-images.jianshu.io/upload_images/9127053-12795ddf1657a2ac.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
