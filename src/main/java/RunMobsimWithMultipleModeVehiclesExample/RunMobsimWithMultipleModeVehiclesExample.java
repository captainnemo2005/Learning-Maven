package RunMobsimWithMultipleModeVehiclesExample;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.examples.ExamplesUtils;
import org.matsim.vehicles.VehicleUtils;
import org.matsim.vehicles.VehicleType;
import org.matsim.api.core.v01.Id;
final class RunMobsimWithMultipleModeVehiclesExample {

	private final String [] args;
	
	private Config config;
	
//	/private String inputFile = "scenarios/equil/config.xml";
	
	public static void main(String [] args) {
		new RunMobsimWithMultipleModeVehiclesExample(args).run();
	}
	RunMobsimWithMultipleModeVehiclesExample( String [] args ) {
		this.args = args ;
		prepareConfig();
	}

	Config prepareConfig() {
		String outputDirectory = "output/RunMobsimWithMultipleModeExample";
		config = ConfigUtils.loadConfig( IOUtils.newUrl( ExamplesUtils.getTestScenarioURL( "equil" ), "config.xml" ) );
		config.controler().setOutputDirectory(outputDirectory);
		config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);
		config.qsim().setLinkDynamics( QSimConfigGroup.LinkDynamics.PassingQ );
		config.qsim().setVehiclesSource(QSimConfigGroup.VehiclesSource.modeVehicleTypesFromVehiclesData);
		config.plans().setRemovingUnneccessaryPlanAttributes(true) ;
		return config ;
	}
	
	public void run() {
		Scenario scenario = ScenarioUtils.loadScenario(config);
		
		VehicleType car = VehicleUtils.getFactory().createVehicleType(Id.create("car", VehicleType.class));
		car.setMaximumVelocity(60.0/3.6);
		car.setPcuEquivalents(1.0);
		scenario.getVehicles().addVehicleType(car);
		
		VehicleType bike = VehicleUtils.getFactory().createVehicleType(Id.create("bike", VehicleType.class));
		car.setMaximumVelocity(60.0/3.6);
		car.setPcuEquivalents(.25);
		scenario.getVehicles().addVehicleType(bike);
		
		VehicleType bicycles = VehicleUtils.getFactory().createVehicleType(Id.create("bicycle", VehicleType.class));
		car.setMaximumVelocity(60.0/3.6);
		car.setPcuEquivalents(.05);
		scenario.getVehicles().addVehicleType(bicycles);
		
		VehicleType walks = VehicleUtils.getFactory().createVehicleType(Id.create("walks", VehicleType.class));
		car.setMaximumVelocity(60.0/3.6);
		car.setPcuEquivalents(0.10);
		scenario.getVehicles().addVehicleType(walks);
		
		Controler controler = new Controler(scenario);
		
		controler.run();
		
	}
}

