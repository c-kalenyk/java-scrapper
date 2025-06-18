package task.javascrapper.dto.api_request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobSearchRequest {
    private int hitsPerPage;
    private int page;
    private Filters filters;
    private String query;
}
