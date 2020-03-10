//this removes LC ROI and only keeps child objects within region
clearDetections();
selectObjects{p -> (p.getLevel()==1) && (p.isAnnotation() == true)};
clearSelectedObjects(true);