/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

export class ElementSelectionBpmEvent extends Event {
    public constructor(type: string, businessObjectJson: any) {
        super("ElementSelectionBpmEvent");
        this.$type = type;
        this.businessObject = JSON.stringify(businessObjectJson);
    }

    public businessObject: string;

    public $type: string;
}

export class XmlImportCompleteEvent extends Event {
    public processDefinitionsJson: string;

    public constructor(processDefinitionsJson: string) {
        super("xml-import-completed");
        this.processDefinitionsJson = processDefinitionsJson;
    }
}