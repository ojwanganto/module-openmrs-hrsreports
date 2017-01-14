package org.openmrs.module.hrsreports.api.reporting.library.flatMoh731;

import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by dev on 1/14/17.
 */
/**
 * Library of cohort definitions used specifically in the MOH731 report
 */
@Component
public class FlatMoh731CohortLibrary {
    public CohortDefinition hivEnrollment(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select distinct e.patient_id " +
                "from kenyaemr_etl.etl_hiv_enrollment e " +
                "join kenyaemr_etl.etl_patient_demographics p on p.patient_id=e.patient_id " +
                "where  e.entry_point <> 160563 " +
                "and date(e.visit_date) between :startDate and :endDate " +
                ";";
        cd.setName("newHhivEnrollment");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("New HIV Enrollment");

        return cd;
    }

    public  CohortDefinition currentlyInCare() {
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery=" select distinct e.patient_id " +
                "from ( " +
                "select fup.visit_date,fup.patient_id,p.dob,p.Gender, min(e.visit_date) as enroll_date,max(fup.visit_date) as latest_vis_date, " +
                "max(fup.next_appointment_date) as latest_tca " +
                "from kenyaemr_etl.etl_patient_hiv_followup fup " +
                "join kenyaemr_etl.etl_patient_demographics p on p.patient_id=fup.patient_id " +
                "join kenyaemr_etl.etl_hiv_enrollment e  on fup.patient_id=e.patient_id " +
                "where (date(fup.visit_date) <= :endDate) " +
                "and fup.patient_id not in (select patient_id from kenyaemr_etl.etl_patient_program_discontinuation " +
                "where date(visit_date) < :endDate and program_name='HIV') " +
                "group by patient_id " +
                // we may need to filter lost to follow-up using this
                //having (timestampdiff(day,latest_tca,'2015-01-31')<=90)
                ") e;";

        cd.setName("currentlyInCare");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("currently In Care");

        return cd;
    }

    public  CohortDefinition startedOnART() {
        String sqlQuery="select distinct net.patient_id " +
//                "from kenyaemr_etl.etl_drug_event e " +
                "  from ( " +
                "  select e.patient_id,e.date_started, e.gender,e.dob,d.visit_date as dis_date, if(d.visit_date is not null, 1, 0) as TOut," +
                "   e.regimen, e.regimen_line, e.alternative_regimen, max(fup.next_appointment_date) as latest_tca, "+
                "  if(enr.transfer_in_date is not null, 1, 0) as TIn, max(fup.visit_date) as latest_vis_date" +
                "    from (select e.patient_id,p.dob,p.Gender,min(e.date_started) as date_started, " +
                "    mid(min(concat(e.date_started,e.regimen_name)),11) as regimen, " +
                "    mid(min(concat(e.date_started,e.regimen_line)),11) as regimen_line, " +
                "    max(if(discontinued,1,0))as alternative_regimen " +
                "    from kenyaemr_etl.etl_drug_event e " +
                "    join kenyaemr_etl.etl_patient_demographics p on p.patient_id=e.patient_id " +
                "    group by e.patient_id) e " +
                "    left outer join kenyaemr_etl.etl_patient_program_discontinuation d on d.patient_id=e.patient_id " +
                "    left outer join kenyaemr_etl.etl_hiv_enrollment enr on enr.patient_id=e.patient_id " +
                "    left outer join kenyaemr_etl.etl_patient_hiv_followup fup on fup.patient_id=e.patient_id " +
                "    where  date(e.date_started) between :startDate and :endDate " +
                "    group by e.patient_id " +
                "    )net; ";
        SqlCohortDefinition cd = new SqlCohortDefinition();
        cd.setName("startingART");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Started on ART");
        return cd;
    }

