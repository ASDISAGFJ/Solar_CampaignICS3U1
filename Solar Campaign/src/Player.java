import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends Rectangle {

    private double velocityX = 0;// the velocity in the x axis
    private double velocityY = 0;//the velocity in the y axis
    private double speedX = 7;//the run speed
    private double speedY = 0.65;//the jump speed
    private double damping = 0.45; // Damping factor to reduce velocity over time

    private double storeX;//store the velocityX for the start method

    private double storeY;//store the velocityY for the start method

    private double gravity = 0.1;//gravity which constantly brings the playerY down



    Bullet bullet;
    private long lastSuperJumpTime; // Time of the last super jump
    private long superJumpCooldown = 2000000000L; // Cooldown time in nanoseconds (2 seconds)

    private double minX = 0; // Minimum X-coordinate for the game border
    private double minY = 0; // Minimum Y-coordinate for the game border
    private double maxX = 1200; // Maximum X-coordinate for the game border
    private double maxY = 600; // Maximum Y-coordinate for the game border

    //gives the player a size, colour, and location
    public Player() {
        super(200, 200, 50, 50);
        setFill(Color.BLUE);
    }

    //the player movement gradually increases, and decreases after pressing the opposite button, e.g: D accelerates the
    // player to the right, and A decelerates until it starts accelerating left

    //moves the character based on velocity
    public void handleKeyPress(KeyCode keyCode) {
        switch (keyCode) {
            case W:
                velocityY -= speedY;
                break;
            case S:
                velocityY +=  speedY;
                break;
            case A:
                velocityX -= speedX;
                break;
            case D:
                velocityX += speedX;
                break;
            case E:
                new Bullet(getX(), getY(), 2, Color.RED);
                break;
            case Q:
                // Check if enough time has passed since the last super jump
                long currentTime = System.nanoTime();
                if (currentTime - lastSuperJumpTime > superJumpCooldown) {
                    velocityY -= speedY + 6;
                    lastSuperJumpTime = currentTime;
                }
                break;
            default:
                break;
        }
    }





    //slows down when the mouse is released
    public void handleKeyRelease(KeyCode keyCode) {
        switch (keyCode) {
            case W:
            case S:
                velocityY *= damping;
                break;
            case A:
            case D:
                velocityX *= damping;
                break;
            default:
                break;
        }
    }

    public void update(Platform[] platforms) {
        double newX = getX() + velocityX;
        double newY = getY() + velocityY;

        // Apply gravity
        velocityY += gravity;


        // Check for collisions with each platform
        for (Platform platform : platforms) {
            if (newX < platform.getX() + platform.getWidth() &&
                    newX + getWidth() > platform.getX() &&
                    getY() < platform.getY() + platform.getHeight() &&
                    getY() + getHeight() > platform.getY()) {
                // Collision detected, adjust the player's position
                if (velocityX > 0) {
                    newX = platform.getX() - getWidth();
                } else if (velocityX < 0) {
                    newX = platform.getX() + platform.getWidth();
                }

                // Reset velocity in x-axis to prevent further movement in that direction
                velocityX = 0;

            }

            if (getX() < platform.getX() + platform.getWidth() &&
                    getX() + getWidth() > platform.getX() &&
                    newY < platform.getY() + platform.getHeight() &&
                    newY + getHeight() > platform.getY()) {
                // Collision detected, adjust the player's position
                if (velocityY > 0) {
                    newY = platform.getY() - getHeight();
                } else if (velocityY < 0) {
                    newY = platform.getY() + platform.getHeight();
                }

                // Reset velocity in y-axis to prevent further movement in that direction
                velocityY = 0;

            }
        }

        // Check if the new position is within the game border
        if (newX >= minX && newX <= maxX - getWidth()) {
            setX(newX);
        }

        if (newY >= minY && newY <= maxY - getHeight()) {
            setY(newY);
        }

        if(velocityX > 0){
            velocityX -= 0.05;
        }else{
                velocityX += 0.05;}

    }



    //stops any movement and stores the lost velocity in the store variables
    public void stop(){
        storeX = velocityX;
        storeY = velocityY;
        velocityX = 0;
        velocityY = 0;
    }

    //starts movement by adding the velocity in store to the velocity variable
    public void start(){
        velocityX = storeX;
        velocityY = storeY;
    }
}
