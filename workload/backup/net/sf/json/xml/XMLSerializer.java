// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.xml;

import nu.xom.ParentNode;
import nu.xom.Serializer;
import org.apache.commons.logging.LogFactory;
import java.io.UnsupportedEncodingException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import net.sf.json.JSONFunction;
import net.sf.json.util.JSONUtils;
import org.apache.commons.lang.ArrayUtils;
import java.util.Arrays;
import nu.xom.Node;
import nu.xom.Elements;
import nu.xom.Text;
import java.util.Iterator;
import net.sf.json.JSONArray;
import nu.xom.Attribute;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import nu.xom.Element;
import nu.xom.Document;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONNull;
import java.io.Reader;
import java.io.StringReader;
import nu.xom.Builder;
import net.sf.json.JSON;
import org.apache.commons.lang.StringUtils;
import java.util.TreeMap;
import java.util.Map;
import org.apache.commons.logging.Log;

public class XMLSerializer
{
    private static final String[] EMPTY_ARRAY;
    private static final String JSON_PREFIX = "json_";
    private static final Log log;
    private String arrayName;
    private String elementName;
    private String[] expandableProperties;
    private boolean forceTopLevelObject;
    private boolean namespaceLenient;
    private Map namespacesPerElement;
    private String objectName;
    private boolean removeNamespacePrefixFromElements;
    private String rootName;
    private Map rootNamespace;
    private boolean skipNamespaces;
    private boolean skipWhitespace;
    private boolean trimSpaces;
    private boolean typeHintsCompatibility;
    private boolean typeHintsEnabled;
    
    public XMLSerializer() {
        this.namespacesPerElement = new TreeMap();
        this.rootNamespace = new TreeMap();
        this.setObjectName("o");
        this.setArrayName("a");
        this.setElementName("e");
        this.setTypeHintsEnabled(true);
        this.setTypeHintsCompatibility(true);
        this.setNamespaceLenient(false);
        this.setSkipNamespaces(false);
        this.setRemoveNamespacePrefixFromElements(false);
        this.setTrimSpaces(false);
        this.setExpandableProperties(XMLSerializer.EMPTY_ARRAY);
        this.setSkipNamespaces(false);
    }
    
    public void addNamespace(final String prefix, final String uri) {
        this.addNamespace(prefix, uri, null);
    }
    
    public void addNamespace(String prefix, final String uri, final String elementName) {
        if (StringUtils.isBlank(uri)) {
            return;
        }
        if (prefix == null) {
            prefix = "";
        }
        if (StringUtils.isBlank(elementName)) {
            this.rootNamespace.put(prefix.trim(), uri.trim());
        }
        else {
            Map nameSpaces = this.namespacesPerElement.get(elementName);
            if (nameSpaces == null) {
                nameSpaces = new TreeMap();
                this.namespacesPerElement.put(elementName, nameSpaces);
            }
            nameSpaces.put(prefix, uri);
        }
    }
    
    public void clearNamespaces() {
        this.rootNamespace.clear();
        this.namespacesPerElement.clear();
    }
    
    public void clearNamespaces(final String elementName) {
        if (StringUtils.isBlank(elementName)) {
            this.rootNamespace.clear();
        }
        else {
            this.namespacesPerElement.remove(elementName);
        }
    }
    
    public String getArrayName() {
        return this.arrayName;
    }
    
    public String getElementName() {
        return this.elementName;
    }
    
    public String[] getExpandableProperties() {
        return this.expandableProperties;
    }
    
    public String getObjectName() {
        return this.objectName;
    }
    
    public String getRootName() {
        return this.rootName;
    }
    
    public boolean isForceTopLevelObject() {
        return this.forceTopLevelObject;
    }
    
    public boolean isNamespaceLenient() {
        return this.namespaceLenient;
    }
    
    public boolean isRemoveNamespacePrefixFromElements() {
        return this.removeNamespacePrefixFromElements;
    }
    
    public boolean isSkipNamespaces() {
        return this.skipNamespaces;
    }
    
    public boolean isSkipWhitespace() {
        return this.skipWhitespace;
    }
    
    public boolean isTrimSpaces() {
        return this.trimSpaces;
    }
    
    public boolean isTypeHintsCompatibility() {
        return this.typeHintsCompatibility;
    }
    
    public boolean isTypeHintsEnabled() {
        return this.typeHintsEnabled;
    }
    
    public JSON read(final String xml) {
        JSON json = null;
        try {
            final Document doc = new Builder().build((Reader)new StringReader(xml));
            final Element root = doc.getRootElement();
            if (this.isNullObject(root)) {
                return JSONNull.getInstance();
            }
            final String defaultType = this.getType(root, "string");
            if (this.isArray(root, true)) {
                json = this.processArrayElement(root, defaultType);
                if (this.forceTopLevelObject) {
                    final String key = this.removeNamespacePrefix(root.getQualifiedName());
                    json = new JSONObject().element(key, json);
                }
            }
            else {
                json = this.processObjectElement(root, defaultType);
                if (this.forceTopLevelObject) {
                    final String key = this.removeNamespacePrefix(root.getQualifiedName());
                    json = new JSONObject().element(key, json);
                }
            }
        }
        catch (JSONException jsone) {
            throw jsone;
        }
        catch (Exception e) {
            throw new JSONException(e);
        }
        return json;
    }
    
