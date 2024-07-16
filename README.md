### 基于SpringBoot + Vue酒店客房管理系统.


#### 安装环境

JAVA 环境 

Node.js环境 [https://nodejs.org/en/] 选择14.17

Yarn 打开cmd， 输入npm install -g yarn !!!必须安装完毕nodejs

Mysql 数据库 [https://blog.csdn.net/qq_40303031/article/details/88935262] 一定要把账户和密码记住

redis

Idea 编译器 [https://blog.csdn.net/weixin_44505194/article/details/104452880]

WebStorm OR VScode 编译器 [https://www.jianshu.com/p/d63b5bae9dff]

#### 采用技术及功能

后端：SpringBoot、MybatisPlus、MySQL、Redis、
前端：Vue、Apex、Antd、Axios

平台前端：vue(框架) + vuex(全局缓存) + rue-router(路由) + axios(请求插件) + apex(图表)  + antd-ui(ui组件)

平台后台：springboot(框架) + redis(缓存中间件) + shiro(权限中间件) + mybatisplus(orm) + restful风格接口 + mysql(数据库)

开发环境：windows10 or windows7 ， vscode or webstorm ， idea + lambok

（1）管理员系统公告管理

（2）用户对客房的收藏与删除

（3）管理员酒店商品管理

（4）酒店部门信息管理

（5）酒店附近美食推荐

（6）客户对酒店及客房的留言信息

（7）客户对已经下单的订单做出评价

（8）对可预约的客房进行下单处理

（9）酒店职位信息管理

（10）客户对酒店商品的采购记录

（11）酒店客房的房间管理

（12）员工管理

（13）用户管理及消息通知

（14）客房类型管理


#### 前台启动方式
安装所需文件 yarn install 
运行 yarn run dev

#### 后端启动方式

1.首先启动redis，进入redis目录终端。输入redis-server回车
2.导入sql文件，修改数据库与redis连接配置
3.idea中启动后端项目

#### 默认后台账户密码
[管理员]
admin
1234qwer

[用户]
test1
1234qwer

#### 项目截图

|  |  |
|---------------------|---------------------|
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/b02c5a3564d57a8e0d55a9e725193a1.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/ecd8d0081bc4d6c577ae351f5a7ada7.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/9013595272e7d99e2393f73cab30620.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/e873f798c7119f33ea9831d277b4778.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/824b300de49b9b218330fc8ace45fa2.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/c6a981234cdaa00f61c74fe4399bd38.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/37fa0b569acb2dbc2678b4b991efa5c.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/bb66240665449fd54ce121b37ed631a.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/9b77e28963f62b06b0200b089136c9e.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/b249398af81717bb5d48e4a70bf7ff0.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/07a1b5ef3f35407c82416c5b61a1fee.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/hotal.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/3af6a7cfe0c8e568a524266c5561135.png) 



#### 演示视频

暂无

#### 获取方式

Email: fan1ke2ke@gmail.com

WeChat: `Storm_Berserker`

`附带部署与讲解服务，因为要恰饭资源非免费，伸手党勿扰，谢谢理解`

#### 其它资源

[2024年答辩顺利通过](https://berserker287.github.io/2024/06/06/2024%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2023年答辩顺利通过](https://berserker287.github.io/2023/06/14/2023%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2022年答辩通过率100%](https://berserker287.github.io/2022/05/25/%E9%A1%B9%E7%9B%AE%E4%BA%A4%E6%98%93%E8%AE%B0%E5%BD%95/)

[毕业答辩导师提问的高频问题](https://berserker287.github.io/2023/06/13/%E6%AF%95%E4%B8%9A%E7%AD%94%E8%BE%A9%E5%AF%BC%E5%B8%88%E6%8F%90%E9%97%AE%E7%9A%84%E9%AB%98%E9%A2%91%E9%97%AE%E9%A2%98/)

[50个高频答辩问题-技术篇](https://berserker287.github.io/2023/06/13/50%E4%B8%AA%E9%AB%98%E9%A2%91%E7%AD%94%E8%BE%A9%E9%97%AE%E9%A2%98-%E6%8A%80%E6%9C%AF%E7%AF%87/)

[计算机毕设答辩时都会问到哪些问题？](https://www.zhihu.com/question/31020988)

[计算机专业毕业答辩小tips](https://zhuanlan.zhihu.com/p/145911029)


#### 接JAVAWEB毕设，纯原创，价格公道，诚信第一

`网站建设、小程序、H5、APP、各种系统 选题+开题报告+任务书+程序定制+安装调试+项目讲解+论文+答辩PPT`

More info: [悲伤的橘子树](https://berserker287.github.io/)
