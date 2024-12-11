/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

// @ts-ignore
import {css, html, LitElement} from 'lit';
// @ts-ignore
import {customElement} from 'lit/decorators.js';
import BpmViewer from "./bpm/BpmViewer";
import selectionChangedEventHandler from "./component/selectionChangedEventHandler";
import {BpmProcessDefinition} from "./component/types";
import {XmlImportCompleteEvent} from "./component/events";

// @ts-ignore
@customElement('openbpm-control-bpmn-viewer')
// @ts-ignore
class OpenBpmControlBpmViewer extends LitElement {
    private readonly BPM_VIEWER_HOLDER: string = "bpmnViewerHolder";
    private readonly viewer: BpmViewer;
    private bpmnXml: string | undefined;
    private readonly bpmDrawing: any;
    private readonly canvas: any;
    private shadowRoot: any;
    private zoomScroll: any;
    private processDefinitionsJson: string | undefined;
    private overlays: any;

    static styles = css`
        .highlighted:not(.djs-connection) .djs-visual > :nth-child(1) {
            fill: var(--bpmn-running-activity-color) !important;
        }

        .incident-overlay {
            background-color: var(--bpmn-incident-overlay-bg-color);
            color: var(--bpmn-incident-overlay-text-color);
            border-radius: 50%;
            padding: 0.025em 0.35em;
            line-height: var(--lumo-line-height-xs);
            display: inline-block;
            text-align: center;
            vertical-align: middle;
            font-size: 12px;
            font-weight: bold;
            border: var(--bpmn-incident-overlay-border);
        }
    `;

    constructor() {
        super();
        this.viewer = new BpmViewer();
        this.bpmDrawing = this.viewer.get("bpmDrawing");
        this.canvas = this.viewer.get("canvas");
        this.zoomScroll = this.viewer.get("zoomScroll");
        this.overlays = this.viewer.get("overlays");

        let viewer = this.viewer as any;
        const canvas = viewer.get("canvas");
        const element = this;

        viewer.on("selection.changed", (e: any) => selectionChangedEventHandler(element as any, canvas.getRootElement(), e as any));
        viewer.on("import.parse.complete", (e: any) => {
            const rootElements = e.definitions?.rootElements as Array<any> || [];
            const processDefinitions: BpmProcessDefinition[] = [];
            rootElements.forEach((item: any) => {
                if (item.$type === "bpmn:Process") {
                    processDefinitions.push({
                        key: item.id,
                        name: item.name
                    });
                }
            });
            this.processDefinitionsJson = JSON.stringify(processDefinitions);
            this.dispatchEvent(new XmlImportCompleteEvent(this.processDefinitionsJson));
        });
    }

    static get properties() {
        return {
            bpmnXml: {type: String}
        }
    }

    public async reloadSchema(xmlSchema: string) {
        await this.viewer.importXML(xmlSchema)
    }

    // @ts-ignore
    public async getXmlSchema(): Promise<string> {
        const requestXml = await this.viewer.saveXML({
            format: true
        });
        return requestXml.xml!;
    }

    render() {
        return html`
            <div id="${(this.BPM_VIEWER_HOLDER)}" style="width: 100%; height: 100%"/>
        `;
    }

    updated(updatedProps: any) {
        this.awaitRun(() => this.initViewer());
    }

    public setElementColor(cmdJson: any) {
        const cmd = JSON.parse(cmdJson);
        this.awaitRun(() => this.bpmDrawing.setElementColor(cmd));
    }

    public setIncidentCount(cmdJson: any) {
        const cmd = JSON.parse(cmdJson);
        const elements = cmd.elements as Array<{ elementId: string, incidentCount: number, tooltipMessage: string }>;
        this.awaitRun(() => elements.forEach(value => {
            this.overlays.add(value.elementId, {
                html: `<div class="incident-overlay" title="${value.tooltipMessage}">${value.incidentCount}</div>`,
                position: {
                    right: 10,
                    bottom: 15
                }
            })
        }));
    }

    public addMarker(cmdJson: any) {
        const cmd = JSON.parse(cmdJson);
        this.awaitRun(() => this.canvas.addMarker(cmd.elementId, cmd.marker));
    }

    public removeMarker(cmdJson: any) {
        const cmd = JSON.parse(cmdJson);
        this.awaitRun(() => this.canvas.removeMarker(cmd.elementId, cmd.marker));
    }

    public resetZoom() {
        this.awaitRun(() => this.zoomScroll.reset());
    }

    private initViewer() {
        this.viewer.attachTo(this.shadowRoot.getElementById(this.BPM_VIEWER_HOLDER)!);
        if (this.bpmnXml) {
            this.viewer.importXML(this.bpmnXml).then(r => {
                console.log(r.warnings);
            });
        }
    }

    private awaitRun(callable: () => void) {
        setTimeout(() => {
            try {
                callable();
            } catch (err) {
                console.log('error while running callable', err);
            }
        }, 100);
    }
}
