/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service.job.impl;

import io.jmix.core.Sort;
import io.openbpm.control.entity.filter.JobFilter;
import io.openbpm.control.entity.job.JobData;
import io.openbpm.control.entity.job.JobDefinitionData;
import io.openbpm.control.mapper.JobMapper;
import io.openbpm.control.service.job.JobLoadContext;
import io.openbpm.control.service.job.JobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.camunda.community.rest.client.api.HistoryApiClient;
import org.camunda.community.rest.client.api.JobApiClient;
import org.camunda.community.rest.client.api.JobDefinitionApiClient;
import org.camunda.community.rest.client.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static io.openbpm.control.util.EngineRestUtils.getCountResult;

@Service("control_JobService")
@Slf4j
public class JobServiceImpl implements JobService {
    protected final JobMapper jobMapper;
    protected final JobApiClient jobApiClient;
    protected final JobDefinitionApiClient jobDefinitionApiClient;
    protected final HistoryApiClient historyApiClient;

    public JobServiceImpl(JobMapper jobMapper,
                          JobApiClient jobApiClient,
                          JobDefinitionApiClient jobDefinitionApiClient,
                          HistoryApiClient historyApiClient) {
        this.jobMapper = jobMapper;
        this.jobApiClient = jobApiClient;
        this.jobDefinitionApiClient = jobDefinitionApiClient;
        this.historyApiClient = historyApiClient;
    }

    @Override
    public List<JobData> findAll(JobLoadContext loadContext) {
        JobQueryDto jobQueryDto = createJobQueryDto(loadContext.getFilter());
        jobQueryDto.setSorting(createSortOptions(loadContext.getSort()));

        ResponseEntity<List<JobDto>> jobsResponse = jobApiClient.queryJobs(loadContext.getFirstResult(), loadContext.getMaxResults(),
                jobQueryDto);
        if (jobsResponse.getStatusCode().is2xxSuccessful()) {
            List<JobDto> jobDtoList = jobsResponse.getBody();
            return CollectionUtils.emptyIfNull(jobDtoList)
                    .stream()
                    .map(jobMapper::fromJobDto)
                    .toList();
        }
        log.error("Error on loading runtime jobs: query {}, status code {}", jobQueryDto, jobsResponse.getStatusCode());
        return List.of();
    }

    @Override
    public long getCount(@Nullable JobFilter jobFilter) {
        JobQueryDto jobQueryDto = createJobQueryDto(jobFilter);

        ResponseEntity<CountResultDto> jobsResponse = jobApiClient.queryJobsCount(jobQueryDto);
        if (jobsResponse.getStatusCode().is2xxSuccessful()) {
            return getCountResult(jobsResponse.getBody());
        }
        log.error("Error on loading runtime jobs count: query {}, status code {}", jobQueryDto, jobsResponse.getStatusCode());
        return 0;
    }

    @Override
    @Nullable
    public JobDefinitionData findJobDefinition(String jobDefinitionId) {
        ResponseEntity<JobDefinitionDto> response = jobDefinitionApiClient.getJobDefinition(jobDefinitionId);
        if (response.getStatusCode().is2xxSuccessful()) {
            JobDefinitionDto jobDefinitionDto = response.getBody();
            return jobDefinitionDto != null ? jobMapper.fromJobDefinitionDto(jobDefinitionDto) : null;
        }
        log.error("Error on loading stacktrace for job definition id {}, status code {}", jobDefinitionId, response.getStatusCode());
        return null;
    }

    @Override
    public void setJobRetries(String jobId, int retries) {
        ResponseEntity<Void> response = jobApiClient.setJobRetries(jobId, new JobRetriesDto().retries(retries));
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Error on loading update retries to {} for job with id {}, status code {}", retries,
                    jobId, response.getStatusCode());
        } else {
            log.debug("Update retries count for job {}. New value: {}", jobId, retries);
        }
    }

    @Override
    public void setJobRetriesAsync(List<String> jobIds, int retries) {
        ResponseEntity<BatchDto> response = jobApiClient.setJobRetriesAsyncOperation(new SetJobRetriesDto()
                .jobIds(jobIds)
                .retries(retries));

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Error on loading update retries to {} for job with ids {}, status code {}", retries,
                    jobIds, response.getStatusCode());
        } else {
            log.debug("Async update retries count for jobs {}. New value: {}", jobIds, retries);
        }
    }

    @Override
    public String getErrorDetails(String jobId) {
        ResponseEntity<Object> response = jobApiClient.getStacktrace(jobId);
        if (response.getStatusCode().is2xxSuccessful()) {
            Object responseBody = response.getBody();
            return responseBody != null ? responseBody.toString() : "";
        }

        return "";
    }

    @Override
    public String getHistoryErrorDetails(String jobId) {
        ResponseEntity<Object> response = historyApiClient.getStacktraceHistoricJobLog(jobId);
        if (response.getStatusCode().is2xxSuccessful()) {
            Object responseBody = response.getBody();
            return responseBody != null ? responseBody.toString() : "";
        }
        return "";
    }

    protected JobQueryDto createJobQueryDto(JobFilter filter) {
        JobQueryDto jobQueryDto = new JobQueryDto();
        if (filter != null) {
            jobQueryDto.processInstanceId(filter.getProcessInstanceId());
        }

        return jobQueryDto;
    }

    @Nullable
    protected List<JobQueryDtoSortingInner> createSortOptions(Sort sort) {
        if (sort == null) {
            return null;
        }
        List<JobQueryDtoSortingInner> jobQueryDtoSortingInners = new ArrayList<>();
        for (Sort.Order order : sort.getOrders()) {
            JobQueryDtoSortingInner sortOption = new JobQueryDtoSortingInner();

            switch (order.getProperty()) {
                case "id" -> sortOption.setSortBy(JobQueryDtoSortingInner.SortByEnum.JOBID);
                case "retries" -> sortOption.setSortBy(JobQueryDtoSortingInner.SortByEnum.JOBRETRIES);
                case "dueDate" -> sortOption.setSortBy(JobQueryDtoSortingInner.SortByEnum.JOBDUEDATE);
                case "priority" -> sortOption.setSortBy(JobQueryDtoSortingInner.SortByEnum.JOBPRIORITY);
                default -> {
                }
            }

            if (order.getDirection() == Sort.Direction.ASC) {
                sortOption.setSortOrder(JobQueryDtoSortingInner.SortOrderEnum.ASC);
            } else if (order.getDirection() == Sort.Direction.DESC) {
                sortOption.setSortOrder(JobQueryDtoSortingInner.SortOrderEnum.DESC);
            }
            if (sortOption.getSortBy() != null && sortOption.getSortOrder() != null) {
                jobQueryDtoSortingInners.add(sortOption);
            }
        }

        return jobQueryDtoSortingInners;
    }
}
