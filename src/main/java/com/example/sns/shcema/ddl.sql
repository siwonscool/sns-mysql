create database sns;

create table Member
(
    id int auto_increment,
    email varchar(20) not null,
    nickname varchar(20) not null,
    birthday date not null,
    createdAt datetime not null,
    constraint member_id_uindex
        primary key (id)
);

create table MemberNicknameHistory
(
    id int auto_increment,
    memberId int not null,
    nickname varchar(20) not null,
    createdAt datetime not null,
    constraint memberNicknameHistory_id_uindex
        primary key (id)
);

create table Follow
(
    id int auto_increment,
    fromMemberId int not null,
    toMemberId int not null,
    createdAt datetime not null,
    constraint Follow_id_uindex
        primary key (id)
);

create unique index Follow_fromMemberId_toMemberId_uindex
    on Follow (fromMemberId, toMemberId);


create table POST
(
    id int auto_increment,
    memberId int not null,
    contents varchar(100) not null,
    createdDate date not null,
    createdAt datetime not null,
    constraint POST_id_uindex
        primary key (id)
);

create index POST__index_member_id
    on POST (memberId);

create index POST__index_created_date
    on POST (createdDate);

create index POST__index_member_id_create_Date
    on POST (memberId, createdDate);


SELECT COUNT(*) AS count
FROM Post
WHERE memberId = 3
  AND createdDate BETWEEN "1900-01-01" AND "2023-12-31";

explain SELECT memberId, createdDate, COUNT(*) AS count
        FROM Post use index (POST__index_member_id_create_Date)
        WHERE memberId = 5
          AND createdDate BETWEEN "1900-01-01" AND "2023-12-31"
        GROUP BY memberId, createdDate;

select memberId,count(id)
from Post
group by memberId ;

select Post.createdDate, count(id)
from Post
group by  createdDate ;

create table Timeline
(
    id int auto_increment,
    memberId int not null,
    postId int not null,
    createdAt datetime not null,
    constraint Timeline_id_uindex primary key (id)
);

select * from Timeline;

select * from Follow;