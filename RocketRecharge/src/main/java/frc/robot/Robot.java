/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
//import frc.robot.subsystems.Drivetrain;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  private int tempCurrAuto = 0;
  private ShuffleboardTab autoTab = Shuffleboard.getTab("Auto Tab");
  private NetworkTableEntry CurrentAuto = autoTab.addPersistent("Current Auto", "initializing").getEntry();
  private NetworkTableEntry allAutos = autoTab.addPersistent("All Auto Programs", "intiializing").getEntry();
  private Joystick gamepad1 = new Joystick(0);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
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
    //SmartDashboard.putBoolean(drivetrain.isVisionAligned());
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
    allAutos.setString(m_robotContainer.getNames());
  }

  @Override
  public void disabledPeriodic() {
    m_robotContainer.setAutoNum(tempCurrAuto);
    //dpad is 6. 0 is up and 4 is down
    if(gamepad1.getPOV() == 180) {
      if(tempCurrAuto < m_robotContainer.getLength() - 1) {
        tempCurrAuto += 1;
      }
      m_robotContainer.setAutoNum(tempCurrAuto);
    }
    else if(gamepad1.getPOV() == 0) {
      if(tempCurrAuto > 0) {
        tempCurrAuto -= 1;
      }
      m_robotContainer.setAutoNum(tempCurrAuto);
    }
    try {
      Thread.sleep(10);
    }
    catch(InterruptedException e) {
      e.printStackTrace();
    }
    CurrentAuto.setString(m_robotContainer.getName(tempCurrAuto));
  }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
   // m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
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
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    
    // Let the driver take over
    m_robotContainer.getDriveCommand().schedule();
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
