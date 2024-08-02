create schema test;
use test;
create table member
(
    id           bigint primary key auto_increment comment 'SEQ',
    username     varchar(100) unique not null comment '회원 EMAIL',
    password     varchar(500)        not null comment '회원 PWD (암호화)',
    name         varchar(100)        not null comment '회원 이름',
    phone_number varchar(100)        not null comment '회원 전화번호',
    image_url    varchar(100)        null comment '프로필 이미지 URL',
    created_at   datetime            not null default now() comment '생성일',
    updated_at   datetime            not null default now() comment '수정일'
) comment '회원 테이블';

create table board
(
    id         bigint primary key auto_increment comment 'SEQ',
    user_id    bigint        not null comment '회원 SEQ',
    title      varchar(100)  not null comment '게시판 제목',
    payload    varchar(1000) not null comment '게시판 본문',
    is_deleted boolean       not null comment '게시판 삭제 여부',
    created_at datetime      not null default now() comment '생성일',
    updated_at datetime      not null default now() comment '수정일'
) comment '게시판 테이블';

create table chat_room
(
    id         varchar(100) primary key comment 'SEQ',
    host_id    bigint       not null comment '방장 회원 SEQ',
    name       varchar(100) not null comment '채팅방 이름',
    created_at datetime     not null default now() comment '생성일',
    updated_at datetime not null default now() comment '수정일'
) comment '채팅방 테이블';

create table chat_member
(
    id         bigint primary key auto_increment comment 'SEQ',
    member_id  bigint       not null comment '회원 SEQ',
    room_id    varchar(100) not null comment '채팅방 SEQ',
    created_at datetime     not null default now() comment '생성일',
    updated_at datetime     not null default now() comment '수정일'
) comment '채팅방 <-> 회원 매핑 테이블';

create table tb_member_connect
(
    id                bigint primary key comment '회원 SEQ',
    is_connected      boolean  not null default false comment '접속 여부',
    last_connected_at datetime not null default now() comment '마지막 접속일'
) comment '회원 접속 기록 테이블 (Socket 접속 여부)';
