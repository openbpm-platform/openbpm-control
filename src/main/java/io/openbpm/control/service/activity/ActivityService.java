/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service.activity;

import io.openbpm.control.entity.activity.ActivityInstanceTreeItem;
import io.openbpm.control.entity.activity.ActivityShortData;
import io.openbpm.control.entity.activity.HistoricActivityInstanceData;
import io.openbpm.control.entity.filter.ActivityFilter;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Provides methods to get data about process activity instances from the BPM engine.
 */
public interface ActivityService {
    /**
     * Loads a list of currently running activities for a process instance with the specified identifier.
     *
     * @param processInstanceId a process instance identifier
     * @return found running activities
     */
    List<ActivityShortData> findRunningActivities(String processInstanceId);

    /**
     * Loads from the engine history a list of finished activities for a process instance with the specified identifier.
     *
     * @param processInstanceId a process instance identifier
     * @return found finished activities
     */
    List<ActivityShortData> findFinishedActivities(String processInstanceId);

    /**
     * Loads running activity instances as tree elements for a process instance with the specified identifier.
     *
     * @param processInstanceId a process instance identifier
     * @return a list of activity instances tree items
     */
    List<ActivityInstanceTreeItem> getActivityInstancesTree(String processInstanceId);

    /**
     * Loads activity instances from the engine history using the specified context.
     *
     * @param loadContext a context to load activity instances
     * @return found historic activity instances
     * @see ActivityLoadContext
     */
    List<HistoricActivityInstanceData> findAllHistoryActivities(ActivityLoadContext loadContext);

    /**
     * Loads from engine history the total count of activity instances that match the specified filter.
     *
     * @param filter an activity filter instance
     * @return count of activity instances
     */
    long getHistoryActivitiesCount(@Nullable ActivityFilter filter);

    /**
     * Loads from the engine history an activity instance with the specified identifier.
     *
     * @param activityInstanceId a historic activity instance identifier
     * @return found instance or null if instance not found
     */
    @Nullable
    HistoricActivityInstanceData findById(String activityInstanceId);
}
