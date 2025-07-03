package io.openbpm.control.menu;

import com.google.common.base.Strings;
import com.vaadin.flow.component.icon.VaadinIcon;
import io.jmix.core.MessageTools;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.kit.component.ComponentUtils;
import io.jmix.flowui.kit.component.main.ListMenu;
import io.jmix.flowui.menu.ListMenuBuilder;
import io.jmix.flowui.menu.MenuConfig;
import io.jmix.flowui.menu.MenuItem;
import io.jmix.flowui.menu.MenuItemCommands;
import io.jmix.flowui.sys.UiAccessChecker;
import io.jmix.flowui.view.ViewRegistry;

public class CustomListMenuBuilder extends ListMenuBuilder {

    public CustomListMenuBuilder(MenuConfig menuConfig,
                                 ViewRegistry viewRegistry,
                                 UiComponents uiComponents,
                                 MessageTools messageTools,
                                 UiAccessChecker uiAccessChecker,
                                 MenuItemCommands menuItemCommands) {
        super(menuConfig, viewRegistry, uiComponents, messageTools, uiAccessChecker, menuItemCommands);
    }

    @Override
    protected void setIcon(MenuItem menuItem, ListMenu.MenuItem listMenuItem) {
        if (!Strings.isNullOrEmpty(menuItem.getIcon())) {
            VaadinIcon vaadinIcon = getVaadinIcon(menuItem.getIcon());
            listMenuItem.withIcon(vaadinIcon)
                    .setPrefixComponent(ComponentUtils.parseIcon(menuItem.getIcon()));
        }
    }
}
