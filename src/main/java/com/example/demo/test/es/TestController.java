package com.example.demo.test.es;

import com.example.demo.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {
     @Autowired
     NBAPlayerService elasticSearchService;

     @GetMapping("/es")
     public JsonData testEs() throws IOException {
         elasticSearchService.importAll();
         Map<String, Object> player = elasticSearchService.getPlayer("1");

         return JsonData.buildSuccess(player);
     }
}
