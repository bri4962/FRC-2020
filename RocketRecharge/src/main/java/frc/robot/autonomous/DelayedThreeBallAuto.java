package frc.robot.autonomous;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import frc.robot.commands.drivetrain.DriveOnHeadingCommand;
import frc.robot.commands.drivetrain.ResetGyroCommand;
import frc.robot.commands.intake.DeployIntakeCommand;

import frc.robot.commands.shooter.ShootBurstCommand;
import frc.robot.commands.shooter.WarmupCommand;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

/** A delayed 3 ball autonomous program */
public class DelayedThreeBallAuto extends SequentialCommandGroup {
    /** A delayed 3 ball autonomous program 
     * @param drivetrain a Drivetrain subsystem
     * @param intake an intake subsystem
     * @param shooter a shooter subsystem
    */
    public DelayedThreeBallAuto(Drivetrain drivetrain, Intake intake, Shooter shooter) {
        super(
            new DeployIntakeCommand(intake, 1), // deploy
            new ResetGyroCommand(drivetrain), // fresh start
            new WaitCommand(5), // delay
            new WarmupCommand(shooter, null, 90, true, drivetrain), // warm up
            new WaitCommand(1), // wait
            new ShootBurstCommand(shooter, intake, null, 90, 5, true, drivetrain), // Shoot!
            new DriveOnHeadingCommand(drivetrain, 0, -0.3, 2) // drive back
            );
    }
}