package org.jinzijun.mybatis.session;

import org.jinzijun.mybatis.mapping.MappedStatement;
import org.jinzijun.mybatis.type.TypeAliasRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置
 * @author jinzijun
 */
public class Configuration {

    protected DataSourceConfig dataSourceConfig;

    //映射的语句,存在Map里  key是sql的ID
    protected final Map<String, MappedStatement> mappedStatements = new StrictMap<MappedStatement>("Mapped Statements collection");


    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    public DataSourceConfig getDataSourceConfig() {
        return dataSourceConfig;
    }

    public void setDataSourceConfig(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }

    public void addStatement(MappedStatement statement){
        mappedStatements.put(statement.getId(),statement);
    }

    public Map<String, MappedStatement> getMappedStatements() {
        return mappedStatements;
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public MappedStatement findStatement(Class<?> clazz, String methodName){
        return mappedStatements.get(MappedStatement.generateId(clazz,methodName));
    }

    /**
     * 静态内部类,严格的Map，不允许多次覆盖key所对应的value
     */
    protected static class StrictMap<V> extends HashMap<String, V> {

        private String name;

        public StrictMap(String name) {
            super();
            this.name = name;
        }

        @Override
        public V put(String key, V value) {
            if (containsKey(key)) {
                //如果已经存在此key了，直接报错
                throw new IllegalArgumentException(name + " already contains value for " + key);
            }
            return super.put(key, value);
        }

        @Override
        public V get(Object key) {
            if(!containsKey(key)){
                throw new IllegalArgumentException(name + " does not contain value for " + key);
            }
            return super.get(key);
        }

    }

}
