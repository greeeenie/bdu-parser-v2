create extension if not exists "uuid-ossp";
create schema parser;

create table parser.bdu (
    id          text primary key,
    name        text,
    description text
);

create table parser.cwe (
    id   int primary key,
    name text
);

create table parser.capec (
    id         int primary key,
    name       text,
    name_ru    text,
    likelihood text
);

create table parser.bdu_cwe (
    bdu_id text references parser.bdu (id),
    cwe_id int  references parser.cwe (id),
    primary key (bdu_id, cwe_id)
);

create table parser.cwe_capec (
    cwe_id   int references parser.cwe (id),
    capec_id int references parser.capec (id),
    primary key (cwe_id, capec_id)
);

create table parser.scan_report (
    id           uuid primary key DEFAULT uuid_generate_v4(),
    bdu_id       text,
    full_bdu_ids text,
    description  text
);
create index scan_report_bdu_id_idx on parser.scan_report using hash (bdu_id);
