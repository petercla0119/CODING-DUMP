// Date: 12-06-2019
	// Successfully runs in QuPath v0.2.0-m2 AND QuPath v0.2.0-m5

//this script runs cell detection in the LC polygon
    //parameters optimized for 1:1000 10MIN VECTOR RED 
selectAnnotations();
runPlugin('qupath.imagej.detect.cells.WatershedCellDetection', '{"detectionImageBrightfield": "Optical density sum",  "requestedPixelSizeMicrons": 1.0,  "backgroundRadiusMicrons": 15.0,  "medianRadiusMicrons": 8.0,  "sigmaMicrons": 2.0,  "minAreaMicrons": 150.0,  "maxAreaMicrons": 2000.0,  "threshold": 0.1,  "maxBackground": 2.0,  "watershedPostProcess": false,  "cellExpansionMicrons": 0.0,  "includeNuclei": true,  "smoothBoundaries": true,  "makeMeasurements": true}');
//convert the (cell) detections to annotations -> allowing user to modify/edit individual cells
import qupath.lib.objects.PathAnnotationObject

// Create new annotations with the same ROIs and classifications as the detections
def detections = getDetectionObjects()
def newAnnotations = detections.collect {detection -> new PathAnnotationObject(detection.getROI(), detection.getPathClass())}

// Remove the detections, add the annotations
removeObjects(detections, false)
addObjects(newAnnotations)
