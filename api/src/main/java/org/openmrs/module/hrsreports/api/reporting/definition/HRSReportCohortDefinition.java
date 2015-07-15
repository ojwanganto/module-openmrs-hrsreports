package org.openmrs.module.hrsreports.api.reporting.definition;

import org.openmrs.module.reporting.cohort.definition.BaseCohortDefinition;
import org.openmrs.module.reporting.common.Localized;
import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
import org.openmrs.module.reporting.evaluation.caching.Caching;

/**
 * RDQA cohort sample frame definition
 */
@Caching(strategy = ConfigurationPropertyCachingStrategy.class)
@Localized("reporting.HRSReportCohortDefinition")
public class HRSReportCohortDefinition extends BaseCohortDefinition {

}
