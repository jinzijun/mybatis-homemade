package org.jinzijun.mybatis.builder.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jinzijun.mybatis.builder.BaseBuilder;
import org.jinzijun.mybatis.io.Resources;
import org.jinzijun.mybatis.session.Configuration;
import org.jinzijun.mybatis.session.DataSourceConfig;


import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

public class XMLConfigBuilder extends BaseBuilder {

    private Document document;


    public XMLConfigBuilder(InputStream inputStream) {
        super(new Configuration());
        try {
            document = new SAXReader().read(inputStream);
        } catch (DocumentException e) {
            throw new RuntimeException("xml读取错误：" + e.getMessage());
        }
    }

    public Configuration parse(){
        parseConfiguration(document.getRootElement());
        return configuration;
    }

    private void parseConfiguration(Element root) {

        Element environments = root.element("environments");
        for (Iterator i = environments.elementIterator(); i.hasNext(); ) {
            Element environment = (Element) i.next();
            Element dataSource = environment.element("dataSource");
            Properties properties = getChildrenAsProperties(dataSource);
            DataSourceConfig dataSourceConfig = new DataSourceConfig(properties);
            configuration.setDataSourceConfig(dataSourceConfig);
        }
        Element mappers = root.element("mappers");
        for (Iterator i = mappers.elementIterator(); i.hasNext(); ) {
            Element mapper = (Element) i.next();
            String resource = mapper.attributeValue("resource");
            InputStream inputStream = null;
            try {
                inputStream = Resources.getResourceAsStream(resource);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration);
            mapperParser.parse();
        }

    }
}
