package com.db.hackhaton.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to JHipster.
 *
 * <p>
 *     Properties are configured in the application.yml file.
 * </p>
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String uploadPath;

    private String adminEmail;

    private String newMedicalCaseSubjectFormat;
    private String newMedicalCaseContentFormat;

    public String getNewMedicalCaseSubjectFormat() {
        return newMedicalCaseSubjectFormat;
    }

    public void setNewMedicalCaseSubjectFormat(String newMedicalCaseSubjectFormat) {
        this.newMedicalCaseSubjectFormat = newMedicalCaseSubjectFormat;
    }

    public String getNewMedicalCaseContentFormat() {
        return newMedicalCaseContentFormat;
    }

    public void setNewMedicalCaseContentFormat(String newMedicalCaseContentFormat) {
        this.newMedicalCaseContentFormat = newMedicalCaseContentFormat;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }
}
