package frc.robot.subsystems;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber extends SubsystemBase {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	// public CANSparkMax Climber;

	private DigitalInput bottomLimitSwitch, topLimitSwitch;
	private AnalogInput encoder;
	private WPI_TalonSRX climberMotor;
	private Solenoid climberPistons1;
	private Solenoid climberPistons2;
	private double distanceSetpoint;
	private Boolean isDeployed = false;
	private PIDController extendclimbPID = new PIDController(Constants.extendclimbkP, Constants.extendclimbkI, Constants.extendclimbkD);
	
	
	//shuffleboard
	private ShuffleboardTab Climbertab = Shuffleboard.getTab("Climber Tab");
	private NetworkTableEntry climbSpeed = Climbertab.addPersistent("climbSpeed", Constants.climbSpeed).getEntry();
	private NetworkTableEntry motorPosition = Climbertab.addPersistent("Motor Position", 0).getEntry();
	private NetworkTableEntry motorUp = Climbertab.addPersistent("motorUp", Constants.motorUp).getEntry();
	private NetworkTableEntry extendclimbkP = Climbertab.addPersistent("extendclimbkP", Constants.extendclimbkP).getEntry();
	private NetworkTableEntry extendclimbkI = Climbertab.addPersistent("extendclimbkI", Constants.extendclimbkI).getEntry();
	private NetworkTableEntry extendclimbkD = Climbertab.addPersistent("extendclimbkD", Constants.extendclimbkD).getEntry();
	private NetworkTableEntry DistanceSetpoint = Climbertab.addPersistent("DistanceSetpoint", Constants.distancesetpoint).getEntry();
	private NetworkTableEntry isitDeployed = Climbertab.addPersistent("is it Deployed", false).withWidget("Boolean Box").withProperties(Map.of("colorWhenTrue", "green", "colorWhenFalse", "red")).getEntry();

	public Climber() {
		bottomLimitSwitch = new DigitalInput(1);
		topLimitSwitch = new DigitalInput(2);
		// need to assign actual channel values
		// !==UPDATE==! --> values assigned now
		climberMotor = new WPI_TalonSRX(10);
		encoder = new AnalogInput(3);
		//slidePID.setSetpoint(4);
		//climberPID.setSetpoint(0.2);
		climberPistons1 = new Solenoid(7);
		climberPistons2 = new Solenoid(6);
	}

	public void ClimberDeploy() {
		if (climberPistons1 != null) {
			climberPistons1.set(false);
			isDeployed = true;
		}
		if (climberPistons2 != null) {
			climberPistons2.set(true);
			isDeployed = true;
		
		}
		isitDeployed.setBoolean(true);
	}

	public void ClimberPistonsBackIn() {
		if (climberPistons1 != null) {
			climberPistons1.set(true);
			isDeployed = false;
		}
		if (climberPistons2 != null) {
			climberPistons2.set(false);
			isDeployed = false;
		}
		isitDeployed.setBoolean(false);
	}

	public void raiseClimberPID() {
		if (isDeployed) {
			if (!topLimitSwitch.get()) {
				if (climberMotor != null) {
					climberMotor.set(extendclimbPID.calculate(encoder.getVoltage()));
				}
			} else {
				climberMotor.set(0);
			}
		}
	}

	public void climbWithPID() {
		if (isDeployed) {
			if (!bottomLimitSwitch.get()) {
				if (climberMotor != null) {
					climberMotor.set(extendclimbPID.calculate(encoder.getVoltage()));
				}
			} else {
				climberMotor.set(0);
			}
		}
	}

	public void climbUp() {
		if (isDeployed) {
			if (!bottomLimitSwitch.get()) {
				if (climberMotor != null) {
					climberMotor.set(-Constants.climbSpeed);
				}
			} else {
				if (climberMotor != null) {
					climberMotor.set(0.001);
				}
			}
		}
	}

	public void climbUsingStick(double x) {
		if (climberMotor != null) {
			climberMotor.set(x);
		}
	}

	public void stop() {
		if (climberMotor != null) {
			climberMotor.set(0);
		}
		climberMotor.set(-Constants.climbSpeed);
	}

	@Override
	public void periodic() {
		double TempClimberSpeed = climbSpeed.getDouble(0.5);
		if (TempClimberSpeed != Constants.climbSpeed) {
			Constants.climbSpeed = TempClimberSpeed;
			climbSpeed.setDouble(Constants.climbSpeed);
		}
		motorPosition.setDouble(encoder.getVoltage());

		double tempECP = extendclimbkP.getDouble(Constants.extendclimbkP);
    	if(Constants.extendclimbkP != tempECP && extendclimbPID != null) {
      		Constants.extendclimbkP = tempECP;
			extendclimbPID.setP(Constants.extendclimbkP);
		}

		double tempECI = extendclimbkI.getDouble(Constants.extendclimbkI);
    	if(Constants.extendclimbkI != tempECI && extendclimbPID != null) {
      		Constants.extendclimbkI = tempECI;
			extendclimbPID.setI(Constants.extendclimbkI);
		}

		double tempECD = extendclimbkD.getDouble(Constants.extendclimbkD);
    	if(Constants.extendclimbkD != tempECD && extendclimbPID != null) {
      		Constants.extendclimbkD = tempECD;
			extendclimbPID.setD(Constants.extendclimbkD);
		} 
		
		double tempDistanceSetpoint = DistanceSetpoint.getDouble(Constants.distancesetpoint);
    	if(Constants.distancesetpoint != tempDistanceSetpoint && extendclimbPID != null) {
      		Constants.distancesetpoint= tempDistanceSetpoint;
			extendclimbPID.setSetpoint(Constants.distancesetpoint);
		} 


    }

	}

	public void stopRaise() {
		climberMotor.set(0);
	}
}
