package task.javascrapper.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import task.javascrapper.config.MapperConfig;
import task.javascrapper.dto.job_function.JobFunctionApiDto;
import task.javascrapper.dto.job_function.JobFunctionRequestDto;
import task.javascrapper.model.JobFunction;

@Mapper(config = MapperConfig.class, uses = JobItemMapper.class)
public interface JobFunctionMapper {
    @Mapping(source = "apiDto.results.count", target = "count")
    @Mapping(source = "apiDto.results.jobs", target = "jobs")
    JobFunctionRequestDto toJobFunctionRequestDto(JobFunctionApiDto apiDto);

    JobFunction toModel(JobFunctionRequestDto requestDto);
}
