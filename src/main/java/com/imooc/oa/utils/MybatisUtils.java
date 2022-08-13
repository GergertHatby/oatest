package com.imooc.oa.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Function;

public class MybatisUtils {
    //利用static（静态）属于类不属于对象，且全局唯一
    private static SqlSessionFactory sqlSessionFactory = null;
    static {
        Reader reader = null;
        try{
            reader = Resources.getResourceAsReader("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            //初始化错误时，通过抛出异常ExceptionInInitializerError通知调用者
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Object executeQuery(Function<SqlSession,Object> function){
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Object obj = function.apply(session);
            return obj;
        }finally {
            session.close();
        }
    }

    /**
     *执行INSERT/UPDATE/DELETE写操作SQL
     * @param function 要执行的写操作代码块
     * @return 写操作后返回的结果
     */
    public static Object executeUpdate(Function<SqlSession,Object> function){
        SqlSession session = sqlSessionFactory.openSession(false);
        try {
            Object obj = function.apply(session);
            session.commit();
            return obj;
        }catch (RuntimeException e){
            session.rollback();
            throw e;
        }finally {
            session.close();
        }
    }
}
