CREATE TABLE issues_event (
    id VARCHAR(120) PRIMARY KEY,
    action VARCHAR(50) NOT NULL,
    issue_id INT NOT NULL REFERENCES issues (id)
);
