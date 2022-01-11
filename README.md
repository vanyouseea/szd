# 次级域名分发系统(Secondary Zone Distribution System)  
次级域名分发系统(Szd)是一个以java语言开发基于cloudflare API的域名分发系统(CF的A记录)，只要域名托管于cloudflare那么就可以使用它。亮点是支持多级域名分享，例如可以将二级域名分享给用户，用户可根据此二级域名创建属于他自己的三级域名，实现了用户权限管理，邀请用户注册，管理员登录安全性验证等功能
```
   _____   ______  _____       _____                                        
  / ____| |___  / |  __ \     / ____|                _                    
 | (___      / /  | |  | |   | (___    _   _   ___  | |_    ___   _ __ ___  
  \___ \    / /   | |  | |    \___ \  | | | | / __| | __|  / _ \ | '_ ` _ \ 
  ____) |  / /__  | |__| |    ____) | | |_| | \__ \ | |_  |  __/ | | | | | |
 |_____/  /_____| |_____/    |_____/   \__  | |___/  \__|  \___| |_| |_| |_|
                                        __/ |                               
                                       |___/       
```

## 最低环境需求
| 类型 | - |
| ---- | ----|
| CPU | 1C |
| RAM | 0.75G |
| 硬盘 | 5GB |
  
## 体验Szd in heroku  
Szd已部署于heroku,你可以访问以下路径体验最新版的次级域名分发系统,你也可以将工程fork到自己的仓库用自己的heroku账号进行部署（推荐）  

https://szds.herokuapp.com  
  
## 功能介绍  
如果是首次使用，系统会直接引导注册管理员，一旦管理员注册成功，注册管理员的功能将被关闭  
### 管理员首页预览  
![alt 管理员首页](https://github.com/vanyouseea/szd/blob/main/pic/01.PNG)
### 普通用户首页预览  
![alt 普通用户首页](https://github.com/vanyouseea/szd/blob/main/pic/02.PNG)  
首页中用户可以看见自己所能持有的当前子域数/最大子域数，由于普通用户的界面和管理员有重合，所以接下来着重介绍管理员界面和初始化流程  

### [配置] -> 系统配置  
第一次使用请先配置Cloudflare的信息，配置好CF信息后就可以使用Szd了  
![alt 系统配置](https://github.com/vanyouseea/szd/blob/main/pic/03.PNG)  
  - **CF_AUTH_EMAIL**  
    你的Cloudflare邮件地址  
  - **CF_AUTH_KEY**  
    CF账号个人资料中的api-token
  - **管理员安全相关**  
    WX_CALLBACK_IND  
    WX_CALLBACK_TOKEN  
    WX_CORPID  
    WX_CORPSECRET  
    WX_AGENTID  
    WX_CALLBACK_AESKEY  
    此步配置非必须，不过强烈建议配置，请参考文档 https://github.com/vanyouseea/o365/raw/master/docs/%E9%85%8D%E7%BD%AE%E5%BE%AE%E4%BF%A1%E5%93%8D%E5%BA%94.docx
  
### [配置] -> 域名管理  
![alt 域名管理](https://github.com/vanyouseea/szd/blob/main/pic/04.PNG)  
当配置好CF的信息后就可以前往此页  
  - **从CF拉取**  
    配置好CF的信息后，点击此按钮可以拉取托管在CF的域名的信息所有**A**记录  
  - **分享/取消**  
    你可以选择分享/取消分享子域/主域  
  - **删除**  
    留下需要分享的域名后，其他的域名都可以删除。理论上一旦开始分享后你就不必再删除这里的记录了  
  
### [用户] -> 邀请注册  
  - **新增**  
  ![alt 新增](https://github.com/vanyouseea/szd/blob/main/pic/05.PNG)  
    生成邀请码时可以选择数量，同时也可以指定通过这些邀请码注册的用户所能持有的最大子域数  
  - **导出**  
    导出所有邀请信息  
  - **删除**  
    删除选中的邀请记录  
  
### [用户] -> 管理用户  
![alt 管理用户](https://github.com/vanyouseea/szd/blob/main/pic/06.PNG)  
  - **启用/禁用**  
    启用和禁用一个非管理员用户(注意即使用户被禁用，只要管理员不删除他的解析那么，他的CF解析仍然可用，如果想要完全禁止用户及其解析，先禁用此用户，然后请前往[用户]->管理用户域名中删除所有属于该用户编号的解析即可)  
  
### [用户] -> 管理用户域名  
![alt 管理用户域名](https://github.com/vanyouseea/szd/blob/main/pic/07.PNG)  
  - **编辑**  
    管理员可以帮助用户编辑其A记录，包括修改其子域前缀  
  - **删除**  
    管理员删除用户的A记录，需要注意的是如果用户的A记录是经由管理员删除，那么他的当前持有子域数-1  
  
### [我的] -> 可用域名  
![alt 可用域名](https://github.com/vanyouseea/szd/blob/main/pic/08.PNG)  
当管理员分享域名后，该域名会在此处出现，如果管理员分享的是二级域名，那么用户只能创建三级域名  

### [我的] -> 我的子域  
当用户在可用域名中新建了子域后，可以在这里找到和修改其信息(修改仅限于IP和是否用CF代理)，不可更改子域前缀，需要注意的是用户如果删除此解析，他所持有的子域数**并不会**减少  
