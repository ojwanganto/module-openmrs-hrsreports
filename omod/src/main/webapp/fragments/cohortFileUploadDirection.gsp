<div>
    <p><em>Uploading Cohort file for HRS Report</em></p>
    <div>
        <p>
            Cohort file is a CSV (Comma separated list) file with date component (effective Date) as first element and a list of unique patient identifiers <br/>
            The file must be named <b><i>testCohort.csv</i></b>
            The first element, effective date, must be in the format yyyy-MM-dd i.e <b><i>2012-05-01</i></b> <br/>
            Where <br/>
            yyyy - is a four (4) digit value for year like 2012 <br/>
            MM - is two digit value for month like 06 for June <br/>
            dd - is a two digit value for day like 01 <br/>
            No other format is acceptable
        </p>
        <p>HRS Report obtains patient list and effective date from cohort file and in the event cohort file is not available,
            patient list defaults to all patients who have had CD4 and/or Viral Load in the past one year.<br/>
            This is not what we want hence need to upload a cohort file.
        </p>
        <p>
            To override existing cohort file, upload a fresh copy having relevant information i.e effective date and patient list
        </p>
        
        <p>To update cohort, use the following:</p>
        <ul>
            <li>Prepare/identify csv file with patient ids</li>
            <li>Upload the file to the system</li>
            <li>This will update the cohort</li>
        </ul>
    </div>
</div>
