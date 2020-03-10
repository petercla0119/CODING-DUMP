//Sets image type
setImageType('BRIGHTFIELD_H_DAB');
//Sets color deconvoluton parameters to the HALO AT8 algorithm
// THIS LINE CHANGES BASED ON THE STAIN
setColorDeconvolutionStains('{"Name" : "H-DAB default", "Stain 1" : "TH", "Values 1" : "0.299786 0.783571 0.538714 ", "Stain 2" : "NM", "Values 2" : "0.36 .613 .703", "Stain 3" : "Hematoxylin", "Values 3" : "0.66345 0.6614 0.3473", "Background" : " 255 255 255 "}');

//creates 2 variables "CLASS1" and "CLASS2" and 
CLASS1 = getPathClass('TH_pos_NM_pos')
CLASS2 = getPathClass('TH_neg_NM_pos')
roi = getAnnotationObjects().findAll {it.getPathClass() == CLASS1 || it.getPathClass() == CLASS2};
getCurrentHierarchy().getSelectionModel().setSelectedObjects(roi, null)
// THIS LINE CHANGES BASED ON THE STAIN 
runPlugin('qupath.imagej.detect.tissue.PositivePixelCounterIJ', '{"downsampleFactor": 1,  "gaussianSigmaMicrons": 1,  "thresholdStain1": 15,  "thresholdStain2": 0.42,  "addSummaryMeasurements": true,  "clearParentMeasurements": true,  "appendDetectionParameters": false,  "legacyMeasurements0.1.2": false}');

print 'Analysis for ' + getProjectEntry().getImageName() + ' Complete!' 