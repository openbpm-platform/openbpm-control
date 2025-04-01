import {css} from 'lit';

export const formViewerStyles = css`
    /**
 * Theming
 */

    .fjs-container {
        --color-grey-225-10-15: hsl(225, 10%, 15%);
        --color-grey-225-10-35: hsl(225, 10%, 35%);
        --color-grey-225-10-45: hsl(225, 10%, 45%);
        --color-grey-225-10-55: hsl(225, 10%, 55%);
        --color-grey-225-10-75: hsl(225, 10%, 75%);
        --color-grey-225-10-80: hsl(225, 10%, 80%);
        --color-grey-225-10-85: hsl(225, 10%, 85%);
        --color-grey-225-10-90: hsl(225, 10%, 90%);
        --color-grey-225-10-93: hsl(225, 10%, 93%);
        --color-grey-225-10-95: hsl(225, 10%, 95%);
        --color-grey-225-10-97: hsl(225, 10%, 97%);
        --color-grey-0-0-88: hsl(0, 0%, 88%);
        --color-blue-219-100-53: hsl(219, 99%, 53%);
        --color-blue-219-100-53-05: hsla(219, 99%, 53%, 0.5);
        --color-blue-205-100-40: hsl(205, 100%, 40%);
        --color-blue-205-100-45: hsl(205, 100%, 45%);
        --color-blue-205-100-50: hsl(205, 100%, 50%);
        --color-blue-205-100-80: hsl(205, 100%, 80%);
        --color-blue-205-100-95: hsl(205, 100%, 95%);
        --color-green-150-86-44: hsl(150, 86%, 44%);
        --color-red-360-100-40: hsl(360, 100%, 40%);
        --color-red-360-100-45: hsl(360, 100%, 45%);
        --color-red-360-100-92: hsl(360, 100%, 92%);
        --color-red-360-100-95: hsl(360, 100%, 95%);
        --color-red-360-100-97: hsl(360, 100%, 97%);
        --color-white: hsl(0, 0%, 100%);
        --color-black: hsl(0, 0%, 0%);
        /**
   * Specify color variables in the following schema:
   * 1 - use specified layer
   * 2 - use layer one
   * 3 - use fallback
   */
        --color-background: var(--cds-field, var(--cds-field-01, var(--color-white)));
        --color-background-disabled: var(--cds-background, var(--color-grey-225-10-95));
        --color-background-readonly: var(--cds-background, var(--color-grey-225-10-95));
        --color-background-adornment: var(--cds-field, var(--cds-field-01, var(--color-grey-225-10-95)));
        --color-background-inverted: var(--cds-background-inverse, var(--color-grey-225-10-90));
        --color-background-inverted-hover: var(--cds-background-inverse-hover, var(--color-grey-225-10-93));
        --color-background-active: var(--cds-background-active, var(--color-grey-225-10-75));
        --color-layer: var(--cds-layer, var(--cds-layer-01, var(--color-white)));
        --color-layer-accent: var(--cds-layer-accent, var(--color-grey-0-0-88));
        --color-background-pre: var(--cds-layer, var(--cds-layer-01, var(--color-white)));
        --color-icon-base: var(--cds-icon-primary, var(--color-black));
        --color-icon-inverted: var(--cds-icon-inverse, var(--color-black));
        --color-text: var(--cds-text-primary, var(--color-grey-225-10-15));
        --color-text-light: var(--cds-text-secondary, var(--color-grey-225-10-35));
        --color-text-lighter: var(--cds-text-secondary, var(--color-grey-225-10-45));
        --color-text-lightest: var(--cds-text-placeholder, var(--color-grey-225-10-55));
        --color-text-inverted: var(--cds-text-inverse, var(--color-text));
        --color-text-disabled: var(--cds-text-disabled, var(--color-text-light));
        --color-borders: var(--cds-border-strong, var(--cds-border-strong-01, var(--color-grey-225-10-55)));
        --color-borders-group: var(--cds-border-subtle, var(--color-grey-225-10-85));
        --color-borders-table: var(--color-borders-group);
        --color-borders-disabled: var(--cds-border-disabled, var(--color-grey-225-10-75));
        --color-borders-adornment: var(--cds-border-subtle, var(--cds-border-subtle-01, var(--color-grey-225-10-85)));
        --color-borders-readonly: var(--cds-border-subtle, var(--color-grey-225-10-75));
        --color-borders-inverted: var(--cds-border-inverse, var(--color-grey-225-10-90));
        --color-borders-pre: var(--cds-border-subtle, var(--color-grey-225-10-85));
        --color-warning: var(--cds-text-error, var(--color-red-360-100-45));
        --color-warning-light: var(--cds-text-error, var(--color-red-360-100-92));
        --color-accent: var(--cds-link-primary, var(--color-blue-205-100-40));
        --color-accent-readonly: var(--cds-border-strong, var(--cds-border-strong-01, var(--color-grey-225-10-55)));
        --color-datepicker-focused-day: var(--cds-button-primary, var(--color-grey-225-10-55));
        --color-shadow: var(--cds-shadow, var(--color-grey-225-10-85));
        --font-family: "IBM Plex Sans", sans-serif;
        --font-size-group: 15px;
        --font-size-base: 14px;
        --font-size-input: 14px;
        --font-size-label: 12px;
        --line-height-base: 20px;
        --line-height-input: 18px;
        --line-height-label: 16px;
        --letter-spacing-base: 0.16px;
        --letter-spacing-input: 0.16px;
        --letter-spacing-label: 0.32px;
        --form-field-height: 36px;
        --border-definition: 1px solid var(--color-borders);
        --border-definition-adornment: 1px solid var(--color-borders-adornment);
        --outline-definition: 1px solid var(--cds-focus, var(--color-borders));
        --button-warning-outline-definition: 2px solid var(--color-warning);
        --border-definition-disabled: 1px solid var(--color-borders-disabled);
        --border-definition-readonly: 1px solid var(--color-borders-readonly);
        height: 100%;
    }

    .fjs-no-theme {
        --cds-field: initial;
        --cds-field-01: initial;
        --cds-background: initial;
        --cds-background-inverse: initial;
        --cds-background-inverse-hover: initial;
        --cds-background-active: initial;
        --cds-layer: initial;
        --cds-layer-01: initial;
        --cds-layer-02: initial;
        --cds-icon-primary: initial;
        --cds-icon-secondary: initial;
        --cds-icon-inverse: initial;
        --cds-text-primary: initial;
        --cds-text-secondary: initial;
        --cds-text-placeholder: initial;
        --cds-text-inverse: initial;
        --cds-text-disabled: initial;
        --cds-border-strong: initial;
        --cds-border-strong-01: initial;
        --cds-border-disabled: initial;
        --cds-border-subtle: initial;
        --cds-border-subtle-01: initial;
        --cds-border-inverse: initial;
        --cds-text-error: initial;
        --cds-link-primary: initial;
        --cds-button-primary: initial;
        --cds-shadow: initial;
        --cds-focus: initial;
        --cds-interactive: initial;
        --cds-border-interactive: initial;
        --cds-highlight: initial;
        --cds-field-02: initial;
        --cds-field-hover-02: initial;
        --cds-toggle-off: initial;
    }

    /**
 * Layout styles
 */

    .fjs-container .fjs-vertical-layout {
        display: flex;
        flex-direction: column;
    }

    .fjs-container .fjs-columns {
        display: flex;
        flex-direction: row;
    }

    .fjs-container .fjs-vertical-group {
        display: flex;
        flex: 1;
        width: 100%;
    }

    .fjs-container .fjs-layout-row {
        flex: auto;
        padding: 9px 0;
        position: relative;
    }

    .fjs-container .fjs-layout-row:empty {
        display: none;
    }

    .fjs-container .fjs-column {
        flex-grow: 1;
    }

    .fjs-container img {
        max-width: 100%;
    }

    .fjs-container .cds--grid {
        margin-left: initial;
        margin-right: initial;
    }

    .fjs-container .cds--grid .cds--row {
        align-items: start;
        margin-left: 0;
        margin-right: 0;
    }

    .fjs-container .cds--grid .cds--grid .cds--row {
        margin-left: -1rem;
        margin-right: -1rem;
    }

    @media (max-width: 66rem) {
        .cds--col {
            flex-basis: unset !important;
        }
    }
    /**
 * Visual styles
 */

    .fjs-container .fjs-form {
        font-family: var(--font-family);
        font-size: var(--font-size-base);
        line-height: var(--line-height-base);
        letter-spacing: var(--letter-spacing-base);
        font-weight: 400;
        color: var(--color-text);
        position: relative;
        padding: 0 4px;
    }

    .fjs-container .fjs-form * {
        box-sizing: border-box;
    }

    .fjs-container .fjs-children {
        display: flex;
        flex-direction: column;
        flex-grow: 1;
        min-height: 80px;
    }

    .fjs-container .fjs-element {
        display: flex;
        flex-direction: column;
        margin: 0 4px 5px 4px;
        padding: 2px 0px;
    }

    .fjs-container .fjs-form .fjs-element {
        margin: 0;
    }

    .fjs-container .fjs-element:first-of-type {
        margin-top: 8px;
    }

    .fjs-container .fjs-form-field:not(.fjs-powered-by) {
        margin: 1px 8px;
    }

    .fjs-container .fjs-powered-by {
        margin: 16px;
    }

    .fjs-container .fjs-form .fjs-element {
        margin: 0;
    }

    .fjs-container .fjs-input,
    .fjs-container .fjs-taglist-input,
    .fjs-container .fjs-textarea,
    .fjs-container .fjs-select {
        color: var(--color-text);
        border-color: var(--color-borders);
        background-color: var(--color-background);
        font-family: inherit;
        line-height: inherit;
        letter-spacing: inherit;
    }

    .fjs-container .fjs-button {
        font-family: inherit;
        line-height: inherit;
        letter-spacing: inherit;
    }

    .fjs-container .fjs-input::placeholder,
    .fjs-container .fjs-taglist-input::placeholder,
    .fjs-container .fjs-textarea::placeholder,
    .fjs-container .fjs-select > option:disabled,
    .fjs-container .fjs-select [disabled],
    .fjs-container .fjs-select > option:read-only,
    .fjs-container .fjs-select [read-only] {
        color: var(--color-text-lightest);
        font-size: var(--font-size-input);
        line-height: var(--line-height-input);
        letter-spacing: var(--letter-spacing-input);
        opacity: 1;
    }

    .fjs-container .fjs-form-field-label {
        display: flex;
        align-items: center;
        white-space: nowrap;
        overflow: hidden;
        color: var(--color-text-light);
    }

    .fjs-container .fjs-incollapsible-label {
        min-height: var(--line-height-label);
    }

    .fjs-container .fjs-form-field-label,
    .fjs-container .fjs-form-field-description,
    .fjs-container .fjs-form-field-error {
        font-size: var(--font-size-label);
        line-height: var(--line-height-label);
        letter-spacing: var(--letter-spacing-label);
    }

    .fjs-container .fjs-form-field-description {
        color: var(--color-text-lighter);
    }

    .fjs-container .fjs-form-field-grouplike {
        padding: 10px 6px 0 6px;
        margin: 0 10px;
    }

    .fjs-container .fjs-form-field-grouplike .cds--grid {
        padding: 4px 16px;
    }

    .fjs-container .fjs-form-field-grouplike .fjs-form-field-grouplike .fjs-layout-column:first-child > .fjs-element > .fjs-form-field-grouplike:not(.fjs-editor-container .fjs-form-field-grouplike),
    .fjs-container .fjs-layout-column:first-child > .fjs-element > .fjs-form-field-grouplike:not(.fjs-editor-container .fjs-form-field-grouplike) {
        margin-left: -6px;
    }

    .fjs-container .fjs-form-field-grouplike .fjs-form-field-grouplike .fjs-layout-column:last-child > .fjs-element > .fjs-form-field-grouplike:not(.fjs-editor-container .fjs-form-field-grouplike),
    .fjs-container .fjs-layout-column:last-child > .fjs-element > .fjs-form-field-grouplike:not(.fjs-editor-container .fjs-form-field-grouplike) {
        margin-right: -6px;
    }

    .fjs-container .fjs-form-field-grouplike .fjs-layout-column:first-child > .fjs-element > .fjs-form-field-grouplike:not(.fjs-editor-container .fjs-form-field-grouplike) {
        margin-left: 11px;
    }

    .fjs-container .fjs-form-field-grouplike .fjs-layout-column:last-child > .fjs-element > .fjs-form-field-grouplike:not(.fjs-editor-container .fjs-form-field-grouplike) {
        margin-right: 11px;
    }

    .fjs-container .fjs-form-field-grouplike.fjs-outlined {
        outline: solid var(--color-borders-group) 2px;
    }

    .fjs-container .fjs-form-field-grouplike label {
        font-size: var(--font-size-label);
    }

    .fjs-container .fjs-form-field-grouplike .fjs-form-field-grouplike .cds--grid {
        padding-left: 2rem;
        padding-right: 2rem;
    }

    .fjs-container .fjs-form-field-grouplike > label {
        font-size: var(--font-size-group);
        line-height: var(--line-height-input);
        margin-left: 7px;
    }

    .fjs-container .fjs-form-field-checkbox .fjs-form-field-label,
    .fjs-container .fjs-form-field-checklist .fjs-form-field-label:not(:first-child),
    .fjs-container .fjs-form-field-radio .fjs-form-field-label:not(:first-child) {
        font-size: var(--font-size-input);
        line-height: var(--line-height-input);
        letter-spacing: var(--letter-spacing-input);
        color: var(--color-text);
    }

    .fjs-container .fjs-form-field-checklist .fjs-form-field-label:first-child,
    .fjs-container .fjs-form-field-radio .fjs-form-field-label:first-child {
        margin-bottom: 4px;
    }

    .fjs-container .fjs-form-field.required label::after,
    .fjs-container .fjs-form-field.required .group-title::after {
        content: "*";
    }

    .fjs-container .fjs-checkbox.group.required label::after,
    .fjs-container .fjs-radio.required label::after {
        display: none;
    }

    .fjs-container .fjs-input,
    .fjs-container .fjs-textarea,
    .fjs-container .fjs-select {
        background-color: var(--color-background);
        font-size: var(--font-size-input);
        line-height: var(--line-height-input);
        letter-spacing: var(--letter-spacing-input);
    }

    .fjs-container .fjs-input-adornment {
        border-style: solid;
        border-color: var(--color-borders-adornment);
        background-color: var(--color-background-adornment);
        line-height: var(--line-height-input);
        color: var(--color-text-lighter);
        padding: 8px;
        width: auto !important;
        min-width: min(34px, 20%);
        display: flex;
        overflow: hidden;
    }

    .fjs-container .fjs-input-adornment span {
        width: 100%;
        text-align: center;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .fjs-container .fjs-input-adornment svg {
        margin: auto;
    }

    .fjs-container .fjs-form-field-spacer {
        background-color: transparent;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .fjs-container hr {
        border: none;
        border-top: solid 2px var(--color-borders-group);
    }

    .fjs-container .fjs-form-field-separator hr {
        margin: 7px 2px;
    }

    .fjs-container .fjs-input[type=text],
    .fjs-container .fjs-input[type=email],
    .fjs-container .fjs-input[type=tel],
    .fjs-container .fjs-input[type=number],
    .fjs-container .fjs-button[type=submit],
    .fjs-container .fjs-button[type=reset],
    .fjs-container .fjs-textarea,
    .fjs-container .fjs-select {
        display: block;
        width: 100%;
        padding: 8px;
        margin: 4px 0;
        border: var(--border-definition);
        border-radius: 3px;
    }

    .fjs-container .fjs-input-group .fjs-input[type=text],
    .fjs-container .fjs-input-group .fjs-input[type=email],
    .fjs-container .fjs-input-group .fjs-input[type=tel],
    .fjs-container .fjs-input-group .fjs-input[type=number],
    .fjs-container .fjs-select {
        height: var(--form-field-height);
    }

    .fjs-container .fjs-taglist {
        min-height: var(--form-field-height);
    }

    .fjs-container .fjs-datepicker,
    .fjs-container .fjs-timepicker {
        width: 100%;
    }

    .fjs-container .fjs-datetime-subsection {
        min-width: 0;
        flex: 1;
    }

    .fjs-container .fjs-datetime-separator {
        width: 10px;
    }

    .fjs-container .fjs-timepicker-anchor {
        height: 100%;
        display: flex;
    }

    .fjs-container .fjs-form-field-datetime .fjs-input-adornment svg {
        color: var(--color-icon-base);
    }

    .fjs-container .fjs-input-group {
        display: flex;
        width: 100%;
        height: var(--form-field-height);
        margin: 4px 0;
        border: var(--border-definition);
        border-radius: 3px;
    }

    .fjs-container .fjs-input-group .fjs-input[type=text],
    .fjs-container .fjs-input-group .fjs-input[type=email],
    .fjs-container .fjs-input-group .fjs-input[type=tel],
    .fjs-container .fjs-input-group .fjs-input[type=number],
    .fjs-container .fjs-input-group .fjs-taglist,
    .fjs-container .fjs-input-group .fjs-select {
        height: unset;
        min-width: min(60px, 40%);
        width: 100%;
    }

    .fjs-container .fjs-taglist .fjs-taglist-input,
    .fjs-container .fjs-input-group .fjs-input {
        border: none;
        border-radius: 0;
        margin: 0;
        outline: 0;
        width: auto;
        flex: 1;
    }

    .fjs-container .fjs-input-group .fjs-select-display {
        line-height: var(--line-height-base);
        user-select: none;
        padding: 8px;
        flex: 1;
    }

    .fjs-container .fjs-input-group .fjs-select-display.fjs-select-placeholder,
    .fjs-container .fjs-disabled .fjs-input-group .fjs-select-display,
    .fjs-container .fjs-readonly .fjs-input-group .fjs-select-display {
        color: var(--color-text-lighter);
        line-height: var(--line-height-input);
    }

    .fjs-container .fjs-input-group .fjs-input-adornment {
        border-width: 0;
    }

    .fjs-container .fjs-input-group .fjs-input-adornment.border-left {
        border-left-width: 1px;
    }

    .fjs-container .fjs-input-group .fjs-input-adornment.border-right {
        border-right-width: 1px;
    }

    .fjs-container .fjs-input-group .fjs-input-adornment.border-radius-right {
        border-radius: 0 3px 3px 0;
    }

    .fjs-container .fjs-input-group .fjs-input-adornment.border-radius-left {
        border-radius: 3px 0 0 3px;
    }

    .fjs-container .fjs-input-group .fjs-input {
        border-radius: 3px;
    }

    .fjs-container .flatpickr-wrapper {
        width: 100%;
        height: 100%;
        display: flex;
        position: relative;
    }

    .fjs-container .fjs-textarea {
        resize: none;
        overflow: hidden;
        height: 75px;
    }

    .fjs-container .fjs-select-cross,
    .fjs-container .fjs-select-arrow {
        padding: 6px 10px;
    }

    .fjs-container .fjs-number-arrow-container {
        display: flex;
        flex-direction: column;
        border-radius: 0 2px 2px 0;
        width: clamp(16px, 40%, 23px);
        overflow: clip;
        border-left: var(--border-definition-adornment);
    }

    .fjs-container .fjs-number-arrow-separator {
        height: 1px;
        background-color: var(--color-borders-adornment);
    }

    .fjs-container .fjs-number-arrow-container button {
        border: none;
        flex: 1;
        color: var(--color-text);
        background-color: var(--color-background);
        font-weight: bold;
        font-size: 10px;
        line-height: 15px;
    }

    .fjs-container .fjs-number-arrow-container button:hover {
        background-color: var(--color-background-inverted-hover);
        color: var(--cds-text-inverse, var(--color-text));
    }

    .fjs-container .fjs-form-field-number.fjs-readonly .fjs-number-arrow-container {
        display: none;
    }

    .fjs-container .fjs-radio {
        display: flex;
        flex-direction: column;
    }

    .fjs-container .fjs-input[type=checkbox],
    .fjs-container .fjs-input[type=radio] {
        margin: 6px 10px 6px 4px;
    }

    .fjs-container .fjs-button[type=submit] {
        color: var(--cds-text-inverse, var(--color-white));
        background-color: var(--color-accent);
        border-color: var(--color-accent);
    }

    .fjs-container .fjs-button[type=reset] {
        color: var(--color-text);
        background-color: transparent;
        border-color: var(--color-borders);
    }

    .fjs-container .fjs-button[type=submit],
    .fjs-container .fjs-button[type=reset] {
        min-width: 100px;
        width: auto;
    }

    .fjs-container .fjs-button[type=submit] {
        font-weight: 600;
    }

    .fjs-container .fjs-taglist:focus-within,
    .fjs-container .fjs-input-group:focus-within,
    .fjs-container .fjs-input[type=text]:focus,
    .fjs-container .fjs-input[type=email]:focus,
    .fjs-container .fjs-input[type=tel]:focus,
    .fjs-container .fjs-input[type=number]:focus,
    .fjs-container .fjs-button[type=submit]:focus,
    .fjs-container .fjs-button[type=reset]:focus,
    .fjs-container .fjs-textarea:focus,
    .fjs-container .fjs-select:focus {
        outline: var(--outline-definition);
    }

    .fjs-container .fjs-input-group .fjs-input,
    .fjs-container .fjs-input-group .fjs-input:focus {
        outline: none;
    }

    .fjs-container .fjs-input-group .fjs-input,
    .fjs-container .fjs-input-group .fjs-input:focus {
        outline: none;
    }

    .fjs-container .fjs-button[type=submit]:focus {
        border-color: var(--color-accent);
    }

    .fjs-container .fjs-input:disabled,
    .fjs-container .fjs-textarea:disabled,
    .fjs-container .fjs-select:disabled,
    .fjs-container .fjs-number-arrow-container.fjs-disabled button,
    .fjs-container .fjs-taglist.fjs-disabled,
    .fjs-container .fjs-disabled .fjs-input-group {
        color: var(--color-text-disabled);
        background-color: var(--cds-field, var(--color-background-disabled));
        border-color: var(--color-borders-disabled);
    }

    .fjs-container .fjs-number-arrow-container.fjs-disabled button {
        background-color: var(--cds-toggle-off, var(--color-background-disabled));
    }

    .fjs-container .fjs-readonly .fjs-input:read-only:not(:disabled),
    .fjs-container .fjs-readonly .fjs-textarea:read-only:not(:disabled),
    .fjs-container .fjs-readonly .fjs-select:read-only:not(:disabled),
    .fjs-container .fjs-number-arrow-container.fjs-readonly button,
    .fjs-container .fjs-taglist.fjs-readonly,
    .fjs-container .fjs-readonly .fjs-input-group {
        background-color: var(--color-background-readonly);
        border-color: var(--color-borders-readonly);
    }

    .fjs-container .fjs-form-field-checkbox.fjs-readonly,
    .fjs-container .fjs-form-field-checklist.fjs-readonly,
    .fjs-container .fjs-form-field-radio.fjs-readonly {
        pointer-events: none;
    }

    .fjs-container .fjs-form-field-checkbox.fjs-readonly .fjs-input:not(:disabled),
    .fjs-container .fjs-form-field-checklist.fjs-readonly .fjs-input:not(:disabled),
    .fjs-container .fjs-form-field-radio.fjs-readonly .fjs-input:not(:disabled) {
        opacity: 0.4;
        accent-color: var(--color-accent-readonly);
    }

    .fjs-container .fjs-button[type=submit]:disabled,
    .fjs-container .fjs-button[type=reset]:disabled {
        color: var(--cds-text-on-color-disabled, var(--color-text-light));
        background-color: var(--color-background-disabled);
        border-color: var(--color-borders-disabled);
    }

    .fjs-container .fjs-button[type=submit]:read-only,
    .fjs-container .fjs-button[type=reset]:read-only {
        color: var(--text-light);
        background-color: var(--color-background-readonly);
        border-color: var(--color-borders-readonly);
    }

    .fjs-container .fjs-disabled .fjs-input-group .fjs-input-adornment,
    .fjs-container .fjs-disabled .fjs-input-group .fjs-number-arrow-container,
    .fjs-container .fjs-disabled .fjs-input-group .fjs-number-arrow-separator {
        border-color: var(--color-borders-disabled);
    }

    .fjs-container .fjs-disabled .fjs-input-group .fjs-number-arrow-container .fjs-number-arrow-up,
    .fjs-container .fjs-disabled .fjs-input-group .fjs-number-arrow-container .fjs-number-arrow-down {
        pointer-events: none;
    }

    .fjs-container .fjs-readonly .fjs-input-group .fjs-input-adornment,
    .fjs-container .fjs-readonly .fjs-input-group .fjs-number-arrow-container,
    .fjs-container .fjs-readonly .fjs-input-group .fjs-number-arrow-separator {
        border-color: var(--color-borders-readonly);
    }

    .fjs-container .fjs-form-field.fjs-has-errors .fjs-input,
    .fjs-container .fjs-form-field.fjs-has-errors .fjs-select,
    .fjs-container .fjs-form-field.fjs-has-errors .fjs-textarea,
    .fjs-container .fjs-form-field.fjs-has-errors .fjs-taglist,
    .fjs-container .fjs-form-field.fjs-has-errors .fjs-input-group,
    .fjs-container .fjs-form-field.fjs-has-errors .fjs-input-group .fjs-input {
        border-color: var(--color-warning);
        outline-color: var(--color-warning);
    }

    .fjs-container .fjs-form-field.fjs-has-errors .fjs-number-arrow-container {
        border-color: var(--color-red-360-100-92);
    }

    .fjs-container .fjs-form-field.fjs-has-errors .fjs-number-arrow-separator {
        background-color: var(--color-red-360-100-92);
    }

    .fjs-container .fjs-form-field.fjs-has-errors .fjs-number-arrow-container button {
        background-color: var(--color-red-360-100-97);
    }

    .fjs-container .fjs-form-field.fjs-has-errors .fjs-number-arrow-container button:hover {
        background-color: var(--color-red-360-100-95);
    }

    .fjs-container .fjs-form-field.fjs-has-errors .fjs-input-adornment {
        border-color: var(--color-warning-light);
    }

    .fjs-container .fjs-form-field-error {
        color: var(--color-warning);
    }

    .fjs-container .fjs-form-field-error > ul {
        list-style-type: none;
        margin: 0;
        padding: 0;
    }

    .fjs-container .fjs-form-field-error > ul > li {
        list-style-type: none;
    }

    .fjs-container .fjs-form-field-html,
    .fjs-container .fjs-form-field-text {
        overflow-wrap: break-word;
    }

    .fjs-container .fjs-form-field-html a,
    .fjs-container .fjs-form-field-text a {
        color: var(--color-accent);
    }

    .fjs-container .fjs-form-field-html .fjs-disabled-link,
    .fjs-container .fjs-form-field-text .fjs-disabled-link {
        pointer-events: none;
        cursor: default;
    }

    .fjs-container .fjs-form-field-html table,
    .fjs-container .fjs-form-field-text table {
        border-collapse: collapse;
        width: auto;
    }

    .fjs-container .fjs-form-field-html table,
    .fjs-container .fjs-form-field-html th,
    .fjs-container .fjs-form-field-html td,
    .fjs-container .fjs-form-field-text table,
    .fjs-container .fjs-form-field-text th,
    .fjs-container .fjs-form-field-text td {
        border: 1px solid var(--color-borders-table);
        padding: 8px;
    }

    .fjs-container .fjs-form-field-html th,
    .fjs-container .fjs-form-field-text th {
        font-weight: bold;
    }

    .fjs-container .fjs-form-field-html td,
    .fjs-container .fjs-form-field-text td {
        white-space: nowrap;
    }

    .fjs-container .fjs-form-field-html pre,
    .fjs-container .fjs-form-field-text pre {
        border: 1px solid var(--color-borders-pre);
        background: var(--color-background-pre);
        padding: 4px;
    }

    .fjs-container .fjs-taglist-anchor,
    .fjs-container .fjs-select-anchor,
    .fjs-container .fjs-timepicker-anchor {
        position: relative;
    }

    .fjs-container .fjs-taglist-anchor .fjs-dropdownlist,
    .fjs-container .fjs-select-anchor .fjs-dropdownlist {
        position: absolute;
        top: -5px;
    }

    .fjs-container .fjs-select-hidden-input {
        width: 0;
        overflow: hidden;
        opacity: 0;
        padding: 0;
        border: none;
    }

    .fjs-container .fjs-taglist {
        display: flex;
        flex-wrap: wrap;
        gap: 5px;
        border: var(--color-borders) solid 1px;
        border-radius: 3px;
        margin: 4px 0;
        padding: 5px;
        background-color: var(--color-background);
    }

    .fjs-container .fjs-taglist-skip-link {
        outline: none;
        border: none;
        background-color: transparent;
        color: transparent;
        height: 0px;
        width: 0px;
        position: absolute;
        left: -100px;
    }

    .fjs-container .fjs-taglist-skip-link:focus {
        position: relative;
        height: auto;
        width: auto;
        border: solid 1px var(--color-accent);
        color: var(--color-accent);
        left: 0;
    }

    .fjs-container .fjs-taglist .fjs-taglist-tag {
        display: flex;
        overflow: hidden;
        user-select: none;
        color: var(--color-text-inverted);
        background-color: var(--color-background-inverted);
        border-radius: 2px;
    }

    .fjs-container .fjs-taglist.fjs-disabled .fjs-taglist-tag,
    .fjs-container .fjs-taglist.fjs-readonly .fjs-taglist-tag {
        background-color: var(--color-background-inverted);
    }

    .fjs-container .fjs-taglist .fjs-taglist-tags {
        display: flex;
        flex-wrap: wrap;
        gap: 5px;
    }

    .fjs-container .fjs-taglist .fjs-taglist-tag .fjs-taglist-tag-label {
        padding: 2px 6px 2px 8px;
    }

    .fjs-container .fjs-taglist.fjs-disabled .fjs-taglist-tag .fjs-taglist-tag-label,
    .fjs-container .fjs-taglist.fjs-readonly .fjs-taglist-tag .fjs-taglist-tag-label {
        padding: 2px 8px;
    }

    .fjs-container .fjs-taglist .fjs-taglist-tag .fjs-taglist-tag-remove {
        overflow: clip;
        width: 24px;
        height: 24px;
        text-align: center;
        line-height: 28px;
        background-color: var(--cds-background-inverse, var(--color-grey-225-10-80));
        border: none;
        padding: 1px 0;
    }

    .fjs-container .fjs-taglist .fjs-taglist-tag .fjs-taglist-tag-remove > svg {
        margin-bottom: 4px;
        opacity: 0.6;
        color: var(--color-icon-inverted);
    }

    .fjs-container .fjs-taglist .fjs-taglist-tag .fjs-taglist-tag-remove:hover,
    .fjs-container .fjs-taglist .fjs-taglist-tag .fjs-taglist-tag-remove:focus-visible {
        background-color: var(--color-background-active);
    }

    .fjs-container .fjs-taglist .fjs-taglist-tag .fjs-taglist-tag-remove:hover > svg,
    .fjs-container .fjs-taglist .fjs-taglist-tag .fjs-taglist-tag-remove:focus-visible > svg {
        opacity: 1;
    }

    .fjs-container .fjs-taglist .fjs-taglist-tag .fjs-taglist-tag-remove:focus-visible {
        outline: none;
    }

    .fjs-container .fjs-taglist .fjs-taglist-input {
        border: none;
        background-color: transparent;
        min-width: 100px;
        height: 22px;
        margin: 1px 0;
        flex-grow: 1;
    }

    .fjs-container .fjs-dropdownlist {
        z-index: 1;
        position: absolute;
        user-select: none;
        overflow-y: auto;
        width: 100%;
        border-radius: 3px;
        margin-top: 3px;
        box-shadow: 0px 0px 5px var(--color-shadow);
        background-color: var(--color-layer);
    }

    .fjs-container .fjs-dropdownlist .fjs-dropdownlist-item {
        padding: 6px 8px;
        border-bottom: 1px solid var(--color-borders-inverted);
        color: var(--color-text-light);
    }

    .fjs-container .fjs-dropdownlist .fjs-dropdownlist-item:last-child {
        border-bottom: none;
    }

    .fjs-container .fjs-dropdownlist .fjs-dropdownlist-item.focused {
        background-color: var(--color-background-inverted);
        color: var(--color-text-inverted);
    }

    .fjs-container .fjs-dropdownlist .fjs-dropdownlist-empty {
        padding: 6px 8px;
        color: var(--color-text-lightest);
    }

    .fjs-container .fjs-image-container {
        width: fit-content;
        height: fit-content;
    }

    .fjs-container .fjs-image {
        object-fit: contain;
    }

    .fjs-container .fjs-iframe {
        margin: 4px 0;
        width: 100%;
        border: 1px solid var(--color-borders-readonly);
    }

    .fjs-container .fjs-image-placeholder,
    .fjs-container .fjs-iframe-placeholder {
        margin: 4px 0;
        width: 100%;
        height: 90px;
        display: flex;
        justify-content: center;
        background: var(--color-background-readonly);
        color: var(--color-text-light);
    }

    .fjs-container .fjs-iframe-placeholder {
        border: 1px solid var(--color-borders-readonly);
    }

    .fjs-container .fjs-image-placeholder .fjs-image-placeholder-inner,
    .fjs-container .fjs-iframe-placeholder .fjs-iframe-placeholder-text {
        display: flex;
        align-items: center;
        justify-content: center;
        flex-wrap: wrap;
        overflow: hidden;
    }

    .fjs-container .fjs-iframe-placeholder .fjs-iframe-placeholder-text {
        font-size: var(--font-size-label);
    }

    .fjs-container .fjs-form-field-table {
        display: flex;
        flex-direction: column;
        row-gap: 4px;
    }

    .fjs-container .fjs-table-middle-container {
        display: flex;
        flex-direction: column;
        overflow-x: hidden;
        border: 1px solid var(--color-borders-group);
        border-radius: 3px;
    }

    .fjs-container .fjs-table-middle-container.fjs-table-empty {
        border: none;
        color: var(--color-text-disabled);
        padding-left: 16px;
    }

    .fjs-container .fjs-table-inner-container {
        display: flex;
        flex-direction: column;
        overflow-x: auto;
    }

    .fjs-container .fjs-table {
        overflow-y: auto;
        border-collapse: collapse;
    }

    .fjs-container .fjs-table-head {
        background-color: var(--color-layer-accent);
    }

    .fjs-container .fjs-table-th {
        min-width: 120px;
        cursor: pointer;
    }

    .fjs-container .fjs-table-th-label {
        user-select: none;
        display: flex;
        align-items: center;
        flex-direction: row;
        justify-content: space-between;
    }

    .fjs-container .fjs-table-th:focus {
        outline: var(--outline-definition);
        outline-offset: -1px;
    }

    .fjs-container .fjs-table-th,
    .fjs-container .fjs-table-td {
        text-align: left;
        height: 32px;
        padding: 0 16px;
        vertical-align: center;
    }

    .fjs-container .fjs-table-body .fjs-table-tr:not(:last-child) {
        border-bottom: 1px solid var(--color-borders-group);
    }

    .fjs-container .fjs-table-nav {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: flex-end;
        border-top: 1px solid var(--color-borders-group);
    }

    .fjs-container .fjs-table-nav-button {
        border: unset;
        background: unset;
        width: 32px;
        height: 32px;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        border-left: 1px solid var(--color-borders-group);
    }

    .fjs-container .fjs-table-nav-button:first-of-type {
        margin-left: 16px;
    }

    .fjs-container .fjs-table-nav-button:focus {
        outline: var(--outline-definition);
        outline-offset: -1px;
    }

    .fjs-container .fjs-table-nav-button svg {
        width: 16px;
    }

    .fjs-container .fjs-table-sort-icon-asc,
    .fjs-container .fjs-table-sort-icon-desc {
        width: 16px;
    }

    .fjs-container .fjs-repeat-row-container {
        display: flex;
        flex-direction: row;
        gap: 1rem;
    }

    .fjs-container .fjs-repeat-row-rows {
        flex: 1;
        margin-right: 1rem;
    }

    .fjs-container .fjs-repeat-row-container .fjs-repeat-row-remove {
        display: flex;
        cursor: pointer;
        background: transparent;
        border: none;
        width: 32px;
        color: var(--color-icon-base);
        align-items: center;
        justify-content: center;
        padding: 0;
    }

    .fjs-container .fjs-repeat-row-container .fjs-repeat-row-remove:focus-visible {
        outline: none;
    }

    .fjs-container .fjs-repeat-row-container .fjs-repeat-row-remove .fjs-repeat-row-remove-icon-container {
        display: flex;
        width: 24px;
        height: 24px;
        border-radius: 2px;
        align-items: center;
        justify-content: center;
    }

    .fjs-container .fjs-repeat-row-container .fjs-repeat-row-remove:focus-visible .fjs-repeat-row-remove-icon-container {
        outline: var(--button-warning-outline-definition);
    }

    .fjs-container .fjs-repeat-row-container:hover .fjs-repeat-render-footer-spacer {
        width: 24px;
    }

    .fjs-container .fjs-repeat-row-container .fjs-repeat-row-remove:hover,
    .fjs-container .fjs-repeat-row-container .fjs-repeat-row-remove:focus-visible {
        color: var(--color-warning);
    }

    .fjs-container .fjs-repeat-row-collapsed {
        display: none;
    }

    .fjs-container .fjs-repeat-render-footer {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: center;
        padding: 12px 4px;
    }

    .fjs-container .fjs-repeat-render-footer.fjs-remove-allowed {
        margin-right: 32px;
        justify-content: space-between;
    }

    .fjs-container .fjs-repeat-render-footer button {
        background: none;
        border: none;
        padding: 4px;
        margin: 0;
        font-family: inherit;
        font-size: inherit;
        cursor: pointer;
        color: var(--color-accent);
        display: flex;
        align-items: center;
    }

    .fjs-container .fjs-repeat-render-footer button svg {
        margin-right: 4px;
    }

    /**
* Flatpickr style adjustments
*/

    .fjs-container .flatpickr-day.today {
        border-color: transparent;
        background-color: transparent;
        font-weight: bold;
    }

    .fjs-container .flatpickr-day.today:hover,
    .fjs-container .flatpickr-day.today:focus {
        border-color: var(--color-borders);
        background-color: var(--color-datepicker-focused-day);
        color: var(--color-text-inverted);
    }

    .fjs-container .flatpickr-day.selected {
        border-color: var(--color-accent);
        background-color: inherit;
        color: inherit;
        font-weight: normal;
    }

    .fjs-container .flatpickr-day.selected.today {
        font-weight: bold;
    }

    .fjs-container .flatpickr-day.selected:hover,
    .fjs-container .flatpickr-day.selected:focus {
        background-color: var(--color-accent);
        font-weight: bold;
        color: var(--color-text-inverted);
        border-color: var(--color-accent);
    }

    .fjs-container .flatpickr-days,
    .flatpickr-weekdays {
        padding: 10px;
        width: 100%;
    }

    .fjs-container .flatpickr-calendar.hasTime.noCalendar {
        width: auto;
        max-width: 250px;
    }

    .fjs-container .flatpickr-calendar {
        width: 326px;
    }

    .cds--grid {
        margin-inline: auto;
        max-inline-size: 99rem;
        padding-inline: 1rem;
    }

    @media (min-width: 42rem) {
        .cds--grid {
            padding-inline: 2rem;
        }
    }
    @media (min-width: 99rem) {
        .cds--grid {
            padding-inline: 2.5rem;
        }
    }

    @media (min-width: 99rem) {
        .cds--grid--full-width {
            max-inline-size: 100%;
        }
    }

    .cds--row {
        display: flex;
        flex-wrap: wrap;
        margin-inline: -1rem;
    }

    .cds--row-padding [class*=cds--col],
    .cds--col-padding {
        padding-block: 1rem;
    }

    .cds--grid--condensed [class*=cds--col] {
        padding-block: 0.03125rem;
    }

    .cds--col {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col, .cds--grid--condensed .cds--col {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col, .cds--grid--narrow .cds--col {
        padding-inline: 0 1rem;
    }

    .cds--col-sm-0 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-sm-0, .cds--grid--condensed .cds--col-sm-0 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-sm-0, .cds--grid--narrow .cds--col-sm-0 {
        padding-inline: 0 1rem;
    }

    .cds--col-sm-1 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-sm-1, .cds--grid--condensed .cds--col-sm-1 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-sm-1, .cds--grid--narrow .cds--col-sm-1 {
        padding-inline: 0 1rem;
    }

    .cds--col-sm-2 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-sm-2, .cds--grid--condensed .cds--col-sm-2 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-sm-2, .cds--grid--narrow .cds--col-sm-2 {
        padding-inline: 0 1rem;
    }

    .cds--col-sm-3 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-sm-3, .cds--grid--condensed .cds--col-sm-3 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-sm-3, .cds--grid--narrow .cds--col-sm-3 {
        padding-inline: 0 1rem;
    }

    .cds--col-sm-4 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-sm-4, .cds--grid--condensed .cds--col-sm-4 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-sm-4, .cds--grid--narrow .cds--col-sm-4 {
        padding-inline: 0 1rem;
    }

    .cds--col-sm,
    .cds--col-sm--auto {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-sm, .cds--grid--condensed .cds--col-sm,
    .cds--row--condensed .cds--col-sm--auto,
    .cds--grid--condensed .cds--col-sm--auto {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-sm, .cds--grid--narrow .cds--col-sm,
    .cds--row--narrow .cds--col-sm--auto,
    .cds--grid--narrow .cds--col-sm--auto {
        padding-inline: 0 1rem;
    }

    .cds--col,
    .cds--col-sm {
        flex-basis: 0;
        flex-grow: 1;
        max-inline-size: 100%;
    }

    .cds--col--auto,
    .cds--col-sm--auto {
        flex: 1 0 0%;
        inline-size: auto;
        max-inline-size: 100%;
    }

    .cds--col-sm-0 {
        display: none;
    }

    .cds--col-sm-1 {
        display: block;
        flex: 0 0 25%;
        max-inline-size: 25%;
    }

    .cds--col-sm-2 {
        display: block;
        flex: 0 0 50%;
        max-inline-size: 50%;
    }

    .cds--col-sm-3 {
        display: block;
        flex: 0 0 75%;
        max-inline-size: 75%;
    }

    .cds--col-sm-4 {
        display: block;
        flex: 0 0 100%;
        max-inline-size: 100%;
    }

    .cds--offset-sm-0 {
        margin-inline-start: 0;
    }

    .cds--offset-sm-1 {
        margin-inline-start: 25%;
    }

    .cds--offset-sm-2 {
        margin-inline-start: 50%;
    }

    .cds--offset-sm-3 {
        margin-inline-start: 75%;
    }

    .cds--col-md-0 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-md-0, .cds--grid--condensed .cds--col-md-0 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-md-0, .cds--grid--narrow .cds--col-md-0 {
        padding-inline: 0 1rem;
    }

    .cds--col-md-1 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-md-1, .cds--grid--condensed .cds--col-md-1 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-md-1, .cds--grid--narrow .cds--col-md-1 {
        padding-inline: 0 1rem;
    }

    .cds--col-md-2 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-md-2, .cds--grid--condensed .cds--col-md-2 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-md-2, .cds--grid--narrow .cds--col-md-2 {
        padding-inline: 0 1rem;
    }

    .cds--col-md-3 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-md-3, .cds--grid--condensed .cds--col-md-3 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-md-3, .cds--grid--narrow .cds--col-md-3 {
        padding-inline: 0 1rem;
    }

    .cds--col-md-4 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-md-4, .cds--grid--condensed .cds--col-md-4 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-md-4, .cds--grid--narrow .cds--col-md-4 {
        padding-inline: 0 1rem;
    }

    .cds--col-md-5 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-md-5, .cds--grid--condensed .cds--col-md-5 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-md-5, .cds--grid--narrow .cds--col-md-5 {
        padding-inline: 0 1rem;
    }

    .cds--col-md-6 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-md-6, .cds--grid--condensed .cds--col-md-6 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-md-6, .cds--grid--narrow .cds--col-md-6 {
        padding-inline: 0 1rem;
    }

    .cds--col-md-7 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-md-7, .cds--grid--condensed .cds--col-md-7 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-md-7, .cds--grid--narrow .cds--col-md-7 {
        padding-inline: 0 1rem;
    }

    .cds--col-md-8 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-md-8, .cds--grid--condensed .cds--col-md-8 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-md-8, .cds--grid--narrow .cds--col-md-8 {
        padding-inline: 0 1rem;
    }

    .cds--col-md,
    .cds--col-md--auto {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-md, .cds--grid--condensed .cds--col-md,
    .cds--row--condensed .cds--col-md--auto,
    .cds--grid--condensed .cds--col-md--auto {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-md, .cds--grid--narrow .cds--col-md,
    .cds--row--narrow .cds--col-md--auto,
    .cds--grid--narrow .cds--col-md--auto {
        padding-inline: 0 1rem;
    }

    @media (min-width: 42rem) {
        .cds--col,
        .cds--col-md {
            flex-basis: 0;
            flex-grow: 1;
            max-inline-size: 100%;
        }

        .cds--col--auto,
        .cds--col-md--auto {
            flex: 1 0 0%;
            inline-size: auto;
            max-inline-size: 100%;
        }

        .cds--col-md-0 {
            display: none;
        }

        .cds--col-md-1 {
            display: block;
            flex: 0 0 12.5%;
            max-inline-size: 12.5%;
        }

        .cds--col-md-2 {
            display: block;
            flex: 0 0 25%;
            max-inline-size: 25%;
        }

        .cds--col-md-3 {
            display: block;
            flex: 0 0 37.5%;
            max-inline-size: 37.5%;
        }

        .cds--col-md-4 {
            display: block;
            flex: 0 0 50%;
            max-inline-size: 50%;
        }

        .cds--col-md-5 {
            display: block;
            flex: 0 0 62.5%;
            max-inline-size: 62.5%;
        }

        .cds--col-md-6 {
            display: block;
            flex: 0 0 75%;
            max-inline-size: 75%;
        }

        .cds--col-md-7 {
            display: block;
            flex: 0 0 87.5%;
            max-inline-size: 87.5%;
        }

        .cds--col-md-8 {
            display: block;
            flex: 0 0 100%;
            max-inline-size: 100%;
        }

        .cds--offset-md-0 {
            margin-inline-start: 0;
        }

        .cds--offset-md-1 {
            margin-inline-start: 12.5%;
        }

        .cds--offset-md-2 {
            margin-inline-start: 25%;
        }

        .cds--offset-md-3 {
            margin-inline-start: 37.5%;
        }

        .cds--offset-md-4 {
            margin-inline-start: 50%;
        }

        .cds--offset-md-5 {
            margin-inline-start: 62.5%;
        }

        .cds--offset-md-6 {
            margin-inline-start: 75%;
        }

        .cds--offset-md-7 {
            margin-inline-start: 87.5%;
        }
    }

    .cds--col-lg-0 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-lg-0, .cds--grid--condensed .cds--col-lg-0 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-lg-0, .cds--grid--narrow .cds--col-lg-0 {
        padding-inline: 0 1rem;
    }

    .cds--col-lg-1 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-lg-1, .cds--grid--condensed .cds--col-lg-1 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-lg-1, .cds--grid--narrow .cds--col-lg-1 {
        padding-inline: 0 1rem;
    }

    .cds--col-lg-2 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-lg-2, .cds--grid--condensed .cds--col-lg-2 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-lg-2, .cds--grid--narrow .cds--col-lg-2 {
        padding-inline: 0 1rem;
    }

    .cds--col-lg-3 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-lg-3, .cds--grid--condensed .cds--col-lg-3 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-lg-3, .cds--grid--narrow .cds--col-lg-3 {
        padding-inline: 0 1rem;
    }

    .cds--col-lg-4 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-lg-4, .cds--grid--condensed .cds--col-lg-4 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-lg-4, .cds--grid--narrow .cds--col-lg-4 {
        padding-inline: 0 1rem;
    }

    .cds--col-lg-5 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-lg-5, .cds--grid--condensed .cds--col-lg-5 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-lg-5, .cds--grid--narrow .cds--col-lg-5 {
        padding-inline: 0 1rem;
    }

    .cds--col-lg-6 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-lg-6, .cds--grid--condensed .cds--col-lg-6 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-lg-6, .cds--grid--narrow .cds--col-lg-6 {
        padding-inline: 0 1rem;
    }

    .cds--col-lg-7 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-lg-7, .cds--grid--condensed .cds--col-lg-7 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-lg-7, .cds--grid--narrow .cds--col-lg-7 {
        padding-inline: 0 1rem;
    }

    .cds--col-lg-8 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-lg-8, .cds--grid--condensed .cds--col-lg-8 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-lg-8, .cds--grid--narrow .cds--col-lg-8 {
        padding-inline: 0 1rem;
    }

    .cds--col-lg-9 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-lg-9, .cds--grid--condensed .cds--col-lg-9 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-lg-9, .cds--grid--narrow .cds--col-lg-9 {
        padding-inline: 0 1rem;
    }

    .cds--col-lg-10 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-lg-10, .cds--grid--condensed .cds--col-lg-10 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-lg-10, .cds--grid--narrow .cds--col-lg-10 {
        padding-inline: 0 1rem;
    }

    .cds--col-lg-11 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-lg-11, .cds--grid--condensed .cds--col-lg-11 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-lg-11, .cds--grid--narrow .cds--col-lg-11 {
        padding-inline: 0 1rem;
    }

    .cds--col-lg-12 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-lg-12, .cds--grid--condensed .cds--col-lg-12 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-lg-12, .cds--grid--narrow .cds--col-lg-12 {
        padding-inline: 0 1rem;
    }

    .cds--col-lg-13 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-lg-13, .cds--grid--condensed .cds--col-lg-13 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-lg-13, .cds--grid--narrow .cds--col-lg-13 {
        padding-inline: 0 1rem;
    }

    .cds--col-lg-14 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-lg-14, .cds--grid--condensed .cds--col-lg-14 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-lg-14, .cds--grid--narrow .cds--col-lg-14 {
        padding-inline: 0 1rem;
    }

    .cds--col-lg-15 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-lg-15, .cds--grid--condensed .cds--col-lg-15 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-lg-15, .cds--grid--narrow .cds--col-lg-15 {
        padding-inline: 0 1rem;
    }

    .cds--col-lg-16 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-lg-16, .cds--grid--condensed .cds--col-lg-16 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-lg-16, .cds--grid--narrow .cds--col-lg-16 {
        padding-inline: 0 1rem;
    }

    .cds--col-lg,
    .cds--col-lg--auto {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-lg, .cds--grid--condensed .cds--col-lg,
    .cds--row--condensed .cds--col-lg--auto,
    .cds--grid--condensed .cds--col-lg--auto {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-lg, .cds--grid--narrow .cds--col-lg,
    .cds--row--narrow .cds--col-lg--auto,
    .cds--grid--narrow .cds--col-lg--auto {
        padding-inline: 0 1rem;
    }

    @media (min-width: 66rem) {
        .cds--col,
        .cds--col-lg {
            flex-basis: 0;
            flex-grow: 1;
            max-inline-size: 100%;
        }

        .cds--col--auto,
        .cds--col-lg--auto {
            flex: 1 0 0%;
            inline-size: auto;
            max-inline-size: 100%;
        }

        .cds--col-lg-0 {
            display: none;
        }

        .cds--col-lg-1 {
            display: block;
            flex: 0 0 6.25%;
            max-inline-size: 6.25%;
        }

        .cds--col-lg-2 {
            display: block;
            flex: 0 0 12.5%;
            max-inline-size: 12.5%;
        }

        .cds--col-lg-3 {
            display: block;
            flex: 0 0 18.75%;
            max-inline-size: 18.75%;
        }

        .cds--col-lg-4 {
            display: block;
            flex: 0 0 25%;
            max-inline-size: 25%;
        }

        .cds--col-lg-5 {
            display: block;
            flex: 0 0 31.25%;
            max-inline-size: 31.25%;
        }

        .cds--col-lg-6 {
            display: block;
            flex: 0 0 37.5%;
            max-inline-size: 37.5%;
        }

        .cds--col-lg-7 {
            display: block;
            flex: 0 0 43.75%;
            max-inline-size: 43.75%;
        }

        .cds--col-lg-8 {
            display: block;
            flex: 0 0 50%;
            max-inline-size: 50%;
        }

        .cds--col-lg-9 {
            display: block;
            flex: 0 0 56.25%;
            max-inline-size: 56.25%;
        }

        .cds--col-lg-10 {
            display: block;
            flex: 0 0 62.5%;
            max-inline-size: 62.5%;
        }

        .cds--col-lg-11 {
            display: block;
            flex: 0 0 68.75%;
            max-inline-size: 68.75%;
        }

        .cds--col-lg-12 {
            display: block;
            flex: 0 0 75%;
            max-inline-size: 75%;
        }

        .cds--col-lg-13 {
            display: block;
            flex: 0 0 81.25%;
            max-inline-size: 81.25%;
        }

        .cds--col-lg-14 {
            display: block;
            flex: 0 0 87.5%;
            max-inline-size: 87.5%;
        }

        .cds--col-lg-15 {
            display: block;
            flex: 0 0 93.75%;
            max-inline-size: 93.75%;
        }

        .cds--col-lg-16 {
            display: block;
            flex: 0 0 100%;
            max-inline-size: 100%;
        }

        .cds--offset-lg-0 {
            margin-inline-start: 0;
        }

        .cds--offset-lg-1 {
            margin-inline-start: 6.25%;
        }

        .cds--offset-lg-2 {
            margin-inline-start: 12.5%;
        }

        .cds--offset-lg-3 {
            margin-inline-start: 18.75%;
        }

        .cds--offset-lg-4 {
            margin-inline-start: 25%;
        }

        .cds--offset-lg-5 {
            margin-inline-start: 31.25%;
        }

        .cds--offset-lg-6 {
            margin-inline-start: 37.5%;
        }

        .cds--offset-lg-7 {
            margin-inline-start: 43.75%;
        }

        .cds--offset-lg-8 {
            margin-inline-start: 50%;
        }

        .cds--offset-lg-9 {
            margin-inline-start: 56.25%;
        }

        .cds--offset-lg-10 {
            margin-inline-start: 62.5%;
        }

        .cds--offset-lg-11 {
            margin-inline-start: 68.75%;
        }

        .cds--offset-lg-12 {
            margin-inline-start: 75%;
        }

        .cds--offset-lg-13 {
            margin-inline-start: 81.25%;
        }

        .cds--offset-lg-14 {
            margin-inline-start: 87.5%;
        }

        .cds--offset-lg-15 {
            margin-inline-start: 93.75%;
        }
    }

    .cds--col-xlg-0 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-xlg-0, .cds--grid--condensed .cds--col-xlg-0 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-xlg-0, .cds--grid--narrow .cds--col-xlg-0 {
        padding-inline: 0 1rem;
    }

    .cds--col-xlg-1 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-xlg-1, .cds--grid--condensed .cds--col-xlg-1 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-xlg-1, .cds--grid--narrow .cds--col-xlg-1 {
        padding-inline: 0 1rem;
    }

    .cds--col-xlg-2 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-xlg-2, .cds--grid--condensed .cds--col-xlg-2 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-xlg-2, .cds--grid--narrow .cds--col-xlg-2 {
        padding-inline: 0 1rem;
    }

    .cds--col-xlg-3 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-xlg-3, .cds--grid--condensed .cds--col-xlg-3 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-xlg-3, .cds--grid--narrow .cds--col-xlg-3 {
        padding-inline: 0 1rem;
    }

    .cds--col-xlg-4 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-xlg-4, .cds--grid--condensed .cds--col-xlg-4 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-xlg-4, .cds--grid--narrow .cds--col-xlg-4 {
        padding-inline: 0 1rem;
    }

    .cds--col-xlg-5 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-xlg-5, .cds--grid--condensed .cds--col-xlg-5 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-xlg-5, .cds--grid--narrow .cds--col-xlg-5 {
        padding-inline: 0 1rem;
    }

    .cds--col-xlg-6 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-xlg-6, .cds--grid--condensed .cds--col-xlg-6 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-xlg-6, .cds--grid--narrow .cds--col-xlg-6 {
        padding-inline: 0 1rem;
    }

    .cds--col-xlg-7 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-xlg-7, .cds--grid--condensed .cds--col-xlg-7 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-xlg-7, .cds--grid--narrow .cds--col-xlg-7 {
        padding-inline: 0 1rem;
    }

    .cds--col-xlg-8 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-xlg-8, .cds--grid--condensed .cds--col-xlg-8 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-xlg-8, .cds--grid--narrow .cds--col-xlg-8 {
        padding-inline: 0 1rem;
    }

    .cds--col-xlg-9 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-xlg-9, .cds--grid--condensed .cds--col-xlg-9 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-xlg-9, .cds--grid--narrow .cds--col-xlg-9 {
        padding-inline: 0 1rem;
    }

    .cds--col-xlg-10 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-xlg-10, .cds--grid--condensed .cds--col-xlg-10 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-xlg-10, .cds--grid--narrow .cds--col-xlg-10 {
        padding-inline: 0 1rem;
    }

    .cds--col-xlg-11 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-xlg-11, .cds--grid--condensed .cds--col-xlg-11 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-xlg-11, .cds--grid--narrow .cds--col-xlg-11 {
        padding-inline: 0 1rem;
    }

    .cds--col-xlg-12 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-xlg-12, .cds--grid--condensed .cds--col-xlg-12 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-xlg-12, .cds--grid--narrow .cds--col-xlg-12 {
        padding-inline: 0 1rem;
    }

    .cds--col-xlg-13 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-xlg-13, .cds--grid--condensed .cds--col-xlg-13 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-xlg-13, .cds--grid--narrow .cds--col-xlg-13 {
        padding-inline: 0 1rem;
    }

    .cds--col-xlg-14 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-xlg-14, .cds--grid--condensed .cds--col-xlg-14 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-xlg-14, .cds--grid--narrow .cds--col-xlg-14 {
        padding-inline: 0 1rem;
    }

    .cds--col-xlg-15 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-xlg-15, .cds--grid--condensed .cds--col-xlg-15 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-xlg-15, .cds--grid--narrow .cds--col-xlg-15 {
        padding-inline: 0 1rem;
    }

    .cds--col-xlg-16 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-xlg-16, .cds--grid--condensed .cds--col-xlg-16 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-xlg-16, .cds--grid--narrow .cds--col-xlg-16 {
        padding-inline: 0 1rem;
    }

    .cds--col-xlg,
    .cds--col-xlg--auto {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-xlg, .cds--grid--condensed .cds--col-xlg,
    .cds--row--condensed .cds--col-xlg--auto,
    .cds--grid--condensed .cds--col-xlg--auto {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-xlg, .cds--grid--narrow .cds--col-xlg,
    .cds--row--narrow .cds--col-xlg--auto,
    .cds--grid--narrow .cds--col-xlg--auto {
        padding-inline: 0 1rem;
    }

    @media (min-width: 82rem) {
        .cds--col,
        .cds--col-xlg {
            flex-basis: 0;
            flex-grow: 1;
            max-inline-size: 100%;
        }

        .cds--col--auto,
        .cds--col-xlg--auto {
            flex: 1 0 0%;
            inline-size: auto;
            max-inline-size: 100%;
        }

        .cds--col-xlg-0 {
            display: none;
        }

        .cds--col-xlg-1 {
            display: block;
            flex: 0 0 6.25%;
            max-inline-size: 6.25%;
        }

        .cds--col-xlg-2 {
            display: block;
            flex: 0 0 12.5%;
            max-inline-size: 12.5%;
        }

        .cds--col-xlg-3 {
            display: block;
            flex: 0 0 18.75%;
            max-inline-size: 18.75%;
        }

        .cds--col-xlg-4 {
            display: block;
            flex: 0 0 25%;
            max-inline-size: 25%;
        }

        .cds--col-xlg-5 {
            display: block;
            flex: 0 0 31.25%;
            max-inline-size: 31.25%;
        }

        .cds--col-xlg-6 {
            display: block;
            flex: 0 0 37.5%;
            max-inline-size: 37.5%;
        }

        .cds--col-xlg-7 {
            display: block;
            flex: 0 0 43.75%;
            max-inline-size: 43.75%;
        }

        .cds--col-xlg-8 {
            display: block;
            flex: 0 0 50%;
            max-inline-size: 50%;
        }

        .cds--col-xlg-9 {
            display: block;
            flex: 0 0 56.25%;
            max-inline-size: 56.25%;
        }

        .cds--col-xlg-10 {
            display: block;
            flex: 0 0 62.5%;
            max-inline-size: 62.5%;
        }

        .cds--col-xlg-11 {
            display: block;
            flex: 0 0 68.75%;
            max-inline-size: 68.75%;
        }

        .cds--col-xlg-12 {
            display: block;
            flex: 0 0 75%;
            max-inline-size: 75%;
        }

        .cds--col-xlg-13 {
            display: block;
            flex: 0 0 81.25%;
            max-inline-size: 81.25%;
        }

        .cds--col-xlg-14 {
            display: block;
            flex: 0 0 87.5%;
            max-inline-size: 87.5%;
        }

        .cds--col-xlg-15 {
            display: block;
            flex: 0 0 93.75%;
            max-inline-size: 93.75%;
        }

        .cds--col-xlg-16 {
            display: block;
            flex: 0 0 100%;
            max-inline-size: 100%;
        }

        .cds--offset-xlg-0 {
            margin-inline-start: 0;
        }

        .cds--offset-xlg-1 {
            margin-inline-start: 6.25%;
        }

        .cds--offset-xlg-2 {
            margin-inline-start: 12.5%;
        }

        .cds--offset-xlg-3 {
            margin-inline-start: 18.75%;
        }

        .cds--offset-xlg-4 {
            margin-inline-start: 25%;
        }

        .cds--offset-xlg-5 {
            margin-inline-start: 31.25%;
        }

        .cds--offset-xlg-6 {
            margin-inline-start: 37.5%;
        }

        .cds--offset-xlg-7 {
            margin-inline-start: 43.75%;
        }

        .cds--offset-xlg-8 {
            margin-inline-start: 50%;
        }

        .cds--offset-xlg-9 {
            margin-inline-start: 56.25%;
        }

        .cds--offset-xlg-10 {
            margin-inline-start: 62.5%;
        }

        .cds--offset-xlg-11 {
            margin-inline-start: 68.75%;
        }

        .cds--offset-xlg-12 {
            margin-inline-start: 75%;
        }

        .cds--offset-xlg-13 {
            margin-inline-start: 81.25%;
        }

        .cds--offset-xlg-14 {
            margin-inline-start: 87.5%;
        }

        .cds--offset-xlg-15 {
            margin-inline-start: 93.75%;
        }
    }

    .cds--col-max-0 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-max-0, .cds--grid--condensed .cds--col-max-0 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-max-0, .cds--grid--narrow .cds--col-max-0 {
        padding-inline: 0 1rem;
    }

    .cds--col-max-1 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-max-1, .cds--grid--condensed .cds--col-max-1 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-max-1, .cds--grid--narrow .cds--col-max-1 {
        padding-inline: 0 1rem;
    }

    .cds--col-max-2 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-max-2, .cds--grid--condensed .cds--col-max-2 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-max-2, .cds--grid--narrow .cds--col-max-2 {
        padding-inline: 0 1rem;
    }

    .cds--col-max-3 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-max-3, .cds--grid--condensed .cds--col-max-3 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-max-3, .cds--grid--narrow .cds--col-max-3 {
        padding-inline: 0 1rem;
    }

    .cds--col-max-4 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-max-4, .cds--grid--condensed .cds--col-max-4 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-max-4, .cds--grid--narrow .cds--col-max-4 {
        padding-inline: 0 1rem;
    }

    .cds--col-max-5 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-max-5, .cds--grid--condensed .cds--col-max-5 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-max-5, .cds--grid--narrow .cds--col-max-5 {
        padding-inline: 0 1rem;
    }

    .cds--col-max-6 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-max-6, .cds--grid--condensed .cds--col-max-6 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-max-6, .cds--grid--narrow .cds--col-max-6 {
        padding-inline: 0 1rem;
    }

    .cds--col-max-7 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-max-7, .cds--grid--condensed .cds--col-max-7 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-max-7, .cds--grid--narrow .cds--col-max-7 {
        padding-inline: 0 1rem;
    }

    .cds--col-max-8 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-max-8, .cds--grid--condensed .cds--col-max-8 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-max-8, .cds--grid--narrow .cds--col-max-8 {
        padding-inline: 0 1rem;
    }

    .cds--col-max-9 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-max-9, .cds--grid--condensed .cds--col-max-9 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-max-9, .cds--grid--narrow .cds--col-max-9 {
        padding-inline: 0 1rem;
    }

    .cds--col-max-10 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-max-10, .cds--grid--condensed .cds--col-max-10 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-max-10, .cds--grid--narrow .cds--col-max-10 {
        padding-inline: 0 1rem;
    }

    .cds--col-max-11 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-max-11, .cds--grid--condensed .cds--col-max-11 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-max-11, .cds--grid--narrow .cds--col-max-11 {
        padding-inline: 0 1rem;
    }

    .cds--col-max-12 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-max-12, .cds--grid--condensed .cds--col-max-12 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-max-12, .cds--grid--narrow .cds--col-max-12 {
        padding-inline: 0 1rem;
    }

    .cds--col-max-13 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-max-13, .cds--grid--condensed .cds--col-max-13 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-max-13, .cds--grid--narrow .cds--col-max-13 {
        padding-inline: 0 1rem;
    }

    .cds--col-max-14 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-max-14, .cds--grid--condensed .cds--col-max-14 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-max-14, .cds--grid--narrow .cds--col-max-14 {
        padding-inline: 0 1rem;
    }

    .cds--col-max-15 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-max-15, .cds--grid--condensed .cds--col-max-15 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-max-15, .cds--grid--narrow .cds--col-max-15 {
        padding-inline: 0 1rem;
    }

    .cds--col-max-16 {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-max-16, .cds--grid--condensed .cds--col-max-16 {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-max-16, .cds--grid--narrow .cds--col-max-16 {
        padding-inline: 0 1rem;
    }

    .cds--col-max,
    .cds--col-max--auto {
        inline-size: 100%;
        padding-inline: 1rem;
    }

    .cds--row--condensed .cds--col-max, .cds--grid--condensed .cds--col-max,
    .cds--row--condensed .cds--col-max--auto,
    .cds--grid--condensed .cds--col-max--auto {
        padding-inline: 0.03125rem;
    }

    .cds--row--narrow .cds--col-max, .cds--grid--narrow .cds--col-max,
    .cds--row--narrow .cds--col-max--auto,
    .cds--grid--narrow .cds--col-max--auto {
        padding-inline: 0 1rem;
    }

    @media (min-width: 99rem) {
        .cds--col,
        .cds--col-max {
            flex-basis: 0;
            flex-grow: 1;
            max-inline-size: 100%;
        }

        .cds--col--auto,
        .cds--col-max--auto {
            flex: 1 0 0%;
            inline-size: auto;
            max-inline-size: 100%;
        }

        .cds--col-max-0 {
            display: none;
        }

        .cds--col-max-1 {
            display: block;
            flex: 0 0 6.25%;
            max-inline-size: 6.25%;
        }

        .cds--col-max-2 {
            display: block;
            flex: 0 0 12.5%;
            max-inline-size: 12.5%;
        }

        .cds--col-max-3 {
            display: block;
            flex: 0 0 18.75%;
            max-inline-size: 18.75%;
        }

        .cds--col-max-4 {
            display: block;
            flex: 0 0 25%;
            max-inline-size: 25%;
        }

        .cds--col-max-5 {
            display: block;
            flex: 0 0 31.25%;
            max-inline-size: 31.25%;
        }

        .cds--col-max-6 {
            display: block;
            flex: 0 0 37.5%;
            max-inline-size: 37.5%;
        }

        .cds--col-max-7 {
            display: block;
            flex: 0 0 43.75%;
            max-inline-size: 43.75%;
        }

        .cds--col-max-8 {
            display: block;
            flex: 0 0 50%;
            max-inline-size: 50%;
        }

        .cds--col-max-9 {
            display: block;
            flex: 0 0 56.25%;
            max-inline-size: 56.25%;
        }

        .cds--col-max-10 {
            display: block;
            flex: 0 0 62.5%;
            max-inline-size: 62.5%;
        }

        .cds--col-max-11 {
            display: block;
            flex: 0 0 68.75%;
            max-inline-size: 68.75%;
        }

        .cds--col-max-12 {
            display: block;
            flex: 0 0 75%;
            max-inline-size: 75%;
        }

        .cds--col-max-13 {
            display: block;
            flex: 0 0 81.25%;
            max-inline-size: 81.25%;
        }

        .cds--col-max-14 {
            display: block;
            flex: 0 0 87.5%;
            max-inline-size: 87.5%;
        }

        .cds--col-max-15 {
            display: block;
            flex: 0 0 93.75%;
            max-inline-size: 93.75%;
        }

        .cds--col-max-16 {
            display: block;
            flex: 0 0 100%;
            max-inline-size: 100%;
        }

        .cds--offset-max-0 {
            margin-inline-start: 0;
        }

        .cds--offset-max-1 {
            margin-inline-start: 6.25%;
        }

        .cds--offset-max-2 {
            margin-inline-start: 12.5%;
        }

        .cds--offset-max-3 {
            margin-inline-start: 18.75%;
        }

        .cds--offset-max-4 {
            margin-inline-start: 25%;
        }

        .cds--offset-max-5 {
            margin-inline-start: 31.25%;
        }

        .cds--offset-max-6 {
            margin-inline-start: 37.5%;
        }

        .cds--offset-max-7 {
            margin-inline-start: 43.75%;
        }

        .cds--offset-max-8 {
            margin-inline-start: 50%;
        }

        .cds--offset-max-9 {
            margin-inline-start: 56.25%;
        }

        .cds--offset-max-10 {
            margin-inline-start: 62.5%;
        }

        .cds--offset-max-11 {
            margin-inline-start: 68.75%;
        }

        .cds--offset-max-12 {
            margin-inline-start: 75%;
        }

        .cds--offset-max-13 {
            margin-inline-start: 81.25%;
        }

        .cds--offset-max-14 {
            margin-inline-start: 87.5%;
        }

        .cds--offset-max-15 {
            margin-inline-start: 93.75%;
        }
    }

    .cds--no-gutter,
    .cds--row.cds--no-gutter [class*=cds--col] {
        padding-inline: 0;
    }

    .cds--no-gutter--start,
    .cds--row.cds--no-gutter--start [class*=cds--col] {
        padding-inline-start: 0;
    }

    .cds--no-gutter--end,
    .cds--row.cds--no-gutter--end [class*=cds--col] {
        padding-inline-end: 0;
    }

    .cds--hang--start {
        padding-inline-start: 1rem;
    }

    .cds--hang--end {
        padding-inline-end: 1rem;
    }

    .flatpickr-calendar {
        background: transparent;
        opacity: 0;
        display: none;
        text-align: center;
        visibility: hidden;
        padding: 0;
        -webkit-animation: none;
        animation: none;
        direction: ltr;
        border: 0;
        font-size: 14px;
        line-height: 24px;
        border-radius: 5px;
        position: absolute;
        width: 307.875px;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        -ms-touch-action: manipulation;
        touch-action: manipulation;
        -webkit-box-shadow: 0 3px 13px rgba(0, 0, 0, 0.08);
        box-shadow: 0 3px 13px rgba(0, 0, 0, 0.08);
    }

    .flatpickr-calendar.open,
    .flatpickr-calendar.inline {
        opacity: 1;
        max-height: 640px;
        visibility: visible;
    }

    .flatpickr-calendar.open {
        display: inline-block;
        z-index: 99999;
    }

    .flatpickr-calendar.animate.open {
        -webkit-animation: fpFadeInDown 300ms cubic-bezier(0.23, 1, 0.32, 1);
        animation: fpFadeInDown 300ms cubic-bezier(0.23, 1, 0.32, 1);
    }

    .flatpickr-calendar.inline {
        display: block;
        position: relative;
        top: 2px;
    }

    .flatpickr-calendar.static {
        position: absolute;
        top: calc(100% + 2px);
    }

    .flatpickr-calendar.static.open {
        z-index: 999;
        display: block;
    }

    .flatpickr-calendar.multiMonth .flatpickr-days .dayContainer:nth-child(n+1) .flatpickr-day.inRange:nth-child(7n+7) {
        -webkit-box-shadow: none !important;
        box-shadow: none !important;
    }

    .flatpickr-calendar.multiMonth .flatpickr-days .dayContainer:nth-child(n+2) .flatpickr-day.inRange:nth-child(7n+1) {
        -webkit-box-shadow: -2px 0 0 #e6e6e6, 5px 0 0 #e6e6e6;
        box-shadow: -2px 0 0 #e6e6e6, 5px 0 0 #e6e6e6;
    }

    .flatpickr-calendar .hasWeeks .dayContainer,
    .flatpickr-calendar .hasTime .dayContainer {
        border-bottom: 0;
        border-bottom-right-radius: 0;
        border-bottom-left-radius: 0;
    }

    .flatpickr-calendar .hasWeeks .dayContainer {
        border-left: 0;
    }

    .flatpickr-calendar.hasTime .flatpickr-time {
        height: 40px;
        border-top: 1px solid #eceef1;
    }

    .flatpickr-calendar.hasTime .flatpickr-innerContainer {
        border-bottom: 0;
    }

    .flatpickr-calendar.hasTime .flatpickr-time {
        border: 1px solid #eceef1;
    }

    .flatpickr-calendar.noCalendar.hasTime .flatpickr-time {
        height: auto;
    }

    .flatpickr-calendar:before,
    .flatpickr-calendar:after {
        position: absolute;
        display: block;
        pointer-events: none;
        border: solid transparent;
        content: "";
        height: 0;
        width: 0;
        left: 22px;
    }

    .flatpickr-calendar.rightMost:before,
    .flatpickr-calendar.arrowRight:before,
    .flatpickr-calendar.rightMost:after,
    .flatpickr-calendar.arrowRight:after {
        left: auto;
        right: 22px;
    }

    .flatpickr-calendar.arrowCenter:before,
    .flatpickr-calendar.arrowCenter:after {
        left: 50%;
        right: 50%;
    }

    .flatpickr-calendar:before {
        border-width: 5px;
        margin: 0 -5px;
    }

    .flatpickr-calendar:after {
        border-width: 4px;
        margin: 0 -4px;
    }

    .flatpickr-calendar.arrowTop:before,
    .flatpickr-calendar.arrowTop:after {
        bottom: 100%;
    }

    .flatpickr-calendar.arrowTop:before {
        border-bottom-color: #eceef1;
    }

    .flatpickr-calendar.arrowTop:after {
        border-bottom-color: #eceef1;
    }

    .flatpickr-calendar.arrowBottom:before,
    .flatpickr-calendar.arrowBottom:after {
        top: 100%;
    }

    .flatpickr-calendar.arrowBottom:before {
        border-top-color: #eceef1;
    }

    .flatpickr-calendar.arrowBottom:after {
        border-top-color: #eceef1;
    }

    .flatpickr-calendar:focus {
        outline: 0;
    }

    .flatpickr-wrapper {
        position: relative;
        display: inline-block;
    }

    .flatpickr-months {
        display: -webkit-box;
        display: -webkit-flex;
        display: -ms-flexbox;
        display: flex;
    }

    .flatpickr-months .flatpickr-month {
        border-radius: 5px 5px 0 0;
        background: #eceef1;
        color: #5a6171;
        fill: #5a6171;
        height: 34px;
        line-height: 1;
        text-align: center;
        position: relative;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
        overflow: hidden;
        -webkit-box-flex: 1;
        -webkit-flex: 1;
        -ms-flex: 1;
        flex: 1;
    }

    .flatpickr-months .flatpickr-prev-month,
    .flatpickr-months .flatpickr-next-month {
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
        text-decoration: none;
        cursor: pointer;
        position: absolute;
        top: 0;
        height: 34px;
        padding: 10px;
        z-index: 3;
        color: #5a6171;
        fill: #5a6171;
    }

    .flatpickr-months .flatpickr-prev-month.flatpickr-disabled,
    .flatpickr-months .flatpickr-next-month.flatpickr-disabled {
        display: none;
    }

    .flatpickr-months .flatpickr-prev-month i,
    .flatpickr-months .flatpickr-next-month i {
        position: relative;
    }

    .flatpickr-months .flatpickr-prev-month.flatpickr-prev-month,
    .flatpickr-months .flatpickr-next-month.flatpickr-prev-month {
        /*
        /*rtl:begin:ignore*/
        /*
        */
        left: 0;
        /*
        /*rtl:end:ignore*/
        /*
        */
    }

    /*
      /*rtl:begin:ignore*/
    /*
      /*rtl:end:ignore*/

    .flatpickr-months .flatpickr-prev-month.flatpickr-next-month,
    .flatpickr-months .flatpickr-next-month.flatpickr-next-month {
        /*
        /*rtl:begin:ignore*/
        /*
        */
        right: 0;
        /*
        /*rtl:end:ignore*/
        /*
        */
    }

    /*
      /*rtl:begin:ignore*/
    /*
      /*rtl:end:ignore*/

    .flatpickr-months .flatpickr-prev-month:hover,
    .flatpickr-months .flatpickr-next-month:hover {
        color: #bbb;
    }

    .flatpickr-months .flatpickr-prev-month:hover svg,
    .flatpickr-months .flatpickr-next-month:hover svg {
        fill: #f64747;
    }

    .flatpickr-months .flatpickr-prev-month svg,
    .flatpickr-months .flatpickr-next-month svg {
        width: 14px;
        height: 14px;
    }

    .flatpickr-months .flatpickr-prev-month svg path,
    .flatpickr-months .flatpickr-next-month svg path {
        -webkit-transition: fill 0.1s;
        transition: fill 0.1s;
        fill: inherit;
    }

    .numInputWrapper {
        position: relative;
        height: auto;
    }

    .numInputWrapper input,
    .numInputWrapper span {
        display: inline-block;
    }

    .numInputWrapper input {
        width: 100%;
    }

    .numInputWrapper input::-ms-clear {
        display: none;
    }

    .numInputWrapper input::-webkit-outer-spin-button,
    .numInputWrapper input::-webkit-inner-spin-button {
        margin: 0;
        -webkit-appearance: none;
    }

    .numInputWrapper span {
        position: absolute;
        right: 0;
        width: 14px;
        padding: 0 4px 0 2px;
        height: 50%;
        line-height: 50%;
        opacity: 0;
        cursor: pointer;
        border: 1px solid rgba(72, 72, 72, 0.15);
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
    }

    .numInputWrapper span:hover {
        background: rgba(0, 0, 0, 0.1);
    }

    .numInputWrapper span:active {
        background: rgba(0, 0, 0, 0.2);
    }

    .numInputWrapper span:after {
        display: block;
        content: "";
        position: absolute;
    }

    .numInputWrapper span.arrowUp {
        top: 0;
        border-bottom: 0;
    }

    .numInputWrapper span.arrowUp:after {
        border-left: 4px solid transparent;
        border-right: 4px solid transparent;
        border-bottom: 4px solid rgba(72, 72, 72, 0.6);
        top: 26%;
    }

    .numInputWrapper span.arrowDown {
        top: 50%;
    }

    .numInputWrapper span.arrowDown:after {
        border-left: 4px solid transparent;
        border-right: 4px solid transparent;
        border-top: 4px solid rgba(72, 72, 72, 0.6);
        top: 40%;
    }

    .numInputWrapper span svg {
        width: inherit;
        height: auto;
    }

    .numInputWrapper span svg path {
        fill: rgba(90, 97, 113, 0.5);
    }

    .numInputWrapper:hover {
        background: rgba(0, 0, 0, 0.05);
    }

    .numInputWrapper:hover span {
        opacity: 1;
    }

    .flatpickr-current-month {
        font-size: 135%;
        line-height: inherit;
        font-weight: 300;
        color: inherit;
        position: absolute;
        width: 75%;
        left: 12.5%;
        padding: 7.48px 0 0 0;
        line-height: 1;
        height: 34px;
        display: inline-block;
        text-align: center;
        -webkit-transform: translate3d(0px, 0px, 0px);
        transform: translate3d(0px, 0px, 0px);
    }

    .flatpickr-current-month span.cur-month {
        font-family: inherit;
        font-weight: 700;
        color: inherit;
        display: inline-block;
        margin-left: 0.5ch;
        padding: 0;
    }

    .flatpickr-current-month span.cur-month:hover {
        background: rgba(0, 0, 0, 0.05);
    }

    .flatpickr-current-month .numInputWrapper {
        width: 6ch;
        width: 7ch \\0;
        display: inline-block;
    }

    .flatpickr-current-month .numInputWrapper span.arrowUp:after {
        border-bottom-color: #5a6171;
    }

    .flatpickr-current-month .numInputWrapper span.arrowDown:after {
        border-top-color: #5a6171;
    }

    .flatpickr-current-month input.cur-year {
        background: transparent;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        color: inherit;
        cursor: text;
        padding: 0 0 0 0.5ch;
        margin: 0;
        display: inline-block;
        font-size: inherit;
        font-family: inherit;
        font-weight: 300;
        line-height: inherit;
        height: auto;
        border: 0;
        border-radius: 0;
        vertical-align: initial;
        -webkit-appearance: textfield;
        -moz-appearance: textfield;
        appearance: textfield;
    }

    .flatpickr-current-month input.cur-year:focus {
        outline: 0;
    }

    .flatpickr-current-month input.cur-year[disabled],
    .flatpickr-current-month input.cur-year[disabled]:hover {
        font-size: 100%;
        color: rgba(90, 97, 113, 0.5);
        background: transparent;
        pointer-events: none;
    }

    .flatpickr-current-month .flatpickr-monthDropdown-months {
        appearance: menulist;
        background: #eceef1;
        border: none;
        border-radius: 0;
        box-sizing: border-box;
        color: inherit;
        cursor: pointer;
        font-size: inherit;
        font-family: inherit;
        font-weight: 300;
        height: auto;
        line-height: inherit;
        margin: -1px 0 0 0;
        outline: none;
        padding: 0 0 0 0.5ch;
        position: relative;
        vertical-align: initial;
        -webkit-box-sizing: border-box;
        -webkit-appearance: menulist;
        -moz-appearance: menulist;
        width: auto;
    }

    .flatpickr-current-month .flatpickr-monthDropdown-months:focus,
    .flatpickr-current-month .flatpickr-monthDropdown-months:active {
        outline: none;
    }

    .flatpickr-current-month .flatpickr-monthDropdown-months:hover {
        background: rgba(0, 0, 0, 0.05);
    }

    .flatpickr-current-month .flatpickr-monthDropdown-months .flatpickr-monthDropdown-month {
        background-color: #eceef1;
        outline: none;
        padding: 0;
    }

    .flatpickr-weekdays {
        background: #eceef1;
        text-align: center;
        overflow: hidden;
        width: 100%;
        display: -webkit-box;
        display: -webkit-flex;
        display: -ms-flexbox;
        display: flex;
        -webkit-box-align: center;
        -webkit-align-items: center;
        -ms-flex-align: center;
        align-items: center;
        height: 28px;
    }

    .flatpickr-weekdays .flatpickr-weekdaycontainer {
        display: -webkit-box;
        display: -webkit-flex;
        display: -ms-flexbox;
        display: flex;
        -webkit-box-flex: 1;
        -webkit-flex: 1;
        -ms-flex: 1;
        flex: 1;
    }

    span.flatpickr-weekday {
        cursor: default;
        font-size: 90%;
        background: #eceef1;
        color: #5a6171;
        line-height: 1;
        margin: 0;
        text-align: center;
        display: block;
        -webkit-box-flex: 1;
        -webkit-flex: 1;
        -ms-flex: 1;
        flex: 1;
        font-weight: bolder;
    }

    .dayContainer,
    .flatpickr-weeks {
        padding: 1px 0 0 0;
    }

    .flatpickr-days {
        position: relative;
        overflow: hidden;
        display: -webkit-box;
        display: -webkit-flex;
        display: -ms-flexbox;
        display: flex;
        -webkit-box-align: start;
        -webkit-align-items: flex-start;
        -ms-flex-align: start;
        align-items: flex-start;
        width: 307.875px;
        border-left: 1px solid #eceef1;
        border-right: 1px solid #eceef1;
    }

    .flatpickr-days:focus {
        outline: 0;
    }

    .dayContainer {
        padding: 0;
        outline: 0;
        text-align: left;
        width: 307.875px;
        min-width: 307.875px;
        max-width: 307.875px;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        display: inline-block;
        display: -ms-flexbox;
        display: -webkit-box;
        display: -webkit-flex;
        display: flex;
        -webkit-flex-wrap: wrap;
        flex-wrap: wrap;
        -ms-flex-wrap: wrap;
        -ms-flex-pack: justify;
        -webkit-justify-content: space-around;
        justify-content: space-around;
        -webkit-transform: translate3d(0px, 0px, 0px);
        transform: translate3d(0px, 0px, 0px);
        opacity: 1;
    }

    .dayContainer + .dayContainer {
        -webkit-box-shadow: -1px 0 0 #eceef1;
        box-shadow: -1px 0 0 #eceef1;
    }

    .flatpickr-day {
        background: none;
        border: 1px solid transparent;
        border-radius: 150px;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        color: #484848;
        cursor: pointer;
        font-weight: 400;
        width: 14.2857143%;
        -webkit-flex-basis: 14.2857143%;
        -ms-flex-preferred-size: 14.2857143%;
        flex-basis: 14.2857143%;
        max-width: 39px;
        height: 39px;
        line-height: 39px;
        margin: 0;
        display: inline-block;
        position: relative;
        -webkit-box-pack: center;
        -webkit-justify-content: center;
        -ms-flex-pack: center;
        justify-content: center;
        text-align: center;
    }

    .flatpickr-day.inRange,
    .flatpickr-day.prevMonthDay.inRange,
    .flatpickr-day.nextMonthDay.inRange,
    .flatpickr-day.today.inRange,
    .flatpickr-day.prevMonthDay.today.inRange,
    .flatpickr-day.nextMonthDay.today.inRange,
    .flatpickr-day:hover,
    .flatpickr-day.prevMonthDay:hover,
    .flatpickr-day.nextMonthDay:hover,
    .flatpickr-day:focus,
    .flatpickr-day.prevMonthDay:focus,
    .flatpickr-day.nextMonthDay:focus {
        cursor: pointer;
        outline: 0;
        background: #e2e2e2;
        border-color: #e2e2e2;
    }

    .flatpickr-day.today {
        border-color: #bbb;
    }

    .flatpickr-day.today:hover,
    .flatpickr-day.today:focus {
        border-color: #bbb;
        background: #bbb;
        color: #fff;
    }

    .flatpickr-day.selected,
    .flatpickr-day.startRange,
    .flatpickr-day.endRange,
    .flatpickr-day.selected.inRange,
    .flatpickr-day.startRange.inRange,
    .flatpickr-day.endRange.inRange,
    .flatpickr-day.selected:focus,
    .flatpickr-day.startRange:focus,
    .flatpickr-day.endRange:focus,
    .flatpickr-day.selected:hover,
    .flatpickr-day.startRange:hover,
    .flatpickr-day.endRange:hover,
    .flatpickr-day.selected.prevMonthDay,
    .flatpickr-day.startRange.prevMonthDay,
    .flatpickr-day.endRange.prevMonthDay,
    .flatpickr-day.selected.nextMonthDay,
    .flatpickr-day.startRange.nextMonthDay,
    .flatpickr-day.endRange.nextMonthDay {
        background: #ff5a5f;
        -webkit-box-shadow: none;
        box-shadow: none;
        color: #fff;
        border-color: #ff5a5f;
    }

    .flatpickr-day.selected.startRange,
    .flatpickr-day.startRange.startRange,
    .flatpickr-day.endRange.startRange {
        border-radius: 50px 0 0 50px;
    }

    .flatpickr-day.selected.endRange,
    .flatpickr-day.startRange.endRange,
    .flatpickr-day.endRange.endRange {
        border-radius: 0 50px 50px 0;
    }

    .flatpickr-day.selected.startRange + .endRange:not(:nth-child(7n+1)),
    .flatpickr-day.startRange.startRange + .endRange:not(:nth-child(7n+1)),
    .flatpickr-day.endRange.startRange + .endRange:not(:nth-child(7n+1)) {
        -webkit-box-shadow: -10px 0 0 #ff5a5f;
        box-shadow: -10px 0 0 #ff5a5f;
    }

    .flatpickr-day.selected.startRange.endRange,
    .flatpickr-day.startRange.startRange.endRange,
    .flatpickr-day.endRange.startRange.endRange {
        border-radius: 50px;
    }

    .flatpickr-day.inRange {
        border-radius: 0;
        -webkit-box-shadow: -5px 0 0 #e2e2e2, 5px 0 0 #e2e2e2;
        box-shadow: -5px 0 0 #e2e2e2, 5px 0 0 #e2e2e2;
    }

    .flatpickr-day.flatpickr-disabled,
    .flatpickr-day.flatpickr-disabled:hover,
    .flatpickr-day.prevMonthDay,
    .flatpickr-day.nextMonthDay,
    .flatpickr-day.notAllowed,
    .flatpickr-day.notAllowed.prevMonthDay,
    .flatpickr-day.notAllowed.nextMonthDay {
        color: rgba(72, 72, 72, 0.3);
        background: transparent;
        border-color: transparent;
        cursor: default;
    }

    .flatpickr-day.flatpickr-disabled,
    .flatpickr-day.flatpickr-disabled:hover {
        cursor: not-allowed;
        color: rgba(72, 72, 72, 0.1);
    }

    .flatpickr-day.week.selected {
        border-radius: 0;
        -webkit-box-shadow: -5px 0 0 #ff5a5f, 5px 0 0 #ff5a5f;
        box-shadow: -5px 0 0 #ff5a5f, 5px 0 0 #ff5a5f;
    }

    .flatpickr-day.hidden {
        visibility: hidden;
    }

    .rangeMode .flatpickr-day {
        margin-top: 1px;
    }

    .flatpickr-weekwrapper {
        float: left;
    }

    .flatpickr-weekwrapper .flatpickr-weeks {
        padding: 0 12px;
        border-left: 1px solid #eceef1;
    }

    .flatpickr-weekwrapper .flatpickr-weekday {
        float: none;
        width: 100%;
        line-height: 28px;
    }

    .flatpickr-weekwrapper span.flatpickr-day,
    .flatpickr-weekwrapper span.flatpickr-day:hover {
        display: block;
        width: 100%;
        max-width: none;
        color: rgba(72, 72, 72, 0.3);
        background: transparent;
        cursor: default;
        border: none;
    }

    .flatpickr-innerContainer {
        display: block;
        display: -webkit-box;
        display: -webkit-flex;
        display: -ms-flexbox;
        display: flex;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        overflow: hidden;
        background: #fff;
        border-bottom: 1px solid #eceef1;
    }

    .flatpickr-rContainer {
        display: inline-block;
        padding: 0;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
    }

    .flatpickr-time {
        text-align: center;
        outline: 0;
        display: block;
        height: 0;
        line-height: 40px;
        max-height: 40px;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        overflow: hidden;
        display: -webkit-box;
        display: -webkit-flex;
        display: -ms-flexbox;
        display: flex;
        background: #fff;
        border-radius: 0 0 5px 5px;
    }

    .flatpickr-time:after {
        content: "";
        display: table;
        clear: both;
    }

    .flatpickr-time .numInputWrapper {
        -webkit-box-flex: 1;
        -webkit-flex: 1;
        -ms-flex: 1;
        flex: 1;
        width: 40%;
        height: 40px;
        float: left;
    }

    .flatpickr-time .numInputWrapper span.arrowUp:after {
        border-bottom-color: #484848;
    }

    .flatpickr-time .numInputWrapper span.arrowDown:after {
        border-top-color: #484848;
    }

    .flatpickr-time.hasSeconds .numInputWrapper {
        width: 26%;
    }

    .flatpickr-time.time24hr .numInputWrapper {
        width: 49%;
    }

    .flatpickr-time input {
        background: transparent;
        -webkit-box-shadow: none;
        box-shadow: none;
        border: 0;
        border-radius: 0;
        text-align: center;
        margin: 0;
        padding: 0;
        height: inherit;
        line-height: inherit;
        color: #484848;
        font-size: 14px;
        position: relative;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        -webkit-appearance: textfield;
        -moz-appearance: textfield;
        appearance: textfield;
    }

    .flatpickr-time input.flatpickr-hour {
        font-weight: bold;
    }

    .flatpickr-time input.flatpickr-minute,
    .flatpickr-time input.flatpickr-second {
        font-weight: 400;
    }

    .flatpickr-time input:focus {
        outline: 0;
        border: 0;
    }

    .flatpickr-time .flatpickr-time-separator,
    .flatpickr-time .flatpickr-am-pm {
        height: inherit;
        float: left;
        line-height: inherit;
        color: #484848;
        font-weight: bold;
        width: 2%;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
        -webkit-align-self: center;
        -ms-flex-item-align: center;
        align-self: center;
    }

    .flatpickr-time .flatpickr-am-pm {
        outline: 0;
        width: 18%;
        cursor: pointer;
        text-align: center;
        font-weight: 400;
    }

    .flatpickr-time input:hover,
    .flatpickr-time .flatpickr-am-pm:hover,
    .flatpickr-time input:focus,
    .flatpickr-time .flatpickr-am-pm:focus {
        background: #eaeaea;
    }

    .flatpickr-input[readonly] {
        cursor: pointer;
    }

    @-webkit-keyframes fpFadeInDown {
        from {
            opacity: 0;
            -webkit-transform: translate3d(0, -20px, 0);
            transform: translate3d(0, -20px, 0);
        }
        to {
            opacity: 1;
            -webkit-transform: translate3d(0, 0, 0);
            transform: translate3d(0, 0, 0);
        }
    }
    @keyframes fpFadeInDown {
        from {
            opacity: 0;
            -webkit-transform: translate3d(0, -20px, 0);
            transform: translate3d(0, -20px, 0);
        }
        to {
            opacity: 1;
            -webkit-transform: translate3d(0, 0, 0);
            transform: translate3d(0, 0, 0);
        }
    }

    span.flatpickr-day.selected {
        font-weight: bold;
    }
`;