    public JSON readFromFile(final File file) {
        if (file == null) {
            throw new JSONException("File is null");
        }
        if (!file.canRead()) {
            throw new JSONException("Can't read input file");
        }
        if (file.isDirectory()) {
            throw new JSONException("File is a directory");
        }
        try {
            return this.readFromStream(new FileInputStream(file));
        }
        catch (IOException ioe) {
            throw new JSONException(ioe);
        }
    }
    
    public JSON readFromFile(final String path) {
        return this.readFromStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
    }
    
    public JSON readFromStream(final InputStream stream) {
        try {
            final StringBuffer xml = new StringBuffer();
            final BufferedReader in = new BufferedReader(new InputStreamReader(stream));
            String line = null;
            while ((line = in.readLine()) != null) {
                xml.append(line);
            }
            return this.read(xml.toString());
        }
        catch (IOException ioe) {
            throw new JSONException(ioe);
        }
    }
    
    public void removeNamespace(final String prefix) {
        this.removeNamespace(prefix, null);
    }
    
    public void removeNamespace(String prefix, final String elementName) {
        if (prefix == null) {
            prefix = "";
        }
        if (StringUtils.isBlank(elementName)) {
            this.rootNamespace.remove(prefix.trim());
        }
        else {
            final Map nameSpaces = this.namespacesPerElement.get(elementName);
            nameSpaces.remove(prefix);
        }
    }
    
    public void setArrayName(final String arrayName) {
        this.arrayName = (StringUtils.isBlank(arrayName) ? "a" : arrayName);
    }
    
    public void setElementName(final String elementName) {
        this.elementName = (StringUtils.isBlank(elementName) ? "e" : elementName);
    }
    
    public void setExpandableProperties(final String[] expandableProperties) {
        this.expandableProperties = ((expandableProperties == null) ? XMLSerializer.EMPTY_ARRAY : expandableProperties);
    }
    
    public void setForceTopLevelObject(final boolean forceTopLevelObject) {
        this.forceTopLevelObject = forceTopLevelObject;
    }
    
    public void setNamespace(final String prefix, final String uri) {
        this.setNamespace(prefix, uri, null);
    }
    
    public void setNamespace(String prefix, final String uri, final String elementName) {
        if (StringUtils.isBlank(uri)) {
            return;
        }
        if (prefix == null) {
            prefix = "";
        }
        if (StringUtils.isBlank(elementName)) {
            this.rootNamespace.clear();
            this.rootNamespace.put(prefix.trim(), uri.trim());
        }
        else {
            Map nameSpaces = this.namespacesPerElement.get(elementName);
            if (nameSpaces == null) {
                nameSpaces = new TreeMap();
                this.namespacesPerElement.put(elementName, nameSpaces);
            }
            nameSpaces.clear();
            nameSpaces.put(prefix, uri);
        }
    }
    
    public void setNamespaceLenient(final boolean namespaceLenient) {
        this.namespaceLenient = namespaceLenient;
    }
    
    public void setObjectName(final String objectName) {
        this.objectName = (StringUtils.isBlank(objectName) ? "o" : objectName);
    }
    
    public void setRemoveNamespacePrefixFromElements(final boolean removeNamespacePrefixFromElements) {
        this.removeNamespacePrefixFromElements = removeNamespacePrefixFromElements;
    }
    
    public void setRootName(final String rootName) {
        this.rootName = (StringUtils.isBlank(rootName) ? null : rootName);
    }
    
    public void setSkipNamespaces(final boolean skipNamespaces) {
        this.skipNamespaces = skipNamespaces;
    }
    
    public void setSkipWhitespace(final boolean skipWhitespace) {
        this.skipWhitespace = skipWhitespace;
    }
    
    public void setTrimSpaces(final boolean trimSpaces) {
        this.trimSpaces = trimSpaces;
    }
    
    public void setTypeHintsCompatibility(final boolean typeHintsCompatibility) {
        this.typeHintsCompatibility = typeHintsCompatibility;
    }
    
    public void setTypeHintsEnabled(final boolean typeHintsEnabled) {
        this.typeHintsEnabled = typeHintsEnabled;
    }
    
    public String write(final JSON json) {
        return this.write(json, null);
    }
    
