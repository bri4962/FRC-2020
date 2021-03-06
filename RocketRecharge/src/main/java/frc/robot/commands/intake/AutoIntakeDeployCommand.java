package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Intake;

public class AutoIntakeDeployCommand extends SequentialCommandGroup {

    public AutoIntakeDeployCommand(Intake intake) {
        super(
            new DeployIntake(intake, 1),
            new RunIntakeCommand(intake, -1),
            new RunIndexerCommand(intake, 0)
            );
    }
}
