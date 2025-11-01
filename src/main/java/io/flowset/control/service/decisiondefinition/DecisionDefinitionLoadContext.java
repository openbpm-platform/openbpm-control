package io.flowset.control.service.decisiondefinition;

import io.jmix.core.Sort;
import io.flowset.control.entity.filter.DecisionDefinitionFilter;
import io.flowset.control.service.ItemListLoadContext;

/**
 * A context that contains the following options to load decision definitions:
 * <ul>
 *     <li>Pagination options: first and max results</li>
 *     <li>Sort options: property and direction to sort. Supported properties:  "name", "key", "version".</li>
 *     <li>Filtering options</li>
 * </ul>
 * <p>
 * Example: load first 50 decision definitions considering latest versions only and sort them by key
 * <pre>
 *     DecisionDefinitionFilter filter = metadata.create(DecisionDefinitionFilter.class);
 *     filter.setLatestVersionOnly(true);
 *
 *     DecisionDefinitionLoadContext context = new DecisionDefinitionLoadContext()
 *      .setFirstResult(0)
 *      .setMaxResults(50)
 *      .setFilter(filter)
 *      .setSort(Sort.by(Sort.Direction.ASC, "key"));
 * </pre>
 *
 * @see DecisionDefinitionFilter
 * @see io.jmix.core.Metadata
 * @see Sort
 */
public class DecisionDefinitionLoadContext extends ItemListLoadContext<DecisionDefinitionFilter> {
    /**
     * Sets a decision definition filter.
     *
     * @param filter an instance of {@link DecisionDefinitionFilter}
     * @return current context
     */
    public DecisionDefinitionLoadContext setFilter(DecisionDefinitionFilter filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Sets a first result number.
     *
     * @param firstResult first result
     * @return current context
     */
    public DecisionDefinitionLoadContext setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    /**
     * Sets a max number of results.
     *
     * @param maxResults max results
     * @return current context
     */
    public DecisionDefinitionLoadContext setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    /**
     * Sets sort options.
     *
     * @param sort sort options
     * @return current context
     */
    public DecisionDefinitionLoadContext setSort(Sort sort) {
        this.sort = sort;
        return this;
    }
}