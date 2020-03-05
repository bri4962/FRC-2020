package frc.robot.autonomous;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.drivetrain.DriveOnHeading;
import frc.robot.commands.drivetrain.ResetGyroCommand;
//import frc.robot.commands.intake.AutoIntakeDeployCommand;
import frc.robot.commands.shooter.ShootBurstCommand;
//import frc.robot.commands.shooter.ShootAngleWarmupCommand;
//import frc.robot.commands.drivetrain.TurnOnHeading;
//import frc.robot.commands.shooter.ShootCommand;
import frc.robot.commands.shooter.WarmupCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class ThreeBallAuto extends SequentialCommandGroup {
    public ThreeBallAuto(Drivetrain drivetrain, Intake intake, Shooter shooter) {
        super(
            new ResetGyroCommand(drivetrain),
            //new AutoIntakeDeployCommand(intake),
            new WarmupCommand(shooter, null, 90, true, drivetrain),
            new WaitCommand(1),
            new ShootBurstCommand(shooter, intake, null, 90, 5, true, drivetrain),//changed for testing
            new DriveOnHeading(drivetrain, 0, -.3, 2)
            );
    }
}

