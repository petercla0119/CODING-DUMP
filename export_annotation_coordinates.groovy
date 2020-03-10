//this script exports annotation coordinates to the project directory
def path = buildFilePath(PROJECT_BASE_DIR, 'annotations-' + getProjectEntry().getImageName() + '.txt')
def annotations = getAnnotationObjects()
new File(path).withObjectOutputStream {
    it.writeObject(annotations)
}
print 'Done!'
