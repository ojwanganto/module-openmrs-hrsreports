package org.openmrs.module.hrsreports.api.reporting.definition.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.hrsreports.api.reporting.definition.HRSReportCohortDefinition;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

/**
 * Evaluator for  HRS report cohort definition
 */
@Handler(supports = {HRSReportCohortDefinition.class})
public class HRSReportCohortDefinitionEvaluator implements CohortDefinitionEvaluator {

    private final Log log = LogFactory.getLog(this.getClass());

    @Override
    public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

        HRSReportCohortDefinition definition = (HRSReportCohortDefinition) cohortDefinition;

        if (definition == null)
            return null;

		String qry = "select patient_id " +
				" from patient p " +
				"	inner join patient_identifier pi " +
				"	using(patient_id) " +
				" where identifier_type = 3 and p.voided = 0  ";

		SqlCohortDefinition sqlCohortDefinition = new SqlCohortDefinition(qry);
		Cohort results = Context.getService(CohortDefinitionService.class).evaluate(sqlCohortDefinition, context);

		return new EvaluatedCohort(results, sqlCohortDefinition, context);
    }

}
