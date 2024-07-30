package com.rvlt.ecommerce.utils;

import com.rvlt.ecommerce.model.Inventory;
import com.rvlt.ecommerce.repository.InventoryRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "com.rvlt.ecommerce")
@EnableJpaRepositories(basePackages = "com.rvlt.ecommerce.repository")
@EntityScan(basePackages = "com.rvlt.ecommerce.model")
@PropertySource("classpath:application.properties")
@EnableAutoConfiguration
public class ExportInventory {

    @Autowired
    private InventoryRepository inventoryRepository;

    private static final String EXPORT_DIR = "src/main/resources/Excel";

    public void exportInventoryToExcel(List<Inventory> inventories, String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Inventory");

            // header row
            Row headerRow = sheet.createRow(0);
            String[] columns = {
                    "ID", "Name", "Total Count", "In Stock Count", "Processing Submit Count",
                    "Delivered Count", "In Session Holding", "Balance", "Processing Cancel Count",
                    "Cancelled Count", "Cancel In Progress Count", "Returned Count",
                    "Return In Progress Count", "Delivery Failed Count", "Return Failed Count",
                    "Cancel Failed Count"
            };
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // data rows
            int rowNum = 1;
            for (Inventory inventory : inventories) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(inventory.getId());
                row.createCell(1).setCellValue(inventory.getName());
                row.createCell(2).setCellValue(inventory.getTotalCount());
                row.createCell(3).setCellValue(inventory.getInStockCount());
                row.createCell(4).setCellValue(inventory.getProcessingSubmitCount());
                row.createCell(5).setCellValue(inventory.getDeliveredCount());
                row.createCell(6).setCellValue(inventory.getInSessionHolding());
                row.createCell(7).setCellValue(inventory.getBalance());
                row.createCell(8).setCellValue(inventory.getProcessingCancelCount());
                row.createCell(9).setCellValue(inventory.getCancelledCount());
                row.createCell(10).setCellValue(inventory.getCancelInProgressCount());
                row.createCell(11).setCellValue(inventory.getReturnedCount());
                row.createCell(12).setCellValue(inventory.getReturnInProgressCount());
                row.createCell(13).setCellValue(inventory.getDeliveryFailedCount());
                row.createCell(14).setCellValue(inventory.getReturnFailedCount());
                row.createCell(15).setCellValue(inventory.getCancelFailedCount());
            }

            // resize column width
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // write to file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    }

    private static String getFilePath() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = "inventory_" + dateFormat.format(new Date()) + ".xlsx";
        File directory = new File(EXPORT_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return EXPORT_DIR + File.separator + fileName;
    }

    public void exportExcelFile() {
        String filePath = getFilePath();
        try {
            List<Inventory> inventories = inventoryRepository.findAll();
            if (inventories.isEmpty()) {
                System.out.println("No inventory data.");
                return;
            }
            exportInventoryToExcel(inventories, filePath);
            System.out.println("Excel file has been created at: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error getting data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = null;
        try {
            context = new AnnotationConfigApplicationContext(ExportInventory.class);
            ExportInventory exporter = context.getBean(ExportInventory.class);
            exporter.exportExcelFile();
            System.out.println("Export complete.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (context != null) {
                context.close();
            }
            System.exit(0);
        }
    }
}