    public CohortDefinition currentlyOnArt() {
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery=" select distinct e.patient_id " +
                "from ( " +
                "select fup.visit_date,fup.patient_id,p.dob,p.Gender, " +
                "min(e.visit_date) as enroll_date, " +
                "max(fup.visit_date) as latest_vis_date, " +
                "max(fup.next_appointment_date) as latest_tca " +
                "from kenyaemr_etl.etl_patient_hiv_followup fup " +
                "join kenyaemr_etl.etl_patient_demographics p on p.patient_id=fup.patient_id " +
                "join kenyaemr_etl.etl_hiv_enrollment e  on fup.patient_id=e.patient_id " +
                "where (date(fup.visit_date) <= :endDate) " +
                " and fup.patient_id not in (select patient_id from kenyaemr_etl.etl_patient_program_discontinuation " +
                " where date(visit_date) < :endDate and program_name='HIV') " +
                "group by patient_id " +
                ") e " +
                "where e.patient_id in (select distinct patient_id  " +
                "from kenyaemr_etl.etl_drug_event  " +
                "where date(date_started)<=:endDate);";

        cd.setName("currentlyOnArt");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("currently on ART");
        return cd;
    }

    public CohortDefinition revisitsArt() {
        String sqlQuery=" select distinct e.patient_id " +
                "from ( " +
                "select fup.visit_date,fup.patient_id,p.dob,p.Gender, " +
                "min(e.visit_date) as enroll_date, " +
                "max(fup.visit_date) as latest_vis_date, " +
                "max(fup.next_appointment_date) as latest_tca " +
                "from kenyaemr_etl.etl_patient_hiv_followup fup " +
                "join kenyaemr_etl.etl_patient_demographics p on p.patient_id=fup.patient_id " +
                "join kenyaemr_etl.etl_hiv_enrollment e  on fup.patient_id=e.patient_id " +
                "where (date(fup.visit_date) <= :endDate) " +
                " and fup.patient_id not in (select patient_id from kenyaemr_etl.etl_patient_program_discontinuation " +
                " where date(visit_date) < :endDate and program_name='HIV') " +
                "group by patient_id " +
                ") e " +
                "where e.patient_id in (select patient_id\n" +
                "from (select e.patient_id,p.dob,p.Gender,min(e.date_started) as date_started,\n" +
                "mid(min(concat(e.date_started,e.regimen_name)),11) as regimen,\n" +
                "mid(min(concat(e.date_started,e.regimen_line)),11) as regimen_line,\n" +
                "max(if(discontinued,1,0))as alternative_regimen\n" +
                "from kenyaemr_etl.etl_drug_event e\n" +
                "join kenyaemr_etl.etl_patient_demographics p on p.patient_id=e.patient_id\n" +
                "group by e.patient_id) e\n" +
                "where  date(e.date_started)<:startDate);";
        SqlCohortDefinition cd = new SqlCohortDefinition();
        cd.setName("revisitsArt");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Revisits on ART");
        return cd;
    }

    public CohortDefinition cummulativeOnArt() {
        String sqlQuery=" select distinct net.patient_id " +
                "from ( " +
                "select e.patient_id,e.date_started,min(enr.visit_date) as enrollment_date,e.gender,e.regimen, " +
                "e.regimen_line,e.alternative_regimen,e.dob,d.visit_date as dis_date,if(d.visit_date is not null, 1, 0) as TOut, " +
                "if(enr.transfer_in_date is not null, 1, 0) as TIn,enr.transfer_in_date,max(fup.visit_date) as latest_vis_date, " +
                "max(fup.next_appointment_date) as latest_tca " +
                "from (select e.patient_id,p.dob,p.Gender,min(e.date_started) as date_started, " +
                "mid(min(concat(e.date_started,e.regimen_name)),11) as regimen, " +
                "mid(min(concat(e.date_started,e.regimen_line)),11) as regimen_line, " +
                "max(if(discontinued,1,0))as alternative_regimen " +
                "from kenyaemr_etl.etl_drug_event e " +
                "join kenyaemr_etl.etl_patient_demographics p on p.patient_id=e.patient_id " +
                "group by e.patient_id) e " +
                "left outer join kenyaemr_etl.etl_patient_program_discontinuation d on d.patient_id=e.patient_id " +
                "left outer join kenyaemr_etl.etl_hiv_enrollment enr on enr.patient_id=e.patient_id " +
                "left outer join kenyaemr_etl.etl_patient_hiv_followup fup on fup.patient_id=e.patient_id " +
                "group by e.patient_id " +
                "having   (TIn=0 and date_started<=:endDate ) or (TIn=1 and date_started>transfer_in_date))net;";

        SqlCohortDefinition cd = new SqlCohortDefinition();
        cd.setName("cummulativeOnArt");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Cummulative ever on ART");
        return cd;
    }
}
