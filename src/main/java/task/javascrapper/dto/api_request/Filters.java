package task.javascrapper.dto.api_request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Filters {
    private List<String> job_functions;
    private int page;
}
