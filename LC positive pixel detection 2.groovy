//this selects all annotations – should only be LC polygon - on images
    // - at which should ONLY be the polygon outline of LC and runs a positive pixel analysis 
    
/* 1:1000 10 MIN VECTOR RED – CONFIRM WITH DAN – ALGORITHM DESIGNED BY RANDOMLY SELECTING 5 CASES FROM EACH COHORT(TDP,TAU,AD,Healthy) then used Estimate stain vector function. HEMA STAIN VALUE FROM H+NM only slide and TH from TH+NM+H slide */
setColorDeconvolutionStains('{"Name" : "H-DAB default", "Stain 1" : "TH", "Values 1" : "0.299786 0.783571 0.538714 ", "Stain 2" : "NM", "Values 2" : "0.51475 0.59095 0.61395", "Stain 3" : "Hematoxylin", "Values 3" : "0.66345 0.6614 0.3473", "Background" : " 255 255 255 "}');
//setColorDeconvolutionStains('{"Name" : "H-DAB default", "Stain 1" : "TH", "Values 1" : "0.299786 0.783571 0.538714 ", "Stain 2" : "NM", "Values 2" : "0.36 .613 .703", "Stain 3" : "Hematoxylin", "Values 3" : "0.66345 0.6614 0.3473", "Background" : " 255 255 255 "}');
    // Select polygon annotations on images
selectAnnotations();

    //Positive Pixel detection on area within polygon
runPlugin('qupath.imagej.detect.tissue.PositivePixelCounterIJ', '{"downsampleFactor": 1,  "gaussianSigmaMicrons": .25,  "thresholdStain1": 15,  "thresholdStain2": 0.42,  "addSummaryMeasurements": true,  "clearParentMeasurements": true,  "appendDetectionParameters": false,  "legacyMeasurements0.1.2": false}');
