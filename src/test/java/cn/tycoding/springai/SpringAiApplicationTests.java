package cn.tycoding.springai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootTest
class SpringAiApplicationTests {

    @Autowired
    private OllamaChatClient chatClient;

    @Test
    void contextLoads() {
        String message = """
                鲁迅和周树人是什么关系？
                """;
        System.out.println(chatClient.call(message));
    }

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
}
