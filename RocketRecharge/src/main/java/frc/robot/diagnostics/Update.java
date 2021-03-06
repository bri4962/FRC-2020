package frc.robot.diagnostics;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Diagnostics;

public class Update extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private Diagnostics diagnostics;

    public Update(Diagnostics diagnostics) {
    this.diagnostics = diagnostics;
    addRequirements(diagnostics);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
   // diagnostics.update();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}