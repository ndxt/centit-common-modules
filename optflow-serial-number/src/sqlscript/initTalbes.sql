/* my sql */
drop table if exists F_OptFlowNoInfo;
drop table if exists F_OptFlowNoPool;


/*==============================================================*/
/* Table: F_OptFlowNoInfo                                       */
/*==============================================================*/
create table F_OptFlowNoInfo
(
   OwnerCode            varchar(8) not null,
   CodeCode             varchar(16) not null,
   CodeDate             date not null,
   CurNo                numeric(6,0) not null default 1,
   LastCodeDate         date,
   CreateDate           date,
   LastModifyDate       date,
   primary key (OwnerCode, CodeDate, CodeCode)
);

/*==============================================================*/
/* Table: F_OptFlowNoPool                                       */
/*==============================================================*/
create table F_OptFlowNoPool
(
   OwnerCode            varchar(8) not null,
   CodeCode             varchar(16) not null,
   CodeDate             date not null,
   CurNo                numeric(6,0) not null default 1,
   CreateDate           date,
   primary key (OwnerCode, CodeDate, CodeCode, CurNo)
);


/* oracle */

drop table F_OptFlowNoInfo cascade constraints;
drop table F_OptFlowNoPool cascade constraints;


/*==============================================================*/
/* Table: F_OptFlowNoInfo                                       */
/*==============================================================*/
create table F_OptFlowNoInfo  (
   OwnerCode            VARCHAR2(8)                     not null,
   CodeCode             VARCHAR2(16)                    not null,
   CodeDate             DATE                           default sysdate not null,
   CurNo                NUMBER(6,0)                    default 1 not null,
   LastCodeDate         DATE,
   CreateDate           DATE,
   LastModifyDate       DATE
);

alter table F_OptFlowNoInfo
   add constraint PK_F_OPTFLOWNOINFO primary key (OwnerCode, CodeDate, CodeCode);

/*==============================================================*/
/* Table: F_OptFlowNoPool                                       */
/*==============================================================*/
create table F_OptFlowNoPool  (
   OwnerCode            VARCHAR2(8)                     not null,
   CodeCode             VARCHAR2(16)                    not null,
   CodeDate             DATE                           default sysdate not null,
   CurNo                NUMBER(6,0)                    default 1 not null,
   CreateDate           DATE
);

alter table F_OptFlowNoPool
   add constraint PK_F_OPTFLOWNOPOOL primary key (OwnerCode, CodeDate, CodeCode, CurNo);

/*  db2  */
drop table F_OptFlowNoInfo;
drop table F_OptFlowNoPool;


/*==============================================================*/
/* Table: F_OptFlowNoInfo                                       */
/*==============================================================*/
create table F_OptFlowNoInfo  (
   OwnerCode            VARCHAR(8)                     not null,
   CodeCode             VARCHAR(16)                    not null,
   CodeDate             TIMESTAMP                           default sysdate not null,
   CurNo                decimal(6,0)                    default 1 not null,
   LastCodeDate         TIMESTAMP,
   CreateDate           TIMESTAMP,
   LastModifyDate       TIMESTAMP
);

alter table F_OptFlowNoInfo
   add constraint PK_F_OPTFLOWNOINFO primary key (OwnerCode, CodeDate, CodeCode);

/*==============================================================*/
/* Table: F_OptFlowNoPool                                       */
/*==============================================================*/
create table F_OptFlowNoPool  (
   OwnerCode            VARCHAR(8)                     not null,
   CodeCode             VARCHAR(16)                    not null,
   CodeDate             TIMESTAMP                           default sysdate not null,
   CurNo                decimal(6,0)                    default 1 not null,
   CreateDate           TIMESTAMP
);

alter table F_OptFlowNoPool
   add constraint PK_F_OPTFLOWNOPOOL primary key (OwnerCode, CodeDate, CodeCode, CurNo);



