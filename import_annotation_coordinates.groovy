//this loads annotations 
	//be sure to include "/annotations" in the path if annotations were exported to the newly created annotation directory
def path = buildFilePath('YOUR PATH TO ANNOTATION FILES', "annotations-" + getProjectEntry().getImageName() + '.txt')
def annotations = null
new File(path).withObjectInputStream {
    annotations = it.readObject()
}
addObjects(annotations)
print 'Added ' + annotations