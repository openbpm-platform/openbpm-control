/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.uicomponent.menu;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import io.jmix.flowui.component.main.JmixListMenu;
import io.jmix.flowui.kit.component.main.ListMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

public class ControlListMenu extends JmixListMenu {
    public static final String MENU_GROUP_LABEL_CLASS = "menu-group-label";
    private static final Logger log = LoggerFactory.getLogger(ControlListMenu.class);

    public void addMenuItemBefore(MenuItem menuItem, String beforeMenuItemId) {
        MenuItem existingMenuItem = getExistingMenu(beforeMenuItemId);
        if (existingMenuItem != null) {
            int prevItemIdx = rootMenuItems.indexOf(existingMenuItem);
            if (prevItemIdx != -1) {
                addMenuItem(menuItem, prevItemIdx);
            } else {
                addMenuItem(menuItem);
            }
        }
    }

    public void addMenuItemAfter(MenuItem menuItem, String afterMenuItemId) {
        MenuItem existingMenuItem = getExistingMenu(afterMenuItemId);
        if (existingMenuItem != null) {
            int nextItemIdx = rootMenuItems.indexOf(existingMenuItem);
            if (nextItemIdx == rootMenuItems.size() - 1) {
                addMenuItem(menuItem);
            } else {
                addMenuItem(menuItem, nextItemIdx + 1);
            }
        }

    }

    @Override
    protected ListItem createMenuRecursively(MenuItem menuItem) {
        if (menuItem instanceof GroupLabelMenuItem) {
            checkItemIdDuplicate(menuItem.getId());

            Component groupLabel = new Span(menuItem.getTitle());
            groupLabel.addClassNames(MENU_GROUP_LABEL_CLASS);
            ListItem menuItemComponent = new ListItem(groupLabel);

            registerMenuItem(menuItem, menuItemComponent);

            return menuItemComponent;
        }
        return super.createMenuRecursively(menuItem);
    }

    @Nullable
    protected MenuItem getExistingMenu(String menuId) {
        MenuItem existingMenuItem = getMenuItem(menuId);
        if (existingMenuItem == null) {
            log.warn("Menu item  not found by id '{}'", menuId);
        }

        return existingMenuItem;
    }

    public static class GroupLabelMenuItem extends ListMenu.MenuItem {
        public GroupLabelMenuItem(String id) {
            super(id);
        }
    }
}
