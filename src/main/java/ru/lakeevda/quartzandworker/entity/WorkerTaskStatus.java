package ru.lakeevda.quartzandworker.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum WorkerTaskStatus {
    CREATED("CREATED"),
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED"),
    FAILED("FAILED"),;

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
