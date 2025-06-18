package task.javascrapper.dto.job_function;

import java.util.List;
import task.javascrapper.dto.job_item.JobItemRequestDto;

public record JobFunctionRequestDto(
        Integer count,
        List<JobItemRequestDto> jobs
) {
}
