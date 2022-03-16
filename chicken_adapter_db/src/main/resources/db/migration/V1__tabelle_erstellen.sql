DROP TABLE IF EXISTS klausur_referenz_dto;
DROP TABLE IF EXISTS klausur_dto;
DROP TABLE IF EXISTS zeitraum_dto;
DROP TABLE IF EXISTS student_dto;


create table student_dto
(
    id           int auto_increment primary key,
    githubhandle varchar(40) unique not null
);

create table klausur_referenz_dto
(
    student_dto       int,
    veranstaltungs_id varchar(260),
    primary key (student_dto, veranstaltungs_id)
);

create table klausur_dto
(
    veranstaltungs_id   varchar(260) primary key,
    veranstaltungs_name varchar(260),
    datum               date not null,
    start_uhrzeit       time not null,
    end_uhrzeit         time not null,
    praesenz            bool
);

create table zeitraum_dto
(
    student_dto   int references student_dto (id),
    datum         date not null,
    start_uhrzeit time not null,
    end_uhrzeit   time not null,
    primary key (student_dto, datum, start_uhrzeit, end_uhrzeit)
);