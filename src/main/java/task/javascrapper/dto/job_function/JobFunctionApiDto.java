package task.javascrapper.dto.job_function;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JobFunctionApiDto(
        ApiResultsDto results
) {
}
