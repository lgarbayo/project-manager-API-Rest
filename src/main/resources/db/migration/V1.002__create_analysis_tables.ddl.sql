CREATE TABLE IF NOT EXISTS project_analysis (
    id SERIAL PRIMARY KEY,
    project_uuid VARCHAR(36) NOT NULL,
    project_title VARCHAR(255) NOT NULL,
    project_description TEXT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS project_analysis_additional_fields (
    project_analysis_id BIGINT NOT NULL,
    field_key VARCHAR(255) NOT NULL,
    field_value VARCHAR(255),
    CONSTRAINT fk_project_analysis_additional_fields FOREIGN KEY (project_analysis_id)
        REFERENCES project_analysis (id) ON DELETE CASCADE,
    CONSTRAINT pk_project_analysis_additional_fields PRIMARY KEY (project_analysis_id, field_key)
);

CREATE TABLE IF NOT EXISTS milestone_analysis (
    id SERIAL PRIMARY KEY,
    milestone_uuid VARCHAR(36) NOT NULL,
    milestone_title VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    initial_completion DOUBLE PRECISION,
    end_completion DOUBLE PRECISION,
    project_analysis_id BIGINT NOT NULL,
    CONSTRAINT fk_milestone_analysis_project_analysis FOREIGN KEY (project_analysis_id)
        REFERENCES project_analysis (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS task_analysis (
    id SERIAL PRIMARY KEY,
    task_uuid VARCHAR(36) NOT NULL,
    task_title VARCHAR(255) NOT NULL,
    initial_completion DOUBLE PRECISION,
    end_completion DOUBLE PRECISION,
    milestone_analysis_id BIGINT NOT NULL,
    CONSTRAINT fk_task_analysis_milestone_analysis FOREIGN KEY (milestone_analysis_id)
        REFERENCES milestone_analysis (id) ON DELETE CASCADE
);
