package ua.com.hastleuniversity.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.hastleuniversity.domain.article.Article;
import ua.com.hastleuniversity.services.ArticleService;
import ua.com.hastleuniversity.services.FileParserService;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/json-parser")
public class JsonParseController {

    @Autowired
    FileParserService parserService;
    @Autowired
    ArticleService articleService;
    @Autowired
    private ObjectMapper objectMapper;

    @ModelAttribute
    public void loadSessionAttribute(Model model) throws IOException{
      parserService.serializeArticlesToFile(articleService.getAllArticles());
      String loaded = parserService.readFile();
        model.addAttribute("loadedData", "");
        model.addAttribute("fileName", "file.json");
        model.addAttribute("article", new Article());
    }

    @GetMapping()
    public String jsonParser(){
        return "json-parser";
    }

    @GetMapping("/read")
    public ResponseEntity<String> readJsonFile() {
        try {
            String json = parserService.readFile();
            return new ResponseEntity<>(json, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error reading JSON file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String searchId,
            @RequestParam(required = false) String searchTitle,
            @RequestParam(required = false, defaultValue = "false") boolean includeId,
            @RequestParam(required = false, defaultValue = "false") boolean includeTitle,
            Model model) {

        try {
            List<Article> searchResults = parserService.searchArticles(searchId, searchTitle, includeId, includeTitle);
            String resultsJson = objectMapper.writeValueAsString(searchResults);
            model.addAttribute("loadedData", resultsJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "json-parser";
    }


    @PostMapping("/save")
    public String handleFormSubmission(@ModelAttribute Article article) {
        File file = new File("file.json");
        try {

            ArrayNode articlesArray;
            if (file.exists() && file.length() > 0) {
                articlesArray = (ArrayNode) objectMapper.readTree(file);
            } else {
                articlesArray = objectMapper.createArrayNode();
            }
            article.setId(articlesArray.size()+1);
            articlesArray.add(objectMapper.valueToTree(article));
            objectMapper.writeValue(file, articlesArray);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/json-parser";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(HttpServletResponse response) {
        Resource resource;
        try {
            resource = parserService.loadFileAsResource();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(Files.probeContentType(resource.getFile().toPath())))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
