package task.javascrapper.dto.job_function;

import task.javascrapper.dto.job_item.JobItemApiDto;

import java.util.List;

public record ApiResultsDto(
        List<JobItemApiDto> jobs,
        int count
) {
}
