package org.openmrs.module.hrsreports.api.reporting.library.flatMoh731;

import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by dev on 1/17/17.
 */
@Component
public class FlatPmtctCohortLibrary {

    public CohortDefinition testedForHivInMchmsTotal(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery =  " select distinct patient_id " +
                "    from kenyaemr_etl.etl_mch_enrollment e " +
                "    where (e.hiv_test_date between :startDate and :endDate) ;";

        cd.setName("testedForHivInMchms");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Mothers tested For Hiv In Mch Program");

        return cd;
    }

    public CohortDefinition testedHivPositiveInMchmsTotal(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery =  " select distinct patient_id " +
                "    from kenyaemr_etl.etl_mch_enrollment e " +
                "    where (hiv_status=703 and e.hiv_test_date between :startDate and :endDate) ;";

        cd.setName("testedHivPositeInMchms");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Mothers tested Hiv Positive In Mch Program");

        return cd;
    }


    public CohortDefinition infantFeeding(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery ="  Select count(distinct if(timestampdiff(month,d.dob,:endDate)<=6 and o.baby_feeding_method=5526,o.patient_id,null)) as 'EBF (at 6 months)', " +
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
        cd.setName("infantFeeding");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("infant Feeding");

        return cd;
    }

    public CohortDefinition hivTestingKnownPositive(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery ="  select count(distinct if (e.visit_date>hiv_test_date,e.patient_id,null)) as 'HV02-10', " +
                "  count(distinct if (ld.patient_id is not null and e.visit_date>hiv_test_date and anc.patient_id is null,ld.patient_id,null)) as 'HV02-06', " +
                "  count(distinct if (ld.patient_id is not null and e.visit_date>hiv_test_date and anc.patient_id is null,ld.patient_id,null)) as 'HV02-07', " +
                "  count(distinct if (panc.patient_id is not null and e.visit_date>hiv_test_date and anc.patient_id is null and ld.patient_id is null,panc.patient_id,null)) as 'HV02-08' " +
                "    from kenyaemr_etl.etl_mch_enrollment e " +
                "    left outer join kenyaemr_etl.etl_mch_antenatal_visit anc on anc.patient_id=e.patient_id " +
                "    left outer join kenyaemr_etl.etl_mchs_delivery ld on ld.patient_id=e.patient_id " +
                "    left outer join kenyaemr_etl.etl_mch_postnatal_visit panc on panc.patient_id=e.patient_id " +
                "    where hiv_status=703 and e.visit_date between :startDate and :endDate; ";

        cd.setName("hivTestingKnownPositive");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Hiv Testing Known Positive");

        return cd;
    }

    public CohortDefinition testedForHivInMchmsAntenatal(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery ="  select distinct e.patient_id " +
                "    from kenyaemr_etl.etl_mch_enrollment e " +
                "    join kenyaemr_etl.etl_mch_antenatal_visit anc on anc.patient_id=e.patient_id " +
                "    where date(hiv_test_date) between :startDate and :endDate; ";

        cd.setName("Tested For Hiv Antenatal");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Tested For Hiv Antenatal");

        return cd;
    }

    public CohortDefinition testedForHivInMchmsDelivery(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery ="  select distinct e.patient_id " +
                "    from kenyaemr_etl.etl_mch_enrollment e " +
                "    join kenyaemr_etl.etl_mchs_delivery ld on ld.patient_id=e.patient_id " +
                "    where date(hiv_test_date) between :startDate and :endDate; ";

        cd.setName("Tested For Hiv Delivery");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Tested For Hiv Delivery");

        return cd;
    }

    public CohortDefinition testedForHivInMchmsPostnatal(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery ="  select distinct e.patient_id " +
                "    from kenyaemr_etl.etl_mch_enrollment e " +
                "    join kenyaemr_etl.etl_mch_postnatal_visit panc on panc.patient_id=e.patient_id " +
                "    where date(hiv_test_date) between :startDate and :endDate; ";

        cd.setName("Tested For Hiv Postnatal");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Tested For Hiv Postnatal");

        return cd;
    }

    public CohortDefinition testedHivPositiveBeforeMchms(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery =  " select distinct e.patient_id " +
                "    from kenyaemr_etl.etl_mch_enrollment e " +
                "    join kenyaemr_etl.etl_mch_antenatal_visit anc on anc.patient_id=e.patient_id " +
                "    where (e.visit_date between :startDate and :endDate) " +
                "    and hiv_status=703 and visit_date>hiv_test_date;";

        cd.setName("hiv Testing");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Hiv Testing Known Positive");

        return cd;
    }

