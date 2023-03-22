// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.select;

import java.util.Iterator;
import org.jsoup.nodes.Element;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Node;

public class NodeTraversor
{
    private NodeVisitor visitor;
    
    @Deprecated
    public NodeTraversor(final NodeVisitor visitor) {
        this.visitor = visitor;
    }
    
    @Deprecated
    public void traverse(final Node root) {
        traverse(this.visitor, root);
    }
    
    public static void traverse(final NodeVisitor visitor, final Node root) {
        Node node = root;
        int depth = 0;
        while (node != null) {
            visitor.head(node, depth);
            if (node.childNodeSize() > 0) {
                node = node.childNode(0);
                ++depth;
            }
            else {
                while (node.nextSibling() == null && depth > 0) {
                    visitor.tail(node, depth);
                    node = node.parentNode();
                    --depth;
                }
                visitor.tail(node, depth);
                if (node == root) {
                    break;
                }
                node = node.nextSibling();
            }
        }
    }
    
    public static void traverse(final NodeVisitor visitor, final Elements elements) {
        Validate.notNull(visitor);
        Validate.notNull(elements);
        for (final Element el : elements) {
            traverse(visitor, el);
        }
    }
    
    public static NodeFilter.FilterResult filter(final NodeFilter filter, final Node root) {
        Node node = root;
        int depth = 0;
        while (node != null) {
            NodeFilter.FilterResult result = filter.head(node, depth);
            if (result == NodeFilter.FilterResult.STOP) {
                return result;
            }
            if (result == NodeFilter.FilterResult.CONTINUE && node.childNodeSize() > 0) {
                node = node.childNode(0);
                ++depth;
            }
            else {
                while (node.nextSibling() == null && depth > 0) {
                    if (result == NodeFilter.FilterResult.CONTINUE || result == NodeFilter.FilterResult.SKIP_CHILDREN) {
                        result = filter.tail(node, depth);
                        if (result == NodeFilter.FilterResult.STOP) {
                            return result;
                        }
                    }
                    final Node prev = node;
                    node = node.parentNode();
                    --depth;
                    if (result == NodeFilter.FilterResult.REMOVE) {
                        prev.remove();
                    }
                    result = NodeFilter.FilterResult.CONTINUE;
                }
                if (result == NodeFilter.FilterResult.CONTINUE || result == NodeFilter.FilterResult.SKIP_CHILDREN) {
                    result = filter.tail(node, depth);
                    if (result == NodeFilter.FilterResult.STOP) {
                        return result;
                    }
                }
                if (node == root) {
                    return result;
                }
                final Node prev = node;
                node = node.nextSibling();
                if (result != NodeFilter.FilterResult.REMOVE) {
                    continue;
                }
                prev.remove();
            }
        }
        return NodeFilter.FilterResult.CONTINUE;
    }
    
    public static void filter(final NodeFilter filter, final Elements elements) {
        Validate.notNull(filter);
        Validate.notNull(elements);
        for (final Element el : elements) {
            if (filter(filter, el) == NodeFilter.FilterResult.STOP) {
                break;
            }
        }
    }
}
