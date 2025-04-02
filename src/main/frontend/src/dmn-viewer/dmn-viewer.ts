/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

// @ts-ignore
import {css, html, LitElement} from 'lit';
// @ts-ignore
import {customElement} from 'lit/decorators.js';
// @ts-ignore
import Viewer from "dmn-js/dist/dmn-viewer.development.js";

// @ts-ignore
import {dmnJsDecisionTableStyles} from './style/dmn-js-decision-table-style.js';
// @ts-ignore
import {dmnJsDrdStyles} from './style/dmn-js-drd-style.js';
// @ts-ignore
import {dmnJsLiteralExpressionStyle} from './style/dmn-js-literal-expression-style.js';
// @ts-ignore
import {dmnJsSharedStyle} from './style/dmn-js-shared-style.js';

// @ts-ignore
import {dmnEmbeddedStyle} from './style/dmn-embedded-style.js';

// @ts-ignore
@customElement('openbpm-control-dmn-viewer')
// @ts-ignore
class OpenBpmControlDmnViewer extends LitElement {

    private readonly DMN_VIEWER_HOLDER: string = "dmnViewerHolder";
    private readonly viewer: Viewer;
    private shadowRoot: any;

    static styles = [
        dmnEmbeddedStyle,
        dmnJsDecisionTableStyles,
        dmnJsDrdStyles,
        dmnJsLiteralExpressionStyle,
        dmnJsSharedStyle,
        css`
        `
      ];

    constructor() {
        super();

        this.viewer = new Viewer();
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
