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
        return new HashSet<Integer>(Arrays.asList(2016364,18477,5189,6446,6528,5196,18477,16393,26095,16792,5056,16520,16559,15367,14417,6457,4860,19082,13749,15649,19916,7432,6178));
    }

    public static String getInitialCohortQuery () {
        String qry = "select v.visit_id from visit v "
                        + " inner join encounter e "
                        + " on e.visit_id=v.visit_id "
                        + " and e.voided=0 and v.voided=0 "
                        + " inner join obs o on o.encounter_id=e.encounter_id "
                  + " where e.encounter_type = 8 and o.concept_id in(5497, 730,856) and v.date_started >= '2014-01-01' "
                  + " and v.patient_id in (:patientIds) "; //consider filtering using concepts for cd4 and viral load
        return qry;

    }

    private static CohortFile processCSVFile () {

        AdministrationService as = Context.getAdministrationService();
        String folderName = as.getGlobalProperty("hrsreports.cohort_file_dir");

        String csvFilename = "testCohort.csv";
        File loaddir = OpenmrsUtil.getDirectoryInApplicationDataDirectory(folderName);
        File csvFile = new File(loaddir, csvFilename);

        System.out.println("File status ==================" + csvFile.exists());

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(csvFile));
            System.out.println("The file has been read successfully");

        } catch (IOException e) {
            e.printStackTrace();
        }

        String line;
        CohortFile cohortFile = new CohortFile();
        Set<Integer> ids = new HashSet<Integer>();

        try {
            while ((line = bufferedReader.readLine()) != null) //we know it is one line
            {
                System.out.println("Looping through");
                String fileBlocks[] = line.split(COMMA_DELIMITER);
                System.out.println("Date component: " + fileBlocks[EFFECTIVE_DATE_INDEX]);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");


                try {
                    Date effectiveDate = df.parse(fileBlocks[EFFECTIVE_DATE_INDEX]);
                    cohortFile.setEffectiveDate(effectiveDate);
                    System.out.println("Parsed date: " + effectiveDate);
                } catch (ParseException e) {
                    System.out.println("There was an error parsing date");
                    e.printStackTrace();
                }

                System.out.println("Block lenght " + fileBlocks.length);

                for (int i=1; i < fileBlocks.length; i++) {
                    Integer id = Integer.valueOf(fileBlocks[i]);
                    ids.add(id);
                    System.out.println("ID: " + id + " at: " + i);
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
