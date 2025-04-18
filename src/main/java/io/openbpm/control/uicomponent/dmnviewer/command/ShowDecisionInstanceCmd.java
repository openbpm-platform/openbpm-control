package io.openbpm.control.uicomponent.dmnviewer.command;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShowDecisionInstanceCmd {

    List<OutputData> outputDataList;
}
