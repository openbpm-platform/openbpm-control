package io.openbpm.control.view.about;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.view.MessageBundle;
import io.jmix.flowui.view.ViewComponent;

@FragmentDescriptor("product-info-fragment.xml")
public class ProductInfoFragment extends Fragment<VerticalLayout> {
    @ViewComponent
    private MessageBundle messageBundle;

    @ViewComponent
    protected Span nameLabel;
    @ViewComponent
    protected Span descriptionLabel;
    @ViewComponent
    protected ExternalLinkFragment learnMoreLink;

    public void setProduct(AboutProductMetadata.Product product) {
        nameLabel.setText(product.getName());
        if (!product.getReleased()) {
            nameLabel.addClassNames(LumoUtility.TextColor.SECONDARY);
        }

        descriptionLabel.setText(product.getDescription());

        learnMoreLink.setLink(messageBundle.getMessage("learnMore"), product.getUrl());
    }
}