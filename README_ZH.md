<p align="center">
  <img src="./docs/logo.png" alt="Capa" width="160">
</p>

# Capa Java SDK

Capa 是面向 Java 应用的富 SDK 模式 Cloud Application API 实现。应用依赖与厂商无关的运行时接口，通过可替换的 SPI 模块连接具体中间件或云服务。

[English](README.md) · [在线文档](https://capa.rxcloud.group/) · [问题反馈](https://github.com/capa-cloud/capa-java/issues)

> **项目边界：**本仓库是 Java SDK。实验性的 Go Sidecar 运行时位于 [capa](https://github.com/capa-cloud/capa)，API 契约位于 [cloud-runtimes-jvm](https://github.com/capa-cloud/cloud-runtimes-jvm)。

## 环境要求

- Java 8 或 Java 11
- Maven 3.8.1 或更高版本
- 为应用实际使用的每项能力提供对应的 Capa SPI 实现

## 引入 SDK

当前仓库版本为 `1.11.13.2.RELEASE`。

```xml
<dependency>
    <groupId>group.rxcloud</groupId>
    <artifactId>capa-sdk</artifactId>
    <version>1.11.13.2.RELEASE</version>
</dependency>
```

`capa-sdk` 提供统一编程接口，但不会自行选择生产基础设施。应用还需要引入兼容的 SPI 实现，例如组织内部适配器或对应的云集成仓库。

仅用于本地体验时，可以使用仓库提供的 Demo SPI：

```xml
<dependency>
    <groupId>group.rxcloud</groupId>
    <artifactId>capa-sdk-spi-demo</artifactId>
    <version>1.11.13.2.RELEASE</version>
    <scope>runtime</scope>
</dependency>
```

Demo SPI 是示例实现，不应作为生产运行时使用。

## 能力范围

SDK 与 API 模块覆盖以下运行时领域：

| 领域 | 用途 | 成熟度说明 |
| --- | --- | --- |
| RPC | 服务调用 | 已包含 API、组件层与 SPI 扩展点 |
| Configuration | 动态配置存储 | 已包含 API、组件层与 SPI 扩展点 |
| Pub/Sub | 消息发布与订阅 | 已包含 API、组件层与 SPI 扩展点 |
| State | 键值状态操作 | 已定义 API，具体支持取决于适配器 |
| Telemetry | 日志、指标与追踪上下文 | 已包含组件和 SPI 支持 |
| Database、Schedule | 扩展运行时 API | 按 alpha 能力对待，并验证所选适配器 |

API 契约存在不代表每个 SPI 适配器都实现了所有操作。用于生产环境前，应检查适配器仓库，并针对目标基础设施运行集成测试。

## 仓库结构

```text
.
├── sdk/                    # Capa 公共 SDK
├── sdk-component/          # 组件发现与共享实现
├── sdk-spi/                # SPI 扩展点
├── sdk-spi-demo/           # Demo SPI 实现
├── sdk-infrastructure/     # 运行时基础设施和钩子
├── sdk-springboot/         # Spring Boot 集成
├── examples/               # 示例应用
└── spec/                   # 协议定义
```

整体设计为“**标准 API + 可替换的 SDK 组件**”：

<p align="center">
  <img src="./docs/capa-design/capa-layer.PNG" alt="Capa SDK 分层" width="720">
</p>

## 构建与验证

```bash
git clone https://github.com/capa-cloud/capa-java.git
cd capa-java
mvn --batch-mode --no-transfer-progress --fail-fast clean verify \
  -Pjacoco,rat,checkstyle \
  -DskipTests=false \
  -Dcheckstyle.skip=false \
  -Drat.skip=false \
  -Dmaven.javadoc.skip=true \
  -Dgpg.skip=true
```

CI 使用同一套命令分别在 Java 8 和 Java 11 上验证。

可运行示例与配置示例位于 [`examples/`](examples/) 和 [`sdk-spi-demo/`](sdk-spi-demo/)。各模块的 `src/main/resources/sample/` 目录包含组件映射样例。

## 响应式 API 模型

Capa 使用 [Project Reactor](https://github.com/reactor/reactor-core) 提供异步操作。返回的 [`Mono`](https://github.com/reactor/reactor-core/blob/main/docs/modules/ROOT/pages/coreFeatures/mono.adoc) 在订阅或阻塞前不会执行：

```java
Mono<String> result = capaRpcClient.invokeMethod(
    SERVICE_APP_ID,
    "say",
    "hello",
    HttpExtension.POST,
    null,
    TypeRef.STRING
);

String response = result.block();
```

响应式应用应优先使用非阻塞组合；只有在明确的同步边界才调用 `block()`。

## 设计背景

- [Dapr API 后续规划](https://github.com/dapr/dapr/issues/2817)
- [Layotto Java SDK 设计讨论](https://github.com/mosn/layotto/issues/188)
- [将 Capa API 适配到 Spring 注解](https://github.com/reactivegroup/sigs/issues/16)
- [将遗留中间件 SDK 迁移到 Capa](https://github.com/reactivegroup/sigs/issues/18)
- [Multi-Runtime 2022：待解决的问题](https://zhuanlan.zhihu.com/p/435012312)

## 参与贡献

1. 从 `master` 创建分支。
2. 公共 API 变更需要保持兼容，或明确说明兼容性影响。
3. SDK 或 SPI 契约变化时同步更新示例。
4. 运行上面的完整验证命令。
5. 在 Pull Request 中说明受影响的能力和适配器范围。

不要在示例或测试资源中提交云凭证、私有端点或客户配置。

## 许可证

Apache License 2.0，详见 [LICENSE](LICENSE)。
