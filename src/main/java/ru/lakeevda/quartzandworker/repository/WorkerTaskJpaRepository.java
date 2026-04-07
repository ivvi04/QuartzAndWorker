package ru.lakeevda.quartzandworker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.lakeevda.quartzandworker.entity.WorkerTaskEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface WorkerTaskJpaRepository extends JpaRepository<WorkerTaskEntity, Long> {

    @Query(value = "SELECT * FROM worker_tasks wt WHERE wt.status NOT IN :statuses ORDER BY wt.count_of_iterations LIMIT :limit FOR UPDATE SKIP LOCKED", nativeQuery = true)
    List<WorkerTaskEntity> findAllByStatusNotInAndLimit(@Param("statuses") Collection<String> statuses, @Param("limit") Integer limit);

    Optional<WorkerTaskEntity> findByIdAndVersion(Long id, Long version);

    @Query(value = "SELECT * FROM worker_tasks wt WHERE wt.status NOT IN :statuses ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<WorkerTaskEntity> findRandom(@Param("statuses") Collection<String> statuses);
}
