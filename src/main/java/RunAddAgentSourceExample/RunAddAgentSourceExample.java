package RunAddAgentSourceExample;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.mobsim.framework.AgentSource;
import org.matsim.core.mobsim.framework.MobsimAgent;
import org.matsim.core.mobsim.qsim.AbstractQSimModule;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.mobsim.qsim.components.QSimComponentsConfigGroup;
import org.matsim.core.mobsim.qsim.qnetsimengine.QVehicle;
import org.matsim.core.mobsim.qsim.qnetsimengine.QVehicleImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.vehicles.Vehicle;
import org.matsim.vehicles.VehicleUtils;

import com.google.inject.Inject;

public class RunAddAgentSourceExample {
		
		public static void main(String [] args) {
			
			String configFile = "scenarios/equil/example5-config.xml";
			String outputDir = "output/runAddAgentSourceExample";
			
			Config config = ConfigUtils.loadConfig(configFile);
			config.controler().setOutputDirectory(outputDir);
			config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);
			
			Scenario scenario = ScenarioUtils.loadScenario(config);
			
			final Controler controler = new Controler(scenario);
			
			{
				QSimComponentsConfigGroup cconfig = ConfigUtils.addOrGetModule(config, QSimComponentsConfigGroup.class);
				cconfig.getActiveComponents().add("newAgent");
				
				controler.addOverridingQSimModule(new AbstractQSimModule() {
					protected void configureQSim() {
						this.addQSimComponentBinding("newAgentSource").to(MyAgentSource.class);
					}
				
				});
				
			}
			
			controler.run();
		}
		
		private static class MyAgentSource implements AgentSource{
			@Inject private QSim qsim;

			//@Override
			public void insertAgentsIntoMobsim(){
				// insert traveler agent:
				final MobsimAgent ag = new MyMobsimAgent( qsim.getScenario(), qsim.getSimTimer() );
				qsim.insertAgentIntoMobsim( ag );

				// insert vehicle:
				final Vehicle vehicle = VehicleUtils.getFactory().createVehicle( Id.create( ag.getId(), Vehicle.class ), VehicleUtils.getDefaultVehicleType() );
				QVehicleImpl qVeh = new QVehicleImpl( vehicle );
				qsim.addParkedVehicle( qVeh, ag.getCurrentLinkId() );

			}
		}
}
