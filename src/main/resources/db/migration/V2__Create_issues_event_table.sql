CREATE TABLE issues_event
(
    id uuid NOT NULL PRIMARY KEY,
    action varchar(50) NOT NULL,
    issue_id int NOT NULL REFERENCES issues (id),
    created_at timestamp NOT NULL
);
