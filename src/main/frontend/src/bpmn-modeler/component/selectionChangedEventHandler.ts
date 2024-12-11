/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

// @ts-ignore
import {LitElement} from "lit";
import {ElementSelectionBpmEvent} from "./events";

export default function selectionChangedEventHandler(element: LitElement, rootElement: any, internalEvent: any) {
    let newElement = internalEvent.newSelection[0] as any;
    if (typeof newElement === 'undefined') {
        newElement = rootElement;
    }
    const bo = newElement.businessObject as any;
    if (bo) {
        if (bo.eventDefinitions) {
            const eventDefinition = bo.eventDefinitions[0] as any;
            if (eventDefinition) {
                const propertiesThatMustBeEnumerable = ["signalRef", "messageRef", "errorRef"];
                for (let i = 0; i < propertiesThatMustBeEnumerable.length; i++) {
                    const propertyName = propertiesThatMustBeEnumerable[i];
                    if (eventDefinition[propertyName]) {
                        Object.defineProperty(eventDefinition, propertyName, {enumerable: true});
                    }
                }
            }
        } else if (bo.$type === 'bpmn:Participant') {
            Object.defineProperty(bo, 'processRef', {enumerable: true})
        }
        element.dispatchEvent(new ElementSelectionBpmEvent(bo.$type, bo))
    }
    //some properties must be marked as enumerable, otherwise they are not serialized to JSON
}