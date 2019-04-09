package org.jinzijun.mybatis.mapping;

/**
 * 映射的语句
 * @author jinzijun
 */
public class MappedStatement {
    private String id;
    private SqlCommandType sqlCommandType;
    private Class parameterType;
    private Class resultType;
    private String sql;


    public MappedStatement(String declaredClassName, String methodName, SqlCommandType sqlCommandType, Class parameterType, Class resultType, String sql) {
        this.id = generateId(declaredClassName, methodName);
        this.sqlCommandType = sqlCommandType;
        this.parameterType = parameterType;
        this.resultType = resultType;
        this.sql = sql;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public void setSqlCommandType(SqlCommandType sqlCommandType) {
        this.sqlCommandType = sqlCommandType;
    }

    public Class getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class parameterType) {
        this.parameterType = parameterType;
    }

    public Class getResultType() {
        return resultType;
    }

    public void setResultType(Class resultType) {
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public static String generateId(Class<?> clazz, String methodName){
        return generateId( clazz.getName(), methodName);
    }

    public static String generateId(String className, String methodName){
        return String.format("%s#%s", className, methodName);
    }
}
