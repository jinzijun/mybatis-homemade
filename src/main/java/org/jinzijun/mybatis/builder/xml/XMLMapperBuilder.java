package org.jinzijun.mybatis.builder.xml;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jinzijun.mybatis.builder.BaseBuilder;
import org.jinzijun.mybatis.mapping.MappedStatement;
import org.jinzijun.mybatis.mapping.SqlCommandType;
import org.jinzijun.mybatis.session.Configuration;

import java.io.InputStream;
import java.util.Iterator;

public class XMLMapperBuilder extends BaseBuilder {

    private final Document document;

    public XMLMapperBuilder(InputStream inputStream, Configuration configuration) {
        super(configuration);
        try {
            document = new SAXReader().read(inputStream);
        } catch (DocumentException e) {
            throw new RuntimeException("xml读取错误：" + e.getMessage());
        }
    }

    public void parse() {
        // 解析mapper.xml
        Element root = document.getRootElement();
        String namespace = root.attributeValue("namespace");
        for (Iterator<Element> ite = root.elementIterator(); ite.hasNext();) {
            Element statementElem = ite.next();
            String sqlCommand = statementElem.getName();
            String id = statementElem.attributeValue("id");
            SqlCommandType sqlCommandType = SqlCommandType.valueOf(sqlCommand.toUpperCase());
            Class<?> parameterType = resolveClass(statementElem.attributeValue("parameterType"));
            Class<?> resultType = resolveClass(statementElem.attributeValue("resultType"));
            String sql = statementElem.getStringValue();
            configuration.addStatement(new MappedStatement(namespace,id,sqlCommandType,parameterType,resultType,sql));
        }
    }

}
