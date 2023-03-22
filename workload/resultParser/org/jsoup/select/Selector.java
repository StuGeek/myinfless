// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.select;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.IdentityHashMap;
import java.util.ArrayList;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Element;

public class Selector
{
    private Selector() {
    }
    
    public static Elements select(final String query, final Element root) {
        Validate.notEmpty(query);
        return select(QueryParser.parse(query), root);
    }
    
    public static Elements select(final Evaluator evaluator, final Element root) {
        Validate.notNull(evaluator);
        Validate.notNull(root);
        return Collector.collect(evaluator, root);
    }
    
    public static Elements select(final String query, final Iterable<Element> roots) {
        Validate.notEmpty(query);
        Validate.notNull(roots);
        final Evaluator evaluator = QueryParser.parse(query);
        final ArrayList<Element> elements = new ArrayList<Element>();
        final IdentityHashMap<Element, Boolean> seenElements = new IdentityHashMap<Element, Boolean>();
        for (final Element root : roots) {
            final Elements found = select(evaluator, root);
            for (final Element el : found) {
                if (!seenElements.containsKey(el)) {
                    elements.add(el);
                    seenElements.put(el, Boolean.TRUE);
                }
            }
        }
        return new Elements(elements);
    }
    
    static Elements filterOut(final Collection<Element> elements, final Collection<Element> outs) {
        final Elements output = new Elements();
        for (final Element el : elements) {
            boolean found = false;
            for (final Element out : outs) {
                if (el.equals(out)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                output.add(el);
            }
        }
        return output;
    }
    
    public static Element selectFirst(final String cssQuery, final Element root) {
        Validate.notEmpty(cssQuery);
        return Collector.findFirst(QueryParser.parse(cssQuery), root);
    }
    
    public static class SelectorParseException extends IllegalStateException
    {
        public SelectorParseException(final String msg, final Object... params) {
            super(String.format(msg, params));
        }
    }
}
