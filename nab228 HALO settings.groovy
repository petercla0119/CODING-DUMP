setImageType('BRIGHTFIELD_H_DAB');
setColorDeconvolutionStains('{"Name" : "DAB AT8 HALO Validation ", "Stain 1" : "Hematoxylin", "Values 1" : "0.283 0.361 0.168", "Stain 2" : "DAB", "Values 2" : "0.385 0.699 0.850", "Background" : " 255 255 255 "}');
selectAnnotations();
runPlugin('qupath.imagej.detect.tissue.PositivePixelCounterIJ', '{"downsampleFactor": 2,  "gaussianSigmaMicrons": 1.0,  "thresholdStain1": 0.085,  "thresholdStain2": 0.177,  "addSummaryMeasurements": true,  "clearParentMeasurements": true,  "appendDetectionParameters": true,  "legacyMeasurements0.1.2": false}');
