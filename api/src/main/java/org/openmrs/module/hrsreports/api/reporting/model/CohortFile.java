package org.openmrs.module.hrsreports.api.reporting.model;

import java.util.Date;
import java.util.Set;

/**
 * A class that holds data in cohort csv file
 */
public class CohortFile {
    private Date effectiveDate;
    private Set<Integer> patientIds;

    public CohortFile() {
    }

    public CohortFile(Date effectiveDate, Set<Integer> patientIds) {
        this.effectiveDate = effectiveDate;
        this.patientIds = patientIds;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Set<Integer> getPatientIds() {
        return patientIds;
    }

    public void setPatientIds(Set<Integer> patientIds) {
        this.patientIds = patientIds;
    }
}
