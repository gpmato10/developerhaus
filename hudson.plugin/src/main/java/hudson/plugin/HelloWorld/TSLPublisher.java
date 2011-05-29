package hudson.plugin.HelloWorld;

import javax.servlet.http.HttpServletRequest;

import hudson.Launcher;
import hudson.model.Action;
import hudson.model.Build;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.AbstractProject;
import hudson.tasks.Publisher;

import org.apache.log4j.Logger;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class TSLPublisher extends Publisher {
 
    private static final Logger logger = Logger.getLogger(TSLPublisher.class.getName());
 
    private Boolean enable;
 
    @DataBoundConstructor
    public TSLPublisher(Boolean enable) {
        this.enable = enable;
    }
 
    /**
     * We’ll use this from the <tt>config.jelly</tt>.
     */
    public Boolean getEnable() {
        return enable;
    }
 
    public boolean needsToRunAfterFinalized() {
        return true;
    }
 
    /**
     * This method should have the logic of the plugin. Access the configuration
     * and execute the the actions.
     */
    public boolean perform(Build build, Launcher launcher, BuildListener listener) {
    	System.out.println("Performing update...");
        System.out.println("Bonjour, 빡썽!1");
        listener.getLogger().println("Bonjour, 빡썽!");
        // TODO: WRITE THE LOGIC OF YOUR PLUGIN HERE!!!!
        return true;
    }
 
     public Descriptor<Publisher> getDescriptor() {
         return DESCRIPTOR;
     }
 
     public Action getProjectAction(AbstractProject<?, ?> project) {
         return null;
     }
 
     /**
     * Descriptor should be singleton.
     */
    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();
 
    /**
     * Descriptor for {@link TSLPublisher}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See <tt>global.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    public static final class DescriptorImpl extends Descriptor<Publisher> {
        /**
         * To persist global configuration information,
         * simply store it in a field and call save().
         *
         * <p>
         * If you don’t want fields to be persisted, use <tt>transient</tt>.
         */
        private String field;
		private String uri;
 
        protected DescriptorImpl() {
            super(TSLPublisher.class);
            load();
        }
 
        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "(SAMPLE)This is the TSL Sample Plugin";
        }
 
        /**
         * Get the fields from the configuration form and persist them.
         */
        public boolean configure(HttpServletRequest req) throws FormException {
            uri = req.getParameter("tsl.field");
            save();
            System.out.println("Saved TSL configuration");
            return super.configure(req);
        }
 
        /**
         * Creates a new instance of {@link TSLPublisher} from a submitted form.
         */
        public TSLPublisher newInstance(StaplerRequest req) throws FormException {
        	System.out.println("(SAMPLE)New instance for a job");
            return new TSLPublisher(req.getParameter("tsl.enable")!=null);
        }
 
        public String getField() {
            return field;
        }
 
        public void setField(String field) {
            this.field = field;
        }
    }    
}
