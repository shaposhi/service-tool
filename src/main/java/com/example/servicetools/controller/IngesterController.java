package com.example.servicetools.controller;

import com.example.servicetools.dto.IngesterResponse;
import com.example.servicetools.dto.PublishRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/ingester")
public class IngesterController {

    @GetMapping("/getById")
    public ResponseEntity<IngesterResponse> getById(@RequestParam String id) {
        IngesterResponse response = new IngesterResponse(
            id,
            UUID.randomUUID().toString(),
            new Date()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/publish")
    public ResponseEntity<IngesterResponse> publish(@RequestBody PublishRequest request) {
        // For now, just return a sample response with the first number as ID
        String firstNumber = request.getNumbers().isEmpty() ? "none" : String.valueOf(request.getNumbers().size());
        IngesterResponse response = new IngesterResponse(
            firstNumber,
            UUID.randomUUID().toString() +  " " + firstNumber  + "-published",
            new Date()
        );
        return ResponseEntity.ok(response);
    }
}
