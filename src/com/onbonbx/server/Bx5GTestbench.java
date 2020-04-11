package com.onbonbx.server;

import onbon.bx05.*;
import onbon.bx05.area.TextCaptionBxArea;
import onbon.bx05.area.page.TextBxPage;
import onbon.bx05.cmd.dyn7.DynamicBxAreaRule;
import onbon.bx05.file.ProgramBxFile;
import onbon.bx05.utils.DisplayStyleFactory;
import onbon.bx05.utils.TextBinary;

import java.awt.*;
import java.util.ArrayList;

public class Bx5GTestbench implements Bx5GServerListener {


    private int port = 8001;
    private int upatetime = 0;
    private int interval = 5000;    // update interval
    private Bx5GServer server;

    //
    // single instance
    private static Bx5GTestbench instance = new Bx5GTestbench();

    private Bx5GTestbench() {

        //
        // 初始化
        try {
            Bx5GEnv.initial("log.properties");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Bx5GTestbench getInstance() {
        return instance;
    }


    /**
     * 启动服务器
     */
    public void run(int port) {




        //
        this.port = port;

        //
        if(server != null) {
            server.stop();
        }

        //
        try {
            server = new Bx5GServer("G5 server", port);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //
        // 启动服务器
        // start the server
        server.start();

        //
        // 显示方式
        final DisplayStyleFactory.DisplayStyle[] styles;
        styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyleFactory.DisplayStyle[0]);


        new Thread(new Runnable() {
            @Override
            public void run() {

                //
                // 等待20秒左右
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //
                // 每隔5秒更新一次节目
                while (true) {
                    //
                    // 每隔几次更新一次
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //
                    // 获取在线的控制器列表
                    ArrayList<Bx5GScreen> screens = (ArrayList<Bx5GScreen>) server.getOnlineScreens();

                    for(Bx5GScreen screen : screens) {


                        Bx5GScreenProfile profile = screen.getProfile();

                        TextBxPage tp = new TextBxPage(TestCaseBuilder.build(0, upatetime, 100));
                        Font font = new Font("Arial", Font.PLAIN, 16);
                        tp.setFont(font);
                        tp.setDisplayStyle(styles[5]);
                        tp.setForeground(Color.red);
                        tp.setHorizontalAlignment(TextBinary.Alignment.CENTER);
                        tp.setVerticalAlignment(TextBinary.Alignment.CENTER);

                        tp.setSpeed(1);
                        tp.setStayTime(0);

                        TextCaptionBxArea textArea = new TextCaptionBxArea(0, 0, profile.getWidth(), profile.getHeight(), profile);
                        textArea.addPage(tp);

                        ProgramBxFile programFile = new ProgramBxFile(1, profile);
                        programFile.addArea(textArea);

                        try {
                            screen.writeProgramQuickly(programFile);
                        } catch (Bx5GException e) {
                            e.printStackTrace();
                            continue;
                        }

                        System.out.println("Program updated");

                        String type = screen.getControllerType();
                        System.out.println(type);

                        //
                        // 以下是动态区部分 Demo
                        // 动态区的特点

                        // DynamicBxAreaRule(id, runMode, immediatePlay, timeout)
                        // runMode 运行模式：
                        //   0：循环显示。
                        //   1：显示完成后静止显示最后一页数据。
                        //   2：循环显示，超过设定时间后数据仍未更新时不再显示。
                        //   3：循环显示，超过设定时间后数据仍未更新时显示 Logo 信息。
                        //   4：循环显示，显示完最后一页后就不再显示。

                        // immediatePlay 是否立即播放：
                        //   0：与异步节目一起播放。
                        //   1：异步节目停止播放，仅播放动态区域。
                        //   2：当播放完节目编号最高的异步节目后播放该动态区域。

                        //
                        // 定义一个动态区
                        // 可以通过ID来更新不同的动态区内容, 此处 ID 为 0
                        DynamicBxAreaRule dynRule = new DynamicBxAreaRule(0, (byte) 4, (byte) 1, 0);
                        //dArea.addProgram("P000");
                        //dArea.addProgram("P001");

                        TextCaptionBxArea dAreaContent = new TextCaptionBxArea(0, 0, profile.getWidth(), profile.getHeight(), profile);
                        TextBxPage page = new TextBxPage(TestCaseBuilder.build(0, upatetime, 100));
                        page.setDisplayStyle(DisplayStyleFactory.getStyle(4));
                        page.setSpeed(2);
                        dAreaContent.addPage(page);

                        // 发送动态区之前，如果需要删除之前的动态区，可以调用以下接口
                        // 通常如果动态区的位置或大小没有发生改变，不用删除
                        screen.deleteAllDynamic();

                        // 更新动态区
                        screen.writeDynamic(dynRule, dAreaContent);

                        //
                        upatetime ++;
                    }
                }
            }
        }).start();




    }

    @Override
    public void connected(String s, String s1, Bx5GScreen bx5GScreen) {
        //
        System.out.println("link up");
    }

    @Override
    public void disconnected(String s, String s1, Bx5GScreen bx5GScreen) {
        //
        System.out.println("link down");
    }
}
