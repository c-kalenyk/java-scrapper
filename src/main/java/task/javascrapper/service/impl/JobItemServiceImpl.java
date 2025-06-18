package task.javascrapper.service.impl;

import java.util.List;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import task.javascrapper.dto.job_item.JobItemRequestDto;
import task.javascrapper.exception.EntityNotFoundException;
import task.javascrapper.mapper.JobItemMapper;
import task.javascrapper.model.JobItem;
import task.javascrapper.repository.JobItemRepository;
import task.javascrapper.service.JobItemService;

@Service
@RequiredArgsConstructor
public class JobItemServiceImpl implements JobItemService {
    private final JobItemRepository jobItemRepository;
    private final JobItemMapper jobItemMapper;

    @Override
    public JobItem save(JobItemRequestDto requestDto) {
        return jobItemRepository.save(jobItemMapper.toModel(requestDto));
    }

    @Override
    public JobItem get(Integer id) {
        return jobItemRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find an item with id " + id));
    }

    @Override
    public List<JobItem> getAll(Pageable pageable) {
        return jobItemRepository.findAll(pageable).getContent();
    }

    @Override
    public List<JobItem> search(String functionName, String location,
                                String dateSortDirection, Pageable pageable) {
        Specification<JobItem> spec = functionNameEquals(functionName);

        if (location != null && !location.isBlank()) {
            spec = spec.and(locationContains(location));
        }

        Sort sort = "desc".equalsIgnoreCase(dateSortDirection)
                ? Sort.by(Sort.Direction.DESC, "postedOn")
                : Sort.by(Sort.Direction.ASC, "postedOn");

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return jobItemRepository.findAll(spec, sortedPageable).getContent();
    }

    private Specification<JobItem> functionNameEquals(String functionName) {
        return (root, query, cb) -> cb.equal(root.get("functionName"), functionName);
    }

    private Specification<JobItem> locationContains(String location) {
        return (root, query, cb) -> {
            Join<JobItem, String> locationJoin = root.join("locations");
            return cb.like(cb.lower(locationJoin), "%" + location.toLowerCase() + "%");
        };
    }
}
