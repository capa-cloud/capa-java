![logo](./docs/logo.png)

# Capa(cloud application api): To be the high-level api layer for all application runtime.

让代码实现"一次编写，随处运行"。

借助Capa体系，使你的Java应用在改动量较小的情况下，拥有跨云、混合云运行的能力。

## 动机

### Mecha架构

Capa项目基于Mecha架构的设计理念，使用 **富SDK模式** 提供Multi-Runtime的标准API。

您可以简单的将Capa项目理解为 [Dapr](https://github.com/dapr/dapr) / [Layotto](https://github.com/mosn/layotto) 等Sidecar模式项目的SDK实现版本。

欲要理解Mecha架构的设计思路，请阅读以下文章：

[死生之地不可不察：论API标准化对Dapr的重要性](https://www.infoq.cn/article/wjkNGoGaaHyKs7xIyTSB)

[MOSN子项目Layotto：开启服务网格+应用运行时新篇章](http://mosn.io/layotto/#/zh/blog/mosn-subproject-layotto-opening-a-new-chapter-in-service-grid-application-runtime/index)

### Sidecar or SDK

基于Mecha架构理念的Multi-Runtime，以Sidecar的方式提供标准API的功能，看起来似乎是最合理的选择。

那为什么不直接使用Dapr/Layotto等项目，而是选择开发 **富SDK模式** 的Capa项目呢。

概括：_以Dapr为代表的Sidecar架构是未来，但现有的很多企业和系统很难一步到位的升级到Sidecar架构，富SDK架构将会长期的存在下去。_

引申：_面对庞大的Java系统体系，Capa项目将使用富SDK模型支持Java系统向Mecha架构过渡。在Dapr等项目成熟后，也可以无缝衔接到Sidecar架构。_

关于此问题的具体讨论请参考：

[SDK模型的Dapr API](https://github.com/dapr/dapr/issues/3261)  

[Dapr API的未来计划](https://github.com/dapr/dapr/issues/2817)

[Java SDK的设计讨论](https://github.com/mosn/layotto/issues/188)

## 特征

### API定义

Capa API设计follow社区标准，请参考 Dapr / Layotto 等开源项目的API定义。

API定义放置于以下独立仓库中，与Capa项目解绑，希望发展成为社区的API标准定义：

+ java: [cloud-runtimes-jvm](https://github.com/capa-cloud/cloud-runtimes-jvm)
+ python(alpha): [cloud-runtimes-python](https://github.com/capa-cloud/cloud-runtimes-python)
+ golang(alpha): [cloud-runtimes-golang](https://github.com/capa-cloud/cloud-runtimes-golang)

#### 为什么不直接使用Dapr API？

由于目前Dapr API和Dapr项目强绑定，但我们希望这套API能够成为整个社区的标准，所以Capa将API定义放在独立仓库中，并时刻和上游社区标准保持同步。

我们希望后续Dapr能够将其API独立部署出来，与Dapr项目相解耦，成为整个社区的标准。

关于此项的讨论，请查看：

[Future plans for dapr api](https://github.com/dapr/dapr/issues/2817)

### Capa特性

Capa(Java SDK)是面向Java应用实现Mecha架构的SDK解决方案，它目前支持以下领域的特性：

+ Service Invocation (RPC服务调用)
+ Configuration Centor (Configuration动态配置)
+ Publish/Subscribe (Pub/Sub发布订阅)
+ State Management (State状态管理)
+ Application Log/Metrics/Traces (Telemetry可观测性)
+ Database (SQL关系型数据库) -alpha
+ Schedule (Schedule定时调度) -alpha
+ ...

## 设计

### Capa设计

设计思路：**标准API + 可拔插可替换的SDK组件** 模式

在不同的分布式中间件领域，Capa提供与具体中间件API无关的统一的标准编程API。 所以应用程序在使用Capa编程时不需要依赖任何具体的中间件API，只需要依赖Capa的标准编程API即可。

在部署到不同的目标环境时，Capa将会装载标准API的不同实现类到应用程序中。当调用统一的编程API时，底层运行时会适配到不同的具体中间件SDK实现。

中间件团队需要针对不同目标环境，开发标准API在目标环境下的实现类即可；而应用代码可以拥有"一次编写，随处运行"的开发体验。

### SDK设计

Capa module划分主要为以下几个部分：

* sdk
* sdk-component
* sdk-spi
* sdk-spi-demo/...

![capa-design](./docs/capa-design/capa-layer.PNG)

应用程序编程时只需要依赖sdk即可，并使用SDK模块中定义的统一编程API。

在运行前，会将具体的SPI实现包引入进去，作为统一编程API的具体实现。

## 使用

### Getting Started

#### Importing Capa's Java SDK

For a Maven project, add the following to your pom.xml file:

```xml

<project>
    ...
    <dependencies>
        ...
        <!-- Capa's core SDK with all features. -->
        <dependency>
            <groupId>group.rxcloud</groupId>
            <artifactId>capa-sdk</artifactId>
            <version>2.11.1.RELEASE</version>
        </dependency>
        ...
    </dependencies>
    ...
</project>
```

Sample implementation library:

```xml

<project>
    ...
    <dependencies>
        ...
        <!-- Capa's core SDK with all features. -->
        <dependency>
            <groupId>group.rxcloud</groupId>
            <artifactId>capa-sdk-spi-demo</artifactId>
            <version>2.11.1.RELEASE</version>
        </dependency>
        ...
    </dependencies>
    ...
</project>
```

### Running the examples

Try the following examples to learn more about Capa's Java SDK:

* [capa-demo](https://github.com/capa-cloud/capa-java/tree/master/sdk-spi-demo)
* [capa-aws](https://github.com/capa-cloud/capa-java-aws)
* [capa-alibaba](https://github.com/capa-cloud/capa-java-alibaba)

### 低改造成本迁移

如果要使用原生的Capa API，您的遗留系统需要面对较大的重构工作量。

为了使迁移做到低成本，我们可以复用目前使用到的中间件API。

通过开发一个适配层项目(提供相同的注解/接口调用方式)，将原中间件API的实现更改为Capa API。

如此一来，应用程序只需要更改很少的代码(例如更换注解/接口的路径名)即可迁移到Capa架构。

关于该问题的讨论，请查看：

[Java sdk design 调研：能否复用业界已有的事实标准](https://github.com/mosn/layotto/issues/206)

[Capa API adapted to spring annotation.](https://github.com/reactivegroup/sigs/issues/16)

[遗留中间件SDK无感迁移到Capa.](https://github.com/reactivegroup/sigs/issues/18)

## 开发

#### Reactor API

考虑到异步调用模式，以及非阻塞IO的使用，我们原生提供Reactor的编程模型，您也可以通过其`block()`方法使用同步调用的功能。

The Java SDK for Capa is built using [Project Reactor](https://projectreactor.io/). It provides an asynchronous API for
Java. When consuming a result is consumed synchronously, as in the examples referenced above, the `block()` method is
used.

The code below does not make any API call, it simply returns
the [Mono](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Mono.html) publisher object. Nothing
happens until the application subscribes or blocks on the result:

```java
Mono<String> result=capaRpcClient.invokeMethod(SERVICE_APP_ID,"say","hello",HttpExtension.POST,null,TypeRef.STRING);
```

To start execution and receive the result object synchronously, use `block()`. The code below shows how to execute the
call and consume an empty response:

```java
Mono<String> result=capaRpcClient.invokeMethod(SERVICE_APP_ID,"say","hello",HttpExtension.POST,null,TypeRef.STRING);
        String response=result.block();
```

#### Exception handling

Most exceptions thrown from the SDK are instances of `CapaException`. `CapaException` extends from `RuntimeException`,
making it compatible with Project Reactor.

## 未来发展

### 关于Multi-Runtime的思考

[Multi-Runtime 2022：待解决的问题](https://zhuanlan.zhihu.com/p/435012312?utm_source=wechat_session&utm_medium=social&utm_oi=618742049890111488&utm_content=group2_article&utm_campaign=shareopn)
