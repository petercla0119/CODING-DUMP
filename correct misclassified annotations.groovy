// following steps were taken and sent to David Coughlin by Claire on 11/20/19 to troubleshoot/correct a crash which he had while running a positive pixel analysis on his DLB MRI cohort. The issue was that some of his WM were classified as Greatest GM Sampling zone which is not correct. therefore the following steps were taken to correct this issue. 


//1. Delete tiles (resulting annotations will just contain user drawn lines, sampling lines, GM ROI box, WM ROI box)

//creates 2 variables "CLASS1" and "CLASS2" and

CLASS1 = getPathClass('Greatest GM Sampling zone')

CLASS2 = getPathClass('WM')

 

roi = getAnnotationObjects().findAll {it.getPathClass().toString() != 'Greatest GM Sampling zone' & it.getPathClass().toString() != 'WM' & it.getROI().getRoiName()!='Line' };

getCurrentHierarchy().getSelectionModel().setSelectedObjects(roi, null)

clearSelectedObjects(true)

 

//2. Export annotations (annotations will just contain user drawn lines, sampling lines, GM ROI box, WM ROI box)

//this script exports annotation coordinates to the project directory

def path = buildFilePath(PROJECT_BASE_DIR, 'annotations_no_tiles-' + getProjectEntry().getImageName() + '.txt')

def annotations = getAnnotationObjects()

new File(path).withObjectOutputStream {

    it.writeObject(annotations)

}

print 'Done!'

 

//3. Tile GM and WM ROIs

// TILE WM AND GM ROI ONLY

selectObjects { p -> p.getPathClass() == getPathClass("Background") || p.getPathClass()==getPathClass("Pathology") }; //Select only the area we want to subdivide into tiles

// Set tile size to 175um

runPlugin('qupath.lib.algorithms.TilerPlugin', '{"tileSizeMicrons": 175.00,  "trimToROI": true,  "makeAnnotations": true,  "removeParentAnnotation": false}');

 

 

def tiles = getAnnotationObjects().findAll {it.getDisplayedName().toString().contains('Tile') == true && it.getROI().getRoiName()!='Rectangle'};

removeObjects(tiles, false)

tiles = getAnnotationObjects().findAll {it.getDisplayedName().toString().contains('Tile') == true};

numTiles = tiles.size();

 

ind2Remove = [];

for (int i=0; i<numTiles; i++){ ind2Remove.add(i) };

Collections.shuffle(ind2Remove);

//Specifies the percentage of tiles to remove, therefore leaving 30% of all tiles created

ind2Remove = ind2Remove[0..(int)(numTiles*0.70-1)];

 

for (index in ind2Remove){

    removeObject(tiles[index],false);

}

 

 

print getProjectEntry().getImageName() + " GM and WM Sampled"

 

//4. At this point, your GM ROI has the appropriate class, but the NAME is incorrect – so this will select all annotations, then remove any annotation that is not assigned to Greatest GM Sampling zone, then renames the objects in the greatest GM sampling zone class.

CLASS1 = getPathClass('Greatest GM Sampling zone')

def annotations = getAnnotationObjects()

def rois_to_remove = annotations.findAll {it.getPathClass().toString() != 'Greatest GM Sampling zone'};

annotations.removeAll(rois_to_remove)

//roi = getAnnotationObjects().findAll {it.getPathClass()== 'Pathology' };

//getCurrentHierarchy().getSelectionModel().setSelectedObjects(roi, null)

annotations.eachWithIndex {annotation, i -> annotation.setName('Greatest GM Sampling zone')}

 

fireHierarchyUpdate()