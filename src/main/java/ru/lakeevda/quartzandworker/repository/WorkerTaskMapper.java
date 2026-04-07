package ru.lakeevda.quartzandworker.repository;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.lakeevda.quartzandworker.boundary.workertask.WorkerTaskParams;
import ru.lakeevda.quartzandworker.entity.WorkerTaskEntity;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WorkerTaskMapper {

    WorkerTaskParams toParams(WorkerTaskEntity entity);
    WorkerTaskEntity toEntity(WorkerTaskParams entity);

}
