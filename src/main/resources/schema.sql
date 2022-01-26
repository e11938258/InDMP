create table public.rdm_service (
    id bigserial not null,
    name varchar(64) not null,
    host varchar(255) not null,
    dmp_endpoint varchar(255) not null,
    primary key (id)
);

create table public.property (
    id bigserial not null primary key,
    dmp_identifier varchar(256) not null,
    class_type varchar(64) not null,
    class_identifier varchar(256) not null,
    property_name varchar(64) not null,
    value varchar(4096) not null,
    reference varchar(256),
    sys_start_time DATETIME2 NOT NULL,
    sys_end_time DATETIME2 NOT NULL,
    rdm_service_id bigserial not null,
    CONSTRAINT fk_rdm_service FOREIGN KEY(rdm_service_id) REFERENCES public.rdm_service(id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    PERIOD FOR SYSTEM_TIME (sys_start_time, sys_end_time)
) WITH (SYSTEM_VERSIONING = ON (HISTORY_TABLE = property_history));

create table public.permission (
    id bigserial not null,
    class_type varchar(64) not null,
    allowed boolean not null,
    rdm_service_id bigserial not null,
    primary key (id),
    CONSTRAINT fk_rdm_service FOREIGN KEY(rdm_service_id) REFERENCES public.rdm_service(id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);