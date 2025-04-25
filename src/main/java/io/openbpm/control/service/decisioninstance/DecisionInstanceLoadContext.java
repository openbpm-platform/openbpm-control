package io.openbpm.control.service.decisioninstance;

import io.jmix.core.Sort;
import io.openbpm.control.entity.filter.DecisionInstanceFilter;
import io.openbpm.control.service.ItemListLoadContext;

/**
 * A context that contains the following options to load decision instances:
 *
 * @see DecisionInstanceFilter
 * @see io.jmix.core.Metadata
 * @see Sort
 */
public class DecisionInstanceLoadContext extends ItemListLoadContext<DecisionInstanceFilter> {

    /**
     * Sets a decision instance filter.
     *
     * @param filter an instance of {@link DecisionInstanceFilter}
     * @return current context
     */
    public DecisionInstanceLoadContext setFilter(DecisionInstanceFilter filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Sets a first result number.
     *
     * @param firstResult first result
     * @return current context
     */
    public DecisionInstanceLoadContext setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    /**
     * Sets a max number of results.
     *
     * @param maxResults max results
     * @return current context
     */
    public DecisionInstanceLoadContext setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    /**
     * Sets sort options.
     *
     * @param sort sort options
     * @return current context
     */
    public DecisionInstanceLoadContext setSort(Sort sort) {
        this.sort = sort;
        return this;
    }
}
