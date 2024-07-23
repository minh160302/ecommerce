package com.rvlt.ecommerce.utils;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.inventory.CreateInventoryBatchRq;
import com.rvlt.ecommerce.dto.inventory.CreateInventoryRq;


import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelUtils {
    public static RequestMessage<CreateInventoryBatchRq> parseExcelToBatchRequest(InputStream inputStream) throws Exception {
        RequestMessage<CreateInventoryBatchRq> toReturn = new RequestMessage<>();
        CreateInventoryBatchRq batch = new CreateInventoryBatchRq();
        List<CreateInventoryRq> inventories = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                // Skip header
                if (row.getRowNum() == 0) {
                    continue;
                }

                CreateInventoryRq inventoryRq = new CreateInventoryRq();
                Cell nameCell = row.getCell(0);
                Cell countCell = row.getCell(1);

                if (nameCell != null && countCell != null) {
                    inventoryRq.setName(nameCell.getStringCellValue().trim());
                    inventoryRq.setCount((int) countCell.getNumericCellValue());
                    inventories.add(inventoryRq);
                }
            }
        }
        batch.setInventories(inventories);
        toReturn.setData(batch);
        // should change to String Date
        toReturn.setTime(new Date());
        return toReturn;
    }

//    public static Boolean isExcelFormat(MultipartFile file) {
//
//    }
}
