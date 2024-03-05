package cn.tycoding.springai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

/**
 * @author tycoding
 * @since 2024/3/5
 */
@SpringBootTest
public class EmbeddingTest {

    @Autowired
    private EmbeddingClient embeddingClient;
    @Autowired
    private VectorStore vectorStore;

    @Value("classpath:data.json")
    private Resource jsonResource;
    @Value("classpath:data.docx")
    private Resource docxResource;
    @Value("classpath:data.xlsx")
    private Resource excelResource;
    @Value("classpath:data.txt")
    private Resource txtResource;

    @Test
    void embedding() {
        String text = "我是一个Java程序员";
        List<Double> embedList = embeddingClient.embed(text);
        List<Document> documents = List.of(new Document(text));
        vectorStore.add(documents);
    }

    @Test
    void search() {
        List<Document> d1 = vectorStore.similaritySearch("我是");
        List<Document> d2 = vectorStore.similaritySearch(SearchRequest.query("我是").withTopK(5));
        System.out.println("----");
    }

    @Test
    void jsonReader() {
        JsonReader reader = new JsonReader(jsonResource, "type", "keyword");
        vectorStore.add(reader.get());
    }

    @Test
    void txtReader() {
        TextReader reader = new TextReader(txtResource);
        reader.getCustomMetadata().put("filename", "data1.txt");
        vectorStore.add(reader.get());
    }

    @Test
    void docReader() {
        TikaDocumentReader reader = new TikaDocumentReader(docxResource);
        vectorStore.add(reader.get());
    }

    @Test
    void filter() {

    }

}