    public String write(final JSON json, final String encoding) {
        if (JSONNull.getInstance().equals(json)) {
            Element root = null;
            root = this.newElement((this.getRootName() == null) ? this.getObjectName() : this.getRootName());
            root.addAttribute(new Attribute(this.addJsonPrefix("null"), "true"));
            final Document doc = new Document(root);
            return this.writeDocument(doc, encoding);
        }
        if (json instanceof JSONArray) {
            final JSONArray jsonArray = (JSONArray)json;
            final Element root2 = this.processJSONArray(jsonArray, this.newElement((this.getRootName() == null) ? this.getArrayName() : this.getRootName()), this.expandableProperties);
            final Document doc2 = new Document(root2);
            return this.writeDocument(doc2, encoding);
        }
        final JSONObject jsonObject = (JSONObject)json;
        Element root2 = null;
        if (jsonObject.isNullObject()) {
            root2 = this.newElement(this.getObjectName());
            root2.addAttribute(new Attribute(this.addJsonPrefix("null"), "true"));
        }
        else {
            root2 = this.processJSONObject(jsonObject, this.newElement((this.getRootName() == null) ? this.getObjectName() : this.getRootName()), this.expandableProperties, true);
        }
        final Document doc2 = new Document(root2);
        return this.writeDocument(doc2, encoding);
    }
    
    private String addJsonPrefix(final String str) {
        if (!this.isTypeHintsCompatibility()) {
            return "json_" + str;
        }
        return str;
    }
    
    private void addNameSpaceToElement(final Element element) {
        String elementName = null;
        if (element instanceof CustomElement) {
            elementName = ((CustomElement)element).getQName();
        }
        else {
            elementName = element.getQualifiedName();
        }
        final Map nameSpaces = this.namespacesPerElement.get(elementName);
        if (nameSpaces != null && !nameSpaces.isEmpty()) {
            this.setNamespaceLenient(true);
            for (final Map.Entry entry : nameSpaces.entrySet()) {
                final String prefix = entry.getKey();
                final String uri = entry.getValue();
                if (StringUtils.isBlank(prefix)) {
                    element.setNamespaceURI(uri);
                }
                else {
                    element.addNamespaceDeclaration(prefix, uri);
                }
            }
        }
    }
    
    private boolean checkChildElements(final Element element, final boolean isTopLevel) {
        final int childCount = ((ParentNode)element).getChildCount();
        final Elements elements = element.getChildElements();
        final int elementCount = elements.size();
        if (childCount == 1 && ((ParentNode)element).getChild(0) instanceof Text) {
            return isTopLevel;
        }
        if (childCount == elementCount) {
            if (elementCount == 0) {
                return true;
            }
            if (elementCount == 1) {
                return this.skipWhitespace || ((ParentNode)element).getChild(0) instanceof Text;
            }
        }
        if (childCount > elementCount) {
            for (int i = 0; i < childCount; ++i) {
                final Node node = ((ParentNode)element).getChild(i);
                if (node instanceof Text) {
                    final Text text = (Text)node;
                    if (StringUtils.isNotBlank(StringUtils.strip(text.getValue())) && !this.skipWhitespace) {
                        return false;
                    }
                }
            }
        }
        final String childName = elements.get(0).getQualifiedName();
        for (int j = 1; j < elementCount; ++j) {
            if (childName.compareTo(elements.get(j).getQualifiedName()) != 0) {
                return false;
            }
        }
        return true;
    }
    
    private String getClass(final Element element) {
        final Attribute attribute = element.getAttribute(this.addJsonPrefix("class"));
        String clazz = null;
        if (attribute != null) {
            final String clazzText = attribute.getValue().trim();
            if ("object".compareToIgnoreCase(clazzText) == 0) {
                clazz = "object";
            }
            else if ("array".compareToIgnoreCase(clazzText) == 0) {
                clazz = "array";
            }
        }
        return clazz;
    }
    
    private String getType(final Element element) {
        return this.getType(element, null);
    }
    
    private String getType(final Element element, final String defaultType) {
        final Attribute attribute = element.getAttribute(this.addJsonPrefix("type"));
        String type = null;
        if (attribute != null) {
            final String typeText = attribute.getValue().trim();
            if ("boolean".compareToIgnoreCase(typeText) == 0) {
                type = "boolean";
            }
            else if ("number".compareToIgnoreCase(typeText) == 0) {
                type = "number";
            }
            else if ("integer".compareToIgnoreCase(typeText) == 0) {
                type = "integer";
            }
            else if ("float".compareToIgnoreCase(typeText) == 0) {
                type = "float";
            }
            else if ("object".compareToIgnoreCase(typeText) == 0) {
                type = "object";
            }
            else if ("array".compareToIgnoreCase(typeText) == 0) {
                type = "array";
            }
            else if ("string".compareToIgnoreCase(typeText) == 0) {
                type = "string";
            }
            else if ("function".compareToIgnoreCase(typeText) == 0) {
                type = "function";
            }
        }
        else if (defaultType != null) {
            XMLSerializer.log.info("Using default type " + defaultType);
            type = defaultType;
        }
        return type;
    }
    
