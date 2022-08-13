package com.imooc.oa.utils;

import org.junit.Test;

public class MybatisUtilsTestor {
     @Test
    public void testcasel(){
        String result = (String) MybatisUtils.executeQuery(session -> {
            String out = (String) session.selectOne("test.sample");
            return out;
        });
        System.out.println(result);
    }
}
