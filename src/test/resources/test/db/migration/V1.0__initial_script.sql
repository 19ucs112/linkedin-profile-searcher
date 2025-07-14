CREATE TABLE IF NOT EXISTS phantom_agent_task
(
    id                  UUID PRIMARY KEY,
    container_id        varchar(100),
    university         varchar(255),
    current_designation varchar(150),
    passed_out_year     int,
    task_status         varchar(50),
    retry_count         int default 0,
    UNIQUE (container_id, university, current_designation)
);

CREATE TABLE IF NOT EXISTS university
(
    id   UUID PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS alumni
(
    id                   UUID PRIMARY KEY,
    title                VARCHAR(255),
    full_name            VARCHAR(255),
    profile_head_line    VARCHAR(255),
    location             VARCHAR(255),
    connection_degree    VARCHAR(100),
    current_organization VARCHAR(255),
    profile_url          VARCHAR(500),
    company_url          VARCHAR(500),
    university_id        UUID,
    passed_out_year      INT,
    CONSTRAINT fk_university
        FOREIGN KEY (university_id)
            REFERENCES university (id)
);
