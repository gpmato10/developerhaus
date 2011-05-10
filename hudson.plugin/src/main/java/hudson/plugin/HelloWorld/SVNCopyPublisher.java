package hudson.plugin.HelloWorld;

import hudson.model.Descriptor;
import hudson.tasks.Publisher;

public class SVNCopyPublisher extends Publisher {

	private Boolean enable;


	private final String urlTarget, nameTarget, passTarget, filesTargetException, filesSourceException;

	//@DataBoundConstructor
	public SVNCopyPublisher(String urlTarget, String nameTarget, String passTarget, String filesSourceException, String filesTargetException) {

	this.urlTarget = urlTarget;

	this.nameTarget = nameTarget;

	this.passTarget = passTarget;

	this.filesTargetException = filesTargetException;

	this.filesSourceException = filesSourceException;

	}


	
	public Descriptor getDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

}
