package com.onbonbx.server;

public class TestCaseBuilder {

    /**
     * 生成测试节目
     * @param proid 节目 ID
     * @param index 第几次更新
     * @param repeat 文本长度
     * @return
     */
    public static String build(int proid, int index, int repeat) {
        String content = "";
        
        String unit = "Program: " + proid + " update:" + index + " ";
        for(int i=0; i<repeat; i++) {
            content += unit;
        }

        return content;
    }
}
