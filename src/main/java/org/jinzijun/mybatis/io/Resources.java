package org.jinzijun.mybatis.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 加载资源的工具类
 * @author jinzijun
 */
public class Resources {

    /**
     * 将classpath下的资源作为流对象返回
     *
     * @param resource 资源的类路径
     * @return 资源的流对象
     * @throws java.io.IOException 如果资源没有被找到或者读取
     */
    public static InputStream getResourceAsStream(String resource) throws IOException {
        return getResourceAsStream(null, resource);
    }

    /**
     * 将classpath下的资源作为流对象返回
     *
     * @param loader   用来获取资源的类加载器
     * @param resource 资源的类路径
     * @return 资源的流对象
     * @throws java.io.IOException 如果资源没有被找到或者读取
     */
    public static InputStream getResourceAsStream(ClassLoader loader, String resource) throws IOException {
        if(loader == null){
            return ClassLoader.getSystemResourceAsStream(resource);
        }
        return loader.getResourceAsStream(resource);
    }

}
