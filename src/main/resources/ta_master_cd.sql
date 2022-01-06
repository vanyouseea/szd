--sys use prop
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('GLOBAL_REG','Y','Y为允许再次注册次级域名分发系统管理员',sysdate,null,sysdate,'szd',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('CF_AUTH_EMAIL','337845818@qq.com','注册在CF的邮件',sysdate,null,sysdate,'szd',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('CF_AUTH_KEY','','CF账号个人资料中的api-token',sysdate,null,sysdate,'szd',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('HTTP_PROXY','','设置代理，例如127.0.0.1:8080',sysdate,null,sysdate,'szd',sysdate);

--update wx info
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('WX_CALLBACK_IND','N','Y to enable the login verify on wx',sysdate,null,sysdate,'szd',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('WX_CALLBACK_TOKEN','','callback token',sysdate,null,sysdate,'szd',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('WX_CORPID','','corp id',sysdate,null,sysdate,'szd',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('WX_CORPSECRET','','corp secret',sysdate,null,sysdate,'szd',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('WX_AGENTID','','corp agent id',sysdate,null,sysdate,'szd',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('WX_CALLBACK_AESKEY','','callback aeskey',sysdate,null,sysdate,'szd',sysdate);

commit;
