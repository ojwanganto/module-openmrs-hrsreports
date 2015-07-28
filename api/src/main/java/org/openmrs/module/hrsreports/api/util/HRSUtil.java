package org.openmrs.module.hrsreports.api.util;

import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.PatientSetService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hrsreports.api.reporting.model.CohortFile;
import org.openmrs.module.reporting.cohort.definition.CodedObsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.util.OpenmrsUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Util class for HRSReports
 */
public class HRSUtil {
    private static final String COMMA_DELIMITER = ",";
    private static final int EFFECTIVE_DATE_INDEX = 0;
    public static Set<Integer> getReportCohort() {
        if (processCSVFile() == null)
            return defaultCohort();
        return processCSVFile().getPatientIds();
    }

    public static Date getReportEffectiveDate() {
        if (processCSVFile() == null)
            return getDefaultDate();
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
        if (!csvFile.exists()) {
            return null;
        }

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

    private static Set<Integer> defaultCohort() {

        Date defaultDate = getDefaultDate();

        CodedObsCohortDefinition cd4count = new CodedObsCohortDefinition();
        cd4count.setQuestion(new Concept(5497));
        cd4count.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd4count.setEncounterTypeList(Arrays.asList(new EncounterType(8)));
        cd4count.setOnOrAfter(defaultDate);

        CodedObsCohortDefinition cd4percent = new CodedObsCohortDefinition();
        cd4percent.setQuestion(new Concept(730));
        cd4percent.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd4percent.setEncounterTypeList(Arrays.asList(new EncounterType(8)));
        cd4percent.setOnOrAfter(defaultDate);

        CodedObsCohortDefinition viralLoad = new CodedObsCohortDefinition();
        viralLoad.setQuestion(new Concept(856));
        viralLoad.setTimeModifier(PatientSetService.TimeModifier.ANY);
        viralLoad.setEncounterTypeList(Arrays.asList(new EncounterType(8)));
        viralLoad.setOnOrAfter(defaultDate);

        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("Default Cohort. No cohort file was found");
        cd.addSearch("cd4Cohort", Mapped.noMappings(cd4count));
        cd.addSearch("cd4Percent", Mapped.noMappings(cd4percent));
        cd.addSearch("viralLoad", Mapped.noMappings(viralLoad));
        cd.setCompositionString("cd4Cohort OR cd4Percent OR viralLoad");


        try {
            Cohort cohort = Context.getService(CohortDefinitionService.class).evaluate(cd, null);
            return cohort.getMemberIds();
        } catch (EvaluationException e) {
            e.printStackTrace();
        }
        return new HashSet<Integer>();
    }

    private static Date getDefaultDate () {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        Date defaultDate = cal.getTime();
        return defaultDate;
    }

}
