package task.javascrapper.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import task.javascrapper.dto.api_request.Filters;
import task.javascrapper.dto.api_request.JobSearchRequest;
import task.javascrapper.dto.job_function.ApiResultsDto;
import task.javascrapper.dto.job_function.JobFunctionApiDto;
import task.javascrapper.dto.job_function.JobFunctionRequestDto;
import task.javascrapper.dto.job_item.JobItemApiDto;
import task.javascrapper.exception.NoJobsFoundException;
import task.javascrapper.mapper.JobFunctionMapper;
import task.javascrapper.service.DataFetchService;

@Service
@RequiredArgsConstructor
public class DataFetchServiceImpl implements DataFetchService {
    private static final String URL = "https://api.getro.com/api/v2/collections/89/search/jobs";
    private static final String MIME_TYPE = "application/json";
    private final JobFunctionMapper jobFunctionMapper;
    private final OkHttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public JobFunctionRequestDto fetchJobFunction(String jobFunction) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Future<List<JobItemApiDto>>> futures = new ArrayList<>();
        List<JobItemApiDto> allJobs = new ArrayList<>();
        JobFunctionApiDto firstPage = fetchJobFunctionPage(jobFunction, 0);
        allJobs.addAll(firstPage.results().jobs());
        int hitsPerPage = 20;
        int totalCount = firstPage.results().count();
        int totalPages = (int) Math.ceil((double) totalCount / hitsPerPage);

        for (int page = 1; page < totalPages; page++) {
            int currentPage = page;
            futures.add(executor.submit(() -> fetchJobsForPage(jobFunction, currentPage)));
        }

        for (Future<List<JobItemApiDto>> future : futures) {
            try {
                allJobs.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Failed to fetch jobs in parallel", e);
            }
        }
        executor.shutdown();

        if (allJobs.isEmpty()) {
            throw new NoJobsFoundException("No jobs found for job function: " + jobFunction);
        }

        return jobFunctionMapper.toJobFunctionRequestDto(
                new JobFunctionApiDto(new ApiResultsDto(allJobs, totalCount)));
    }

    private List<JobItemApiDto> fetchJobsForPage(String jobFunction, int page) {
        JobFunctionApiDto dto = fetchJobFunctionPage(jobFunction, page);
        List<JobItemApiDto> jobs = dto.results().jobs();
        return jobs != null ? jobs : Collections.emptyList();
    }

    private JobFunctionApiDto fetchJobFunctionPage(String jobFunction, int page) {
        String json;
        try {
            json = mapper.writeValueAsString(
                    new JobSearchRequest(20, page,
                            new Filters(List.of(jobFunction), page), "")
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Request generation error", e);
        }

        RequestBody body = RequestBody.create(json, MediaType.parse(MIME_TYPE));
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .header("Accept", MIME_TYPE)
                .build();

        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IllegalStateException("Failed to fetch jobs: response code " + response.code());
            }
            if (response.body() == null) {
                throw new IllegalStateException("Response body is null");
            }
            return mapper.readValue(response.body().string(), JobFunctionApiDto.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch jobs", e);
        }
    }

    @Override
    public String fetchJobDescription(String url) {
        try {
            Document doc = Jsoup.connect(url).timeout(100000).get();
            Element description = doc.selectFirst("div[data-testid=careerPage]");
            if (description != null) {
                return description.html();
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch description", e);
        }
    }
}
