package com.example.sanity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/sanity")
public class SanityController {

    private Random random = new Random();
    private boolean bobiSane = random.nextBoolean();
    
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

    @GetMapping("/bobi")
    public Map<String, Object> bobi() {
        Map<String, Object> response = null;
        if (bobiSane) {
            response = Map.of(
                "status", 200,
                "message", "A-OK, chief."
            );
        } else {
            response = Map.of(
                "status", 500,
                "message", "He missed his meds, so he atleast has all his friends! :D"
            );
        }
        return response;
    }
}
