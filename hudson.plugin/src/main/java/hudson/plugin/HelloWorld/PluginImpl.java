package hudson.plugin.HelloWorld;


import hudson.Plugin;
import hudson.tasks.BuildStep;
import hudson.tasks.Builder;
import hudson.tasks.Publisher;

public class PluginImpl extends Plugin {

	public void start() throws Exception {
		System.out.println("================== start");
		//Publisher.PUBLISHERS.add(TSLPublisher.DESCRIPTOR);
		BuildStep.BUILDERS.add(HelloWorldBuilder.DESCRIPTOR);
	}
}
