package ru.lakeevda.quartztest.repository;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.lakeevda.quartztest.boundary.workertask.WorkerTaskParams;
import ru.lakeevda.quartztest.entity.WorkerTaskEntity;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WorkerTaskMapper {

    WorkerTaskParams toParams(WorkerTaskEntity entity);
    WorkerTaskEntity toEntity(WorkerTaskParams entity);

}
