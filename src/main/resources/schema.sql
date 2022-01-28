create table rdm_service (
    id bigserial not null,
    name varchar(64) not null,
    host varchar(255) not null,
    dmp_endpoint varchar(255) not null,
    primary key (id)
);

create table property (
    id bigserial not null primary key,
    dmp_identifier varchar(256) not null,
    class_type varchar(64) not null,
    class_identifier varchar(256) not null,
    property_name varchar(64) not null,
    value varchar(4096) not null,
    reference varchar(256),
    rdm_service_id bigserial not null,
    CONSTRAINT fk_rdm_service FOREIGN KEY(rdm_service_id) REFERENCES rdm_service(id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

ALTER TABLE property ADD COLUMN sys_period tstzrange NOT NULL;

CREATE TABLE property_history (LIKE property);

CREATE TRIGGER versioning_trigger
BEFORE INSERT OR UPDATE OR DELETE ON property
FOR EACH ROW EXECUTE PROCEDURE versioning('sys_period',
                                          'property_history',
                                          true);

create table permission (
    id bigserial not null,
    class_type varchar(64) not null,
    allowed boolean not null,
    rdm_service_id bigserial not null,
    primary key (id),
    CONSTRAINT fk_rdm_service FOREIGN KEY(rdm_service_id) REFERENCES rdm_service(id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);