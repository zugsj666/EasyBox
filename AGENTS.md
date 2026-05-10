# EasyBox 项目工作指引

本文档用于记录当前工作区已经确认的项目信息、阶段进度、边界约束和后续维护规则。后续每推进到一个明确阶段或关键节点，都应先通读当前工作区，再更新本文件。

## 1. 项目定位

- 项目名：`EasyBox`
- 项目类型：开源、自托管软件
- 核心目标：面向新手和不想折腾的用户，提供“一键式 Android 代理客户端 + 自托管管理面板”
- 核心理念：
  - 普通用户只登录、选地区、连接/断开
  - 管理员通过后端和管理面板统一控制用户、节点、公告、版本和配置下发
  - Android 客户端不暴露节点真实参数，不提供节点编辑、订阅导入、JSON 配置查看等高级入口

## 2. 当前仓库结构

- `apps/android-client`
  - 当前已进入 Phase 1 技术验证阶段
  - 已创建最小 Android Demo 工程骨架
- `apps/panel-api`
  - 当前只有 README 占位
- `apps/panel-web`
  - 当前只有 README 占位
- `packages/shared-types`
  - 当前只有类型定义
- `packages/subscription-parser`
  - 当前只有接口定义，不含解析实现
- `packages/config-builder`
  - 当前只有接口定义，不含配置生成实现
- `docs`
  - 已包含架构、MVP 计划、API 设计、安全说明
- `scripts`
  - 当前只有 README 占位

## 3. 已确认产品边界

- EasyBox 只提供自托管软件，不提供：
  - 公共节点
  - 代理服务
  - 订阅服务
  - Hosted SaaS 服务
- Android 客户端 UI 不允许出现：
  - 订阅链接
  - 节点真实地址
  - UUID
  - 端口
  - SNI
  - Reality 参数
  - TLS 参数
  - DNS 高级设置
  - 路由规则编辑
  - 配置 JSON
  - 导入/导出节点配置
- 安全边界说明：
  - UI 隐藏不是安全边界
  - 运行时 sing-box 配置必然包含连接参数
  - 不承诺防逆向、防 Root、防抓包
  - `rawConfig` 第一版允许明文存储，但只适合 MVP 和可信自托管环境
  - 客户端流量上报第一版只做粗略统计，不可作为可信计费依据

## 4. 当前主链路定义

EasyBox MVP 主链路固定为：

```text
管理员创建用户
-> 导入订阅源
-> 解析节点
-> 生成 sing-box 配置
-> Android 登录
-> Android 拉取配置
-> Android 一键连接
```

后续开发、设计和验收都应围绕这条链路，不应被管理面板美化或高级功能分散优先级。

## 5. 当前开发阶段和状态

### 已完成

- Phase 0：项目骨架与文档
  - monorepo 目录已建立
  - README、LICENSE、NOTICE、`.gitignore`、`.env.example` 已创建
  - `docs/architecture.md`、`docs/mvp-plan.md`、`docs/security-notes.md`、`docs/api-design.md` 已创建
  - `packages/shared-types`、`packages/subscription-parser`、`packages/config-builder` 已建立 package skeleton 和接口定义

- Phase 1：Android 本地技术验证 Demo 的代码骨架
  - 已创建最小 Android 工程结构
  - 已实现本地 `VpnService`、Foreground Service、常驻通知、加载本地 config、启动/停止本地 sing-box 进程、日志输出、错误提示的代码路径
  - 当前代码仅位于 `apps/android-client`

### 尚未完成

- 尚未在当前环境完成 Android SDK/Gradle 编译验证
- 尚未接入真实 sing-box Android 专用库或完整 TUN fd 交接方案
- 尚未接入后端 API
- 尚未实现登录、用户、订阅、节点管理、管理面板、远程配置拉取

## 6. Android Phase 1 已确认事实

- `apps/android-client` 当前是技术验证 Demo，不是正式客户端
- 当前 Demo 目标：
  - 请求 `VpnService` 权限
  - 启动 Foreground Service
  - 显示常驻通知
  - 从 `assets/sing-box-demo-config.json` 加载本地配置
  - 启动本地 sing-box 进程
  - 停止进程
  - 输出基础日志和错误信息
- 当前 sing-box 集成方式：
  - 预期将 Android 兼容的 sing-box 可执行文件放到 `app/src/main/jniLibs/<abi>/libsing-box.so`
  - 运行方式是：
    - 复制本地 config 到缓存目录
    - 执行 `libsing-box.so run -c <config-path>`
- 已知限制：
  - 独立 sing-box 进程不会自动拿到 Android `VpnService` 的 TUN fd
  - 当前 Demo 主要验证应用侧生命周期、本地 config、进程启动/停止和日志
  - 后续若要变成正式 VPN 数据路径，可能需要 sing-box Android 专用库或明确的 TUN 接入方案

