<fieldset>
     <legend>Cohort File Upload</legend>
        <form method="post" enctype="multipart/form-data" action="${ ui.actionLink("hrsreports", "uploadFile", "uploadFile") }">
            <span>
                <input type="file" name="cohortFile"/>
            </span>
            <span>
                <button type="submit">
                    <img src="${ ui.resourceLink("kenyaui", "images/glyphs/ok.png") }" /> Upload File
                </button>
            </span>
        </form>
</fieldset>