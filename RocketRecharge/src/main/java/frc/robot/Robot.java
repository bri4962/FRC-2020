/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Lights;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command autonomousCommand;

  private RobotContainer robotContainer;

  private int tempCurrAuto = 0;
  private ShuffleboardTab autoTab = Shuffleboard.getTab("Auto Tab");
  private NetworkTableEntry CurrentAuto = autoTab.addPersistent("Current Auto", "initializing").getEntry();
  private NetworkTableEntry allAutos = autoTab.addPersistent("All Auto Programs", "intiializing").getEntry();
  private Joystick gamepad1;
  private Joystick gamepad2;
  private Lights blinkin;
  private Boolean wasPressed = false;
  private Boolean unplugged1 = false;
  private Boolean unplugged2 = false;
  private UsbCamera camera1;
  private UsbCamera climberCamera;
  private Command monitor;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    robotContainer = new RobotContainer();
    gamepad1 = robotContainer.getJoystick1();
    gamepad2 = robotContainer.getJoystick2();
    blinkin = robotContainer.getBlinkin();
    
    camera1 = CameraServer.getInstance().startAutomaticCapture(0);
    camera1.setVideoMode(PixelFormat.kMJPEG, 400, 300, 10);
    //climberCamera = CameraServer.getInstance().startAutomaticCapture(1);
    //climberCamera.setVideoMode(PixelFormat.kMJPEG, 25, 25, 2);

   // monitor = robotContainer.getDiagnosticsCommand();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
    //monitor.schedule();

  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
    allAutos.setString(robotContainer.getNames());
    blinkin.setFire();
  }

  @Override
  public void disabledPeriodic() {
    //if(!isSet) {blinkin.setRedLarson();isSet=true;} //for lights might work just so we can look cool this is not important
    if(gamepad1.getPOV() == -1) {
      wasPressed = false;
    }
    else if(!wasPressed) {
      
      //180 is up and 0 is down
      if(gamepad1.getPOV() == 180) {
        if(tempCurrAuto < robotContainer.getLength() - 1) {
          tempCurrAuto += 1;
          wasPressed = true;
        }
        robotContainer.setAutoNum(tempCurrAuto);
      }
      else if(gamepad1.getPOV() == 0) {
        if(tempCurrAuto > 0) {
          tempCurrAuto -= 1;
          wasPressed = true;
        }
        robotContainer.setAutoNum(tempCurrAuto);
      }
    }
   
    
    CurrentAuto.setString(robotContainer.getName(tempCurrAuto));
  }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {

    autonomousCommand = robotContainer.getAutonomousCommand();

    blinkin.setRainbow();

    // schedule the autonomous command (example)
    if (autonomousCommand != null) {
      autonomousCommand.schedule();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (autonomousCommand != null) {
      autonomousCommand.cancel();
    }
    
    // Let the driver take over
    robotContainer.getDriveCommand().schedule();
    blinkin.setRedLarson();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic(){
  }


  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
