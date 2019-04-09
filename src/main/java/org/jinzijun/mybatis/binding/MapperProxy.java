package org.jinzijun.mybatis.binding;

import org.jinzijun.mybatis.mapping.MappedStatement;
import org.jinzijun.mybatis.mapping.SqlCommandType;
import org.jinzijun.mybatis.session.Configuration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MapperProxy<T> implements InvocationHandler {

    private final Configuration configuration;

    private final Connection connection;

    public MapperProxy(Configuration configuration, Connection connection) {
        this.configuration = configuration;
        this.connection = connection;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }

        // 查找配置
        Class<?> clazz = method.getDeclaringClass();
        String methodName = method.getName();
        MappedStatement statement = configuration.findStatement(clazz, methodName);
        // 执行SQL
        PreparedStatement preparedStatement = connection.prepareStatement(handleParam(statement.getSql()));
        for (int i = 0; i < args.length; i++) {
            int index = i + 1;
            if (args[i] instanceof Integer) {
                preparedStatement.setInt(index, (Integer) args[i]);
            }
            if (args[i] instanceof Boolean) {
                preparedStatement.setBoolean(index, (Boolean) args[i]);
            }
            if (args[i] instanceof String) {
                preparedStatement.setString(index, (String) args[i]);
            }
            if (args[i] instanceof Byte) {
                preparedStatement.setByte(index, (Byte) args[i]);
            }
            if (args[i] instanceof byte[]) {
                preparedStatement.setBytes(index, (byte[]) args[i]);
            }

            if (args[i] instanceof Date) {
                preparedStatement.setDate(index, (Date) args[i]);
            }
        }
        SqlCommandType sqlCommandType = statement.getSqlCommandType();
        if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE || sqlCommandType == SqlCommandType.DELETE) {
            int affectRowCount = preparedStatement.executeUpdate();
            return affectRowCount;
        }
        if (statement.getSqlCommandType() == SqlCommandType.SELECT) {
            ResultSet rs = preparedStatement.executeQuery();
            int col = rs.getMetaData().getColumnCount();

            Class resultType = statement.getResultType();
            Class<?> returnType = method.getReturnType();
            boolean isReturnsMany = Collection.class.isAssignableFrom(resultType) || resultType.isArray();
            if(!isReturnsMany){
                resultType = returnType;
            }
            LinkedList<Object> resultList = new LinkedList<Object>();
            while (rs.next()) {
                Object result = handlerResult(rs, resultType);
                resultList.add(result);
            }
            if(!isReturnsMany){
                return resultList.getFirst();
            }
            if(returnType.isArray()){
                return resultList.toArray();
            }
            return resultList;
        }
        throw new RuntimeException("unsupport sql command type:" + sqlCommandType);
    }

    Object handlerResult(ResultSet rs, Class<?> clazz) throws Exception {
        if (clazz == Integer.class
                || clazz == Long.class
                || clazz == String.class
                || clazz == Byte.class
                || clazz == Boolean.class
                || clazz == Float.class
                || clazz == Double.class) {
            if (rs.getMetaData().getColumnCount() == 0) {
                return null;
            }
            return rs.getObject(1);
        }
        return resolveBeanType(rs, clazz);
    }

    private Object resolveBeanType(ResultSet rs, Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getConstructor();
        Object target = constructor.newInstance();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        HashMap<String, Method> filedMethodMap = new HashMap<String, Method>(16);
        for (Method method : declaredMethods) {
            if (!method.getName().startsWith("set")) {
                continue;
            }
            String fieldName = method.getName().replaceFirst("^set", "");
            filedMethodMap.put(fieldName, method);
        }
        int col = rs.getMetaData().getColumnCount();
        for (int i = 1; i <= col; i++) {
            String colName = rs.getMetaData().getColumnName(i);
            String camelName = underlineToCamel(colName);
            if(!filedMethodMap.containsKey(camelName)){
                continue;
            }
            Method method = filedMethodMap.get(camelName);
            method.invoke(target, rs.getObject(i));
        }
        return target;
    }

    private String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            // 首字母大写
            if(i == 0 ){
                sb.append(Character.toUpperCase(param.charAt(i)));
                continue;
            }
            char c = Character.toLowerCase(param.charAt(i));
            if (c == '_') {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    private static String handleParam(String sql) {
        return sql.replaceAll("#\\{.*?\\}", "?");
    }

    public static void main(String[] args) {
        String a = "setSetHashSetset";
        System.out.println(a.replaceFirst("^set", ""));

    }


}
