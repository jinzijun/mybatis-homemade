package org.jinzijun.mybatis.builder;

import org.dom4j.Element;
import org.jinzijun.mybatis.session.Configuration;
import org.jinzijun.mybatis.type.TypeAliasRegistry;

import java.util.Iterator;
import java.util.Properties;

public abstract class BaseBuilder {

    protected final Configuration configuration;

    /**
     * 别名配置注册器
     */
    protected final TypeAliasRegistry typeAliasRegistry;


    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = configuration.getTypeAliasRegistry();

    }

    public static Properties getChildrenAsProperties(Element element) {
        Properties properties = new Properties();
        for (Iterator<Element> ite = element.elementIterator(); ite.hasNext();){
            Element child = ite.next();
            String name = child.attributeValue("name");
            String value = child.attributeValue("value");
            if (name != null && value != null) {
                properties.setProperty(name, value);
            }
        }
        return properties;
    }

    protected <T> Class<? extends T> resolveClass(String alias) {
        if (alias == null) {
            return null;
        }
        try {
            return resolveAlias(alias);
        } catch (Exception e) {
            throw new RuntimeException("Error resolving class. Cause: " + e, e);
        }
    }

    protected <T> Class<? extends T> resolveAlias(String alias) {
        return typeAliasRegistry.resolveAlias(alias);
    }
}
