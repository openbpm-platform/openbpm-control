/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

import {
    getDi
} from 'bpmn-js/lib/draw/BpmnRenderUtil';

export default function BpmDrawing(elementRegistry, graphicsFactory) {
    this._elementRegistry = elementRegistry;
    this._graphicsFactory = graphicsFactory;
}

BpmDrawing.$inject = ['elementRegistry', 'graphicsFactory'];

BpmDrawing.prototype.setElementColor = function(context) {
    const elementRegistry = this._elementRegistry;
    const graphicsFactory = this._graphicsFactory;
    const element = this._elementRegistry.get(context.elementId);
    if (!element) {
        return console.error('element not found: ' + context.elementId);
    }

    getDi(element).set('stroke', context.stroke);
    getDi(element).set('fill', context.fill);

    const gfx = elementRegistry.getGraphics(element);
    const type = element.waypoints ? 'connection' : 'shape';

    graphicsFactory.update(type, element, gfx);
};