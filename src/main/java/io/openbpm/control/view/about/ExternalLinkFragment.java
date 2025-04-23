package io.openbpm.control.view.about;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.view.ViewComponent;

@FragmentDescriptor("external-link-fragment.xml")
public class ExternalLinkFragment extends Fragment<FlexLayout> {

    @ViewComponent
    protected Anchor urlField;

    public void setLink(String text, String href) {
        urlField.setText(text);
        urlField.setHref(href);

        if (href == null) {
            getContent().setVisible(false);
        }
    }
}