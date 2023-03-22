// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.select;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.Element;

public class Collector
{
    private Collector() {
    }
    
    public static Elements collect(final Evaluator eval, final Element root) {
        final Elements elements = new Elements();
        NodeTraversor.traverse(new Accumulator(root, elements, eval), root);
        return elements;
    }
    
    public static Element findFirst(final Evaluator eval, final Element root) {
        final FirstFinder finder = new FirstFinder(root, eval);
        NodeTraversor.filter(finder, root);
        return finder.match;
    }
    
    private static class Accumulator implements NodeVisitor
    {
        private final Element root;
        private final Elements elements;
        private final Evaluator eval;
        
        Accumulator(final Element root, final Elements elements, final Evaluator eval) {
            this.root = root;
            this.elements = elements;
            this.eval = eval;
        }
        
        @Override
        public void head(final Node node, final int depth) {
            if (node instanceof Element) {
                final Element el = (Element)node;
                if (this.eval.matches(this.root, el)) {
                    this.elements.add(el);
                }
            }
        }
        
        @Override
        public void tail(final Node node, final int depth) {
        }
    }
    
    private static class FirstFinder implements NodeFilter
    {
        private final Element root;
        private Element match;
        private final Evaluator eval;
        
        FirstFinder(final Element root, final Evaluator eval) {
            this.match = null;
            this.root = root;
            this.eval = eval;
        }
        
        @Override
        public FilterResult head(final Node node, final int depth) {
            if (node instanceof Element) {
                final Element el = (Element)node;
                if (this.eval.matches(this.root, el)) {
                    this.match = el;
                    return FilterResult.STOP;
                }
            }
            return FilterResult.CONTINUE;
        }
        
        @Override
        public FilterResult tail(final Node node, final int depth) {
            return FilterResult.CONTINUE;
        }
    }
}
