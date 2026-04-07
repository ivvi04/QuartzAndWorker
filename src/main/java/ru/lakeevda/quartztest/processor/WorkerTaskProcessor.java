package ru.lakeevda.quartztest.processor;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.lakeevda.quartztest.boundary.workertask.WorkerTaskParams;
import ru.lakeevda.quartztest.entity.WorkerTaskStatus;
import ru.lakeevda.quartztest.repository.WorkerTaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkerTaskProcessor {

    private final WorkerTaskRepository repository;

    @Transactional
    public List<WorkerTaskParams> getTasks() {
        return repository.getAll().stream()
                .map(this::inProgress)
                .collect(Collectors.toList());
    }

    public WorkerTaskParams getRandom() {
        return repository.getRandom();
    }

    public WorkerTaskParams created(WorkerTaskParams params) {
        WorkerTaskParams inProgress = new WorkerTaskParams(params.getId(),
                WorkerTaskStatus.CREATED.toString(),
                params.getVersion() + 1,
                params.getCountOfIterations());
        return repository.create(inProgress);
    }

    public WorkerTaskParams inProgress(WorkerTaskParams params) {
        WorkerTaskParams inProgress = new WorkerTaskParams(params.getId(),
                WorkerTaskStatus.IN_PROGRESS.toString(),
                params.getVersion(),
                params.getCountOfIterations() + 1);
        return repository.update(inProgress);
    }

    public WorkerTaskParams completed(WorkerTaskParams params) {
        WorkerTaskParams completed = new WorkerTaskParams(params.getId(),
                WorkerTaskStatus.COMPLETED.toString(),
                params.getVersion(),
                params.getCountOfIterations());
        return repository.update(completed);
    }

    public WorkerTaskParams fail(WorkerTaskParams params) {
        WorkerTaskParams fail = new WorkerTaskParams(params.getId(),
                WorkerTaskStatus.FAILED.toString(),
                params.getVersion(),
                params.getCountOfIterations());
        return repository.update(fail);
    }

}
