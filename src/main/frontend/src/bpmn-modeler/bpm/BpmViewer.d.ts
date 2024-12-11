/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

// @ts-ignore
import Viewer from 'bpmn-js/lib/Viewer';
// @ts-ignore
import {BaseViewerOptions} from "bpmn-js/lib/BaseViewer";


export default class BpmViewer extends Viewer {
    constructor(options?: BaseViewerOptions);

    get(canvas: string): any;

    importXML(s: string, func?: () => void): Promise<any>

    saveXML(param: { [str: string]: any }): Promise<any>

    attachTo(htmlElement: HTMLElement | string);
}