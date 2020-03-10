//this removes all child objects within an LC ROI
clearDetections();
selectObjects{p -> (p.getLevel()==2) && (p.isAnnotation() == true)};
clearSelectedObjects(false);