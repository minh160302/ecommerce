//package com.rvlt.ecommerce.util;
//
//import com.rvlt.ecommerce.dto.inventory.CreateInventoryBatchRq;
//import com.rvlt.ecommerce.dto.inventory.CreateInventoryRq;
//
//
//import java.io.FileInputStream;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ExcelUtils {
//    public static CreateInventoryBatchRq parseExcelToCreateInventoryBatchRq(InputStream inputStream) throws Exception {
//        CreateInventoryBatchRq batchRequest = new CreateInventoryBatchRq();
//        List<CreateInventoryRq> inventoryRequests = new ArrayList<>();
//
//        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
//            Sheet sheet = workbook.getSheetAt(0);
//            for (Row row : sheet) {
//                if (row.getRowNum() == 0) continue; // Skip header
//
//                CreateInventoryRq request = new CreateInventoryRq();
//                request.setName(row.getCell(0).getStringCellValue().trim());
//                request.setCount((int) row.getCell(1).getNumericCellValue());
//                inventoryRequests.add(request);
//            }
//        }
//
//        batchRequest.setInventories(inventoryRequests);
//        return batchRequest;
//    }
//}