## 7. 后续阶段顺序

当前约定的线性路线如下：

1. Phase 0：项目骨架与文档
2. Phase 1：Android `VpnService + sing-box` 本地 config 技术验证
3. Phase 2：后端数据模型和基础认证
4. Phase 3：用户管理、设备绑定、会话管理
5. Phase 4：订阅源同步和节点解析
6. Phase 5：config-builder 配置生成
7. Phase 6：客户端 API：`profile / bootstrap / config / version`
8. Phase 7：Android 登录、首页、配置拉取
9. Phase 8：Android 一键连接、断开、修复网络
10. Phase 9：最小管理面板
11. Phase 10：流量、到期、公告、版本
12. Phase 11：端到端测试和 Alpha 0.1 发布准备

## 8. 串行依赖规则

- Android `VpnService + sing-box` 技术验证必须先完成，才能稳定客户端方向
- 数据模型必须在管理面板和客户端 API 完整实现前稳定
- `config-builder` 必须在 Android 远程配置连接前完成
- 端到端测试必须在 Android、后端、节点解析、配置生成都完成后进行
- 当前第一优先级不是管理面板，也不是 UI 美化，而是 Android 本地 sing-box 技术验证 Demo

## 9. 仓库与 Git 现状

- GitHub 仓库地址：
  - `https://github.com/zugsj666/EasyBox`
- 当前默认分支：
  - `main`
- 当前工作区 Git 采用独立 git 元数据目录：
  - 工作区根目录 `.git` 是一个指针文件
  - 实际 git 数据目录在：`C:\Users\zugsj666\Documents\EasyBox.gitdata`
- 这是为绕过当前目录的 `.git` 权限异常问题所做的处理

## 10. 更新规则

- 每推进到一个明确阶段、子阶段或关键节点后，都应执行以下动作：
  - 通读当前工作区相关文件
  - 区分“已实现”“已确认但未实现”“已知问题”“下一步依赖”
  - 更新本文件
- 除了阶段节点更新外，也应定期补充当前进度、难点、风险和临时决策，避免后续上下文丢失
- 更新本文件时要求：
  - 使用中文
  - 格式清楚，优先使用标题和短条目
  - 不写模糊表述，尽量写已确认事实
  - 明确哪些内容是代码已落地，哪些只是设计约束

## 11. 当前进度记录

### 最近完成

- 已完成项目骨架初始化
- 已完成基础文档、接口定义和 Android Demo 说明
- 已完成 Android Phase 1 最小技术验证 Demo 代码骨架
- 已完成本地 Git 仓库清理、重建，并推送到 GitHub `main`

### 当前所处节点

- 当前处于 Phase 1：Android `VpnService + sing-box` 本地 config 技术验证阶段
- 当前代码已经覆盖：
  - `VpnService` 权限申请路径
  - Foreground Service
  - 常驻通知
  - 本地 config 复制与加载
  - 本地 sing-box 进程启动/停止
  - 基础日志输出
  - 启停错误提示
- 当前还没有完成真机或模拟器上的实际编译和运行验证

### 下一步最近动作

- 优先验证 Android Demo 在 Android Studio 和真机/模拟器上的可运行性
- 确认当前 sing-box 二进制方式是否足够支撑后续正式 VPN 数据路径
- 根据验证结果决定：
  - 直接进入 Phase 2 后端数据模型
  - 或先补 sing-box Android TUN 集成方案验证

## 12. 当前难点与风险

### 已识别难点

- 当前环境没有现成 Android SDK/Gradle 运行条件，无法在本轮内完成 APK 编译验证
- 当前 Demo 采用独立 sing-box 进程方式，只验证了本地核心启动路径，不等同于完整 Android VPN 数据通路
- Android 上真正可用的 TUN 流量接管，后续可能需要 sing-box Android 专用库或额外的 TUN fd 交接设计

### 已发生的问题

- 当前项目目录的 `.git` 权限存在异常，直接使用默认 `.git` 目录时容易触发锁文件和所有权问题
- 为了稳定使用 Git，本地仓库改成了独立 git 元数据目录模式

### 当前风险判断

- 最高优先级风险仍然是 Android sing-box 技术可行性，而不是后端或管理面板
- 如果 Phase 1 只验证了进程启动而没有验证可用的 VPN 数据路径，后续正式客户端集成仍可能需要回头调整

## 13. 后续记录格式建议

后续每次更新本文件时，除了更新阶段状态，建议至少补这几类信息：

- 本次推进到哪个节点
- 新增了哪些已落地代码或文件
- 解决了什么问题
- 还剩下什么未解决难点
- 下一步最直接要做什么
