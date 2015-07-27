package org.openmrs.module.hrsreports.api.util;

import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hrsreports.api.reporting.model.CohortFile;
import org.openmrs.util.OpenmrsUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Util class for HRSReports
 */
public class HRSUtil {
    private static final String COMMA_DELIMITER = ",";
    private static final int EFFECTIVE_DATE_INDEX = 0;
    public static Set<Integer> getReportCohort() {
        return processCSVFile().getPatientIds();
    }

    public static Date getReportEffectiveDate() {
        return processCSVFile().getEffectiveDate();
    }

    public static String getInitialCohortQuery () {
        String qry = "select v.visit_id from visit v "
                        + " inner join encounter e "
                        + " on e.visit_id=v.visit_id "
                        + " and e.voided=0 and v.voided=0 "
                        + " inner join obs o on o.encounter_id=e.encounter_id "
                  + " where e.encounter_type = 8 and o.concept_id in(5497, 730,856) and v.date_started >= :effectiveDate "
                  + " and v.patient_id in (:patientIds) "; //consider filtering using concepts for cd4 and viral load
        return qry;

    }

    private static CohortFile processCSVFile () {

        AdministrationService as = Context.getAdministrationService();
        String folderName = as.getGlobalProperty("hrsreports.cohort_file_dir");
        String csvFilename = "testCohort.csv";
        File loaddir = OpenmrsUtil.getDirectoryInApplicationDataDirectory(folderName);
        File csvFile = new File(loaddir, csvFilename);
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(csvFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line;
        CohortFile cohortFile = new CohortFile();
        Set<Integer> ids = new HashSet<Integer>();

        try {
            while ((line = bufferedReader.readLine()) != null) //we know it is one line
            {
                String fileBlocks[] = line.split(COMMA_DELIMITER);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    Date effectiveDate = df.parse(fileBlocks[EFFECTIVE_DATE_INDEX]);
                    cohortFile.setEffectiveDate(effectiveDate);
                } catch (ParseException e) {
                    System.out.println("There was an error parsing date");
                    e.printStackTrace();
                }

                for (int i=1; i < fileBlocks.length; i++) {
                    Integer id = Integer.valueOf(fileBlocks[i]);
                    ids.add(id);
                }
                cohortFile.setPatientIds(ids);
                return cohortFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cohortFile;
    }

}
