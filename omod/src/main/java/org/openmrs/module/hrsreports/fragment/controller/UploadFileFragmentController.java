package org.openmrs.module.hrsreports.fragment.controller;

import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * controller for cohort import fragment
 */
public class UploadFileFragmentController {
    public void controller(FragmentModel model){

    }
    public void uploadFile (@RequestParam("cohortFile") MultipartFile cohortFile, UiUtils ui) {
        System.out.println("This is for the post stuff:::::::::::::::::::::::::::::::::::::::::::::::::::::::");
        System.out.println("uploaded File::::::::: " + cohortFile.getOriginalFilename());
        System.out.println("uploaded File::::::::: " + cohortFile.getSize());
        InputStream inputStream = null;
        try {
            inputStream = cohortFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        try {
            while ((line = bufferedReader.readLine()) != null)
            {
                // do your processing       
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
