package io.openbpm.control.service.decisiondefinition.impl;

import feign.FeignException;
import feign.utils.ExceptionUtils;
import io.jmix.core.Sort;
import io.openbpm.control.entity.decisiondefinition.DecisionDefinitionData;
import io.openbpm.control.entity.filter.DecisionDefinitionFilter;
import io.openbpm.control.exception.EngineNotSelectedException;
import io.openbpm.control.mapper.DecisionDefinitionMapper;
import io.openbpm.control.service.decisiondefinition.DecisionDefinitionLoadContext;
import io.openbpm.control.service.decisiondefinition.DecisionDefinitionService;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.repository.DecisionDefinition;
import org.camunda.bpm.engine.repository.DecisionDefinitionQuery;
import org.camunda.community.rest.client.api.DecisionDefinitionApiClient;
import org.camunda.community.rest.client.model.DecisionDefinitionDiagramDto;
import org.camunda.community.rest.impl.RemoteRepositoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.List;

import static io.openbpm.control.util.QueryUtils.addDecisionDefinitionFilters;
import static io.openbpm.control.util.QueryUtils.addDecisionDefinitionSort;

@Service("control_DecisionDefinitionService")
@Slf4j
public class DecisionDefinitionServiceImpl implements DecisionDefinitionService {

    protected final RemoteRepositoryService remoteRepositoryService;
    protected final DecisionDefinitionMapper decisionDefinitionMapper;
    protected final DecisionDefinitionApiClient decisionDefinitionApiClient;

    public DecisionDefinitionServiceImpl(RemoteRepositoryService remoteRepositoryService,
                                         DecisionDefinitionMapper decisionDefinitionMapper,
                                         DecisionDefinitionApiClient decisionDefinitionApiClient) {
        this.remoteRepositoryService = remoteRepositoryService;
        this.decisionDefinitionMapper = decisionDefinitionMapper;
        this.decisionDefinitionApiClient = decisionDefinitionApiClient;
    }

    @Override
    public List<DecisionDefinitionData> findLatestVersions() {
        try {
            return createDecisionDefinitionQuery()
                    .orderByDecisionDefinitionVersion()
                    .asc()
                    .latestVersion()
                    .list()
                    .stream()
                    .map(decisionDefinitionMapper::fromDecisionDefinitionModel)
                    .toList();
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load latest versions of decision definitions because BPM engine not selected");
                return List.of();
            }
            throw e;
        }
    }

    @Override
    public List<DecisionDefinitionData> findAllByKey(String decisionDefinitionKey) {
        return createDecisionDefinitionQuery()
                .orderByDecisionDefinitionVersion()
                .asc()
                .decisionDefinitionKey(decisionDefinitionKey)
                .list()
                .stream()
                .map(decisionDefinitionMapper::fromDecisionDefinitionModel)
                .toList();
    }

    @Override
    public List<DecisionDefinitionData> findAll(DecisionDefinitionLoadContext context) {
        try {
            DecisionDefinitionQuery decisionDefinitionQuery = createDecisionDefinitionQuery(
                    context.getFilter(), context.getSort());

            List<DecisionDefinition> decisionDefinitions;
            if (context.getFirstResult() != null && context.getMaxResults() != null) {
                decisionDefinitions = decisionDefinitionQuery.listPage(
                        context.getFirstResult(), context.getMaxResults());
            } else {
                decisionDefinitions = decisionDefinitionQuery.list();
            }

            return decisionDefinitions
                    .stream()
                    .map(decisionDefinitionMapper::fromDecisionDefinitionModel)
                    .toList();
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load decision definitions because BPM engine not selected");
                return List.of();
            }
            if (rootCause instanceof ConnectException) {
                log.error("Unable to load decision definitions because of connection error: ", e);
                return List.of();
            }
            throw e;
        }
    }

    @Override
    public long getCount(@Nullable DecisionDefinitionFilter filter) {
        DecisionDefinitionQuery decisionDefinitionQuery = createDecisionDefinitionQuery(filter, null);
        return decisionDefinitionQuery.count();
    }

    @Override
    public DecisionDefinitionData getById(String decisionDefinitionId) {
        try {
            DecisionDefinition decisionDefinition = createDecisionDefinitionQuery()
                    .decisionDefinitionId(decisionDefinitionId)
                    .singleResult();
            return decisionDefinitionMapper.fromDecisionDefinitionModel(decisionDefinition);
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load decision definition by id '{}' because BPM engine not selected",
                        decisionDefinitionId);
                return null;
            }
            if (rootCause instanceof ConnectException) {
                log.error("Unable load decision definition by id '{}' because of connection error: ",
                        decisionDefinitionId, e);
                return null;
            }
            throw e;
        }
    }

    @Override
    public String getDmnXml(String decisionDefinitionId) {
        try {
            ResponseEntity<DecisionDefinitionDiagramDto> decisionDefinitionDmnXml =
                    decisionDefinitionApiClient.getDecisionDefinitionDmnXmlById(decisionDefinitionId);
            if (decisionDefinitionDmnXml.getStatusCode().is2xxSuccessful()
                    && decisionDefinitionDmnXml.getBody() != null) {
                return decisionDefinitionDmnXml.getBody().getDmnXml();
            }
            return null;
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load decision definition XML by id '{}' because BPM engine not selected",
                        decisionDefinitionId);
                return null;
            }

            if (rootCause instanceof ConnectException) {
                log.error("Unable load decision definition XML by id '{}' because of connection error: ",
                        decisionDefinitionId, e);
                return null;
            }

            if (rootCause instanceof FeignException feignException && feignException.status() == 404) {
                log.warn("Unable to load decision definition XML by id '{}' because decision does not exist",
                        decisionDefinitionId);
                return null;
            }

            throw e;
        }
    }

    protected DecisionDefinitionQuery createDecisionDefinitionQuery(@Nullable DecisionDefinitionFilter filter,
                                                                    @Nullable Sort sort) {
        DecisionDefinitionQuery decisionDefinitionQuery = new DecisionDefinitionQueryImpl(decisionDefinitionApiClient);

        addDecisionDefinitionFilters(decisionDefinitionQuery, filter);
        addDecisionDefinitionSort(decisionDefinitionQuery, sort);
        return decisionDefinitionQuery;
    }

    protected DecisionDefinitionQuery createDecisionDefinitionQuery() {
        return createDecisionDefinitionQuery(null, null);
    }
}