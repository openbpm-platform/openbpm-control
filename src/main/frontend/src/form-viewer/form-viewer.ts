/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

// @ts-ignore
import {css, html, LitElement} from 'lit';
// @ts-ignore
import {customElement} from 'lit/decorators.js';
import {Form} from "@bpmn-io/form-js-viewer";
// @ts-ignore
import {formViewerStyles} from './style/form-js-style.js';

// @ts-ignore
@customElement('openbpm-control-form-viewer')
// @ts-ignore
class OpenBpmControlFormViewer extends LitElement {

    private readonly FORM_VIEWER_HOLDER: string = "formViewerHolder";
    private readonly viewer: Form;
    private shadowRoot: any;

    static styles = [
        formViewerStyles,
        css`
        `
      ];

    constructor() {
        super();

        this.viewer = new Form();
        this.viewer.setProperty("readOnly", true);
    }

    static get properties() {
        return {
        };
    }

    public async reloadSchema(xmlSchema: string) {
        const schema = JSON.parse(xmlSchema);
        await this.viewer.importSchema(schema, {});
    }

    render() {
        return html`
            <div id="${(this.FORM_VIEWER_HOLDER)}" style="width: 100%; height: 100%"/>
        `;
    }

    updated(updatedProps: any) {
        this.awaitRun(() => this.initViewer());
    }

    private initViewer() {
        this.viewer.attachTo(this.shadowRoot.getElementById(this.FORM_VIEWER_HOLDER)!);
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
