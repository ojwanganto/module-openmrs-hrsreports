package org.openmrs.module.hrsreports.api.reporting.library.flatMoh731;

import org.openmrs.module.kenyaemr.Dictionary;
import org.openmrs.module.kenyaemr.PregnancyStage;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.openmrs.module.kenyacore.report.ReportUtils.map;
import static org.openmrs.module.kenyaemr.reporting.EmrReportingUtils.cohortIndicator;

/**
 * Created by dev on 1/18/17.
 */
@Component
public class FlatPmtctIndicatorLibrary {

    @Autowired
    private FlatPmtctCohortLibrary mchmsCohortLibrary;

    /**
     * Number of patients who tested for HIV in MCHMS during any {@link org.openmrs.module.kenyaemr.PregnancyStage} before or after enrollment
     *
     * @return the indicator
     */
    public CohortIndicator testedForHivBeforeOrDuringMchms() {
        return cohortIndicator(null,
                map(mchmsCohortLibrary.mchKnownPositiveTotal(), "startDate=${startDate},endDate=${endDate}")
        );
    }


    /**
     * Number of patients who tested for HIV in MCHMS during any {@link org.openmrs.module.kenyaemr.PregnancyStage} after enrollment
     *
     * @return the indicator
     */
    public CohortIndicator testedForHivInMchms() {
        return cohortIndicator(null,
                map(mchmsCohortLibrary.testedForHivInMchmsTotal(), "startDate=${startDate},endDate=${endDate}")
        );
    }

    /**
     * Number of patients who tested for HIV in MCHMS during the ANTENATAL {@link org.openmrs.module.kenyaemr.PregnancyStage}
     *
     * @return the indicator
     */
    public CohortIndicator testedForHivInMchmsAntenatal() {
        return cohortIndicator(null,
                map(mchmsCohortLibrary.testedForHivInMchmsAntenatal(), "startDate=${startDate},endDate=${endDate}")
        );
    }

    /**
     * Number of patients who tested for HIV in MCHMS during the DELIVERY {@link org.openmrs.module.kenyaemr.PregnancyStage}
     *
     * @return the indicator
     */
    public CohortIndicator testedForHivInMchmsDelivery() {
        return cohortIndicator(null,
                map(mchmsCohortLibrary.testedForHivInMchmsDelivery(), "startDate=${startDate},endDate=${endDate}")
        );
    }

    /**
     * Number of patients who tested for HIV in MCHMS during the POSTNATAL {@link org.openmrs.module.kenyaemr.PregnancyStage}
     *
     * @return the indicator
     */
    public CohortIndicator testedForHivInMchmsPostnatal() {
        return cohortIndicator(null,
                map(mchmsCohortLibrary.testedForHivInMchmsPostnatal(), "startDate=${startDate},endDate=${endDate}")
        );
    }

    /**
     * Number of patients who tested HIV Positive in MCHMS during any {@link org.openmrs.module.kenyaemr.PregnancyStage} after enrollment
     *
     * @return the indicator
     */
    public CohortIndicator testedHivPositiveInMchms() {
        return cohortIndicator(null,
                map(mchmsCohortLibrary.testedHivPositiveInMchmsTotal(), "startDate=${startDate},endDate=${endDate}")
        );
    }

    /**
     * Number of patients who tested HIV Positive in MCHMS during the ANTENATAL {@link org.openmrs.module.kenyaemr.PregnancyStage}
     *
     * @return the indicator
     */
    public CohortIndicator testedHivPositiveInMchmsAntenatal() {
        return cohortIndicator(null,
                map(mchmsCohortLibrary.testedHivPositiveInMchmsAntenatal(), "startDate=${startDate},endDate=${endDate}")
        );
    }

    /**
     * Number of patients who tested for HIV in MCHMS during the DELIVERY {@link org.openmrs.module.kenyaemr.PregnancyStage}
     *
     * @return the indicator
     */
    public CohortIndicator testedHivPositiveInMchmsDelivery() {
        return cohortIndicator(null,
                map(mchmsCohortLibrary.testedHivPositiveInMchmsDelivery(), "startDate=${startDate},endDate=${endDate}")
        );
    }

    /**
     * Number of patients who tested HIV Positive in MCHMS during the POSTNATAL {@link org.openmrs.module.kenyaemr.PregnancyStage}
     *
     * @return the indicator
     */
    public CohortIndicator testedHivPositiveInMchmsPostnatal() {
        return cohortIndicator(null,
                map(mchmsCohortLibrary.testedHivPositiveInMchmsPostnatal(), "startDate=${startDate},endDate=${endDate}")
        );
    }

    /**
     * Number of patients who tested HIV +ve before MCHMS
     *
     * @return the indicator
     */
    public CohortIndicator testedHivPositiveBeforeMchms() {

        return cohortIndicator(null,
                map(mchmsCohortLibrary.testedHivPositiveBeforeMchms(), "startDate=${startDate},endDate=${endDate}")
        );
    }

    /**
     * Number of patients whose partners tested HIV +ve or -ve in MCHMS during either their ANTENATAL or DELIVERY
     * {@link org.openmrs.module.kenyaemr.PregnancyStage}
     *
     * @return the indicator
     */
    public CohortIndicator partnerTestedDuringAncOrDelivery() {
        return cohortIndicator(null,
                map(mchmsCohortLibrary.partnerTestedDuringAncOrDelivery(), "starDate=${startDate},endDate=${endDate}")
        );
    }

    /**
     * Number of MCHMS patients whose HIV status is discordant with that of their male partners
     *
     * @return the cohort definition
     */

    public CohortIndicator discordantCouples() {
        return cohortIndicator(null,
                map(mchmsCohortLibrary.discordantCouples(), "startDate=${startDate},endDate=${endDate}")
        );
    }
}
