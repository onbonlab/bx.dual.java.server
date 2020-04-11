# Server模式测试平台
此项目主要用于测试仰邦五代和六代网口控制器的服务器模式。其工作方式如下：
1. 服务端启动后，开始等待控制器上线
2. 每隔5秒，检查一下在线控制器
3. 依次发送节目至在线的控制器
4. 如果控制器支持动态区，则更新动态区

其主要包含以下两个类：
* Bx5GTestbench: 五代控制器的测试平台
* Bx6GTestbench: 六代控制器的测试平台

----
调用方式如下：

```java
//
// 获取 testbench 实例
Bx5GTestbench g5tb = Bx5GTestbench.getInstance();

//
// run
g5tb.run(8001);
```
