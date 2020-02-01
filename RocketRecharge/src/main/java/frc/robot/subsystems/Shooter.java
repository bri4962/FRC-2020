/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

/*BIG NOTE: 
 In order for the code to download smoothly and not freak out,
 all of the motor related actions are commented out. 
 Once we got all of the Spark Maxes attached for shooter
 UNCOMMENT THEM
 
 ALSO:
 We may or may not remove the angle motor stuff depending on what we are doing*/

package frc.robot.subsystems;

//talon
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

//spark
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class Shooter extends SubsystemBase {
  //CANSparkMax shooterMotor;
  //CANSparkMax shooterMotor2;
  //WPI_TalonSRX angleMotor;
  CANPIDController shooterPID;
  CANPIDController anglePID;
  CANEncoder encoder;
  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;
  private double RPMSetpoint;
  private double angleSetpoint;

  private ShuffleboardTab tab1 = Shuffleboard.getTab("Shooter");
  private NetworkTableEntry ShooterkP = tab1.addPersistent("ShooterkP", Constants.ShooterkP).getEntry();
  private NetworkTableEntry ShooterkI = tab1.addPersistent("ShooterkI", Constants.ShooterkI).getEntry();
  private NetworkTableEntry ShooterkD = tab1.addPersistent("ShooterkD", Constants.ShooterkD).getEntry();
  private NetworkTableEntry ShooterkIz = tab1.addPersistent("ShooterkIz", Constants.ShooterkIz).getEntry();
  private NetworkTableEntry ShooterkFF = tab1.addPersistent("ShooterkFF", Constants.ShooterkFF).getEntry();
  private NetworkTableEntry ShooterkMaxOutput = tab1.addPersistent("ShooterkMaxOutput", Constants.ShooterkMaxOutput).getEntry();
  private NetworkTableEntry ShooterkMinOutput = tab1.addPersistent("ShooterkMinOutput", Constants.ShooterkMinOutput).getEntry();
  //RIP MaxRPM you will be missed D:

  /*
  private NetworkTableEntry AnglekP = tab1.addPersistent("AnglekP", Constants.AnglekP).getEntry();
  private NetworkTableEntry AnglekI = tab1.addPersistent("AnglekI", Constants.AnglekI).getEntry();
  private NetworkTableEntry AnglekD = tab1.addPersistent("AnglekD", Constants.AnglekD).getEntry();
  */

  private ShuffleboardTab tab2 = Shuffleboard.getTab("Shooter Presets");
  private NetworkTableEntry TrenchAngle = tab2.addPersistent("TrenchAngle", Constants.TrenchAngle).getEntry();
  private NetworkTableEntry TrenchRPM = tab2.addPersistent("TrenchRPM", Constants.TrenchRPM).getEntry();
  private NetworkTableEntry TenFootAngle = tab2.addPersistent("TenFootAngle", Constants.TenFootAngle).getEntry();
  private NetworkTableEntry TenFootRPM = tab2.addPersistent("TenFootRPM", Constants.TenFootRPM).getEntry();
  private NetworkTableEntry LayupAngle = tab2.addPersistent("LayupAngle", Constants.LayupAngle).getEntry();
  private NetworkTableEntry LayupRPM = tab2.addPersistent("LayupRPM", Constants.LayupRPM).getEntry();


  /**
   * Creates a new ExampleSubsystem.
   */
  public Shooter() {
    //shooterMotor = new CANSparkMax(7, MotorType.kBrushless);
    //shooterMotor.restoreFactoryDefaults();
    //shooterPID = shooterMotor.getPIDController();
    //encoder = shooterMotor.getEncoder();
 
    //second motor
    //shooterMotor2 = new CANSparkMax(8,MotorType.kBrushless);
    //shooterMotor2.restoreFactoryDefaults();
    //shooterMotor2.follow(shooterMotor, true);

    // set PID coefficients
    //shooterPID.setP(Constants.ShooterkP);
    //shooterPID.setI(Constants.ShooterkI);
    //shooterPID.setD(Constants.ShooterkD);
    //shooterPID.setIZone(Constants.ShooterkIz);
    //shooterPID.setFF(Constants.ShooterkFF);
    //shooterPID.setOutputRange(Constants.ShooterkMinOutput, Constants.ShooterkMaxOutput);

    /*
    //angle motor config
    angleMotor.configAllowableClosedloopError(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);

    /* Config the peak and nominal outputs 
		angleMotor.configNominalOutputForward(0, Constants.kTimeoutMs);
		angleMotor.configNominalOutputReverse(0, Constants.kTimeoutMs);
		angleMotor.configPeakOutputForward(1, Constants.kTimeoutMs);
		angleMotor.configPeakOutputReverse(-1, Constants.kTimeoutMs);

  
		/* Config the Velocity closed loop gains in slot0 
		angleMotor.config_kF(Constants.kPIDLoopIdx, Constants.AnglekF, Constants.kTimeoutMs);
		angleMotor.config_kP(Constants.kPIDLoopIdx, Constants.AnglekP, Constants.kTimeoutMs);
		angleMotor.config_kI(Constants.kPIDLoopIdx, Constants.AnglekI, Constants.kTimeoutMs);
    angleMotor.config_kD(Constants.kPIDLoopIdx, Constants.AnglekD, Constants.kTimeoutMs);
    */

    /* Shooter with talon
    //shooter motor config

      shooterMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,Constants.kPIDLoopIdx,Constants.kTimeoutMs);

    // Config the peak and nominal outputs 
		shooterMotor.configNominalOutputForward(0, Constants.kTimeoutMs);
		shooterMotor.configNominalOutputReverse(0, Constants.kTimeoutMs);
		shooterMotor.configPeakOutputForward(1, Constants.kTimeoutMs);
		shooterMotor.configPeakOutputReverse(-1, Constants.kTimeoutMs);

		// Config the Velocity closed loop gains in slot0 
		shooterMotor.config_kF(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kF, Constants.kTimeoutMs);
		shooterMotor.config_kP(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kP, Constants.kTimeoutMs);
		shooterMotor.config_kI(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kI, Constants.kTimeoutMs);
    shooterMotor.config_kD(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kD, Constants.kTimeoutMs);
    */
    
  }

  public boolean warmUp(double RPM) { 
    //Shooter with Neo
    //shooterPID.setReference(RPM, ControlType.kVelocity);
    return true;
  }

  /*
  public boolean shooterAngle(double angle) {
    angleMotor.set(ControlMode.Position, angle);
    //return Math.abs(angleMotor.getClosedLoopError()) < 1;
    return true;
  }
  */

  public boolean stopPlease(){
    //shooterMotor.setVoltage(0);
    return true;
  }

  public void trenchShot(){
    ///shooterAngle(Constants.TrenchAngle);
    warmUp(Constants.TrenchRPM);
    RPMSetpoint = Constants.TrenchRPM;
    //angleSetpoint = Constants.TrenchAngle;
  }

  public void tenFootShot(){
    //shooterAngle(Constants.TenFootAngle);
    warmUp(Constants.TenFootRPM);
    RPMSetpoint = Constants.TenFootRPM;
    //angleSetpoint = Constants.TenFootAngle;
  }

  public void layupShot(){
   // shooterAngle(Constants.LayupAngle);
    warmUp(Constants.LayupAngle);
    RPMSetpoint = Constants.LayupRPM;
    //angleSetpoint = Constants.LayupAngle;
  }

  public boolean isShooterGood(){
    if(Math.abs(encoder.getVelocity() - RPMSetpoint) < Constants.RPMTolerance /*&& Math.abs(encoder.getPosition() - angleSetpoint) < Constants.AnglekF */){
      return true;
    }
    return false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    //now for changing the PID values on robot and in Constants.java. this is going to be _very_ long
    double tempSP = ShooterkP.getDouble(Constants.ShooterkP);
    if(Constants.ShooterkP != tempSP) {
      Constants.ShooterkP = tempSP;
      //shooterPID.setP(Constants.ShooterkP);
    }

    double tempSI = ShooterkI.getDouble(Constants.ShooterkI);
    if(Constants.ShooterkI != tempSI) {
      Constants.ShooterkI = tempSI;
      //shooterPID.setI(Constants.ShooterkI);
    }

    double tempSD = ShooterkD.getDouble(Constants.ShooterkD);
    if(Constants.ShooterkD != tempSD) {
      Constants.ShooterkD = tempSD;
      //shooterPID.setD(Constants.ShooterkD);
    }

    double tempSIz = ShooterkIz.getDouble(Constants.ShooterkIz);
    if(Constants.ShooterkIz != tempSIz) {
      Constants.ShooterkIz = tempSIz;
      //shooterPID.setIZone(Constants.ShooterkIz);
    }

    double tempSFF = ShooterkFF.getDouble(Constants.ShooterkFF);
    if(Constants.ShooterkFF != tempSFF) {
      Constants.ShooterkFF = tempSFF;
     // shooterPID.setFF(Constants.ShooterkFF);
    }

    double tempSMax = ShooterkMaxOutput.getDouble(Constants.ShooterkMaxOutput);
    if(Constants.ShooterkMaxOutput != tempSMax) {
      Constants.ShooterkMaxOutput = tempSMax;
      //shooterPID.setOutputRange(Constants.ShooterkMinOutput,Constants.ShooterkMaxOutput);
    }
  
    double tempSMin = ShooterkMinOutput.getDouble(Constants.ShooterkMinOutput);
    if(Constants.ShooterkMinOutput != tempSMin) {
      Constants.ShooterkMaxOutput = tempSMin;
      //shooterPID.setOutputRange(Constants.ShooterkMinOutput, Constants.ShooterkMaxOutput);
    }


    /*
    //angle motor

    double tempAkp = AnglekP.getDouble(Constants.AnglekP);
    if(Constants.AnglekP != tempAkp) {
      Constants.AnglekP = tempAkp;
      anglePID.setP(Constants.AnglekP);
    }
    
    double tempAki = AnglekI.getDouble(Constants.AnglekI);
    if(Constants.AnglekI != tempAki) {
      Constants.AnglekI = tempAki;
      anglePID.setI(Constants.AnglekI);
    }

    double tempAkd = AnglekD.getDouble(Constants.AnglekD);
    if(Constants.AnglekD != tempAkd) {
      Constants.AnglekD = tempAkd;
      anglePID.setD(Constants.AnglekD);
    }
    */
    
    //Preset
    //double tempTrenchAngle = TrenchAngle.getDouble(Constants.TrenchAngle);
    //if(Constants.TrenchAngle != tempTrenchAngle) {
    //  Constants.TrenchAngle = tempTrenchAngle;
    //}

    double tempTrenchRPM = TrenchRPM.getDouble(Constants.TrenchRPM);
    if(Constants.TrenchRPM != tempTrenchRPM){
      Constants.TrenchRPM = tempTrenchRPM;
    }

    //double tempTenFootAngle = TenFootAngle.getDouble(Constants.TenFootAngle);
    //if(Constants.TenFootAngle != tempTenFootAngle) {
    //  Constants.TenFootAngle = tempTenFootAngle;
    //}

    double tempTenFootRPM = TenFootRPM.getDouble(Constants.TenFootRPM);
    if(Constants.TenFootRPM != tempTenFootRPM){
      Constants.TenFootRPM = tempTenFootRPM;
    }

    //double tempLayupAngle = LayupAngle.getDouble(Constants.LayupAngle);
    //if(Constants.LayupAngle != tempLayupAngle) {
    //  Constants.LayupAngle = tempTrenchAngle;
    //}

    double tempLayupRPM = LayupRPM.getDouble(Constants.LayupRPM);
    if(Constants.LayupRPM != tempLayupRPM){
      Constants.LayupRPM = tempLayupRPM;
    }

  }
}

/*command list for shooter

Warm up:
- connect with shooter subsystem
- speed up motor
- apply the current RPM to the shooter
ENDS WHEN- button is released
WHEN INTERUPTED- do nothing

Adjust power per position:
-connect with shooter subsystem
- read the current RPM of the motor
- have a preset shooter command that the shooter motor can stick with
   - detect vision alinement
   - run warmup
- once ready, then shoot

:-D

*/