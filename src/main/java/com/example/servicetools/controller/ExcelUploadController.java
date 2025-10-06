package com.example.servicetools.controller;

import com.example.servicetools.dto.SimpleClient;
import com.example.servicetools.service.ExcelUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelUploadController {

    @Autowired
    private ExcelUploadService excelUploadService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<SimpleClient>> upload(@RequestPart("file") MultipartFile file) {
        try {
            List<SimpleClient> rows = excelUploadService.parseExcel(file);
            return ResponseEntity.ok(rows);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}


