package task.javascrapper.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import task.javascrapper.dto.job_item.JobItemRequestDto;
import task.javascrapper.model.JobItem;

public interface JobItemService {
    JobItem save(JobItemRequestDto requestDto);

    JobItem get(Integer id);

    List<JobItem> getAll(Pageable pageable);

    List<JobItem> search(String functionName, String location, String dateSortDirection, Pageable pageable);
}
