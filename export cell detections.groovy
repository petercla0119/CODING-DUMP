// Author: Claire S. Peterson
// Date: 12-06-2019
	// Successfully runs in QuPath v0.2.0-m2 AND QuPath v0.2.0-m5
	
	// DO NOT MODIFY ANY LINES IN THIS SCRIPT FOR – INTENDED USE FOR LC PROJECT
 
def name = getProjectEntry().getImageName() + '.txt'
def path = buildFilePath(PROJECT_BASE_DIR, 'CELL DETECTIONS')
mkdirs(path)
path = buildFilePath(path, name)
saveAnnotationMeasurements(path)
print 'Results exported to ' + path

