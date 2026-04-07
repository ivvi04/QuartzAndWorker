package ru.lakeevda.quartztest.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.lakeevda.quartztest.boundary.workertask.WorkerTaskParams;
import ru.lakeevda.quartztest.entity.WorkerTaskStatus;
import ru.lakeevda.quartztest.processor.WorkerTaskProcessor;

@Slf4j
@Component
public class CreateTaskJob implements Job {

    @Autowired
    private WorkerTaskProcessor processor;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Create Task Job");

        WorkerTaskParams params = null;
        if (Math.random() * 2 > 1) {
            params = processor.getRandom();
        }
        if (params == null)
            params = new WorkerTaskParams(null, WorkerTaskStatus.CREATED.toString(), 0L, 0L);
        else
            params = new WorkerTaskParams(params.getId(), WorkerTaskStatus.CREATED.toString(), params.getVersion(), params.getCountOfIterations());

        processor.created(params);

    }
}
