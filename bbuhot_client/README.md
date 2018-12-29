# 哥谭菠菜项目前端

本项目旨在为了更好的服务哥谭抑制通货膨胀, 供给侧结构性改革.

## 准备部分

本项目管理部分采用[Trello平台](https://trello.com/), UI部分采用[蓝湖平台](https://lanhuapp.com/web/#/item)进行协作, 自行注册后联系前端群内熊猫头进行邀请

## 项目技术栈

- [项目框架: Angular 7](https://www.angular.cn/)
- [语言: Typescript](https://www.tslang.cn/)
- [UI: 阿里antd](https://ng.ant.design/)
- [语言扩展: RxJs](https://cn.rx.js.org/)

## 运行

在项目[bbuhot_client](https://github.com/yhvictor/bbuhot/tree/master/bbuhot_client)文件夹下进行npm相关依赖安装后运行, 命令行依次输入

```shell
1. npm install
2. ng serve --open
```

浏览器会自动打开项目, 运行在http://localhost:4200  地址

## 远程合作

本项目是由全球各地韦恩们远程协作共同完成的, 各位韦恩在提交代码的同时不可避免的会出现因为交流不畅产生的代码冲突, 所以项目采用[GitFlow](https://www.cnblogs.com/myqianlan/p/4195994.html)策略管理项目代码提交, 因GitFlow是用来管理大型团队的会显得复杂, 所以只需记住一下几个原则即可.

1. 每个程序员新加入后从master上checkout属于自己的分支, 格式为 client/你的专属id 
2. 每个程序员只负责维护自己的分支部分,非特殊情况下不要修改其他人的分支
3. client/release作为前端发布分支
4. 每个程序员完成自己所派发任务的模块,需要保证可以启动, 无报错, 在浏览器内可见的前提下, 先从client/release分支merge到自己的分支, 在自己分支下输入如下命令

```
git rebase client/release
```

解决relase分支与本分支所有冲突后达到本模块功能可用, 可以启动, 编译无报错, 在浏览器内可见的前提下, 提交修改

```
git add .
git rebase --continue
```

推送自己远程分支

```
git push
```

5. 切换分支到client/release, 将自己所完成分支与release分支merge, 此时release分支上应不会产生冲突, 会顺利合并完成, 提交merge后推送远程client/release分支

6. 原则上release分支只用来merge操作不应有任何实际上的代码修改, 如第五步产生conflict需复查第四步
