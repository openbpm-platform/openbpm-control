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
import {BusinessRuleTaskOverlayClickedEvent} from "./component/events";

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
    private elementRegistry:any;

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

        .decision-instance-link-overlay {
            background-color: var(--bpmn-decision-instance-link-overlay-background);
            cursor: pointer;
            display: flex;
            border-radius: 20%;
            justify-content: center;
            align-items: center;
            width: 1.4em;
            height: 1.4em;
        }
    `;

    constructor() {
        super();
        this.viewer = new BpmViewer();
        this.bpmDrawing = this.viewer.get("bpmDrawing");
        this.canvas = this.viewer.get("canvas");
        this.zoomScroll = this.viewer.get("zoomScroll");
        this.overlays = this.viewer.get("overlays");
        this.elementRegistry = this.viewer.get("elementRegistry");

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

    public showBusinessRuleTaskOverlay(cmdJson: any) {
        const cmd = JSON.parse(cmdJson);
        let elements = this.elementRegistry.getAll();
        for (let i = 0; i < elements.length; i++) {
            let value = elements[i];
            if (value.type == "bpmn:BusinessRuleTask" && value.id == cmd.activityId) {
                const htmlDiv = document.createElement('div');
                htmlDiv.innerHTML = `
                <div class="decision-instance-link-overlay" title="${cmd.tooltipMessage}">
                    <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M16 7.9L10 3V6C9.5 6 8.9 6 8 6C0 6 0 14 0 14C0 14 1 10 7.8 10C8.9 10 9.6 10 10 10V12.9L16 7.9Z" fill="#ffffff"/>
                    </svg>
                </div>
                `;
                (function(element:OpenBpmControlBpmViewer, decisionInstanceId:string) {
                    htmlDiv.addEventListener('click', (event:MouseEvent) => {
                        element.dispatchEvent(new BusinessRuleTaskOverlayClickedEvent(decisionInstanceId));
                    });
                })(this, cmd.decisionInstanceId);
                this.awaitRun(() =>
                    this.overlays.add(value.id, {
                        html: htmlDiv,
                        position: {
                            left: -10,
                            bottom: 15
                        }
                    }
                ));
                break;
            }
        };
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
