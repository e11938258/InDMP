--Delete old tables if exists
DROP TABLE IF EXISTS entity_history;
DROP TABLE IF EXISTS entity;
DROP TABLE IF EXISTS activity;
DROP TABLE IF EXISTS data_service_rights;
DROP TABLE IF EXISTS data_service;

--Create new tables
create table data_service (
    identifier bigserial NOT NULL,
    title varchar(64) NOT NULL,
    access_rights varchar(64) NOT NULL UNIQUE,
    endpoint_url varchar(255) NOT NULL UNIQUE,
    endpoint_description varchar(512),
    PRIMARY KEY (identifier)
);

create table data_service_rights (
    data_service_identifier bigserial NOT NULL,
    rights varchar(255),
    CONSTRAINT fk_data_service FOREIGN KEY (data_service_identifier)
        REFERENCES data_service (identifier) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

create table activity (
    id bigserial NOT NULL,
    started_at_time timestamp without time zone NOT NULL,
    ended_at_time timestamp without time zone,
    was_associated_with bigserial NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_data_service_ac FOREIGN KEY (was_associated_with)
        REFERENCES data_service (identifier) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

create table entity (
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

--Activate temporal tables
ALTER TABLE entity ADD COLUMN generated_at_time tstzrange NOT NULL;

CREATE TABLE entity_history (LIKE entity);

CREATE TRIGGER versioning_trigger
BEFORE INSERT OR UPDATE OR DELETE ON entity
FOR EACH ROW EXECUTE PROCEDURE versioning('generated_at_time', 'entity_history', true);