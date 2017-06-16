package com.db.hackhaton.service.export.excel;

import com.db.hackhaton.domain.CaseFieldValue;
import com.db.hackhaton.domain.MedicalCase;
import com.db.hackhaton.domain.Registry;
import com.db.hackhaton.domain.RegistryField;
import com.db.hackhaton.repository.RegistryRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ExcelRegistryGenerator {

    @Autowired
    private RegistryRepository registryRepository;

    public void generateReportWithData(OutputStream outputStream, Long registryId) {
        List<NameValues> fieldValues = getFieldValues(registryId);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Registry");

        for(int i = 0; i<fieldValues.size(); i++) {
            int rowNum = 0;
            XSSFRow row = createOrGetRow(sheet, rowNum++);

            // add header column
            Cell headerCell = row.createCell(i);
            headerCell.setCellValue(fieldValues.get(i).fieldName);

            // add data values
            List<String> values = fieldValues.get(i).values;
            for(int j=0; j<values.size(); j++) {
                XSSFRow valueRow = createOrGetRow(sheet, rowNum++);
                Cell cell = valueRow.createCell(i);
                cell.setCellValue(values.get(j));
            }
        }

        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException("Could not generate Excel file for requested registry");
        }

    }

    public void generateReportWithoutData(OutputStream outputStream, Long registryId) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Registry");
        int rowNum = 0;
        XSSFRow row = sheet.createRow(rowNum++);

        int colNum = 0;
        for(String headerValue : getRegistryFieldNames(registryId)) {
            Cell cell = row.createCell(colNum++);
            cell.setCellValue(headerValue);
        }

        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException("Could not generate Excel file for requested registry");
        }

    }

    private List<String> getRegistryFieldNames(Long registryId) {
        Set<RegistryField> fields = getFields(registryId);
        return fields.stream().map(e->e.getName()).collect(Collectors.toList());

    }

    private Set<RegistryField> getFields(Long registryId) {
        return registryRepository.findOne(registryId).getRegistryFields();
    }

    private List<NameValues> getFieldValues(Long registryId) {
        Registry registry = registryRepository.findOne(registryId);
        Set<MedicalCase> medicalCases = registry.getMedicalCases();
        Map<String, List<String>> results = new HashMap<>();

        for (MedicalCase medicalCase : medicalCases) {
            Set<CaseFieldValue> caseFields = medicalCase.getCaseFields();
            for(CaseFieldValue fieldValue : caseFields) {
                if(results.get(fieldValue.getRegistryField().getName()) == null) {
                    ArrayList<String> arrayValues = new ArrayList<>();
                    results.put(fieldValue.getRegistryField().getName(), arrayValues);
                    arrayValues.add(fieldValue.getValue());

                } else {
                    List<String> values = results.get(fieldValue.getRegistryField().getName());
                    values.add(fieldValue.getValue());
                }
            }
        }

        List<NameValues> resultNameValues = new ArrayList<>();
        results.entrySet().stream().forEach( e ->
        {
            NameValues nv = new NameValues();
            nv.fieldName = e.getKey();
            nv.values = e.getValue();
            resultNameValues.add(nv);
        });

        return resultNameValues;
    }

    private XSSFRow createOrGetRow(XSSFSheet sheet, int rowNum) {
        if(sheet.getRow(rowNum) != null) {
            return sheet.getRow(rowNum);
        }

        return sheet.createRow(rowNum);
    }
}