    public CohortDefinition testedHivPositiveInMchmsAntenatal(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery ="  select distinct e.patient_id " +
                "    from kenyaemr_etl.etl_mch_enrollment e " +
                "    join kenyaemr_etl.etl_mch_antenatal_visit anc on anc.patient_id=e.patient_id " +
                "    where date(hiv_test_date) between :startDate and :endDate  and hiv_status=703; ";

        cd.setName("Tested Hiv Postive at Antenatal");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Tested Hiv Postive at Antenatal");

        return cd;
    }

    public CohortDefinition testedHivPositiveInMchmsDelivery(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery ="  select distinct e.patient_id " +
                "    from kenyaemr_etl.etl_mch_enrollment e " +
                "    join kenyaemr_etl.etl_mchs_delivery ld on ld.patient_id=e.patient_id " +
                "    where date(hiv_test_date) between :startDate and :endDate  and hiv_status=703; ";

        cd.setName("Tested Hiv Hive at Delivery");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Tested Hiv Positive at Delivery");

        return cd;
    }

    public CohortDefinition testedHivPositiveInMchmsPostnatal(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery ="  select distinct e.patient_id " +
                "    from kenyaemr_etl.etl_mch_enrollment e " +
                "    join kenyaemr_etl.etl_mch_postnatal_visit panc on panc.patient_id=e.patient_id " +
                "    where date(hiv_test_date) between :startDate and :endDate  and hiv_status=703; ";

        cd.setName("Tested Hiv Postive at Postnatal");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Tested Hiv Positive Postnatal");

        return cd;
    }

    public CohortDefinition infantTestingInitial(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery ="select  count(distinct if(timestampdiff(month,p.dob,:endDate)<=2,e.patient_id,null)) as 'HV02-24', " +
                "  count(distinct if(timestampdiff(month,p.dob,:endDate) between 3 and 8,e.patient_id,null)) as 'HV02-25', " +
                "  count(distinct if(timestampdiff(month,p.dob,:endDate) between 9 and 12,e.patient_id,null)) as 'HV02-26', " +
                "  count(distinct if(timestampdiff(month,p.dob,:endDate)<=12,e.patient_id,null)) as 'HV02-27' " +
                "    from kenyaemr_etl.etl_hei_follow_up_visit e " +
                "    join kenyaemr_etl.etl_patient_demographics p on p.patient_id=e.patient_id " +
                "    where dna_pcr_result is not null and " +
                "    (e.visit_date between :startDate and :endDate); ";

        cd.setName("hiv Testing");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Hiv Testing Known Positive");

        return cd;
    }

    public CohortDefinition mchEligibilityAssessment(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "  Select count(distinct if(who_stage is not null,patient_id,null)) as 'HV02-18', " +
                "    count(distinct if(cd4 is not null ,patient_id,null)) as 'HV02-19', " +
                "    count(distinct patient_id) as 'HV02-20' " +
                "    from kenyaemr_etl.etl_mch_antenatal_visit m " +
                "    where date(m.visit_date) between :startDate and :endDate " +
                "    and who_stage is not null or cd4 is not null;";

        cd.setName("hiv Testing");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Hiv Testing Known Positive");

        return cd;
    }

    public CohortDefinition discordantCouples(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = " select patient_id " +
                "    from kenyaemr_etl.etl_mch_enrollment e " +
                "    where (e.visit_date between :startDate and :endDate) and ((hiv_status=703 and partner_hiv_status=664) or (hiv_status=664 and partner_hiv_status=703));";

        cd.setName("discordantCouples");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Discordant Couples");

        return cd;
    }

    public CohortDefinition partnerTestedDuringAncOrDelivery(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "   select distinct patient_id " +
                "    from kenyaemr_etl.etl_mch_enrollment e " +
                "    where (partner_hiv_status is not null and e.visit_date between :startDate and :endDate);";

        cd.setName("partnerTestedDuringAncOrDelivery");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("partner Tested During Anc Or Delivery");

        return cd;
    }

    public CohortDefinition mchKnownPositiveTotal(){
        //testedForHivBeforeOrDuringMchms
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery =  " select distinct patient_id " +
                "    from kenyaemr_etl.etl_mch_enrollment e " +
                "    where (e.visit_date between :startDate and :endDate) " +
                "    and hiv_status=703 and visit_date>hiv_test_date;";

        cd.setName("hiv Testing Known Positive");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Hiv Testing Known Positive");

        return cd;
    }
}
