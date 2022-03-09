create table student_dto
(
    id     serial primary key,
    handle varchar(40) unique not null
);

create table klausur_referenz_dto
(
    student_dto       int,
    veranstaltungs_id int,
    primary key (student_dto, veranstaltungs_id)
);

create table klausur_dto
(
    veranstaltungs_id   int primary key,
    veranstaltungs_name varchar(260),
    datum               date not null,
    start_uhrzeit       time not null,
    end_uhrzeit         time not null,
    praesenz            bool
);

create table urlaub_zeitraum_dto
(
    student_dto   int references student_dto (id),
    datum         date not null,
    start_uhrzeit time not null,
    end_uhrzeit   time not null,
    primary key (student_dto, datum, start_uhrzeit, end_uhrzeit)
);