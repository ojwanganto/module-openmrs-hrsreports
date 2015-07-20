package org.openmrs.module.hrsreports.api.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Util class for HRSReports
 */
public class HRSUtil {
    public static Set<Integer> getReportCohort() {
        Set<Integer> cohort = new HashSet<Integer>();
        for (int i=20; i < 1000; i++) {
            cohort.add(i);
        }
        return cohort;
    }

    public static String getInitialCohortQuery () {
        String qry = "select v.visit_id from visit v "
                        + " inner join encounter e "
                        + " on e.visit_id=v.visit_id "
                        + " and e.voided=0 and v.voided=0 "
                  + " where e.encounter_type = 8 and v.date_started = :startDate "
                  + " and v.patient_id in (:patientIds) ";
        return qry;

    }
}
