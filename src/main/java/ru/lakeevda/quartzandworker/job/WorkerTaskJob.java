package ru.lakeevda.quartzandworker.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.lakeevda.quartzandworker.boundary.workertask.WorkerTaskParams;
import ru.lakeevda.quartzandworker.processor.WorkerTaskProcessor;

import java.util.List;

@Slf4j
@Component
public class WorkerTaskJob implements Job {

    @Autowired
    private WorkerTaskProcessor processor;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Worker Task Job");

        List<WorkerTaskParams> params = processor.getTasks();
        for (WorkerTaskParams param : params) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (Math.random() * 10 > 5) processor.completed(param);
            else processor.fail(param);
        }
    }
}
