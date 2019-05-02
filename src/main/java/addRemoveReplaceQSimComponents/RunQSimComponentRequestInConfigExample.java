package addRemoveReplaceQSimComponents;

import org.apache.log4j.Logger;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.mobsim.qsim.AbstractQSimModule;
import org.matsim.core.mobsim.qsim.InternalInterface;
import org.matsim.core.mobsim.qsim.components.QSimComponentsConfigGroup;
import org.matsim.core.mobsim.qsim.interfaces.MobsimEngine;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.api.core.v01.Scenario;

import org.matsim.core.controler.Controler;
import java.util.List;


public class RunQSimComponentRequestInConfigExample {
	private static final Logger log = Logger.getLogger(RunQSimComponentRequestInConfigExample.class);
	
	public static void main(String[] args) {

		Config config = ConfigUtils.createConfig();
		config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);
		config.controler().setLastIteration(2);
		
		QSimComponentsConfigGroup qsimComponentsConfig = ConfigUtils.addOrGetModule(config, QSimComponentsConfigGroup.class);
		List<String> cmps = qsimComponentsConfig.getActiveComponents();
		qsimComponentsConfig.setActiveComponents(cmps);
		
		Scenario scenario = ScenarioUtils.loadScenario(config);
		
		Controler controler = new Controler(scenario);
		controler.addOverridingQSimModule( new AbstractQSimModule(){
			@Override protected void configureQSim(){
				this.addQSimComponentBinding( "abc" ).to( MyQSimComponent.class ) ;
			}
		} ) ;
		controler.run();
	}
	
	private static class MyQSimComponent implements MobsimEngine{
		//@Override 
		public void onPrepareSim() {
			log.info("calling onPrepareSim");
		}
		//@Override
		public void afterSim(){
			log.info("calling afterSim") ;
		}

		//@Override
		public void setInternalInterface( InternalInterface internalInterface ){
			log.info("calling setInternalInterface") ;
		}

		//@Override
		public void doSimStep( double time ){
		}
	}
}