    private boolean hasNamespaces(final Element element) {
        int namespaces = 0;
        for (int i = 0; i < element.getNamespaceDeclarationCount(); ++i) {
            final String prefix = element.getNamespacePrefix(i);
            final String uri = element.getNamespaceURI(prefix);
            if (!StringUtils.isBlank(uri)) {
                ++namespaces;
            }
        }
        return namespaces > 0;
    }
    
    private boolean isArray(final Element element, final boolean isTopLevel) {
        boolean isArray = false;
        final String clazz = this.getClass(element);
        if (clazz != null && clazz.equals("array")) {
            isArray = true;
        }
        else if (element.getAttributeCount() == 0) {
            isArray = this.checkChildElements(element, isTopLevel);
        }
        else if (element.getAttributeCount() == 1 && (element.getAttribute(this.addJsonPrefix("class")) != null || element.getAttribute(this.addJsonPrefix("type")) != null)) {
            isArray = this.checkChildElements(element, isTopLevel);
        }
        else if (element.getAttributeCount() == 2 && element.getAttribute(this.addJsonPrefix("class")) != null && element.getAttribute(this.addJsonPrefix("type")) != null) {
            isArray = this.checkChildElements(element, isTopLevel);
        }
        if (isArray) {
            for (int j = 0; j < element.getNamespaceDeclarationCount(); ++j) {
                final String prefix = element.getNamespacePrefix(j);
                final String uri = element.getNamespaceURI(prefix);
                if (!StringUtils.isBlank(uri)) {
                    return false;
                }
            }
        }
        return isArray;
    }
    
