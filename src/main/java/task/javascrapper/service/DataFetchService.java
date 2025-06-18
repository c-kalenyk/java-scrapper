package task.javascrapper.service;

import task.javascrapper.dto.job_function.JobFunctionRequestDto;

public interface DataFetchService {
    JobFunctionRequestDto fetchJobFunction(String jobFunction);

    String fetchJobDescription(String url);
}
