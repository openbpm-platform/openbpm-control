package io.openbpm.control.service.decisioninstance.impl;

import feign.utils.ExceptionUtils;
import io.jmix.core.Sort;
import io.openbpm.control.entity.decisioninstance.HistoricDecisionInstanceShortData;
import io.openbpm.control.entity.filter.DecisionInstanceFilter;
import io.openbpm.control.exception.EngineNotSelectedException;
import io.openbpm.control.mapper.DecisionInstanceMapper;
import io.openbpm.control.service.decisioninstance.DecisionInstanceLoadContext;
import io.openbpm.control.service.decisioninstance.DecisionInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.history.HistoricDecisionInstance;
import org.camunda.bpm.engine.history.HistoricDecisionInstanceQuery;
import org.camunda.community.rest.client.api.HistoryApiClient;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.List;

import static io.openbpm.control.util.QueryUtils.addDecisionInstanceFilters;
import static io.openbpm.control.util.QueryUtils.addDecisionInstanceSort;

@Service("control_DecisionInstanceService")
@Slf4j
public class DecisionInstanceServiceImpl implements DecisionInstanceService {

    protected final HistoryApiClient historyApiClient;
    protected final DecisionInstanceMapper decisionInstanceMapper;

    public DecisionInstanceServiceImpl(HistoryApiClient historyApiClient,
                                       DecisionInstanceMapper decisionInstanceMapper) {
        this.historyApiClient = historyApiClient;
        this.decisionInstanceMapper = decisionInstanceMapper;
    }

    @Override
    public List<HistoricDecisionInstanceShortData> findAllHistoryDecisionInstances(DecisionInstanceLoadContext loadContext) {
        HistoricDecisionInstanceQuery decisionInstanceQuery = createHistoricDecisionInstanceQuery(
                loadContext.getFilter(), loadContext.getSort());

        try {
            List<HistoricDecisionInstance> historicDecisionInstances;
            if (loadContext.getFirstResult() != null && loadContext.getMaxResults() != null) {
                historicDecisionInstances = decisionInstanceQuery.listPage(loadContext.getFirstResult(), loadContext.getMaxResults());
            } else {
                historicDecisionInstances = decisionInstanceQuery.list();
            }

            return historicDecisionInstances
                    .stream()
                    .map(decisionInstanceMapper::fromHistoricDecisionInstance)
                    .toList();

        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load deployments because BPM engine not selected");
                return List.of();
            }
            if (rootCause instanceof ConnectException) {
                log.error("Unable to load deployments because of connection error: ", e);
                return List.of();
            }
            throw e;
        }
    }

    @Override
    public long getHistoryDecisionInstancesCount(DecisionInstanceFilter filter) {
        HistoricDecisionInstanceQuery historicDecisionInstanceQuery = createHistoricDecisionInstanceQuery(filter, null);
        return historicDecisionInstanceQuery.count();
    }

    @Override
    public HistoricDecisionInstanceShortData getById(String decisionInstanceId) {
        HistoricDecisionInstanceQuery decisionInstanceQuery = createHistoricDecisionInstanceQuery()
                .decisionInstanceId(decisionInstanceId)
                .includeInputs()
                .includeOutputs();

        try {
            return decisionInstanceQuery.list()
                    .stream()
                    .map(decisionInstanceMapper::fromHistoricDecisionInstance)
                    .findFirst().orElse(null);

        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load deployments because BPM engine not selected");
                return null;
            }
            if (rootCause instanceof ConnectException) {
                log.error("Unable to load deployments because of connection error: ", e);
                return null;
            }
            throw e;
        }
    }

    @Override
    public long getCountByDecisionDefinitionId(String decisionDefinitionId) {
        HistoricDecisionInstanceQuery decisionInstanceQuery = createHistoricDecisionInstanceQuery()
                .decisionDefinitionId(decisionDefinitionId);
        return decisionInstanceQuery.count();
    }

    @Override
    public long getCountByDecisionDefinitionKey(String decisionDefinitionKey) {
        HistoricDecisionInstanceQuery decisionInstanceQuery = createHistoricDecisionInstanceQuery()
                .decisionDefinitionKey(decisionDefinitionKey);
        return decisionInstanceQuery.count();
    }

    protected HistoricDecisionInstanceQuery createHistoricDecisionInstanceQuery(@Nullable DecisionInstanceFilter filter,
                                                                                @Nullable Sort sort) {
        HistoricDecisionInstanceQuery historicDecisionInstanceQuery =
                new HistoricDecisionInstanceQueryImpl(historyApiClient);

        addDecisionInstanceFilters(historicDecisionInstanceQuery, filter);
        addDecisionInstanceSort(historicDecisionInstanceQuery, sort);
        return historicDecisionInstanceQuery;
    }

    protected HistoricDecisionInstanceQuery createHistoricDecisionInstanceQuery() {
        return createHistoricDecisionInstanceQuery(null, null);
    }
}
