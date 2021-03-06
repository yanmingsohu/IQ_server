if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_Friend_table_User_table]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[Friend_table] DROP CONSTRAINT FK_Friend_table_User_table
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_Info_table_User_table]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[Info_table] DROP CONSTRAINT FK_Info_table_User_table
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_offline_msg_User_table]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[offline_msg] DROP CONSTRAINT FK_offline_msg_User_table
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_offline_msg_User_table1]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[offline_msg] DROP CONSTRAINT FK_offline_msg_User_table1
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[Friend_table]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[Friend_table]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[Info_table]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[Info_table]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[User_table]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[User_table]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[offline_msg]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[offline_msg]
GO

CREATE TABLE [dbo].[Friend_table] (
	[fid] [int] IDENTITY (1, 1) NOT NULL ,
	[uid] [int] NOT NULL ,
	[list] [varchar] (500) COLLATE Chinese_PRC_CI_AS NULL 
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[Info_table] (
	[uid] [int] NOT NULL ,
	[info] [nvarchar] (1000) COLLATE Chinese_PRC_CI_AS NOT NULL 
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[User_table] (
	[id] [int] IDENTITY (10000, 1) NOT NULL ,
	[password] [varchar] (10) COLLATE Chinese_PRC_CI_AS NOT NULL ,
	[name] [nvarchar] (10) COLLATE Chinese_PRC_CI_AS NOT NULL 
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[offline_msg] (
	[sid] [int] NOT NULL ,
	[rid] [int] NOT NULL ,
	[msg] [nvarchar] (200) COLLATE Chinese_PRC_CI_AS NULL 
) ON [PRIMARY]
GO

