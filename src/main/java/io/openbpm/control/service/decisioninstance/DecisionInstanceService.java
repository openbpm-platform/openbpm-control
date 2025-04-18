package io.openbpm.control.service.decisioninstance;

import io.openbpm.control.entity.decisioninstance.HistoricDecisionInstanceShortData;
import io.openbpm.control.entity.filter.DecisionInstanceFilter;
import org.springframework.lang.Nullable;

import java.util.List;

public interface DecisionInstanceService {
    /**
     * Loads decision instances from the engine history using the specified context.
     *
     * @param loadContext a context to load decision instances
     * @return found historic decision instances
     * @see DecisionInstanceLoadContext
     */
    List<HistoricDecisionInstanceShortData> findAllHistoryDecisionInstances(DecisionInstanceLoadContext loadContext);

    /**
     * Loads a decision instance with the specified identifier.
     *
     * @param decisionInstanceId a decision instance identifier
     * @return found decision instance or null if not found
     */
    @Nullable
    HistoricDecisionInstanceShortData getById(String decisionInstanceId);


    /**
     * Loads from engine history the total count of dicision instances that match the specified filter.
     *
     * @param filter a decision filter instance
     * @return count of decision instances
     */
    long getHistoryDecisionInstancesCount(@Nullable DecisionInstanceFilter filter);

    /**
     * Loads the total count of instances of the decision definition version with the specified identifier.
     *
     * @param decisionDefinitionId a decision definition identifier
     * @return count of instances
     */
    long getCountByDecisionDefinitionId(String decisionDefinitionId);

    /**
     * Loads the count of running instances of the decision definition with the specified key.
     *
     * @param decisionDefinitionKey a process key
     * @return count of instances
     */
    long getCountByDecisionDefinitionKey(String decisionDefinitionKey);
}
