package com.db.hackhaton.web.rest.export;

import com.db.hackhaton.service.export.excel.ExcelRegistryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/export/registry")
public class RegistryExportResource {

    @Autowired
    private ExcelRegistryGenerator excelRegistryGenerator;

    @RequestMapping(method = RequestMethod.GET, value = "/template/{id}")
    public void generateReportWithoutData(@PathVariable("id") Long registryId, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String filename = createFilename(registryId);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        excelRegistryGenerator.generateReportWithoutData(response.getOutputStream(), registryId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/data/{id}")
    public void generateReportWithData(@PathVariable("id") Long registryId, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String filename = createFilename(registryId);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        excelRegistryGenerator.generateReportWithData(response.getOutputStream(), registryId);
    }

    private String createFilename(Long registryId) {
        return "registry_" + registryId + ".xlsx";
    }
}
