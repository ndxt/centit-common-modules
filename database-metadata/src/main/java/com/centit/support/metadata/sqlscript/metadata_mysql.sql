drop table if exists F_MD_COLUMN;

drop table if exists F_MD_RELATION;

drop table if exists F_MD_REL_DETAIL;

drop table if exists F_MD_TABLE;

create table F_MD_COLUMN
(
   TABLE_ID                numeric(12,0) not null,
   COLUMN_CODE             varchar(32) not null,
   COLUMN_NAME             varchar(64),
   COLUMN_TYPE             varchar(32) not null,
   COLUMN_LENGTH           numeric(6,0),
   COLUMN_PRECISION        numeric(3,0),
   ACCESS_TYPE             char(1) not null,
   PRIMARY_KEY             char(1),
   MANDATORY               char(1),
   COLUMN_STATE            char(1) not null,
   CONSTRAINT_DESC         varchar(256) comment '专指程序约束类别',
   REF_DATA_CATALOG        varchar(16),
   REF_DATA_SQL            varchar(1024),
   COLUMN_COMMENT          varchar(256),
   COLUMN_ORDER            numeric(3,0) default 99,
   RECORD_DATE            datetime,
   RECORDER               varchar(32),
   primary key (TABLE_ID, COLUMN_CODE)
);

create table F_MD_RELATION
(
   RELATION_ID                   numeric(12,0) not null,
   RELATION_NAME                 varchar(64) not null,
   PARENT_TABLE_ID               numeric(12,0) not null,
   CHILD_TABLE_ID                numeric(12,0) not null,
   RELATION_STATE                char(1) not null,
   RELATION_COMMENT              varchar(256),
   primary key (RELATION_ID)
);

create table F_MD_REL_DETAIL
(
   RELATION_ID                numeric(12,0) not null,
   PARENT_COLUMN_CODE             varchar(32) not null,
   CHILD_COLUMN_CODE             varchar(32) not null,
   primary key (RELATION_ID, PARENT_COLUMN_CODE)
);

create table F_MD_TABLE
(
   TABLE_ID               numeric(12,0) not null,
   TABLE_CODE             varchar(32) not null,
   DATABASE_CODE          varchar(32) not null comment '数据库代码',
   TABLE_NAME             varchar(64),
   TABLE_TYPE             char(1) not null comment '表/视图 目前只能是表',
   TABLE_STATE            char(1) not null comment '系统 S / R 查询(只读)/ N 新建(读写)',
   TABLE_COMMENT          varchar(256),
   WORKFLOW_OPT_TYPE      char(1) not null default '0',
   Record_Date            datetime,
   Recorder               varchar(32),
   primary key (TABLE_ID)
);

alter table F_MD_TABLE comment '状态分为 系统/查询/更新 系统，不可以做任何操作 查询，仅用于通用查询模块，不可以更新';

