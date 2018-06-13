/* h2 */
drop table if exists F_WORK_CLASS;
drop table if exists F_WORK_DAY;
drop table if exists F_STAT_MONTH;


/*==============================================================*/
/* Table: F_WORK_CLASS                                          */
/*==============================================================*/
create table F_WORK_CLASS
(
   CLASS_ID             numeric(12,0) not null,
   CLASS_NAME           varchar(50) not null,
   SHORT_NAME           varchar(10) not null,
   begin_time           varchar(6) comment '9:00',
   end_time             varchar(6) comment '+4:00 ''+''表示第二天',
   has_break            char(1),
   break_begin_time     varchar(6) comment '9:00',
   break_end_time       varchar(6) comment '+4:00 ''+''表示第二天',
   class_desc           varchar(500),
   record_date          datetime,
   recorder             varchar(8)
);

alter table F_WORK_CLASS
   add primary key (CLASS_ID);

/*==============================================================*/
/* Table: F_WORK_DAY                                            */
/*==============================================================*/
create table F_WORK_DAY
(
   Work_Day              datetime not null,
   Day_Type              char(1) not null comment 'A:工作日放假，B:周末调休成工作时间 C 正常上班 D正常休假',
   Work_Time_Type         varchar(20),
   Work_Day_Desc          varchar(255)
);

alter table F_WORK_DAY
   add primary key (Work_Day);

/*==============================================================*/
/* Table: F_STAT_MONTH                                          */
/*==============================================================*/
create table F_STAT_MONTH
(
   YEAR_MONTH            varchar(6) not null comment 'YYYYMM',
   Begin_Day             datetime not null,
   End_Day              datetime not null,
   End_Schedule          char(1) comment '这个字段忽略',
   Begin_Schedule        char(1) comment '这个字段忽略'
);


alter table F_STAT_MONTH
   add primary key (YEAR_MONTH);

/* my sql */

drop table if exists F_WORK_CLASS;
drop table if exists F_WORK_DAY;
drop table if exists F_STAT_MONTH;


/*==============================================================*/
/* Table: F_WORK_CLASS                                          */
/*==============================================================*/
create table F_WORK_CLASS
(
   CLASS_ID             numeric(12,0) not null,
   CLASS_NAME           varchar(50) not null,
   SHORT_NAME           varchar(10) not null,
   begin_time           varchar(6) comment '9:00',
   end_time             varchar(6) comment '+4:00 ''+''表示第二天',
   has_break            char(1),
   break_begin_time     varchar(6) comment '9:00',
   break_end_time       varchar(6) comment '+4:00 ''+''表示第二天',
   class_desc           varchar(500),
   record_date          datetime,
   recorder             varchar(8)
);

alter table F_WORK_CLASS comment 'CLASS_ID
 为 0 的表示休息，可以不在这个表中出现
 为 1 的为默认班次信息';

alter table F_WORK_CLASS
   add primary key (CLASS_ID);

/*==============================================================*/
/* Table: F_WORK_DAY                                            */
/*==============================================================*/
create table F_WORK_DAY
(
   Work_Day              datetime not null,
   Day_Type              char(1) not null comment 'A:工作日放假，B:周末调休成工作时间 C 正常上班 D正常休假',
   Work_Time_Type         varchar(20),
   Work_Day_Desc          varchar(255)
);

alter table F_WORK_DAY comment '非正常作业时间日
A:工作日放假 B:周末调休成工作时间  C: 正常上班  D:正常休假
';

alter table F_WORK_DAY
   add primary key (Work_Day);


/*==============================================================*/
/* Table: F_STAT_MONTH                                          */
/*==============================================================*/
create table F_STAT_MONTH
(
   YEAR_MONTH            varchar(6) not null comment 'YYYYMM',
   Begin_Day             datetime not null,
   End_Day              datetime not null,
   End_Schedule          char(1) comment '这个字段忽略',
   Begin_Schedule        char(1) comment '这个字段忽略'
);

alter table F_STAT_MONTH comment 'OA业务统计月，可以自定义统计月的起止日期';

alter table F_STAT_MONTH
   add primary key (YEAR_MONTH);

/* oracle */

drop table F_WORK_CLASS cascade constraints;
drop table F_WORK_DAY cascade constraints;
drop table F_STAT_MONTH cascade constraints;


create table F_WORK_CLASS
(
   CLASS_ID             number(12,0) not null,
   CLASS_NAME           varchar2(50) not null,
   SHORT_NAME           varchar2(10) not null,
   begin_time           varchar2(6)  ,
   end_time             varchar2(6)  ,
   has_break            char(1),
   break_begin_time     varchar2(6) ,
   break_end_time       varchar2(6) ,
   class_desc           varchar2(500),
   record_date          date,
   recorder             varchar2(8)
);
comment on column F_WORK_CLASS.begin_time   is   '9:00'   ;
comment on column F_WORK_CLASS.end_time is  '+4:00 ''+''表示第二天'    ;
comment on table  F_WORK_CLASS is 'CLASS_ID
 为 0 的表示休息，可以不在这个表中出现
 为 1 的为默认班次信息';
alter table F_WORK_CLASS  add primary key (CLASS_ID);

create table F_WORK_DAY
(
   Work_Day              date not null,
   Day_Type              char(1) not null,
   Work_Time_Type         varchar2(20),
   Work_Day_Desc          varchar2(255)
);
comment on column F_WORK_DAY.Day_Type is '非正常作业时间日
A:工作日放假 B:周末调休成工作时间  C: 正常上班  D:正常休假
';
alter table F_WORK_DAY add primary key (Work_Day);


create table F_STAT_MONTH
(
   YEAR_MONTH            varchar2(6) not null ,
   Begin_Day             date not null,
   End_Day              date not null,
   End_Schedule          char(1) ,
   Begin_Schedule        char(1)
);

comment on table F_STAT_MONTH  is'OA业务统计月，可以自定义统计月的起止日期';

alter table F_STAT_MONTH add primary key (YEAR_MONTH);
