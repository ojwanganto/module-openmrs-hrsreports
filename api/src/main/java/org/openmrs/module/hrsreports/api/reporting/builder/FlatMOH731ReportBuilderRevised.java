package org.openmrs.module.hrsreports.api.reporting.builder;

import org.openmrs.module.hrsreports.api.reporting.library.flatMoh731.FlatMoh731IndicatorLibrary;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyaemr.reporting.ColumnParameters;
import org.openmrs.module.kenyaemr.reporting.EmrReportingUtils;
import org.openmrs.module.kenyaemr.reporting.library.shared.common.CommonDimensionLibrary;
import org.openmrs.module.kenyaemr.reporting.library.shared.hiv.HivIndicatorLibrary;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by dev on 1/14/17.
 */
@Component
@Builds({"hrsreports.common.report.flatMOH731ReportRevised"})
public class FlatMOH731ReportBuilderRevised extends AbstractReportBuilder {
    @Autowired
    private CommonDimensionLibrary commonDimensions;

    @Autowired
    private FlatMoh731IndicatorLibrary hivIndicators;

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
                ReportUtils.map(careAndTreatmentDataSet(), "startDate=${startDate},endDate=${endDate}")
        );
    }

    /**
     * Creates the dataset for section #3: Care and Treatment
     *
     * @return the dataset
     */
    protected DataSetDefinition careAndTreatmentDataSet() {
        CohortIndicatorDataSetDefinition cohortDsd = new CohortIndicatorDataSetDefinition();
        cohortDsd.setName("3");
        cohortDsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDsd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDsd.addDimension("age", ReportUtils.map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
        cohortDsd.addDimension("gender", ReportUtils.map(commonDimensions.gender()));

        ColumnParameters colInfants = new ColumnParameters(null, "<1", "age=<1");
        ColumnParameters colMPeds = new ColumnParameters(null, "<15, Male", "gender=M|age=<15");
        ColumnParameters colFPeds = new ColumnParameters(null, "<15, Female", "gender=F|age=<15");
        ColumnParameters colMAdults = new ColumnParameters(null, "15+, Male", "gender=M|age=15+");
        ColumnParameters colFAdults = new ColumnParameters(null, "15+, Female", "gender=F|age=15+");
        ColumnParameters colTotal = new ColumnParameters(null, "Total", "");

        List<ColumnParameters> allColumns = Arrays.asList(colInfants, colMPeds, colFPeds, colMAdults, colFAdults, colTotal);
        List<ColumnParameters> nonInfantColumns = Arrays.asList(colMPeds, colFPeds, colMAdults, colFAdults, colTotal);

        String indParams = "startDate=${startDate},endDate=${endDate}";

        // 3.2 (Enrolled in Care)
        EmrReportingUtils.addRow(cohortDsd, "HV03", "Enrolled in care", ReportUtils.map(hivIndicators.newHivEnrollment(), indParams), allColumns, Arrays.asList("08", "09", "10", "11", "12", "13")); //adds 08, 09 to the col titles

        // 3.3 (Currently in Care)
        EmrReportingUtils.addRow(cohortDsd, "HV03", "Currently in care", ReportUtils.map(hivIndicators.currentlyInCare(), indParams), allColumns, Arrays.asList("14", "15", "16", "17", "18", "19"));

        // 3.4 (Starting ART)
        EmrReportingUtils.addRow(cohortDsd, "HV03", "Starting ART", ReportUtils.map(hivIndicators.startedOnArt(), indParams), allColumns, Arrays.asList("20", "21", "22", "23", "24", "25"));
        cohortDsd.addColumn("HV03-26", "Starting ART (Pregnant)", ReportUtils.map(hivIndicators.startedArtWhilePregnant(), indParams), "");

        // 3.5 (Revisits ART)
        EmrReportingUtils.addRow(cohortDsd, "HV03", "Revisits ART", ReportUtils.map(hivIndicators.revisitsArt(), indParams), allColumns, Arrays.asList("28", "29", "30", "31", "32", "33"));

        // 3.6 (Currently on ART [All])
        EmrReportingUtils.addRow(cohortDsd, "HV03", "Currently on ART [All]", ReportUtils.map(hivIndicators.currentlyOnArt(), indParams), allColumns, Arrays.asList("34", "35", "36", "37", "38", "39"));


        // 3.7 (Cumulative Ever on ART)
        EmrReportingUtils.addRow(cohortDsd, "HV03", "Cumulative ever on ART", ReportUtils.map(hivIndicators.cumulativeOnArt(), indParams), nonInfantColumns, Arrays.asList("40", "41", "42", "43", "44"));

        // 3.8 (Survival and Retention on ART at 12 months)
        cohortDsd.addColumn("HV03-45", "ART Net Cohort at 12 months", ReportUtils.map(hivIndicators.art12MonthNetCohort(), indParams), "");
        cohortDsd.addColumn("HV03-46", "On original 1st Line at 12 months", ReportUtils.map(hivIndicators.onOriginalFirstLineAt12Months(), indParams), "");
        cohortDsd.addColumn("HV03-47", "On alternative 1st Line at 12 months", ReportUtils.map(hivIndicators.onAlternateFirstLineAt12Months(), indParams), "");
        cohortDsd.addColumn("HV03-48", "On 2nd Line (or higher) at 12 months ", ReportUtils.map(hivIndicators.onSecondLineAt12Months(), indParams), "");
        cohortDsd.addColumn("HV03-49", "On therapy at 12 months (Total) ", ReportUtils.map(hivIndicators.onTherapyAt12Months(), indParams), "");

        // 3.9 (Screening)
        EmrReportingUtils.addRow(cohortDsd, "HV03", "Screened for TB", ReportUtils.map(hivIndicators.screenedForTb(), indParams), nonInfantColumns, Arrays.asList("50", "51", "52", "53", "54"));

        return cohortDsd;

    }
}
