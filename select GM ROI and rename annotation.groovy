CLASS1 = getPathClass('Greatest GM Sampling zone')
def annotations = getAnnotationObjects()
def rois_to_remove = annotations.findAll {it.getPathClass().toString() != 'Greatest GM Sampling zone'};
annotations.removeAll(rois_to_remove)
//roi = getAnnotationObjects().findAll {it.getPathClass()== 'Pathology' };
//getCurrentHierarchy().getSelectionModel().setSelectedObjects(roi, null)
annotations.eachWithIndex {annotation, i -> annotation.setName('Greatest GM Sampling zone')}
