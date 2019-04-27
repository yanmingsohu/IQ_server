select name from user_table where name='jym'

select id from user_table where name='jym'

insert into  user_table (name,password) values ('ooo','3455')

select id from user_table where name='ooo'

insert into info_table (uid,info) values (2,'3455')

insert into friend_table(uid,list) values (2, '')

select list from friend_table where uid=1000

select * from info_table
select * from friend_table

update friend_table set list='' where uid=1000

select list from friend_table where uid=1000

update info_table set info='' where uid=1000

select uid from info_table 
where (info like '%sex=ÄÐ%') 
	and (info like '%age=%')
	and (info like '%city=´óÁ¬%')
	and (info like '%name=%')

select * from offline_msg

insert into offline_msg(sid,rid,msg) values (10000,10000,'ff')

select msg,sid from offline_msg where rid=0

delete from offline_msg where rid=0