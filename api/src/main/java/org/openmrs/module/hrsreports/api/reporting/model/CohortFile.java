package org.openmrs.module.hrsreports.api.reporting.model;

import java.util.Date;
import java.util.Set;

/**
 * A class that holds data in cohort csv file
 */
public class CohortFile {
    private Date effectiveDate;
    private Set<Long> patientIds;

    public CohortFile() {
    }

    public CohortFile(Date effectiveDate, Set<Long> patientIds) {
        this.effectiveDate = effectiveDate;
        this.patientIds = patientIds;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Set<Long> getPatientIds() {
        return patientIds;
    }

    public void setPatientIds(Set<Long> patientIds) {
        this.patientIds = patientIds;
    }
}
