package org.openmrs.module.hrsreports.fragment.controller;

import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hrsreports.api.util.CSVFileReader;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * controller for cohort import fragment
 */
public class UploadFileFragmentController {
    public void controller(FragmentModel model){

    }
    public void uploadFile (@RequestParam("cohortFile") MultipartFile cohortFile, UiUtils ui) {
        // find the directory to put the file in
        AdministrationService as = Context.getAdministrationService();
        String folderName = as.getGlobalProperty("hrsreports.cohort_file_dir");

        String csvFilename = "testCohort.csv";
        File loaddir = OpenmrsUtil.getDirectoryInApplicationDataDirectory(folderName);
        File csvFile = new File(loaddir, csvFilename);


        try {
            cohortFile.transferTo(csvFile);
            System.out.println("Cohort file was successfully uploaded. You can now run hrsreport from Reports app on the main page");
        } catch (IOException e) {
            System.out.println("Sorry, the file could not be copied");
            e.printStackTrace();
        }

       /* BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(csvFile));

        } catch (IOException e) {
            e.printStackTrace();
        }

        String line;

        try {
            while ((line = bufferedReader.readLine()) != null)
            {
                String fileBlocks[] = line.split(",");

                System.out.println("Date component: " + fileBlocks[0]);

                for (int i=1; i < fileBlocks.length; i++) {
                    System.out.println("ID: " + fileBlocks[i] + " at: " + i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
