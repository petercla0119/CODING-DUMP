/* TLDR; EXPORTS ANNOTATION AND DETECTION MEASURMENTS
 *This is a QuPath Script which exports the 
 *annotations and detections and places them 
 *into a NEW subdirectory called "annotation results" 
 */
 
 
def name = getProjectEntry().getImageName() + '.txt'
def path = buildFilePath(PROJECT_BASE_DIR, 'STAIN PARAMETERS LASTNAME')
mkdirs(path)
path = buildFilePath(path, name)
saveAnnotationMeasurements(path)
print 'Results exported to ' + path




/*
 * QuPath v0.1.2 has some bugs that make exporting annotations a bit annoying, specifically it doesn't include the 'dot' 
 * needed in the filename if you run it in batch, and it might put the 'slashes' the wrong way on Windows.
 * Manually fixing these afterwards is not very fun.
 * 
 * Anyhow, until this is fixed you could try the following script with Run -> Run for Project.
 * It should create a new subdirectory in the project, and write text files containing results there.
 *
 * @author Pete Bankhead
 */