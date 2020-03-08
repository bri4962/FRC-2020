package frc.robot.commands.intake;

import frc.robot.commands.RocketTimedCommand;
import frc.robot.subsystems.Intake;
//import edu.wpi.first.wpilibj2.command.CommandBase;

public class DeployIntake extends RocketTimedCommand {
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private Intake intake;
  private double timeout;

  public DeployIntake(Intake intake, double timeout) {
    this.intake = intake;
    this.timeout = timeout;
    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intake.extendIntake();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return super.isTimedOut();
  }
}