--Delete old tables if exists
DROP TABLE IF EXISTS property;
DROP TABLE IF EXISTS activity;
DROP TABLE IF EXISTS rdm_service_rights;
DROP TABLE IF EXISTS rdm_service;

--Create new tables
create table rdm_service (
    id bigserial NOT NULL,
    title varchar(64) NOT NULL,
    access_rights varchar(64) NOT NULL UNIQUE,
    endpoint_url varchar(255) NOT NULL,
    state varchar(64) NOT NULL,
    PRIMARY KEY (identifier)
);

create table rdm_service_property_rights (
    rdm_service_identifier bigserial NOT NULL,
    property_right varchar(255),
    CONSTRAINT fk_rdm_service FOREIGN KEY (rdm_service_identifier)
        REFERENCES rdm_service (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

create table activity (
    id bigserial NOT NULL,
    started_at_time timestamp without time zone NOT NULL,
    ended_at_time timestamp without time zone,
    was_started_by bigserial NOT NULL,
    was_ended_by bigserial,
    PRIMARY KEY (id),
    CONSTRAINT fk_rdm_service_st FOREIGN KEY (was_started_by)
        REFERENCES rdm_service (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
    CONSTRAINT fk_rdm_service_ed FOREIGN KEY (was_ended_by)
        REFERENCES rdm_service (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

create table property (
    id bigserial NOT NULL,
    at_location varchar(256) NOT NULL,
    specialization_of varchar(256) NOT NULL,
    value varchar(4096) NOT NULL,
    was_generated_by bigserial,
    PRIMARY KEY (id),
    CONSTRAINT fk_activity FOREIGN KEY (was_generated_by)
        REFERENCES activity (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);