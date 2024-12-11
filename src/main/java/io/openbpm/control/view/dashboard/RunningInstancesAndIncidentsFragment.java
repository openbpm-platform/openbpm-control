package io.openbpm.control.view.dashboard;

import com.google.common.collect.ImmutableList;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.chartsflowui.component.Chart;
import io.jmix.chartsflowui.kit.component.model.Title;
import io.jmix.chartsflowui.kit.component.model.shared.Color;
import io.jmix.core.Metadata;
import io.jmix.core.entity.KeyValueEntity;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.KeyValueCollectionContainer;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.openbpm.control.entity.dashboard.IncidentStatistics;
import io.openbpm.control.entity.dashboard.ProcessDefinitionStatistics;
import io.openbpm.control.view.incidentsstatistics.IncidentsStatisticsView;
import io.openbpm.control.view.processdefinitionsstatistics.RunningProcessesStatisticsView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;

@FragmentDescriptor("running-instances-and-incidents-fragment.xml")
public class RunningInstancesAndIncidentsFragment extends DashboardCard<Div> {
    public static final List<Color> INCIDENT_COLOR_PALETTE = ImmutableList.of(
            new Color("#ebdba4"),
            new Color("#f2d643"),
            new Color("#ffb248"),
            new Color("#eb8146"),
            new Color("#d95850"),
            new Color("#893448")
    );

    protected static final Logger log = LoggerFactory.getLogger(RunningInstancesAndIncidentsFragment.class);

    @ViewComponent
    protected KeyValueCollectionContainer groupedProcessInstancesDc;
    @ViewComponent
    protected CollectionContainer<ProcessDefinitionStatistics> processDefinitionStatisticsDc;
    @Autowired
    protected Metadata metadata;
    @ViewComponent
    protected Chart runningInstancesChart;
    @ViewComponent
    protected KeyValueCollectionContainer groupedIncidentsDc;
    @ViewComponent
    protected Chart incidentsChart;
    @Autowired
    protected DialogWindows dialogWindows;
    @ViewComponent
    protected VerticalLayout incidentLoadingBox;
    @ViewComponent
    protected VerticalLayout runningInstanceLoadingBox;
    @ViewComponent
    protected JmixButton viewIncidentsStatBtn;
    @ViewComponent
    protected JmixButton viewRunningInstancesStatBtn;
    @ViewComponent
    protected FlexLayout incidentsChartContainer;
    @ViewComponent
    protected FlexLayout runningInstancesChartContainer;


    @Subscribe
    protected void onAttachEvent(final AttachEvent event) {
        incidentsChart.setColorPalette(INCIDENT_COLOR_PALETTE.toArray(new Color[0]));
        initCharts(List.of());
    }

    public void refresh(List<ProcessDefinitionStatistics> items) {
        updateComponents(false);

        initCharts(items);
    }

    protected void initCharts(List<ProcessDefinitionStatistics> items) {
        processDefinitionStatisticsDc.setItems(items);
        initRunningInstancesChart();
        initIncidentChart();
    }

    protected void updateComponents(boolean isLoading) {
        runningInstanceLoadingBox.setVisible(isLoading);
        runningInstancesChartContainer.setVisible(!isLoading);
        viewRunningInstancesStatBtn.setVisible(!isLoading);

        incidentLoadingBox.setVisible(isLoading);
        incidentsChartContainer.setVisible(!isLoading);
        viewIncidentsStatBtn.setVisible(!isLoading);
    }

    @Subscribe(id = "viewRunningInstancesStatBtn", subject = "clickListener")
    public void onViewRunningInstancesStatBtnClick(final ClickEvent<JmixButton> event) {
        dialogWindows.view(getCurrentView(), RunningProcessesStatisticsView.class)
                .withViewConfigurer(view -> {
                    view.setStatistics(groupedProcessInstancesDc.getItems());
                })
                .build()
                .open();
    }

    @Subscribe(id = "viewIncidentsStatBtn", subject = "clickListener")
    public void onViewIncidentStatBtnClick(final ClickEvent<JmixButton> event) {
        dialogWindows.view(getCurrentView(), IncidentsStatisticsView.class)
                .withViewConfigurer(view -> {
                    view.setStatistics(groupedIncidentsDc.getItems());
                })
                .build()
                .open();
    }

    protected void initRunningInstancesChart() {
        List<ProcessDefinitionStatistics> definitionStatistics = processDefinitionStatisticsDc.getItems();

        Map<String, Integer> instancesStatistics = definitionStatistics.stream()
                .filter(processDefinitionStatistics -> processDefinitionStatistics.getInstanceCount() != null && processDefinitionStatistics.getInstanceCount() > 0)
                .collect(Collectors.groupingBy(o -> o.getProcessDefinition().getKey(),
                        Collectors.summingInt(ProcessDefinitionStatistics::getInstanceCount)));

        groupedProcessInstancesDc.setItems(instancesStatistics.entrySet()
                .stream()
                .map(entry -> {
                            KeyValueEntity keValueEntity = metadata.create(KeyValueEntity.class);
                            keValueEntity.setValue("process", entry.getKey());
                            keValueEntity.setValue("instanceCount", entry.getValue());
                            return keValueEntity;
                        }
                ).toList());

        long totalCount = definitionStatistics
                .stream()
                .mapToInt(ProcessDefinitionStatistics::getInstanceCount)
                .sum();

        runningInstancesChart.setTitle(createTotalCountTitle(totalCount));
    }

    protected void initIncidentChart() {
        List<ProcessDefinitionStatistics> definitionStatistics = processDefinitionStatisticsDc.getItems();
        Map<String, Integer> groupedIncidentStatistics = definitionStatistics
                .stream()
                .filter(processDefinitionStatistics -> !processDefinitionStatistics.getIncidents().isEmpty())
                .collect(Collectors.groupingBy(o -> o.getProcessDefinition().getKey(),
                        Collectors.summingInt(value -> value.getIncidents()
                                .stream()
                                .mapToInt(IncidentStatistics::getIncidentCount)
                                .sum())));

        groupedIncidentsDc.setItems(groupedIncidentStatistics
                .entrySet()
                .stream()
                .map(entry -> {
                            KeyValueEntity keValueEntity = metadata.create(KeyValueEntity.class);
                            keValueEntity.setValue("process", entry.getKey());
                            keValueEntity.setValue("incidentCount", entry.getValue());
                            return keValueEntity;
                        }
                ).toList());

        long totalIncidentCount = groupedIncidentStatistics
                .values()
                .stream()
                .reduce(0, Integer::sum);

        incidentsChart.setTitle(createTotalCountTitle(totalIncidentCount));
    }

    protected Title createTotalCountTitle(long totalCount) {
        return new Title()
                .withText(formattedCount(totalCount))
                .withLeft("center")
                .withTop("center")
                .withTextStyle(new Title.TextStyle().withFontSize(32));
    }
}