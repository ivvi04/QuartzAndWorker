package ru.lakeevda.quartzandworker.boundary.workertask;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WorkerTaskParams {
    private Long id;
    private String status;
    private Long version;
    private Long countOfIterations;
}
