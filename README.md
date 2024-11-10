# Frequency-Control-Spring-Boot-Starter

基于SpringBoot + Redis实现的频率控制组件

[飞书文档](https://ocn5kp8vpe7g.feishu.cn/wiki/Rwhgw4jquigTdlklbhCcMYiRnzh?from=from_copylink)

## 项目亮点：
1. 使用lua脚本保证redis的频率计数的**原子性**
2. 允许某个接口拥有多种**频控策略**（如允许5s内3次、30秒内10次）
3. 允许某个接口拥有多种**频控算法**（如固定窗口 + 滑动窗口）
4. 实现**核心配置类**，允许用户通过配置文件自定义默认频控时间范围、频控时间单位、单位频控时间范围内最大访问次数
5. 可通过**配置文件**的参数指定**替换**限流算法
6. 实现**SPI**机制，允许用户自定义实现限流算法
7. 采用**策略模式+模版方法模式+工厂模式**，抽象限流策略服务，实现**固定窗口、滑动窗口、令牌桶**等限流算法策略