    private boolean isFunction(final Element element) {
        final int attrCount = element.getAttributeCount();
        if (attrCount > 0) {
            final Attribute typeAttr = element.getAttribute(this.addJsonPrefix("type"));
            final Attribute paramsAttr = element.getAttribute(this.addJsonPrefix("params"));
            if (attrCount == 1 && paramsAttr != null) {
                return true;
            }
            if (attrCount == 2 && paramsAttr != null && typeAttr != null && (typeAttr.getValue().compareToIgnoreCase("string") == 0 || typeAttr.getValue().compareToIgnoreCase("function") == 0)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isNullObject(final Element element) {
        if (((ParentNode)element).getChildCount() == 0) {
            if (element.getAttributeCount() == 0) {
                return true;
            }
            if (element.getAttribute(this.addJsonPrefix("null")) != null) {
                return true;
            }
            if (element.getAttributeCount() == 1 && (element.getAttribute(this.addJsonPrefix("class")) != null || element.getAttribute(this.addJsonPrefix("type")) != null)) {
                return true;
            }
            if (element.getAttributeCount() == 2 && element.getAttribute(this.addJsonPrefix("class")) != null && element.getAttribute(this.addJsonPrefix("type")) != null) {
                return true;
            }
        }
        return this.skipWhitespace && ((ParentNode)element).getChildCount() == 1 && ((ParentNode)element).getChild(0) instanceof Text;
    }
    
    private boolean isObject(final Element element, final boolean isTopLevel) {
        boolean isObject = false;
        if (!this.isArray(element, isTopLevel) && !this.isFunction(element)) {
            if (this.hasNamespaces(element)) {
                return true;
            }
            final int attributeCount = element.getAttributeCount();
            if (attributeCount > 0) {
                int attrs = (element.getAttribute(this.addJsonPrefix("null")) != null) ? 1 : 0;
                attrs += ((element.getAttribute(this.addJsonPrefix("class")) != null) ? 1 : 0);
                attrs += ((element.getAttribute(this.addJsonPrefix("type")) != null) ? 1 : 0);
                switch (attributeCount) {
                    case 1: {
                        if (attrs == 0) {
                            return true;
                        }
                        break;
                    }
                    case 2: {
                        if (attrs < 2) {
                            return true;
                        }
                        break;
                    }
                    case 3: {
                        if (attrs < 3) {
                            return true;
                        }
                        break;
                    }
                    default: {
                        return true;
                    }
                }
            }
            final int childCount = ((ParentNode)element).getChildCount();
            if (childCount == 1 && ((ParentNode)element).getChild(0) instanceof Text) {
                return isTopLevel;
            }
            isObject = true;
        }
        return isObject;
    }
    
    private Element newElement(final String name) {
        if (name.indexOf(58) != -1) {
            this.namespaceLenient = true;
        }
        return this.namespaceLenient ? new CustomElement(name) : new Element(name);
    }
    
    private JSON processArrayElement(final Element element, final String defaultType) {
        final JSONArray jsonArray = new JSONArray();
        for (int childCount = ((ParentNode)element).getChildCount(), i = 0; i < childCount; ++i) {
            final Node child = ((ParentNode)element).getChild(i);
            if (child instanceof Text) {
                final Text text = (Text)child;
                if (StringUtils.isNotBlank(StringUtils.strip(text.getValue()))) {
                    jsonArray.element(text.getValue());
                }
            }
            else if (child instanceof Element) {
                this.setValue(jsonArray, (Element)child, defaultType);
            }
        }
        return jsonArray;
    }
    
    private Object processElement(final Element element, final String type) {
        if (this.isNullObject(element)) {
            return JSONNull.getInstance();
        }
        if (this.isArray(element, false)) {
            return this.processArrayElement(element, type);
        }
        if (this.isObject(element, false)) {
            return this.processObjectElement(element, type);
        }
        return this.trimSpaceFromValue(element.getValue());
    }
    
    private Element processJSONArray(final JSONArray array, final Element root, final String[] expandableProperties) {
        for (int l = array.size(), i = 0; i < l; ++i) {
            final Object value = array.get(i);
            final Element element = this.processJSONValue(value, root, null, expandableProperties);
            ((ParentNode)root).appendChild((Node)element);
        }
        return root;
    }
    
    private Element processJSONObject(final JSONObject jsonObject, final Element root, final String[] expandableProperties, final boolean isRoot) {
        if (jsonObject.isNullObject()) {
            root.addAttribute(new Attribute(this.addJsonPrefix("null"), "true"));
            return root;
        }
        if (jsonObject.isEmpty()) {
            return root;
        }
        if (isRoot && !this.rootNamespace.isEmpty()) {
            this.setNamespaceLenient(true);
            for (final Map.Entry entry : this.rootNamespace.entrySet()) {
                final String prefix = entry.getKey();
                final String uri = entry.getValue();
                if (StringUtils.isBlank(prefix)) {
                    root.setNamespaceURI(uri);
                }
                else {
                    root.addNamespaceDeclaration(prefix, uri);
                }
            }
        }
        this.addNameSpaceToElement(root);
        final Object[] names = jsonObject.names().toArray();
        Arrays.sort(names);
        Element element = null;
        for (int i = 0; i < names.length; ++i) {
            final String name = (String)names[i];
            final Object value = jsonObject.get(name);
            if (name.startsWith("@xmlns")) {
                this.setNamespaceLenient(true);
                final int colon = name.indexOf(58);
                if (colon == -1) {
                    if (StringUtils.isBlank(root.getNamespaceURI())) {
                        root.setNamespaceURI(String.valueOf(value));
                    }
                }
                else {
                    final String prefix2 = name.substring(colon + 1);
                    if (StringUtils.isBlank(root.getNamespaceURI(prefix2))) {
                        root.addNamespaceDeclaration(prefix2, String.valueOf(value));
                    }
                }
            }
            else if (name.startsWith("@")) {
                root.addAttribute(new Attribute(name.substring(1), String.valueOf(value)));
            }
            else if (name.equals("#text")) {
                if (value instanceof JSONArray) {
                    root.appendChild(((JSONArray)value).join("", true));
                }
                else {
                    root.appendChild(String.valueOf(value));
                }
            }
            else if (value instanceof JSONArray && (((JSONArray)value).isExpandElements() || ArrayUtils.contains(expandableProperties, name))) {
                final JSONArray array = (JSONArray)value;
                for (int l = array.size(), j = 0; j < l; ++j) {
                    final Object item = array.get(j);
                    element = this.newElement(name);
                    if (item instanceof JSONObject) {
                        element = this.processJSONValue(item, root, element, expandableProperties);
                    }
                    else if (item instanceof JSONArray) {
                        element = this.processJSONValue(item, root, element, expandableProperties);
                    }
                    else {
                        element = this.processJSONValue(item, root, element, expandableProperties);
                    }
                    this.addNameSpaceToElement(element);
                    ((ParentNode)root).appendChild((Node)element);
                }
            }
            else {
                element = this.newElement(name);
                element = this.processJSONValue(value, root, element, expandableProperties);
                this.addNameSpaceToElement(element);
                ((ParentNode)root).appendChild((Node)element);
            }
        }
        return root;
    }
    
    private Element processJSONValue(Object value, final Element root, Element target, final String[] expandableProperties) {
        if (target == null) {
            target = this.newElement(this.getElementName());
        }
        if (JSONUtils.isBoolean(value)) {
            if (this.isTypeHintsEnabled()) {
                target.addAttribute(new Attribute(this.addJsonPrefix("type"), "boolean"));
            }
            target.appendChild(value.toString());
        }
        else if (JSONUtils.isNumber(value)) {
            if (this.isTypeHintsEnabled()) {
                target.addAttribute(new Attribute(this.addJsonPrefix("type"), "number"));
            }
            target.appendChild(value.toString());
        }
        else if (JSONUtils.isFunction(value)) {
            if (value instanceof String) {
                value = JSONFunction.parse((String)value);
            }
            final JSONFunction func = (JSONFunction)value;
            if (this.isTypeHintsEnabled()) {
                target.addAttribute(new Attribute(this.addJsonPrefix("type"), "function"));
            }
            String params = ArrayUtils.toString(func.getParams());
            params = params.substring(1);
            params = params.substring(0, params.length() - 1);
            target.addAttribute(new Attribute(this.addJsonPrefix("params"), params));
            ((ParentNode)target).appendChild((Node)new Text("<![CDATA[" + func.getText() + "]]>"));
        }
        else if (JSONUtils.isString(value)) {
            if (this.isTypeHintsEnabled()) {
                target.addAttribute(new Attribute(this.addJsonPrefix("type"), "string"));
            }
            target.appendChild(value.toString());
        }
        else if (value instanceof JSONArray) {
            if (this.isTypeHintsEnabled()) {
                target.addAttribute(new Attribute(this.addJsonPrefix("class"), "array"));
            }
            target = this.processJSONArray((JSONArray)value, target, expandableProperties);
        }
        else if (value instanceof JSONObject) {
            if (this.isTypeHintsEnabled()) {
                target.addAttribute(new Attribute(this.addJsonPrefix("class"), "object"));
            }
            target = this.processJSONObject((JSONObject)value, target, expandableProperties, false);
        }
        else if (JSONUtils.isNull(value)) {
            if (this.isTypeHintsEnabled()) {
                target.addAttribute(new Attribute(this.addJsonPrefix("class"), "object"));
            }
            target.addAttribute(new Attribute(this.addJsonPrefix("null"), "true"));
        }
        return target;
    }
    
    private JSON processObjectElement(final Element element, final String defaultType) {
        if (this.isNullObject(element)) {
            return JSONNull.getInstance();
        }
        final JSONObject jsonObject = new JSONObject();
        if (!this.skipNamespaces) {
            for (int j = 0; j < element.getNamespaceDeclarationCount(); ++j) {
                String prefix = element.getNamespacePrefix(j);
                final String uri = element.getNamespaceURI(prefix);
                if (!StringUtils.isBlank(uri)) {
                    if (!StringUtils.isBlank(prefix)) {
                        prefix = ":" + prefix;
                    }
                    this.setOrAccumulate(jsonObject, "@xmlns" + prefix, this.trimSpaceFromValue(uri));
                }
            }
        }
        for (int attrCount = element.getAttributeCount(), i = 0; i < attrCount; ++i) {
            final Attribute attr = element.getAttribute(i);
            final String attrname = attr.getQualifiedName();
            if (this.isTypeHintsEnabled()) {
                if (this.addJsonPrefix("class").compareToIgnoreCase(attrname) == 0) {
                    continue;
                }
                if (this.addJsonPrefix("type").compareToIgnoreCase(attrname) == 0) {
                    continue;
                }
            }
            final String attrvalue = attr.getValue();
            this.setOrAccumulate(jsonObject, "@" + this.removeNamespacePrefix(attrname), this.trimSpaceFromValue(attrvalue));
        }
        for (int childCount = ((ParentNode)element).getChildCount(), k = 0; k < childCount; ++k) {
            final Node child = ((ParentNode)element).getChild(k);
            if (child instanceof Text) {
                final Text text = (Text)child;
                if (StringUtils.isNotBlank(StringUtils.strip(text.getValue()))) {
                    this.setOrAccumulate(jsonObject, "#text", this.trimSpaceFromValue(text.getValue()));
                }
            }
            else if (child instanceof Element) {
                this.setValue(jsonObject, (Element)child, defaultType);
            }
        }
        return jsonObject;
    }
    
    private String removeNamespacePrefix(final String name) {
        if (this.isRemoveNamespacePrefixFromElements()) {
            final int colon = name.indexOf(58);
            return (colon != -1) ? name.substring(colon + 1) : name;
        }
        return name;
    }
    
    private void setOrAccumulate(final JSONObject jsonObject, final String key, final Object value) {
        if (jsonObject.has(key)) {
            jsonObject.accumulate(key, value);
            final Object val = jsonObject.get(key);
            if (val instanceof JSONArray) {
                ((JSONArray)val).setExpandElements(true);
            }
        }
        else {
            jsonObject.element(key, value);
        }
    }
    
    private void setValue(final JSONArray jsonArray, final Element element, final String defaultType) {
        final String clazz = this.getClass(element);
        String type = this.getType(element);
        type = ((type == null) ? defaultType : type);
        if (this.hasNamespaces(element) && !this.skipNamespaces) {
            jsonArray.element(this.simplifyValue(null, this.processElement(element, type)));
            return;
        }
        if (element.getAttributeCount() <= 0) {
            boolean classProcessed = false;
            if (clazz != null) {
                if (clazz.compareToIgnoreCase("array") == 0) {
                    jsonArray.element(this.processArrayElement(element, type));
                    classProcessed = true;
                }
                else if (clazz.compareToIgnoreCase("object") == 0) {
                    jsonArray.element(this.simplifyValue(null, this.processObjectElement(element, type)));
                    classProcessed = true;
                }
            }
            if (!classProcessed) {
                if (type.compareToIgnoreCase("boolean") == 0) {
                    jsonArray.element(Boolean.valueOf(element.getValue()));
                }
                else if (type.compareToIgnoreCase("number") == 0) {
                    try {
                        jsonArray.element(Integer.valueOf(element.getValue()));
                    }
                    catch (NumberFormatException e) {
                        jsonArray.element(Double.valueOf(element.getValue()));
                    }
                }
                else if (type.compareToIgnoreCase("integer") == 0) {
                    jsonArray.element(Integer.valueOf(element.getValue()));
                }
                else if (type.compareToIgnoreCase("float") == 0) {
                    jsonArray.element(Double.valueOf(element.getValue()));
                }
                else if (type.compareToIgnoreCase("function") == 0) {
                    String[] params = null;
                    final String text = element.getValue();
                    final Attribute paramsAttribute = element.getAttribute(this.addJsonPrefix("params"));
                    if (paramsAttribute != null) {
                        params = StringUtils.split(paramsAttribute.getValue(), ",");
                    }
                    jsonArray.element(new JSONFunction(params, text));
                }
                else if (type.compareToIgnoreCase("string") == 0) {
                    final Attribute paramsAttribute2 = element.getAttribute(this.addJsonPrefix("params"));
                    if (paramsAttribute2 != null) {
                        String[] params2 = null;
                        final String text2 = element.getValue();
                        params2 = StringUtils.split(paramsAttribute2.getValue(), ",");
                        jsonArray.element(new JSONFunction(params2, text2));
                    }
                    else if (this.isArray(element, false)) {
                        jsonArray.element(this.processArrayElement(element, defaultType));
                    }
                    else if (this.isObject(element, false)) {
                        jsonArray.element(this.simplifyValue(null, this.processObjectElement(element, defaultType)));
                    }
                    else {
                        jsonArray.element(this.trimSpaceFromValue(element.getValue()));
                    }
                }
            }
            return;
        }
        if (this.isFunction(element)) {
            final Attribute paramsAttribute3 = element.getAttribute(this.addJsonPrefix("params"));
            String[] params = null;
            final String text = element.getValue();
            params = StringUtils.split(paramsAttribute3.getValue(), ",");
            jsonArray.element(new JSONFunction(params, text));
            return;
        }
        jsonArray.element(this.simplifyValue(null, this.processElement(element, type)));
    }
    
    private void setValue(final JSONObject jsonObject, final Element element, final String defaultType) {
        final String clazz = this.getClass(element);
        String type = this.getType(element);
        type = ((type == null) ? defaultType : type);
        final String key = this.removeNamespacePrefix(element.getQualifiedName());
        if (this.hasNamespaces(element) && !this.skipNamespaces) {
            this.setOrAccumulate(jsonObject, key, this.simplifyValue(jsonObject, this.processElement(element, type)));
            return;
        }
        if (element.getAttributeCount() > 0 && this.isFunction(element)) {
            final Attribute paramsAttribute = element.getAttribute(this.addJsonPrefix("params"));
            final String text = element.getValue();
            final String[] params = StringUtils.split(paramsAttribute.getValue(), ",");
            this.setOrAccumulate(jsonObject, key, new JSONFunction(params, text));
            return;
        }
        boolean classProcessed = false;
        if (clazz != null) {
            if (clazz.compareToIgnoreCase("array") == 0) {
                this.setOrAccumulate(jsonObject, key, this.processArrayElement(element, type));
                classProcessed = true;
            }
            else if (clazz.compareToIgnoreCase("object") == 0) {
                this.setOrAccumulate(jsonObject, key, this.simplifyValue(jsonObject, this.processObjectElement(element, type)));
                classProcessed = true;
            }
        }
        if (!classProcessed) {
            if (type.compareToIgnoreCase("boolean") == 0) {
                this.setOrAccumulate(jsonObject, key, Boolean.valueOf(element.getValue()));
            }
            else if (type.compareToIgnoreCase("number") == 0) {
                try {
                    this.setOrAccumulate(jsonObject, key, Integer.valueOf(element.getValue()));
                }
                catch (NumberFormatException e) {
                    this.setOrAccumulate(jsonObject, key, Double.valueOf(element.getValue()));
                }
            }
            else if (type.compareToIgnoreCase("integer") == 0) {
                this.setOrAccumulate(jsonObject, key, Integer.valueOf(element.getValue()));
            }
            else if (type.compareToIgnoreCase("float") == 0) {
                this.setOrAccumulate(jsonObject, key, Double.valueOf(element.getValue()));
            }
            else if (type.compareToIgnoreCase("function") == 0) {
                String[] params2 = null;
                final String text2 = element.getValue();
                final Attribute paramsAttribute2 = element.getAttribute(this.addJsonPrefix("params"));
                if (paramsAttribute2 != null) {
                    params2 = StringUtils.split(paramsAttribute2.getValue(), ",");
                }
                this.setOrAccumulate(jsonObject, key, new JSONFunction(params2, text2));
            }
            else if (type.compareToIgnoreCase("string") == 0) {
                final Attribute paramsAttribute3 = element.getAttribute(this.addJsonPrefix("params"));
                if (paramsAttribute3 != null) {
                    String[] params = null;
                    final String text3 = element.getValue();
                    params = StringUtils.split(paramsAttribute3.getValue(), ",");
                    this.setOrAccumulate(jsonObject, key, new JSONFunction(params, text3));
                }
                else if (this.isArray(element, false)) {
                    this.setOrAccumulate(jsonObject, key, this.processArrayElement(element, defaultType));
                }
                else if (this.isObject(element, false)) {
                    this.setOrAccumulate(jsonObject, key, this.simplifyValue(jsonObject, this.processObjectElement(element, defaultType)));
                }
                else {
                    this.setOrAccumulate(jsonObject, key, this.trimSpaceFromValue(element.getValue()));
                }
            }
        }
    }
    
    private Object simplifyValue(final JSONObject parent, final Object json) {
        if (json instanceof JSONObject) {
            final JSONObject object = (JSONObject)json;
            if (parent != null) {
                for (final Map.Entry entry : parent.entrySet()) {
                    final String key = entry.getKey();
                    final Object value = entry.getValue();
                    if (key.startsWith("@xmlns") && value.equals(object.opt(key))) {
                        object.remove(key);
                    }
                }
            }
            if (object.size() == 1 && object.has("#text")) {
                return object.get("#text");
            }
        }
        return json;
    }
    
    private String trimSpaceFromValue(final String value) {
        if (this.isTrimSpaces()) {
            return value.trim();
        }
        return value;
    }
    
    private String writeDocument(final Document doc, String encoding) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            final XomSerializer serializer = (encoding == null) ? new XomSerializer(baos) : new XomSerializer(baos, encoding);
            serializer.write(doc);
            encoding = serializer.getEncoding();
        }
        catch (IOException ioe) {
            throw new JSONException(ioe);
        }
        String str = null;
        try {
            str = baos.toString(encoding);
        }
        catch (UnsupportedEncodingException uee) {
            throw new JSONException(uee);
        }
        return str;
    }
    
    static {
        EMPTY_ARRAY = new String[0];
        log = LogFactory.getLog(XMLSerializer.class);
    }
    
    private static class CustomElement extends Element
    {
        private String prefix;
        
        private static String getName(final String name) {
            final int colon = name.indexOf(58);
            if (colon != -1) {
                return name.substring(colon + 1);
            }
            return name;
        }
        
        private static String getPrefix(final String name) {
            final int colon = name.indexOf(58);
            if (colon != -1) {
                return name.substring(0, colon);
            }
            return "";
        }
        
        public CustomElement(final String name) {
            super(getName(name));
            this.prefix = getPrefix(name);
        }
        
        public final String getQName() {
            if (this.prefix.length() == 0) {
                return this.getLocalName();
            }
            return this.prefix + ":" + this.getLocalName();
        }
    }
    
    private class XomSerializer extends Serializer
    {
        public XomSerializer(final OutputStream out) {
            super(out);
        }
        
        public XomSerializer(final OutputStream out, final String encoding) throws UnsupportedEncodingException {
            super(out, encoding);
        }
        
        protected void write(final Text text) throws IOException {
            String value = text.getValue();
            if (value.startsWith("<![CDATA[") && value.endsWith("]]>")) {
                value = value.substring(9);
                value = value.substring(0, value.length() - 3);
                this.writeRaw("<![CDATA[");
                this.writeRaw(value);
                this.writeRaw("]]>");
            }
            else {
                super.write(text);
            }
        }
        
        protected void writeEmptyElementTag(final Element element) throws IOException {
            if (element instanceof CustomElement && XMLSerializer.this.isNamespaceLenient()) {
                this.writeTagBeginning((CustomElement)element);
                this.writeRaw("/>");
            }
            else {
                super.writeEmptyElementTag(element);
            }
        }
        
        protected void writeEndTag(final Element element) throws IOException {
            if (element instanceof CustomElement && XMLSerializer.this.isNamespaceLenient()) {
                this.writeRaw("</");
                this.writeRaw(((CustomElement)element).getQName());
                this.writeRaw(">");
            }
            else {
                super.writeEndTag(element);
            }
        }
        
        protected void writeNamespaceDeclaration(final String prefix, final String uri) throws IOException {
            if (!StringUtils.isBlank(uri)) {
                super.writeNamespaceDeclaration(prefix, uri);
            }
        }
        
        protected void writeStartTag(final Element element) throws IOException {
            if (element instanceof CustomElement && XMLSerializer.this.isNamespaceLenient()) {
                this.writeTagBeginning((CustomElement)element);
                this.writeRaw(">");
            }
            else {
                super.writeStartTag(element);
            }
        }
        
        private void writeTagBeginning(final CustomElement element) throws IOException {
            this.writeRaw("<");
            this.writeRaw(element.getQName());
            this.writeAttributes((Element)element);
            this.writeNamespaceDeclarations((Element)element);
        }
    }
}
