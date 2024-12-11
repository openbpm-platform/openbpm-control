/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service.engine;

import io.openbpm.control.entity.engine.BpmEngine;
import org.springframework.lang.Nullable;

import java.util.Set;

/**
 * Provides methods to manage the configured BPM engines.
 */
public interface EngineService {

    /**
     * Returns a selected BPN engine stored in HTTP session or the default engine if an engine set in session not found.
     *
     * @return an engine selected by user or default engine
     */
    @Nullable
    BpmEngine getSelectedEngine();

    /**
     * Searches a BPM engine that is marked as default.
     *
     * @return default engine or null if not found
     */
    @Nullable
    BpmEngine findDefaultEngine();

    /**
     * Saves the specified engine identifier to the HTTP session.
     *
     * @param engine a BPM engine which should be selected for the authenticated user
     */
    void setSelectedEngine(BpmEngine engine);

    /**
     * Checks whether at least one BPM engine exists.
     *
     * @return at least one engine exists or not
     */
    boolean engineExists();

    /**
     * Saves the specified BPM engine to the database. If specified engine is default, other engines will be marked as non-default.
     *
     * @param engine a BPM engine to save
     * @return saved engine and other engines if the specified one is default.
     */
    Set<Object> saveEngine(BpmEngine engine);

    /**
     * Marks the specified engine as default. Other engines will be marked as non-default.
     *
     * @param engine an engine that should be marked as default
     */
    void markAsDefault(BpmEngine engine);
}