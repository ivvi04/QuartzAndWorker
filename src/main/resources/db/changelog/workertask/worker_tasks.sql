-- liquibase formatted sql

-- changeset Denis:worker_tasks-1
DROP TABLE public.worker_tasks;

CREATE TABLE public.worker_tasks (
    id                  bigserial primary key,
    status              text   not null,
    version             bigint not null,
    count_of_iterations bigint
);