CREATE TABLE IF NOT EXISTS project (
    uuid VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS project_additional_fields (
    project_uuid VARCHAR(36) NOT NULL,
    field_key VARCHAR(255) NOT NULL,
    field_value VARCHAR(255),
    CONSTRAINT fk_project_additional_fields_project FOREIGN KEY (project_uuid)
        REFERENCES project (uuid) ON DELETE CASCADE,
    CONSTRAINT pk_project_additional_fields PRIMARY KEY (project_uuid, field_key)
);

CREATE TABLE IF NOT EXISTS milestone (
    uuid VARCHAR(36) PRIMARY KEY,
    project_uuid VARCHAR(36) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    date DATE NOT NULL,
    CONSTRAINT fk_milestone_project FOREIGN KEY (project_uuid)
        REFERENCES project (uuid) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS task (
    uuid VARCHAR(36) PRIMARY KEY,
    project_uuid VARCHAR(36) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    duration_weeks INT NOT NULL,
    start_date DATE NOT NULL,
    CONSTRAINT fk_task_project FOREIGN KEY (project_uuid)
        REFERENCES project (uuid) ON DELETE CASCADE
);