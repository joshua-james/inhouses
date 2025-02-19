package com.example.sanity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/sanity")
public class SanityController {
    
    @GetMapping("")
    public Map<String, Object> main() {

        Map<String, Object> response = null;

        boolean databaseConnected = false;
        boolean riotAPIConnected = false;
        boolean apiVersionLoaded = false;
        boolean datadragonLoaded = false;
        //TODO Add working sanity checks. Remember to update as we add start-up processes. 

        try {
            response = Map.of(
                "status", 200,
                "message", "Application is currently running!",
                "databaseConnected", databaseConnected,
                "riotAPIConnected", riotAPIConnected,
                "apiVersionLoaded", apiVersionLoaded,
                "dataDragonLoaded", datadragonLoaded  
            );
            return response;
        } catch (Exception e) {
            response = Map.of(
                "status", 500, 
                "message", e);
            return response;
        }
    }
}
