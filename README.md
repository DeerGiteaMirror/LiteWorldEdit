# LiteWorldEdit

## 轻量创世神

适用于生存服务器的轻量级创世神插件。

## 说明

这个插件也是很早之前在mcbbs见过一个类似的，但是后面找不到了，也不知道原作者是否有继续维护。

对于一些玩家来说平整区域、铺地板这样的重复性机械劳动是一件很痛苦的事。

原版的创世神插件很强大，但是在生存模式的服务器中存在一些问题：

1. 权限过高，很容易误操作；
2. 虽然填充可以开启“消耗玩家物品栏”，但是对于破坏操作没有任何限制；
3. 创世神采用的是直接在主线程执行任务，因此如果大量玩家在同一时间操作大量方块会不可避免地造成服务器卡死；

由此便诞生了本插件，适用于想要尽可能减少重复性机械劳动的建筑党。

> 本人是C++程序员，在此之前没有写过Java。所以如果发现代码里有不合理的地方，欢迎指出。

## 功能介绍

1. 采用异步的方式执行操作，玩家提交的操作会被放入一个队列中，每tick只会从队列中取出一个操作进行执行，因此不会造成服务器卡顿；
2. 使用破坏操作需要拥有下届合金镐（作为一种使用门槛），并且消耗耐久度，同时支持耐久附魔效果；
3. 当合金镐耐久度不足10时会自动终止任务（暂不支持恢复）；
4. 支持填充操作，填充操作需要消耗玩家物品栏中的物品；
5. 禁止超视距操作（128以外），防止玩家利用创世神插件加载大量区块导致服务器卡顿；

## 支持版本

- 1.17 及以上 Bukkit/Spigot/Paper

## 使用方法

1. 将插件放入服务器的 `plugins` 目录下
2. 重启服务器
3. 在 `plugins/LiteWorldEdit/config.yml` 中配置
4. 控制台或OP输入 `/lwe reload` 重载配置

## 指令

### 玩家指令

`/lwe help` 查看帮助

`/lwe point [点序号(整数)] [x] [y] [z]` 创建点

`/lwe p [点序号(整数)] [x] [y] [z]` 创建点

`/lwe points` 查看当前添加的所有点

`/lwe fill [点序号A] [点序号B]` 在AB点对角线间放置方块 - 需要手持被放置的方块

`/lwe empty [点序号A] [点序号B]` 破坏AB点对角线间方块 - 需要拥有下届合金镐

`/lwe cancel` 取消（终止）所有任务

`/lwe reload` 重载配置（仅管理员）

## 配置文件参考

```yaml
MaxX: 64

MaxY: 64

MaxZ: 64

Debug: false
```

## TODO

- [x] 从潜影盒中自动补充材料

- [ ] 选区可视化

- [ ] 可手动暂停、恢复任务

- [ ] 更多的任务类型支持（如画圆、弧线）
