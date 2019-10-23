/* my sql */
drop table if exists F_OptFlowNoInfo;
drop table if exists F_OptFlowNoPool;


/*==============================================================*/
/* Table: F_OptFlowNoInfo                                       */
/*==============================================================*/
create table F_OptFlowNoInfo
(
   Owner_Code            varchar(32) not null,
   Code_Code             varchar(32) not null,
   Code_Date             date not null,
   Cur_No                numeric(6,0) not null default 1,
   Last_Code_Date         date,
   Create_Date           date,
   Last_Modify_Date       date,
   primary key (Owner_Code, Code_Date, Code_Code)
);

/*==============================================================*/
/* Table: F_OptFlowNoPool                                       */
/*==============================================================*/
create table F_OptFlowNoPool
(
   Owner_Code            varchar(32) not null,
   Code_Code             varchar(32) not null,
   Code_Date             date not null,
   Cur_No                numeric(6,0) not null default 1,
   Create_Date           date,
   primary key (Owner_Code, Code_Date, Code_Code, Cur_No)
);


/* oracle */

drop table F_OptFlowNoInfo cascade constraints;
drop table F_OptFlowNoPool cascade constraints;


/*==============================================================*/
/* Table: F_OptFlowNoInfo                                       */
/*==============================================================*/
create table F_OptFlowNoInfo  (
   Owner_Code            VARCHAR2(32)                     not null,
   Code_Code             VARCHAR2(32)                    not null,
   Code_Date             DATE                           default sysdate not null,
   Cur_No                NUMBER(6,0)                    default 1 not null,
   Last_Code_Date         DATE,
   Create_Date           DATE,
   Last_Modify_Date       DATE
);

alter table F_OptFlowNoInfo
   add constraint PK_F_OPTFLOWNOINFO primary key (Owner_Code, Code_Date, Code_Code);

/*==============================================================*/
/* Table: F_OptFlowNoPool                                       */
/*==============================================================*/
create table F_OptFlowNoPool  (
   Owner_Code            VARCHAR2(32)                     not null,
   Code_Code             VARCHAR2(32)                    not null,
   Code_Date             DATE                           default sysdate not null,
   Cur_No                NUMBER(6,0)                    default 1 not null,
   Create_Date           DATE
);

alter table F_OptFlowNoPool
   add constraint PK_F_OPTFLOWNOPOOL primary key (Owner_Code, Code_Date, Code_Code, Cur_No);

/*  db2  */
drop table F_OptFlowNoInfo;
drop table F_OptFlowNoPool;


/*==============================================================*/
/* Table: F_OptFlowNoInfo                                       */
/*==============================================================*/
create table F_OptFlowNoInfo  (
   Owner_Code            VARCHAR(32)                     not null,
   Code_Code             VARCHAR(32)                    not null,
   Code_Date             TIMESTAMP                      default sysdate not null,
   Cur_No                decimal(6,0)                   default 1 not null,
   Last_Code_Date         TIMESTAMP,
   Create_Date           TIMESTAMP,
   Last_Modify_Date       TIMESTAMP
);

alter table F_OptFlowNoInfo
   add constraint PK_F_OPTFLOWNOINFO primary key (Owner_Code, Code_Date, Code_Code);

/*==============================================================*/
/* Table: F_OptFlowNoPool                                       */
/*==============================================================*/
create table F_OptFlowNoPool  (
   Owner_Code            VARCHAR(32)                     not null,
   Code_Code             VARCHAR(32)                    not null,
   Code_Date             TIMESTAMP                      default sysdate not null,
   Cur_No                decimal(6,0)                   default 1 not null,
   Create_Date           TIMESTAMP
);

alter table F_OptFlowNoPool
   add constraint PK_F_OPTFLOWNOPOOL primary key (Owner_Code, Code_Date, Code_Code, Cur_No);



