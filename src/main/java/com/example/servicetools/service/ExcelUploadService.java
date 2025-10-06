package com.example.servicetools.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Service
public class ExcelUploadService {

    public List<Map<String, Object>> parseExcel(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return Collections.emptyList();
        }

        String filename = Optional.ofNullable(file.getOriginalFilename()).orElse("").toLowerCase(Locale.ROOT);
        try (InputStream in = file.getInputStream(); Workbook workbook = createWorkbook(filename, in)) {
            Sheet sheet = workbook.getNumberOfSheets() > 0 ? workbook.getSheetAt(0) : null;
            if (sheet == null) return Collections.emptyList();

            Iterator<Row> rowIterator = sheet.iterator();
            if (!rowIterator.hasNext()) return Collections.emptyList();

            // Read header row
            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            for (int c = 0; c < headerRow.getLastCellNum(); c++) {
                headers.add(getCellString(headerRow.getCell(c)));
            }

            List<Map<String, Object>> rows = new ArrayList<>();
            DataFormatter formatter = new DataFormatter();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, Object> obj = new LinkedHashMap<>();
                for (int c = 0; c < headers.size(); c++) {
                    String header = headers.get(c);
                    if (header == null || header.isBlank()) continue;
                    Cell cell = row.getCell(c);
                    Object value = getCellValue(cell, formatter);
                    obj.put(header, value);
                }
                rows.add(obj);
            }
            return rows;
        }
    }

    private Workbook createWorkbook(String filename, InputStream in) throws Exception {
        if (filename.endsWith(".xlsx")) {
            return new XSSFWorkbook(in);
        } else if (filename.endsWith(".xls")) {
            return new HSSFWorkbook(in);
        }
        // Try to auto-detect; default to XSSFWorkbook
        return new XSSFWorkbook(in);
    }

    private String getCellString(Cell cell) {
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }

    private Object getCellValue(Cell cell, DataFormatter formatter) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case BOOLEAN -> cell.getBooleanCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue();
                }
                yield cell.getNumericCellValue();
            }
            case FORMULA -> formatter.formatCellValue(cell);
            case BLANK -> null;
            default -> formatter.formatCellValue(cell);
        };
    }
}


