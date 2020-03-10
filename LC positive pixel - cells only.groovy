//this script selects all child objects and runs positive pixel analysis

     // creates variable to represent child objects 
def topLevel = getObjects{return it.getLevel()==2 && it.isAnnotation()}

     // selects all child objects - does not select parent objects (LC polygon)
getCurrentHierarchy().getSelectionModel().setSelectedObjects(topLevel, null)

     // positive pixel analysis on selected annotations (optimized for 1:1000 10 MIN VECTOR RED)
runPlugin('qupath.imagej.detect.tissue.PositivePixelCounterIJ', '{"downsampleFactor": 1,  "gaussianSigmaMicrons": 0.5,  "thresholdStain1": 15,  "thresholdStain2": 0.32,  "addSummaryMeasurements": true,  "clearParentMeasurements": true,  "appendDetectionParameters": false,  "legacyMeasurements0.1.2": false}');
