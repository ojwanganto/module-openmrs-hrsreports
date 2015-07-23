package org.openmrs.module.hrsreports.api.util;

import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hrsreports.api.reporting.model.CohortFile;
import org.openmrs.util.OpenmrsUtil;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * csv file reader
 */
public class CSVFileReader {
    private static final String COMMA_DELIMITER = ",";
    private static final int EFFECTIVE_DATE_INDEX = 0;

    public CohortFile readCSVFile (String fileName) {

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
                   // cohortFile.getPatientIds().add(Integer.valueOf(fileBlocks[i].trim()));
                    System.out.println("ID: " + fileBlocks[i] + " at: " + i);
                }
                 return cohortFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cohortFile;
    }
}
