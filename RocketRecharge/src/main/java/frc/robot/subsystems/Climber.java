package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber extends SubsystemBase {

	private WPI_TalonSRX extendMotor;
	private WPI_TalonSRX climbMotor;
	private DigitalInput bottomLimitSwitch;

	double downSetpoint = 0;
	
	// shuffleboard
	private ShuffleboardTab climberTab = Shuffleboard.getTab("Climber");
	private NetworkTableEntry climbSpeed = climberTab.addPersistent("Climbing Speed", Constants.climbSpeed).getEntry();
	
	private NetworkTableEntry extendPosition = climberTab.addPersistent("Extend Motor Position", 0).getEntry();
	private NetworkTableEntry extendkP = climberTab.addPersistent("ExtendkP", Constants.extendclimbkP).getEntry();
	private NetworkTableEntry extendkI = climberTab.addPersistent("ExtendkI", Constants.extendclimbkI).getEntry();
	private NetworkTableEntry extendkD = climberTab.addPersistent("ExtendkD", Constants.extendclimbkD).getEntry();
	private NetworkTableEntry distanceSetpoint = climberTab.addPersistent("Extend Distance Setpoint", Constants.distancesetpoint).getEntry();
	private NetworkTableEntry extendError = climberTab.addPersistent("Extend Error", 0).getEntry();
	
	public Climber() {
		extendMotor = new WPI_TalonSRX(13);
		climbMotor = new WPI_TalonSRX(14);
		//bottomLimitSwitch = new DigitalInput(0); don't need this
		extendMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,Constants.kPIDLoopIdx,Constants.kTimeoutMs);
		extendMotor.setSelectedSensorPosition(0);
		extendMotor.setSensorPhase(true);
		extendMotor.setInverted(true);

		if (extendMotor != null) {
			extendMotor.configAllowableClosedloopError(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
		}
		// Config the peak and nominal outputs
		if (extendMotor != null) {
			extendMotor.configNominalOutputForward(0, Constants.kTimeoutMs);
			extendMotor.configNominalOutputReverse(0, Constants.kTimeoutMs);
			extendMotor.configPeakOutputForward(1, Constants.kTimeoutMs);
			extendMotor.configPeakOutputReverse(-1, Constants.kTimeoutMs);
		}

		// Config the Velocity closed loop gains in slot0
		if (extendMotor != null) {
			extendMotor.config_kP(Constants.kPIDLoopIdx, Constants.extendclimbkP, Constants.kTimeoutMs);
			extendMotor.config_kI(Constants.kPIDLoopIdx, Constants.extendclimbkI, Constants.kTimeoutMs);
			extendMotor.config_kD(Constants.kPIDLoopIdx, Constants.extendclimbkD, Constants.kTimeoutMs);
			extendMotor.config_kF(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);
		}
	}
	
	public double GetExtendError() {
		return extendMotor.getSelectedSensorPosition() - Constants.distancesetpoint;
	}

	public void raiseClimberPID() {//not really PID
		if (extendMotor != null) {
			extendMotor.set(ControlMode.PercentOutput, -Constants.climbSpeed);
		}
	}


	public void climbUp() {
		if (climbMotor != null) {
			climbMotor.set(ControlMode.PercentOutput, Constants.climbSpeed);
		}
	}

	public void climbDown() {
		if (climbMotor != null) {
			climbMotor.set(ControlMode.PercentOutput, Constants.climbSpeed);
		}
	}

	public void stopRaise() {
		if (extendMotor != null) {
			extendMotor.set(0);
		}
	}

	public void stopClimb() {
		if (climbMotor != null) {
			climbMotor.set(0);
		}
	}
	
	@Override
	public void periodic() {
		extendError.setDouble(GetExtendError());
		extendPosition.setDouble(extendMotor.getSelectedSensorPosition());
		
		double TempClimberSpeed = climbSpeed.getDouble(1);
		if (TempClimberSpeed != Constants.climbSpeed) {
			Constants.climbSpeed = TempClimberSpeed;
			climbSpeed.setDouble(Constants.climbSpeed);
		}

		double tempECP = extendkP.getDouble(Constants.extendclimbkP);
		if (Constants.extendclimbkP != tempECP && extendMotor != null) {
			Constants.extendclimbkP = tempECP;
			extendMotor.config_kP(Constants.kPIDLoopIdx, Constants.extendclimbkP, Constants.kTimeoutMs);
		}

		double tempECI = extendkI.getDouble(Constants.extendclimbkI);
		if (Constants.extendclimbkI != tempECI && extendMotor != null) {
			Constants.extendclimbkI = tempECI;
			extendMotor.config_kI(Constants.kPIDLoopIdx, Constants.extendclimbkI, Constants.kTimeoutMs);
		}

		double tempECD = extendkD.getDouble(Constants.extendclimbkD);
		if (Constants.extendclimbkD != tempECD && extendMotor != null) {
			Constants.extendclimbkD = tempECD;
			extendMotor.config_kD(Constants.kPIDLoopIdx, Constants.extendclimbkD, Constants.kTimeoutMs);
		}

		double tempdistanceSetpoint = distanceSetpoint.getDouble(Constants.distancesetpoint);
		if (Constants.distancesetpoint != tempdistanceSetpoint) {
			Constants.distancesetpoint = tempdistanceSetpoint;
		}
	}
}		