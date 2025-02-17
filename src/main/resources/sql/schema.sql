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