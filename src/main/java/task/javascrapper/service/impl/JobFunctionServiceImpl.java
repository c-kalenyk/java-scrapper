package task.javascrapper.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import task.javascrapper.dto.job_function.JobFunctionRequestDto;
import task.javascrapper.mapper.JobFunctionMapper;
import task.javascrapper.model.JobFunction;
import task.javascrapper.model.JobItem;
import task.javascrapper.repository.JobFunctionRepository;
import task.javascrapper.service.DataFetchService;
import task.javascrapper.service.JobFunctionService;

@Service
@RequiredArgsConstructor
public class JobFunctionServiceImpl implements JobFunctionService {
    private final JobFunctionRepository jobFunctionRepository;
    private final JobFunctionMapper jobFunctionMapper;
    private final DataFetchService dataFetchService;

    @Override
    public JobFunction save(JobFunctionRequestDto requestDto, String functionName) {
        JobFunction jobFunction = jobFunctionMapper.toModel(requestDto);
        jobFunction.setFunction(functionName);
        if (jobFunction.getJobs() != null) {
            List<JobItem> items = jobFunction.getJobs();

            ExecutorService executor = Executors.newFixedThreadPool(7);
            List<Future<Void>> futures = new ArrayList<>();

            for (JobItem item : items) {
                futures.add(executor.submit(() -> {
                    item.setJobFunction(jobFunction);
                    item.setFunctionName(functionName);
                    try {
                        Thread.sleep(500 + new Random().nextInt(2000));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    item.setDescription(dataFetchService.fetchJobDescription(item.getUrl()));
                    return null;
                }));
            }

            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException("Error while fetching job description", e);
                }
            }
            executor.shutdown();
        }
        return jobFunctionRepository.save(jobFunction);
    }

    @Override
    public JobFunction get(String function) {
        if (jobFunctionRepository.existsByFunction(function)) {
            return jobFunctionRepository.findByFunction(function);
        } else {
            return save(dataFetchService.fetchJobFunction(function), function);
        }
    }
}
