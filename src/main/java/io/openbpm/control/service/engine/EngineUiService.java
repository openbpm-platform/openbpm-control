package io.openbpm.control.service.engine;

import io.openbpm.control.entity.EngineConnectionCheckResult;
import io.openbpm.control.entity.engine.BpmEngine;

public interface EngineUiService {
    /**
     * Checks connection to the specified BPM engine.
     *
     * @return a result of the check. If successfully connected to engine, then the engine version is also returned.
     */
    EngineConnectionCheckResult checkConnection(BpmEngine bpmEngine);

    /**
     * Returns a version of the specified BPM engine.
     *
     * @return a response containing engine version
     */
    String getVersion(BpmEngine engine);


    /**
     * Sets the specified BPM engine as selected in the user's HTTP session and sends an event to the UI.
     *
     * @param engine the BPM engine the application should be connected to
     */
    void selectEngine(BpmEngine engine);
}
