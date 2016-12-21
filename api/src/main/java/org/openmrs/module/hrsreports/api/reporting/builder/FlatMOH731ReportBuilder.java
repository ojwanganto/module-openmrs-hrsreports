/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.hrsreports.api.reporting.builder;

import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Builds({"hrsreports.common.report.flatMOH731Report"})
public class FlatMOH731ReportBuilder extends AbstractReportBuilder {
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    protected List<Parameter> getParameters(ReportDescriptor reportDescriptor) {
        return Arrays.asList(
                new Parameter("startDate", "Start Date", Date.class),
                new Parameter("endDate", "End Date", Date.class)
        );
    }

    @Override
    protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor reportDescriptor, ReportDefinition reportDefinition) {
        return Arrays.asList(
                ReportUtils.map(enrollments(),  "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(currentInCare(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(startedOnART(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(revisitsOnART(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(currentlyOnART(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(cumulativeOnART(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(startingARTPregnant(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(tbScreening(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(pwp(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(hivCareVisits(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(ctxProphylaxis(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(hei(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(scheduledVisits(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(unscheduledVisits(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(mchEligibilityAssessment(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(mchKnownPositive(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(mchMaleInvolvement(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(mchTotalTested(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(hivTesting(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(hivTestingKnownPositive(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(onTherapy(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(netCohort(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(infantTestingInitial(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(infantFeeding(), "startDate=${startDate},endDate=${endDate}")
        );
    }

    protected DataSetDefinition enrollments() {

        String testQuery = "select count(distinct if(timestampdiff(year,p.dob,:endDate)<1,e.patient_id,null)) as enrolled_below_1_yr, " +
                "count(distinct if(timestampdiff(year,p.dob,:endDate)<15 and p.gender='M',e.patient_id,null)) as enrolled_below_15yrs_M, " +
                "count(distinct if(timestampdiff(year,p.dob,:endDate)<15 and p.gender='F',e.patient_id,null)) as enrolled_below_15yrs_F, " +
                "count(distinct if(timestampdiff(year,p.dob,:endDate)>=15 and p.gender='M',e.patient_id,null)) as enrolled_15yrs_and_above_M, " +
                "count(distinct if(timestampdiff(year,p.dob,:endDate)>=15 and p.gender='F',e.patient_id,null)) as enrolled_15yrs_and_above_F, " +
                "count(distinct e.patient_id) as enrolled_Total " +
                "from kenyaemr_etl.etl_hiv_enrollment e " +
                "join kenyaemr_etl.etl_patient_demographics p on p.patient_id=e.patient_id " +
                "where  e.entry_point <> 160563 " +
                "and date(e.visit_date) between :startDate and :endDate " +
                ";";
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("newEnrollment");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("New Enrollment");


        return ds;

    }

    protected DataSetDefinition currentInCare() {

        String testQuery = " select count(distinct if(timestampdiff(year,e.dob,:endDate)<1,e.patient_id,null)) as current_in_care_below_1_yr, " +
                "count(distinct if(timestampdiff(year,e.dob,:endDate)<15 and e.gender='M',e.patient_id,null)) as current_in_care_below_15yrs_M, " +
                "count(distinct if(timestampdiff(year,e.dob,:endDate)<15 and e.gender='F',e.patient_id,null)) as current_in_care_below_15yrs_F, " +
                "count(distinct if(timestampdiff(year,e.dob,:endDate)>=15 and e.gender='M',e.patient_id,null)) as current_in_care_15yrs_and_above_M, " +
                "count(distinct if(timestampdiff(year,e.dob,:endDate)>=15 and e.gender='F',e.patient_id,null)) as current_in_care_15yrs_and_above_F, " +
                "count(distinct e.patient_id) as current_in_care_Total " +
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
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("currentInCare");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("Current in Care");


        return ds;

    }

    protected DataSetDefinition startedOnART() {

        String testQuery = " select count(distinct if(timestampdiff(year,net.dob,:endDate)<1,net.patient_id,null)) as Starting_Art_below_1_yr, " +
                "count(distinct if(timestampdiff(year,net.dob,:endDate)<15 and net.gender='M',net.patient_id,null)) as Starting_Art_below_15yrs_M, " +
                "count(distinct if(timestampdiff(year,net.dob,:endDate)<15 and net.gender='F',net.patient_id,null)) as Starting_Art_below_15yrs_F, " +
                "count(distinct if(timestampdiff(year,net.dob,:endDate)>=15 and net.gender='M',net.patient_id,null)) as Starting_Art_15yrs_and_above_M, " +
                "count(distinct if(timestampdiff(year,net.dob,:endDate)>=15 and net.gender='F',net.patient_id,null)) as Starting_Art_15yrs_and_above_F, " +
                "count(distinct net.patient_id) as Starting_Art_Total " +
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
                //"join kenyaemr_etl.etl_patient_demographics p on p.patient_id=e.patient_id " +
                //"where  date(e.date_started) between :startDate and :endDate;";
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("startingART");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("Starting ART");


        return ds;

    }

    protected DataSetDefinition revisitsOnART() {

        String testQuery = " select count(distinct if(timestampdiff(year,e.dob,:endDate)<1,e.patient_id,null)) as Revisit_on_ART_below_1_yr, " +
                "count(distinct if(timestampdiff(year,e.dob,:endDate)<15 and e.gender='M',e.patient_id,null)) as Revisit_on_ART_below_15yrs_M, " +
                "count(distinct if(timestampdiff(year,e.dob,:endDate)<15 and e.gender='F',e.patient_id,null)) as Revisit_on_ART_below_15yrs_F, " +
                "count(distinct if(timestampdiff(year,e.dob,:endDate)>=15 and e.gender='M',e.patient_id,null)) as Revisit_on_ART_15yrs_and_above_M, " +
                "count(distinct if(timestampdiff(year,e.dob,:endDate)>=15 and e.gender='F',e.patient_id,null)) as Revisit_on_ART_15yrs_and_above_F, " +
                "count(distinct e.patient_id) as Revisit_on_ART_Total " +
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
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("revisitOnART");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("Revisits on ART");


        return ds;

    }


    protected DataSetDefinition currentlyOnART() {

        String testQuery = " select count(distinct if(timestampdiff(year,e.dob,:endDate)<1,e.patient_id,null)) as Currently_on_ART_below_1_yr, " +
                "count(distinct if(timestampdiff(year,e.dob,:endDate)<15 and e.gender='M',e.patient_id,null)) as Currently_on_ART_below_15yrs_M, " +
                "count(distinct if(timestampdiff(year,e.dob,:endDate)<15 and e.gender='F',e.patient_id,null)) as Currently_on_ART_below_15yrs_F, " +
                "count(distinct if(timestampdiff(year,e.dob,:endDate)>=15 and e.gender='M',e.patient_id,null)) as Currently_on_ART_15yrs_and_above_M, " +
                "count(distinct if(timestampdiff(year,e.dob,:endDate)>=15 and e.gender='F',e.patient_id,null)) as Currently_on_ART_15yrs_and_above_F, " +
                "count(distinct e.patient_id) as Currently_on_ART_Total " +
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
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("currentOnART");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("Current on ART");


        return ds;

    }

    protected DataSetDefinition cumulativeOnART() {

        String testQuery = " select count(distinct if(timestampdiff(year,net.dob,:endDate)<15 and net.gender='M',net.patient_id,null)) as Ever_on_Art_below_15yrs_M, " +
                "count(distinct if(timestampdiff(year,net.dob,:endDate)<15 and net.gender='F',net.patient_id,null)) as Ever_on_Art_below_15yrs_F, " +
                "count(distinct if(timestampdiff(year,net.dob,:endDate)>=15 and net.gender='M',net.patient_id,null)) as Ever_on_Art_15yrs_and_above_M, " +
                "count(distinct if(timestampdiff(year,net.dob,:endDate)>=15 and net.gender='F',net.patient_id,null)) as Ever_on_Art_15yrs_and_above_F, " +
                "count(distinct net.patient_id) as Ever_on_Art_Total " +
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
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("everOnART");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("Cumulative on ART");


        return ds;

    }

    protected DataSetDefinition startingARTPregnant() {

        String testQuery = " select count(distinct fup.patient_id) as Starting_Pregnant " +
                "from kenyaemr_etl.etl_patient_hiv_followup fup " +
                "join (select * from kenyaemr_etl.etl_drug_event e " +
                "where date_started between :startDate and :endDate) started_art on  " +
                "started_art.patient_id = fup.patient_id " +
                "where fup.pregnancy_status =1065 " +
                "and fup.visit_date between :startDate and :endDate;";
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("startingARTPregnant");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("Starting ART Pregnant");


        return ds;

    }

    protected DataSetDefinition tbScreening() {

        String testQuery = " select count(distinct if(timestampdiff(year,p.dob,:endDate)<15 and p.gender='M',tb.patient_id,null)) as Screen_for_TB_below_15yrs_M, " +
                "count(distinct if(timestampdiff(year,p.dob,:endDate)<15 and p.gender='F',tb.patient_id,null)) as Screen_for_TB_below_15yrs_F, " +
                "count(distinct if(timestampdiff(year,p.dob,:endDate)>=15 and p.gender='M',tb.patient_id,null)) as Screen_for_TB_15yrs_and_above_M, " +
                "count(distinct if(timestampdiff(year,p.dob,:endDate)>=15 and p.gender='F',tb.patient_id,null)) as Screen_for_TB_15yrs_and_above_F, " +
                "count(distinct tb.patient_id) as Screen_for_TB_Art_Total " +
                "from kenyaemr_etl.etl_tb_screening tb " +
                "join kenyaemr_etl.etl_patient_demographics p on p.patient_id=tb.patient_id " +
                "where  date(tb.visit_date) between :startDate and :endDate;";
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("tbscreening");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("TB Screening");


        return ds;

    }

    protected DataSetDefinition pwp() {

        String testQuery = " select count(distinct if(family_planning_method is not null  " +
                "and family_planning_method<>190,e.patient_id,null)) as modern_contraceptives, " +
                "count(distinct if(condom_provided=1065,e.patient_id,null)) as provided_with_condoms " +
                "from kenyaemr_etl.etl_patient_hiv_followup e " +
                "join kenyaemr_etl.etl_patient_demographics p on p.patient_id=e.patient_id " +
                "where  date(e.visit_date) between :startDate and :endDate;";
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("pwp");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("PWP");


        return ds;

    }

    protected DataSetDefinition hivCareVisits() {

        String testQuery = " select count(distinct if(timestampdiff(year,p.dob,:endDate)>=18 and p.gender='F',e.patient_id,null)) as 'Females_18+', " +
                "count(distinct if(family_planning_method is not null  " +
                "and family_planning_method<>190,e.patient_id,null)) as modern_contraceptives, " +
                "count(distinct if(condom_provided=1065,e.patient_id,null)) as provided_with_condoms " +
                "from kenyaemr_etl.etl_patient_hiv_followup e " +
                "join kenyaemr_etl.etl_patient_demographics p on p.patient_id=e.patient_id " +
                "join kenyaemr_etl.etl_hiv_enrollment enr on enr.patient_id=e.patient_id " +
                "where  date(e.visit_date) between :startDate and :endDate;";
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("hivCareVisits");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("HIV Visits");


        return ds;

    }

    protected DataSetDefinition ctxProphylaxis() {

        String testQuery = " select count(distinct if(timestampdiff(year,p.dob,:endDate)<15 and p.gender='M' and ctx_adherence is not null,e.patient_id,null)) as On_CTX_Below_15yrs_M, " +
                "count(distinct if(timestampdiff(year,p.dob,:endDate)<15 and p.gender='F' and ctx_adherence is not null,e.patient_id,null)) as On_CTX_Below_15yrs_F, " +
                "count(distinct if(timestampdiff(year,p.dob,:endDate)>=15 and p.gender='F' and ctx_adherence is not null,e.patient_id,null)) as On_CTX_15yrs_and_above_F, " +
                "count(distinct if(timestampdiff(year,p.dob,:endDate)>=15 and p.gender='M' and ctx_adherence is not null,e.patient_id,null)) as On_CTX_15yrs_and_above_M, " +
                "count(distinct if(ctx_adherence is not null,e.patient_id,null)) as On_CTX_Total " +
                "from kenyaemr_etl.etl_patient_hiv_followup e " +
                "join kenyaemr_etl.etl_patient_demographics p on p.patient_id=e.patient_id " +
                "join kenyaemr_etl.etl_hiv_enrollment enr on enr.patient_id=e.patient_id " +
                "where  date(e.visit_date) <= :endDate " +
                " and e.patient_id not in (select patient_id from kenyaemr_etl.etl_patient_program_discontinuation " +
                " where date(visit_date) < :endDate and program_name='HIV') " ;
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("ctxProphylaxis");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("CTX Prophylaxis");


        return ds;

    }


    protected DataSetDefinition hei() {

        String testQuery = " select count(distinct  if(timestampdiff(month,p.dob,:endDate)<=2,e.patient_id,null)) as 'HV03-01', count(distinct  if(timestampdiff(month,p.dob,:endDate)<=2,e.patient_id,null)) as 'HV03-02' " +
                "    from kenyaemr_etl.etl_hei_enrollment e " +
                "    join kenyaemr_etl.etl_patient_demographics p on p.patient_id=e.patient_id " +
                "    where  date(e.visit_date) between :startDate and :endDate";
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("hei");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("HEI");


        return ds;

    }


    protected DataSetDefinition scheduledVisits() {

        String testQuery = " select\n" +
                "count(distinct if(next_appointment_date is not null, patient_id,null)) as total_visits,\n" +
                "count(distinct if(visit_date=next_appointment_date, patient_id, null)) as scheduled_visits,\n" +
                "count(distinct if(visit_date<>next_appointment_date, patient_id, null)) as unscheduled_visits,\n" +
                "count(distinct patient_id) as total_visits1\n" +
                "-- patient_id,max(visit_date), max(next_appointment_date)\n" +
                "from (\n" +
                "select f1.patient_id,max(f1.visit_date) as visit_date, max(f2.next_appointment_date) as next_appointment_date \n" +
                "from kenyaemr_etl.etl_patient_hiv_followup f1\n" +
                "join kenyaemr_etl.etl_patient_hiv_followup f2 on f1.visit_date>f2.visit_date\n" +
                "and f1.patient_id=f2.patient_id\n" +
                "join kenyaemr_etl.etl_hiv_enrollment enr on enr.patient_id=f1.patient_id\n" +
                "where date(f1.visit_date) between :startDate and :endDate\n" +
                "group by f1.patient_id, f1.visit_date)vis";
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("scheduledVisits");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("Scheduled Visits");


        return ds;

    }


    protected DataSetDefinition unscheduledVisits() {

        String testQuery = " select\n" +
                "count(distinct if(next_appointment_date is not null, patient_id,null)) as total_visits,\n" +
                "count(distinct if(visit_date=next_appointment_date, patient_id, null)) as scheduled_visits,\n" +
                "count(distinct if(visit_date<>next_appointment_date, patient_id, null)) as unscheduled_visits,\n" +
                "count(distinct patient_id) as total_visits1\n" +
                "-- patient_id,max(visit_date), max(next_appointment_date)\n" +
                "from (\n" +
                "select f1.patient_id,max(f1.visit_date) as visit_date, max(f2.next_appointment_date) as next_appointment_date \n" +
                "from kenyaemr_etl.etl_patient_hiv_followup f1\n" +
                "join kenyaemr_etl.etl_patient_hiv_followup f2 on f1.visit_date>f2.visit_date\n" +
                "and f1.patient_id=f2.patient_id\n" +
                "join kenyaemr_etl.etl_hiv_enrollment enr on enr.patient_id=f1.patient_id\n" +
                "where date(f1.visit_date) between :startDate and :endDate\n" +
                "group by f1.patient_id, f1.visit_date)vis";
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("unscheduledVisits");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("Unscheduled Visits");


        return ds;

    }

    protected DataSetDefinition mchTotalTested() {

        String testQuery = " select count(distinct patient_id) as 'Total_Tested' " +
                "    from kenyaemr_etl.etl_mch_enrollment e " +
                "    where (e.hiv_test_date between :startDate and :endDate) ;";
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("mchTotalTested");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("MCH Total Tested");

        return ds;

    }

    protected DataSetDefinition mchKnownPositive() {

        String testQuery = " select count(distinct if (visit_date>hiv_test_date,patient_id,null)) as 'Known Positive', " +
                "    count(distinct patient_id) as 'Total Positive' " +
                "    from kenyaemr_etl.etl_mch_enrollment e " +
                "    where (e.visit_date between :startDate and :endDate) " +
                "    and hiv_status=703;";
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("mchKnownPositive");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("MCH Known Positive");


        return ds;

    }

    protected DataSetDefinition mchMaleInvolvement() {

        String testQuery = "   select count(distinct if(partner_hiv_status is not null, patient_id,null)) as 'Male Involvement', " +
                "    count(distinct if ((hiv_status=703 and partner_hiv_status=664) or (hiv_status=664 and partner_hiv_status=703),patient_id,null)) as discordant_couple " +
                "    from kenyaemr_etl.etl_mch_enrollment e " +
                "    where (e.visit_date between :startDate and :endDate);";
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("mchMaleInvolvement");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("MCH Male Involvement");


        return ds;

    }

    protected DataSetDefinition mchEligibilityAssessment() {

        String testQuery = "  Select count(distinct if(who_stage is not null,patient_id,null)) as 'HV02-18', " +
                "    count(distinct if(cd4 is not null ,patient_id,null)) as 'HV02-19', " +
                "    count(distinct patient_id) as 'HV02-20' " +
                "    from kenyaemr_etl.etl_mch_antenatal_visit m " +
                "    where date(m.visit_date) between :startDate and :endDate " +
                "    and who_stage is not null or cd4 is not null;";
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("mchEligibilityAssessment");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("MCH Eligibility Assessment");


        return ds;

    }


    protected DataSetDefinition infantTestingInitial() {

        String testQuery = "select  count(distinct if(timestampdiff(month,p.dob,:endDate)<=2,e.patient_id,null)) as 'HV02-24', " +
                "  count(distinct if(timestampdiff(month,p.dob,:endDate) between 3 and 8,e.patient_id,null)) as 'HV02-25', " +
                "  count(distinct if(timestampdiff(month,p.dob,:endDate) between 9 and 12,e.patient_id,null)) as 'HV02-26', " +
                "  count(distinct if(timestampdiff(month,p.dob,:endDate)<=12,e.patient_id,null)) as 'HV02-27' " +
                "    from kenyaemr_etl.etl_hei_follow_up_visit e " +
                "    join kenyaemr_etl.etl_patient_demographics p on p.patient_id=e.patient_id " +
                "    where dna_pcr_result is not null and " +
                "    (e.visit_date between :startDate and :endDate); ";
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("InfantTestingInitial");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("Infant Testing initial tests only");

        return ds;

    }


    protected DataSetDefinition hivTesting() {

        String testQuery = "  select count(distinct e.patient_id) as 'HV02-04', " +
        "  count(distinct if(anc.patient_id is not null, anc.patient_id,null)) as 'HV02-01', " +
        "  count(distinct if(ld.patient_id is not null and anc.patient_id is null, ld.patient_id,null)) as 'HV02-02', " +
        "  count(distinct if(panc.patient_id is not null and anc.patient_id is null and ld.patient_id is null, panc.patient_id,null)) as 'HV02-03' " +
        "    from kenyaemr_etl.etl_mch_enrollment e " +
                "    left outer join kenyaemr_etl.etl_mch_antenatal_visit anc on anc.patient_id=e.patient_id " +
        "    left outer join kenyaemr_etl.etl_mchs_delivery ld on ld.patient_id=e.patient_id " +
        "    left outer join kenyaemr_etl.etl_mch_postnatal_visit panc on panc.patient_id=e.patient_id " +
        "    where date(hiv_test_date) between :startDate and :endDate; ";
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("2");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("Hiv Testing");


        return ds;

    }

    protected DataSetDefinition hivTestingKnownPositive() {

        String testQuery = "  select count(distinct if (e.visit_date>hiv_test_date,e.patient_id,null)) as 'HV02-10', " +
                "  count(distinct if (ld.patient_id is not null and e.visit_date>hiv_test_date and anc.patient_id is null,ld.patient_id,null)) as 'HV02-06', " +
                "  count(distinct if (ld.patient_id is not null and e.visit_date>hiv_test_date and anc.patient_id is null,ld.patient_id,null)) as 'HV02-07', " +
                "  count(distinct if (panc.patient_id is not null and e.visit_date>hiv_test_date and anc.patient_id is null and ld.patient_id is null,panc.patient_id,null)) as 'HV02-08' " +
                "    from kenyaemr_etl.etl_mch_enrollment e " +
                "    left outer join kenyaemr_etl.etl_mch_antenatal_visit anc on anc.patient_id=e.patient_id " +
                "    left outer join kenyaemr_etl.etl_mchs_delivery ld on ld.patient_id=e.patient_id " +
                "    left outer join kenyaemr_etl.etl_mch_postnatal_visit panc on panc.patient_id=e.patient_id " +
                "    where hiv_status=703 and e.visit_date between :startDate and :endDate; ";
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("KP");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("Hiv Testing Known Positive");

        return ds;

    }


    //we will need to revise this
    protected DataSetDefinition netCohort() {

        String testQuery = "  select count(distinct net.patient_id) as 'HV03-45', " +
                "  count(distinct if(net.regimen_line='1st Line' and net.alternative_regimen=0,net.patient_id,null)) as 'HV03-46', " +
                "  count(distinct if(net.regimen_line='1st Line' and net.alternative_regimen=1,net.patient_id,null)) as 'HV03-47', " +
                "  count(distinct if(net.regimen_line='2nd Line',net.patient_id,null)) as 'HV03-48',  "+
                "  count(distinct if(timestampdiff(day,latest_tca,:endDate)<=90,net.patient_id,null)) as on_therapy  " +
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
                "    where  date(e.date_started) between date_sub(:startDate , interval 1 year) and date_sub(:endDate , interval 1 year) " +
                "    group by e.patient_id " +
                "    having   (dis_date>:endDate or dis_date is null) )net; ";
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("3");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("Net Cohort");

        return ds;

    }

    protected DataSetDefinition onTherapy() {

        String testQuery = "  select count(distinct net.patient_id) as On_Therapy " +
                "  from (" +
                "  select e.patient_id,e.date_started, p.gender,p.dob,d.visit_date as dis_date, if(d.visit_date is not null, 1, 0) as TOut," +
                "  if(enr.transfer_in_date is not null, 1, 0) as TIn, max(fup.visit_date) as latest_vis_date, max(fup.next_appointment_date) as latest_tca" +
                "    from kenyaemr_etl.etl_drug_event e " +
                "    join kenyaemr_etl.etl_patient_demographics p on p.patient_id=e.patient_id " +
                "    left outer join kenyaemr_etl.etl_patient_program_discontinuation d on d.patient_id=e.patient_id " +
                "    left outer join kenyaemr_etl.etl_hiv_enrollment enr on enr.patient_id=e.patient_id " +
                "    left outer join kenyaemr_etl.etl_patient_hiv_followup fup on fup.patient_id=e.patient_id " +
                "    where  date(e.date_started) between date_sub(:startDate , interval 1 year) and date_sub(:endDate , interval 1 year) " +
                "    group by e.patient_id " +
                "    having   (dis_date>:endDate or dis_date is null) and (datediff(latest_tca,:endDate)<=90))net; ";
        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("onTherapy");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("on Therapy");

        return ds;

    }


    protected DataSetDefinition infantFeeding(){
        String testQuery = "  Select count(distinct if(timestampdiff(month,d.dob,:endDate)<=6 and o.baby_feeding_method=5526,o.patient_id,null)) as 'EBF (at 6 months)', " +
                " count(distinct if(timestampdiff(month,d.dob,:endDate)<=6 and o.baby_feeding_method=1595,o.patient_id,null)) as 'ERF (at 6 months)', " +
                " count(distinct if(timestampdiff(month,d.dob,:endDate)<=6 and o.baby_feeding_method=6046,o.patient_id,null)) as 'MF (at 6 months)',  " +
                " count(distinct if(timestampdiff(month,d.dob,:endDate)<=12,o.patient_id,null)) as 'BF (12 months)', " +
                " count(distinct if(timestampdiff(month,d.dob,:endDate)<=12 and o.baby_feeding_method is null,o.patient_id,null)) as 'BF (Not Known)', " +
                " count(distinct if(timestampdiff(month,d.dob,:endDate)<=12,o.patient_id,null)) as 'Total_Exposed' " +
                " from kenyaemr_etl.etl_mch_postnatal_visit o " +
                " join kenyaemr_etl.etl_hei_enrollment e on e.patient_id=o.patient_id " +
                " join kenyaemr_etl.etl_patient_demographics d on d.patient_id = o.patient_id " +
                " where  " +
                "  date(o.visit_date) between :startDate and :endDate ;" ;

        SqlDataSetDefinition ds = new SqlDataSetDefinition();
        ds.setName("infantFeeding");
        ds.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ds.addParameter(new Parameter("endDate", "End Date", Date.class));
        ds.setSqlQuery(testQuery);
        ds.setDescription("Infant Feeding");

        return ds;
    }

}