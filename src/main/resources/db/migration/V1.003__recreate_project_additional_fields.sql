DROP TABLE IF EXISTS project_additional_fields;

CREATE TABLE project_additional_fields (
    project_uuid VARCHAR(36) NOT NULL,
    field_key VARCHAR(255) NOT NULL,
    field_value VARCHAR(255),
    CONSTRAINT fk_project_additional_fields_project FOREIGN KEY (project_uuid)
        REFERENCES project (uuid) ON DELETE CASCADE,
    CONSTRAINT pk_project_additional_fields PRIMARY KEY (project_uuid, field_key)
);
