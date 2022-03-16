DROP TABLE IF EXISTS klausur_referenz;
DROP TABLE IF EXISTS klausur;
DROP TABLE IF EXISTS zeitraum_dto;
DROP TABLE IF EXISTS student;


create table student
(
    id     int auto_increment primary key,
    handle varchar(40) unique not null
);

create table klausur_referenz
(
    student           int,
    veranstaltungs_id int,
    primary key (student, veranstaltungs_id)
);

create table klausur
(
    veranstaltungs_id   int primary key,
    veranstaltungs_name varchar(260),
    datum               date not null,
    start_uhrzeit       time not null,
    end_uhrzeit         time not null,
    praesenz            bool
);

create table zeitraum_dto
(
    student       int references student (id),
    datum         date not null,
    start_uhrzeit time not null,
    end_uhrzeit   time not null,
    primary key (student, datum, start_uhrzeit, end_uhrzeit)
);