/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */
 
import Viewer from 'bpmn-js/lib/Viewer';
import inherits from 'inherits';
import BpmDrawing from './js/features/bpm-drawing';
import BpmRenderer from './js/features/bpm-renderer';
import KeyboardMoveModule from 'diagram-js/lib/navigation/keyboard-move';
import MoveCanvasModule from 'diagram-js/lib/navigation/movecanvas';
import ZoomScrollModule from 'diagram-js/lib/navigation/zoomscroll';

export default function BpmViewer(options) {
    Viewer.call(this, options);
}

inherits(BpmViewer, Viewer);

BpmViewer.prototype._customModules = [
    BpmDrawing,
    BpmRenderer,
    KeyboardMoveModule,
    MoveCanvasModule,
    ZoomScrollModule
];

BpmViewer.prototype._modules = [].concat(
    Viewer.prototype._modules,
    BpmViewer.prototype._customModules
);