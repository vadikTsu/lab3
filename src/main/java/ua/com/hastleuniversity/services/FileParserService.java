package ua.com.hastleuniversity.services;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import ua.com.hastleuniversity.domain.article.Article;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileParserService {

    private final ObjectMapper objectMapper;

    private final Path fileStorage = Paths.get("file.json");
    private final Path htmlStorage = Paths.get("files.html");

    public FileParserService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String readFile() throws IOException {
        JsonNode jsonNode = objectMapper.readTree(fileStorage.toFile());
        return jsonNode.toPrettyString();
    }

    public Resource loadFileAsResource() throws Exception {
        byte[] strToBytes = convertJsonToHtmlTable().getBytes();
        Files.write(htmlStorage, strToBytes);
        Resource resource = new UrlResource(htmlStorage.toUri());
        if (resource.exists()) {
            return resource;
        } else {
            throw new Exception("File not found " + fileStorage.getFileName());
        }
    }

    public String convertJsonToHtmlTable() throws IOException {
        JsonNode arrayNode = objectMapper.readTree(fileStorage.toFile());

        if (!arrayNode.isArray()) {
            throw new IllegalArgumentException("JSON must be an array of objects");
        }

        StringBuilder htmlTable = new StringBuilder("<table border='1'>");

        JsonNode firstElement = arrayNode.elements().next();
        htmlTable.append("<tr>");
        firstElement.fieldNames().forEachRemaining(fieldName ->
                htmlTable.append("<th>").append(fieldName).append("</th>")
        );
        htmlTable.append("</tr>");

        for (JsonNode jsonNode : arrayNode) {
            htmlTable.append("<tr>");
            jsonNode.fields().forEachRemaining(entry -> {
                if (entry.getValue().isObject()) {
                    htmlTable.append("<td>");
                    entry.getValue().fields().forEachRemaining(nestedEntry ->
                            htmlTable.append(nestedEntry.getKey()).append(": ").append(nestedEntry.getValue().asText()).append("<br>")
                    );
                    htmlTable.append("</td>");
                } else {
                    htmlTable.append("<td>").append(entry.getValue().asText()).append("</td>");
                }
            });
            htmlTable.append("</tr>");
        }

        htmlTable.append("</table>");
        System.out.println(htmlTable.toString());
        return htmlTable.toString();
    }

    public List<Article> searchArticles(String searchId, String searchTitle, boolean includeId, boolean includeTitle) throws IOException {
        List<Article> matchingArticles = new ArrayList<>();
        JsonFactory jsonFactory = new JsonFactory();
        File file = fileStorage.toFile();
        try (JsonParser jsonParser = jsonFactory.createParser(file)) {
            if (jsonParser.nextToken() == JsonToken.START_ARRAY) {
                while (jsonParser.nextToken() == JsonToken.START_OBJECT) {
                    Article article = objectMapper.readValue(jsonParser, Article.class);
                    boolean matches = false;
                    if (includeId && includeTitle) {
                        if (includeId) {
                            if (searchId != null && !searchId.isEmpty() && article.getId() == Long.parseLong(searchId) &&
                                    searchTitle != null && !searchTitle.isEmpty() && article.getTitle() != null &&
                                    article.getTitle().toLowerCase().contains(searchTitle.toLowerCase())) {
                                matches = true;
                            } else {
                                matches = false;
                            }

                        }
                    } else {
                        if (includeId) {
                            if (searchId != null && !searchId.isEmpty() && article.getId() == Long.parseLong(searchId)) {
                                matches = true;
                            } else {
                                matches = false;
                            }

                        }
                        if (includeTitle) {
                            if (searchTitle != null && !searchTitle.isEmpty() && article.getTitle() != null &&
                                    article.getTitle().toLowerCase().contains(searchTitle.toLowerCase())) {
                                matches = true;
                            } else {
                                matches = false;
                            }
                        }
                    }
                    if (matches) {
                        matchingArticles.add(article);
                    }
                }
            }
        }
        return matchingArticles;
    }


    public void serializeArticlesToFile(List<Article> articles) throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        try (FileOutputStream fos = new FileOutputStream(fileStorage.toFile());
             JsonGenerator generator = jsonFactory.createGenerator(fos)) {
            generator.setPrettyPrinter(new DefaultPrettyPrinter());
            generator.writeStartArray();

            for (Article article : articles) {
                generator.writeStartObject();

                generator.writeNumberField("id", article.getId());
                generator.writeStringField("title", article.getTitle());
                generator.writeStringField("imageUrl", article.getImageUrl());
                generator.writeStringField("text", article.getText());
                generator.writeStringField("publishDate", article.getPublishDate().toString());
                if (article.getAuthor() != null) {
                    generator.writeObjectFieldStart("author");
                    generator.writeNumberField("id", article.getAuthor().getId());
                    generator.writeStringField("authorName", article.getAuthor().getAuthorName());
                    generator.writeStringField("authorPosition", article.getAuthor().getAuthorPosition());
                    generator.writeStringField("authorEmail", article.getAuthor().getAuthorEmail());
                    generator.writeEndObject();
                }
                generator.writeEndObject();
            }
            generator.writeEndArray();
        }
    }
}

