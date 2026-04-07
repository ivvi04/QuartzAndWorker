package ru.lakeevda.quartzandworker.repository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.lakeevda.quartzandworker.boundary.workertask.WorkerTaskParams;
import ru.lakeevda.quartzandworker.entity.WorkerTaskEntity;
import ru.lakeevda.quartzandworker.entity.WorkerTaskStatus;

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

    private final WorkerTaskJpaRepository repository;
    private final WorkerTaskMapper mapper;

    public WorkerTaskRepository(WorkerTaskJpaRepository repository, WorkerTaskMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

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

        Optional<WorkerTaskEntity> entityOptional = params.getId() == null ? Optional.empty() : repository.findById(params.getId());
        WorkerTaskEntity entity;
        entity = entityOptional.orElseGet(WorkerTaskEntity::new);

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
