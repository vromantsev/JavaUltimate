CREATE TABLE IF NOT EXISTS advisor (
    id BIGSERIAL NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS applicant (
    id BIGSERIAL NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    social_security_number VARCHAR(255) NOT NULL,
    city VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    number VARCHAR(50) NOT NULL,
    zip VARCHAR(50) NOT NULL,
    apt VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS application (
    id BIGSERIAL NOT NULL,
    amount_of_money_usd DECIMAL NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    assigned_at TIMESTAMP,
    resolved_at TIMESTAMP,
    applicant_id BIGINT NOT NULL,
    advisor_id BIGINT,
    CONSTRAINT applicant_id_fk FOREIGN KEY(applicant_id) REFERENCES applicant(id),
    CONSTRAINT advisor_id_fk FOREIGN KEY(advisor_id) REFERENCES advisor(id),
    PRIMARY KEY (id)
);
