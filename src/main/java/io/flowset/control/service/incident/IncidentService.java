/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.service.incident;

import io.flowset.control.entity.filter.IncidentFilter;
import io.flowset.control.entity.incident.HistoricIncidentData;
import io.flowset.control.entity.incident.IncidentData;
import io.flowset.control.dto.ActivityIncidentData;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Provides methods to get data about incidents from the BPM engine.
 */
public interface IncidentService {

    /**
     * Loads open incidents for elements in the process instance with the specified identifier.
     *
     * @param processInstanceId a process instance identifier
     * @return open incidents
     */
    List<ActivityIncidentData> findRuntimeIncidents(String processInstanceId);

    /**
     * Loads open incidents from the engine using the specified context.
     *
     * @param loadContext a context to load open incidents
     * @return a list of open incidents
     */
    List<IncidentData> findRuntimeIncidents(IncidentLoadContext loadContext);

    /**
     * Loads an open incident with the specified identifier.
     *
     * @param incidentId an incident identifier
     * @return found open incident or null if not found
     */
    @Nullable
    IncidentData findRuntimeIncidentById(String incidentId);

    /**
     * Loads from engine the total count of open incidents that match the specified filter.
     *
     * @param filter an incident filter instance
     * @return count of open incidents
     */
    long getRuntimeIncidentCount(@Nullable IncidentFilter filter);

    /**
     * Loads open incidents from the engine history using the specified context.
     *
     * @param loadContext a context to load incidents from history
     * @return a list of found incidents
     */
    List<HistoricIncidentData> findHistoricIncidents(IncidentLoadContext loadContext);

    /**
     * Loads from engine history the total count of incidents that match the specified filter.
     *
     * @param filter an incident filter instance
     * @return count of found incidents
     */
    long getHistoricIncidentCount(@Nullable IncidentFilter filter);

    /**
     * Loads an incident with the specified identifier from the engine history.
     *
     * @param id an incident identifier
     * @return found incident or null if not found
     */
    @Nullable
    HistoricIncidentData findHistoricIncidentById(String id);
}