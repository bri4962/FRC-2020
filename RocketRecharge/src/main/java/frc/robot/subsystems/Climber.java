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
	private Boolean isDeployed = false;
	private PIDController slidePID = new PIDController(0.0001, 0.00001, 0.00001);
	private PIDController climberPID = new PIDController(0.0001, 0.00001, 0.00001);
	private ShuffleboardTab Climbertab = Shuffleboard.getTab("Climber Tab");
	private NetworkTableEntry CLIMBER_CLIMBER_SPEED = Climbertab
			.addPersistent("CLIMBER_CLIMBER_SPEED", Constants.CLIMBER_CLIMBER_SPEED).getEntry();
	private NetworkTableEntry INTAKE_OUTTAKE_SPEED = Climbertab
			.addPersistent("INTAKE_OUTTAKE_SPEED", Constants.INTAKE_OUTTAKE_SPEED).getEntry();
	private NetworkTableEntry motorPosition = Climbertab.addPersistent("Motor Position", 0).getEntry();
	private NetworkTableEntry motorUp = Climbertab.addPersistent("motorUp", Constants.motorUp).getEntry();
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

	public void raiseClimber() {
		if (isDeployed) {
			if (!topLimitSwitch.get()) {
				if (climberMotor != null) {
					climberMotor.set(Constants.CLIMBER_CLIMBER_SPEED);
				}
			} else {
				if (climberMotor != null) {
					climberMotor.set(-0.01);
				}
			}
		}
	}

	public void raiseClimberPID() {
		if (isDeployed) {
			if (!topLimitSwitch.get()) {
				if (climberMotor != null) {
					climberMotor.set(slidePID.calculate(encoder.getVoltage()));
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
					climberMotor.set(climberPID.calculate(encoder.getVoltage()));
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
					climberMotor.set(-Constants.CLIMBER_CLIMBER_SPEED);
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
		climberMotor.set(-Constants.CLIMBER_CLIMBER_SPEED);
	}

	@Override
	public void periodic() {
		double TempClimberSpeed = CLIMBER_CLIMBER_SPEED.getDouble(0.5);
		if (TempClimberSpeed != Constants.CLIMBER_CLIMBER_SPEED) {
			Constants.CLIMBER_CLIMBER_SPEED = TempClimberSpeed;
			CLIMBER_CLIMBER_SPEED.setDouble(Constants.CLIMBER_CLIMBER_SPEED);
		}
		motorPosition.setDouble(encoder.getVoltage());
	}

	public void stopRaise() {
		climberMotor.set(0);
	}
}
