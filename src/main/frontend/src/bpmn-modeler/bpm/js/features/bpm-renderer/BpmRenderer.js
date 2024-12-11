/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

import BaseRenderer from 'diagram-js/lib/draw/BaseRenderer';

import inherits from 'inherits';

import {isAny} from 'bpmn-js/lib/features/modeling/util/ModelingUtil';

const HIGH_PRIORITY = 1500;

/**
 * A bpmn:ServiceTask with a 'type' attribute that is equal to 'dmn' must be displayed as a Decision Table element. That's because in Flowable
 * business rule tasks are presented with service tasks.
 */
export default function BpmRenderer(eventBus, bpmnRenderer, pathMap) {
    BaseRenderer.call(this, eventBus, HIGH_PRIORITY);
    this.bpmnRenderer = bpmnRenderer;
    this.pathMap = pathMap;
}

inherits(BpmRenderer, BaseRenderer);

BpmRenderer.$inject = ['eventBus', 'bpmnRenderer', 'pathMap'];

BpmRenderer.prototype.canRender = function (element) {
    const elementType = element.businessObject.type || element.businessObject.get('flowable:type');
    return isAny(element, ['bpmn:ServiceTask'])
        && ['dmn', 'jmix-send-email', 'jmix-load-entities-jpql', 'jmix-modify-entity', 'jmix-create-entity'].includes(elementType);
};

BpmRenderer.prototype.drawShape = function (parentNode, element) {
    const elementType = element.businessObject.type || element.businessObject.get('flowable:type');
    if (['jmix-load-entities-jpql', 'jmix-modify-entity', 'jmix-create-entity'].includes(elementType)) {
        return _drawEntityDataTask.bind(this)(parentNode, element);
    } else if (elementType === 'jmix-send-email') {
        return this.bpmnRenderer.handlers['bpmn:SendTask'](parentNode, element);
    } else {
        return this.bpmnRenderer.handlers['bpmn:BusinessRuleTask'](parentNode, element);
    }
};

function _drawEntityDataTask(parentGfx, element) {
    const rect = this.bpmnRenderer.handlers['bpmn:Task'](parentGfx, element);

    const pathData = this.pathMap.getScaledPath('DATA_STORE', {
        abspos: {
            x: 6,
            y: 8
        },
        xScaleFactor: 0.2,
        yScaleFactor: 0.2,
        containerWidth: element.width,
        containerHeight: element.height,
        position: {
            mx: 0,
            my: 0.133
        }
    });

    this.bpmnRenderer._drawPath(parentGfx, pathData, {
        strokeWidth: 1,
        fill: 'white',
        fillOpacity: .95,
        stroke: 'black'
    });

    return rect;
}