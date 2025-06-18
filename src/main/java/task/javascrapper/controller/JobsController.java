package task.javascrapper.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import task.javascrapper.model.JobFunction;
import task.javascrapper.model.JobItem;
import task.javascrapper.service.JobFunctionService;
import task.javascrapper.service.JobItemService;

//As in the task we have only one user I don't think that user functionality and security is required anywhere in this implementation
//Also I don't really understand how scrapper must work from user's POW, if where should be some frontend or not, etc. It would be much simpler to implement correct pagination with endpoint only for scraping
@RestController
@RequiredArgsConstructor
@RequestMapping("/jobs")
public class JobsController {
    private final JobFunctionService jobFunctionService;
    private final JobItemService jobItemService;

    @GetMapping("/{function}")
    public JobFunction getFunction(@PathVariable String function) {
        return jobFunctionService.get(function);
    }

    @GetMapping("/job/{id}")
    public JobItem getItem(@PathVariable Integer id) {
        return jobItemService.get(id);
    }

    @GetMapping("/search")
    public List<JobItem> search(
            @RequestParam String functionName,
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "asc") String dateSortDirection
    ) {
        return jobItemService.search(functionName, location, dateSortDirection, pageable);
    }
}
