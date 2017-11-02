package org.firstinspires.ftc.teamcode.reece.code.important.files;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.util.Arrays;

/**
 * A newer better designed version of the "holonomic" TeleOp file. This one uses a lot of arrays
 * and objects to make more efficient use of code space.
 * I recommend you read every single comment (ugh. yes I know) to fully understand all the code in
 * this file. Please note all variables named "i" are there for while loops and are not meaningful
 * variables that should be named. All other variables/objects are named accurately
 * Created by Reece on 11/01/17.
 */

@TeleOp(name = "New TeleOp Test", group = "EXP")
public class TeleOpTests extends OpMode {

    //Variables with names for cleaner code
    int left = 1;
    int right = -1;
    int front = 1;
    int back = -1;

    //Drive related variables for speed control
    double speed = 0.6;
    double turn = 0.3;

    //Drive train objects
    DcMotor frontLeft, backLeft, frontRight, backRight;
    Servo jewel;
    HardwareDevice[] drive = {frontLeft, backLeft, frontRight, backRight, jewel};

    //Glyph manipulator objects
    DcMotor armBot, armTop;
    Servo clawLeft, clawRight;
    HardwareDevice[] glyph = {armBot, armTop, clawLeft, clawRight};

    //Relic manipulator objects
    DcMotor slide;
    Servo clamp;
    HardwareDevice[] relic = {slide, clamp};

    /**
     * In the init (initialization) method objects are defined and mapped on the hardware in
     * segments based on subteams. The purpose of this is for a cleaner code and superior
     * organization. Your welcome people of github.
     */

    public void init() {
        //Initialize and set adjust drive train devices
        hwMap(drive);
        motorDirec((DcMotor[]) Arrays.copyOfRange(drive, 0, 3), DcMotor.Direction.FORWARD);                 //Sets the direction of all the drive motors to forward

        //Initialize and adjust glyph manipulator devices
        hwMap(glyph);
        armBot.setDirection(DcMotor.Direction.REVERSE);                                                     //Sets the direction of the bottom arm motor to reverse because it is upside down

        //Initialize and adjust relic manipulator devices
        hwMap(relic);
    }

    /**
     * The loop method is required when extending from OpMode, while the program is running based on
     * the robot controller phone it loops all code inside the method.
     */

    @Override
    public void loop() {
        //Drive train motor loop code
        frontLeft.setPower(driveCode(front, left));                                                         //Set drive motors to their speed based on holonomic drive formula and position on the robot
        backLeft.setPower(driveCode(back, left));
        frontRight.setPower(driveCode(front, right));
        backRight.setPower(driveCode(back, right));
    }

    /**
     * This method has changed a lot recently. Basically what it does is it rounds the input of the
     * double to the nearest tenth.
     *
     * @param input   The input you want rounded
     * @param roundTo The place you want it rounded to
     * @return The input rounded to the nearest (selected in roundTo)
     */

    private double round(double input, double roundTo) {
        double multiplier = 1 / roundTo;                                                                    //Finds the number that it has to be multiplied by to round
        double rounded = ((int) (input * multiplier)) / multiplier;                                         //Multiplies input by multiplier, casts it to an int (rounds it), then divides by the multiplier
        return Range.clip(rounded, -1, 1);                                                                  //Clips the input so it doesn't exceed 1.0 or go under -1.0
    }

    /**
     * The driveCode method has also gone through some changes. It uses the variables (left, right,
     * front, back) as coefficients to calculate what motors need to be set to what power.
     *
     * @param horSide  The side horizontally based on our robot    (left, right)
     * @param vertSide The side vertically based on our robot      (front, back)
     * @return The method returns a number inbetween -1 and 1 used for powering the motors
     */

    private double driveCode(int horSide, int vertSide) {
        double formula = (vertSide * driveLeftX() + horSide * driveLeftY()) * speed + driveRightX() * turn; //The formula works with the joystick values to move the robot omnidirectionally
        return Range.clip(formula, -1, 1);
    }

    /**
     * The hwMap method (short for hardware map) is designed to take in an array of Objects called
     * Hardware Devices and use their names to map them on the phones in the most hassle free way
     * possible. It maps them in order based on the method, it automatically finds its class, and
     * names it a lowercase version of the object's name.
     *
     * @param devices The array of devices you want to the method to map
     */

    private void hwMap(Object[] devices) {
        int i = 0;
        int amount = devices.length;
        while (i < amount) {
            devices[i] = hardwareMap.get(devices[i].getClass(), devices[i].toString().toLowerCase());
            i++;
        }
    }

    /**
     * The motorDirec method (short for motor direction) is similar to the hwMap method where it is
     * used for bulk setting of motor's directions.
     *
     * @param motors    The array of motors you want be set to a direction
     * @param direction The direction you want the motors to be set to
     */

    private void motorDirec(DcMotor[] motors, DcMotor.Direction direction) {                                //Set direction of all motors in an array for efficiency
        int i = 0;
        int amount = motors.length;
        while (i < amount) {
            motors[i].setDirection(direction);
            i++;
        }
    }

    /**
     * These "methods" are here for ease of access and better naming of our joysticks. The joysticks
     * with "drive" in their name are controllers used by the driver, and joysticks with "man" in
     * their name are controllers used by the manipulator.
     *
     * @return For each one of these it returns the joystick value specified scaled to the
     * nearest tenth
     */

    private double driveLeftX() {
        return round(gamepad1.left_stick_x, 0.1);
    }

    private double driveLeftY() {
        return round(gamepad1.left_stick_y, 0.1);
    }

    private double driveRightX() {
        return round(gamepad1.right_stick_x, 0.1);
    }

    private double manLeftY() {
        return round(gamepad2.left_stick_y, 0.1);
    }
}