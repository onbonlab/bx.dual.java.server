package com.onbonbx.server;

import onbon.bx05.*;
import onbon.bx05.area.TextCaptionBxArea;
import onbon.bx05.area.page.TextBxPage;
import onbon.bx05.cmd.dyn7.DynamicBxAreaRule;
import onbon.bx05.file.ProgramBxFile;
import onbon.bx05.message.area.DynamicArea;
import onbon.bx05.utils.DisplayStyleFactory;
import onbon.bx05.utils.TextBinary;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) {
	// write your code here

        //
        // 获取 testbench 实例
        Bx5GTestbench g5tb = Bx5GTestbench.getInstance();

        //
        // run
        g5tb.run(8001);


        while (true);



    }
}
