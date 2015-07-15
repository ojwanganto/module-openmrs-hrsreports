package org.openmrs.module.hrsreports.fragment.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * controller for cohort import fragment
 */
public class FileUploadFragmentController {

    String successPage = "cohortImportSuccess";
    protected final Log log = LogFactory.getLog(getClass());

    public String controller(@FragmentParam("cohortFile") MultipartFile cohortFile, UiUtils ui){
        System.out.println("HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEERRRRRRRRRRRRRR");
        log.info("This request is reaching the controller---------------------------------------------------------------------" + cohortFile.getSize());
        return "redirect:" + ui.pageLink("hrsreports", successPage, null);

    }
}
