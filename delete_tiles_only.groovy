//Delete tiles (resulting annotations will just contain user drawn lines, sampling lines, GM ROI box, WM ROI box)
//creates 2 variables "CLASS1" and "CLASS2" and 
CLASS1 = getPathClass('Greatest GM Sampling zone')
CLASS2 = getPathClass('WM')

roi = getAnnotationObjects().findAll {it.getPathClass().toString() != 'Greatest GM Sampling zone' & it.getPathClass().toString() != 'WM' & it.getROI().getRoiName()!='Line' };
getCurrentHierarchy().getSelectionModel().setSelectedObjects(roi, null)
clearSelectedObjects(true)
