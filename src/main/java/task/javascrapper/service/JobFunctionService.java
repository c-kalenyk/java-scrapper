package task.javascrapper.service;

import task.javascrapper.dto.job_function.JobFunctionRequestDto;
import task.javascrapper.model.JobFunction;

public interface JobFunctionService {
    JobFunction save(JobFunctionRequestDto requestDto, String functionName);

    JobFunction get(String function);
}
