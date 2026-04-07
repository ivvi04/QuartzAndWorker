package ru.lakeevda.quartztest.repository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.lakeevda.quartztest.boundary.workertask.WorkerTaskParams;
import ru.lakeevda.quartztest.entity.WorkerTaskEntity;
import ru.lakeevda.quartztest.entity.WorkerTaskStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class WorkerTaskRepository {

    private static final int LIMIT = 5;
    private final Set<String> STATUSES_IGNORED = Set.of(WorkerTaskStatus.IN_PROGRESS.toString(), WorkerTaskStatus.COMPLETED.toString());
    private static final long VERSION = 0L;
    private static final long COUNT_OF_ITERATIONS = 0L;

    @Autowired
    private WorkerTaskJpaRepository repository;
    @Autowired
    private WorkerTaskMapper mapper;

    public List<WorkerTaskParams> getAll() {
        List<WorkerTaskEntity> entities = repository.findAllByStatusNotInAndLimit(STATUSES_IGNORED, LIMIT);
        return entities.stream().map(mapper::toParams).collect(Collectors.toList());
    }

    public WorkerTaskParams getRandom() {
        return repository.findRandom(Collections.singletonList(WorkerTaskStatus.COMPLETED.toString())).map(mapper::toParams).orElse(null);
    }

    @Transactional
    public WorkerTaskParams create(WorkerTaskParams params) {
        if (params == null) {
            throw new IllegalArgumentException("params is null");
        }

        Optional<WorkerTaskEntity> entityOptional = params.getId() == null ? Optional.empty(): repository.findById(params.getId());
        WorkerTaskEntity entity;
        if (entityOptional.isPresent()) {
            entity = entityOptional.get();
        } else {
            entity = new WorkerTaskEntity();
        }

        entity.setVersion(params.getVersion());
        entity.setStatus(params.getStatus());
        entity.setCountOfIterations(params.getCountOfIterations());
        log.info("Creating worker task {}", entity);

        return mapper.toParams(repository.save(entity));
    }

    @Transactional
    public WorkerTaskParams update(WorkerTaskParams params) {
        if (params == null) {
            throw new IllegalArgumentException("params is null");
        }

        Optional<WorkerTaskEntity> entityOptional = repository.findByIdAndVersion(params.getId(), params.getVersion());
        if (entityOptional.isPresent()) {
            WorkerTaskEntity entity = entityOptional.get();
            entity.setStatus(params.getStatus());
            entity.setCountOfIterations(params.getCountOfIterations());
            log.info("Updating worker task {}", entity);
            return mapper.toParams(repository.save(entity));
        }
        return null;
    }

}
