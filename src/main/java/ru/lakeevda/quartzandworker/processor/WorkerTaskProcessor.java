package ru.lakeevda.quartzandworker.processor;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.lakeevda.quartzandworker.boundary.workertask.WorkerTaskParams;
import ru.lakeevda.quartzandworker.entity.WorkerTaskStatus;
import ru.lakeevda.quartzandworker.repository.WorkerTaskRepository;

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

    public void created(WorkerTaskParams params) {
        WorkerTaskParams inProgress = new WorkerTaskParams(params.getId(),
                WorkerTaskStatus.CREATED.toString(),
                params.getVersion() + 1,
                params.getCountOfIterations());
        repository.create(inProgress);
    }

    public WorkerTaskParams inProgress(WorkerTaskParams params) {
        WorkerTaskParams inProgress = new WorkerTaskParams(params.getId(),
                WorkerTaskStatus.IN_PROGRESS.toString(),
                params.getVersion(),
                params.getCountOfIterations() + 1);
        return repository.update(inProgress);
    }

    public void completed(WorkerTaskParams params) {
        WorkerTaskParams completed = new WorkerTaskParams(params.getId(),
                WorkerTaskStatus.COMPLETED.toString(),
                params.getVersion(),
                params.getCountOfIterations());
        repository.update(completed);
    }

    public void fail(WorkerTaskParams params) {
        WorkerTaskParams fail = new WorkerTaskParams(params.getId(),
                WorkerTaskStatus.FAILED.toString(),
                params.getVersion(),
                params.getCountOfIterations());
        repository.update(fail);
    }

}
