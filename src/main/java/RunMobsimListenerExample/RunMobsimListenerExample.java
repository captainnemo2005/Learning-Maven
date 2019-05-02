package RunMobsimListenerExample;

import org.apache.log4j.Logger;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.mobsim.framework.events.MobsimBeforeSimStepEvent;
import org.matsim.core.mobsim.framework.listeners.MobsimBeforeSimStepListener;

public class RunMobsimListenerExample {
	
		public static String  outputDirectory = "output/mobsimListenerExample";
		
		public static void main(String [] args) {
			Config config = ConfigUtils.createConfig();
			config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);
			config.controler().setLastIteration(2);
			config.controler().setOutputDirectory(outputDirectory);
			
			Controler controler = new Controler(config);
			controler.addOverridingModule(new AbstractModule() {
				public void install() {
					addMobsimListenerBinding().to(CountingMobsimListener.class);
				}
			});
			
			controler.run();
		}
		
		public static class CountingMobsimListener implements MobsimBeforeSimStepListener {
			
			private int step = 0;
			
			//@Override
			public void notifyMobsimBeforeSimStep(MobsimBeforeSimStepEvent e) {
				Logger.getLogger(this.getClass()).info("We are at step " + step + ". Note that this restarts counting at zero in every iteration,"
						+ " implying that the class is re-instantiated in every iteration.");
				step++ ;
			}

		}
}


