package task.javascrapper.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import task.javascrapper.config.MapperConfig;
import task.javascrapper.dto.job_item.JobItemApiDto;
import task.javascrapper.dto.job_item.JobItemRequestDto;
import task.javascrapper.model.JobItem;

@Mapper(config = MapperConfig.class)
public interface JobItemMapper {
    @Mapping(source = "title", target = "positionTitle")
    @Mapping(target = "url", expression = "java(generateJobUrl(apiDto))")
    @Mapping(source = "organization.name", target = "organisationTitle")
    @Mapping(source = "organization.logo_url", target = "logo")
    @Mapping(source = "created_at", target = "postedOn")
    @Mapping(target = "tags", expression = "java(mapTags(apiDto))")
    JobItemRequestDto toJobItemRequestDto(JobItemApiDto apiDto);

    JobItem toModel(JobItemRequestDto dto);

    default String generateJobUrl(JobItemApiDto apiDto) {
        if (apiDto == null || apiDto.slug() == null || apiDto.organization().name() == null) {
            return null;
        }

        String company = apiDto.organization().name()
                .toLowerCase()
                .replaceAll("[\\s']", "_")
                .replaceAll("[^a-z0-9_\\-]", "");

        return new StringBuilder("https://jobs.techstars.com/companies/")
                .append(company)
                .append("/jobs/")
                .append(apiDto.slug())
                .append("#content")
                .toString();
    }

    default List<String> mapTags(JobItemApiDto dto) {
        List<String> tags = new ArrayList<>();

        if (dto.organization() != null) {
            List<String> industryTags = dto.organization().industry_tags();
            if (industryTags != null) {
                tags.addAll(industryTags);
            }
            String headCount = dto.organization().head_count();
            if (headCount != null) {
                Map<Integer, String> headCountMap = Map.of(
                        0, "1 - 10 employees",
                        1, "11 - 50 employees",
                        2, "51 - 200 employees",
                        3, "201 - 1000 employees",
                        4, "1001 - 5000 employees",
                        5, "5001+ employees"
                );
                if (headCountMap.containsKey(headCount)) {
                    tags.add(headCountMap.get(headCount));
                }
            }
            String stage = formatSpecialTag(dto.organization().stage());
            if (stage != null) {
                tags.add(stage);
            }
        }
        String seniority = formatSpecialTag(dto.seniority());
        if (seniority != null) {
            tags.add(seniority);
        }
        return tags;
    }

    default String formatSpecialTag(String value) {
        if (value == null) {
            return null;
        }

        switch (value.toLowerCase()) {
            case "ico": return "ICO";
            case "series_c_plus": return "Series C+";
            case "mid_senior": return "Mid-Senior";
            case "cxo": return "CXO";
            default:
                return Arrays.stream(value.split("_"))
                        .map(w -> Character.toUpperCase(w.charAt(0)) + w.substring(1).toLowerCase())
                        .collect(Collectors.joining(" "));
        }
    }
}
