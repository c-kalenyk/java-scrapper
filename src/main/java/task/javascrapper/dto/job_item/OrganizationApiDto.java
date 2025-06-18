package task.javascrapper.dto.job_item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrganizationApiDto(
        String stage,
        String name,
        String logo_url,
        String head_count,
        List<String> industry_tags
) {
}
