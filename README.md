#  Spring AI使用案例

![](http://cdn.tycoding.cn/wxcode.png)

```java
    @Test
    void streamChat() throws ExecutionException, InterruptedException {
        // 构建一个异步函数，实现手动关闭测试函数
        CompletableFuture<Void> future = new CompletableFuture<>();

        String message = """
                年终总结
                """;
        PromptTemplate promptTemplate = new PromptTemplate("""
                你是一个Java开发工程师，你擅长于写公司年底的工作总结报告，
                根据：{message} 场景写100字的总结报告
                """);
        Prompt prompt = promptTemplate.create(Map.of("message", message));
        chatClient.stream(prompt).subscribe(
                chatResponse -> {
                    System.out.println("response: " + chatResponse.getResult().getOutput().getContent());
                },
                throwable -> {
                    System.err.println("err: " + throwable.getMessage());
                },
                () -> {
                    System.out.println("complete~!");
                    // 关闭函数
                    future.complete(null);
                }
        );
        future.get();
    }
```



## 合作和联系

- 个人博客：[http://tycoding.cn](http://tycoding.cn/)
- GitHub：https://github.com/tycoding
- 微信公众号：程序员涂陌
- 微信交流群：公众号后台回复：`微信群`
