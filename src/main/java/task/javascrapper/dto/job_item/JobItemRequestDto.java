package task.javascrapper.dto.job_item;

import java.util.List;

public record JobItemRequestDto(
        String positionTitle,
        String url,
        String logo,
        String organisationTitle,
        List<String> locations,
        Long postedOn,
        String description,
        List<String> tags
) {
}
