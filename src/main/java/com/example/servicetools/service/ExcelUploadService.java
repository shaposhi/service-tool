package com.example.servicetools.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.example.servicetools.dto.SimpleClient;
import com.example.servicetools.model.ColumnToObjectMapping;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExcelUploadService {

    private final ColumnToObjectMappingService columnToObjectMappingService;

    public ExcelUploadService(ColumnToObjectMappingService columnToObjectMappingService) {
        this.columnToObjectMappingService = columnToObjectMappingService;
    }


    public List<SimpleClient> parseExcel(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return Collections.emptyList();
        }

        List<ColumnToObjectMapping> availableMapping = columnToObjectMappingService.findAll();
        final Map<String, String> mapping = availableMapping.stream()
            .collect(Collectors.toMap(ColumnToObjectMapping::getMainColumnName, ColumnToObjectMapping::getJsonPath));


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

            List<SimpleClient> simpleClients = new ArrayList<>();
            rows.forEach(row -> {

                SimpleClient simpleClient = new SimpleClient();

                ObjectMapper mapper = new ObjectMapper();
                ObjectNode root = mapper.createObjectNode();


                // Configure JsonPath to create missing nodes automatically
                //Configuration conf = Configuration.builder().options(EnumSet.of(Option.CREATE_MISSING_LEAF_NODES)).build();

                // Start with empty JSON
                //DocumentContext context = JsonPath.using(conf).parse("{}");

                row.forEach((key, value) -> {
                    if (mapping.containsKey(key)) {
                        System.out.println(key + " " + value + " " + mapping.get(key));


                        setJsonPathValue(root, mapping.get(key), String.valueOf(value), mapper);
                        //context.set(mapping.get(key), value);
                        
                    }
                });
                //  String resultJson = context.jsonString();
                
                //ObjectMapper mapper = new ObjectMapper();

                try {
                    String resultJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
                    simpleClient = mapper.readValue(resultJson, SimpleClient.class);
                    simpleClients.add(simpleClient);
                } catch (JsonMappingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JsonProcessingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            });

            return simpleClients;
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
                
                yield formatter.formatCellValue(cell);
                //yield cell.getNumericCellValue();
            }
            case FORMULA -> formatter.formatCellValue(cell);
            case BLANK -> null;
            default -> formatter.formatCellValue(cell);
        };
    }


    private static void setJsonPathValue(ObjectNode root, String path, String value, ObjectMapper mapper) {
        // Remove "$." prefix
        if (path.startsWith("$.")) {
            path = path.substring(2);
        }

        String[] parts = path.split("\\.");
        ObjectNode current = root;

        // Traverse or create nested nodes
        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            JsonNode child = current.get(part);
            if (child == null || !child.isObject()) {
                ObjectNode newNode = mapper.createObjectNode();
                current.set(part, newNode);
                current = newNode;
            } else {
                current = (ObjectNode) child;
            }
        }

        String lastPart = parts[parts.length - 1];
        // Try to detect type
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            current.put(lastPart, Boolean.parseBoolean(value));
        } else {
            try {
                long num = Long.parseLong(value);
                current.put(lastPart, num);
            } catch (NumberFormatException e) {
                current.put(lastPart, value);
            }
        }
    }
}


