/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

// @ts-ignore
import {css, html, LitElement} from 'lit';
// @ts-ignore
import {customElement} from 'lit/decorators.js';
// @ts-ignore
//import {DmnViewer} from "dmn-js";
// @ts-ignore
import {DmnJS} from "dmn-js/dist/dmn-viewer.development.js";

// @ts-ignore
@customElement('openbpm-control-dmn-viewer')
// @ts-ignore
class OpenBpmControlDmnViewer extends LitElement {

    private readonly DMN_VIEWER_HOLDER: string = "dmnViewerHolder";
    private readonly viewer: DmnJS;
    private shadowRoot: any;

    static styles = [
        css`
        `
      ];

    constructor() {
        super();

        this.viewer = new DmnJS();
        this.viewer.setProperty("readOnly", true);
    }

    static get properties() {
        return {
        };
    }

    public async reloadSchema(xmlSchema: string) {
        await this.viewer.importXML(xmlSchema);
    }

    render() {
        return html`
            <div id="${(this.DMN_VIEWER_HOLDER)}" style="width: 100%; height: 100%"/>
        `;
    }

    updated(updatedProps: any) {
        this.awaitRun(() => this.initViewer());
    }

    private initViewer() {
        this.viewer.attachTo(this.shadowRoot.getElementById(this.DMN_VIEWER_HOLDER)!);
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
