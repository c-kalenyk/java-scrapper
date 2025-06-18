package task.javascrapper.dto.job_item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JobItemApiDto(
        String title,
        String slug,
        String description,
        List<String> locations,
        List<String> searchable_locations,
        OrganizationApiDto organization,
        Long created_at,
        String seniority
) {
}
