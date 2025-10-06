package com.example.servicetools.controller;

import com.example.servicetools.service.ExcelUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/excel")
public class ExcelUploadController {

    @Autowired
    private ExcelUploadService excelUploadService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<Map<String, Object>>> upload(@RequestPart("file") MultipartFile file) {
        try {
            List<Map<String, Object>> rows = excelUploadService.parseExcel(file);
            return ResponseEntity.ok(rows);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}


