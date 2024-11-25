# ai-biz
集成阿里的通义千问模型，进行模型调用  

## 大模型集成
### 1.1 新建ai-biz子模块，引入以下依赖
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>dashscope-sdk-java</artifactId>
        <version>2.14.4</version>
    </dependency>
</dependencies>
```
### 1.2 配置通义千问的配置
在yaml文件中配置senders
```yaml
server:
  port: 9986
spring:
  application:
    name: ai-biz-service

  ai:
    qwen:
      api-key : 你的API-KEY(在阿里云的百炼平台申请一个)
      chat:
        options:
          systeMessage : 你是一个问答小助手！
          model : qwen-turbo
          temperature : 0.7
```
### 1.3 定义读取配置的QwenConfigProperties
```java
@ConfigurationProperties(
        prefix = "spring.ai.qwen.chat.options"
)
@Data
@NoArgsConstructor
public class QwenConfigProperties {

    private String systeMessage = "";
    private String model = "";
    private Float temperature = 0.9f;

}
```
### 1.4 定义controller接口
```java
@EnableConfigurationProperties(QwenConfigProperties.class)
@RestController
public class AIController {


    @Value("${spring.ai.qwen.api-key}")
    private String apiKey = "";

    @Autowired
    private QwenConfigProperties qwenConfigProperties;

    @GetMapping("/ai/generate")
    public String generate(@RequestParam(value = "message", defaultValue = "你是谁？") String message) throws NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(qwenConfigProperties.getSysteMessage())
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(message)
                .build();
        GenerationParam param = GenerationParam.builder()
                .apiKey(apiKey)
                .model(qwenConfigProperties.getModel())
                .temperature(qwenConfigProperties.getTemperature())
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        GenerationResult result = gen.call(param);
        GenerationOutput output = result.getOutput();
        return output.getChoices().get(0).getMessage().getContent();
    }

}
```

### 1.5 使用：http://localhost:9986/ai/generate 测试访问