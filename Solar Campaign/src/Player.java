import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends Rectangle {

    private double velocityX = 0;// the velocity in the x axis
    private double velocityY = 0;//the velocity in the y axis
    private double speed = 0.5; //the rate of change for the player
    private double damping = 0.9; // Damping factor to reduce velocity over time

    private double storeX;//store the velocityX for the start method

    private double storeY;//store the velocityY for the start method

    //gives the player a size, colour, and location
    public Player() {
        super(200, 200, 50, 50);
        setFill(Color.BLUE);
    }
    //the player movement gradually increases, and decreases after pressing the oppsite button, D accelerated the player right,
    //and A decelerates until it starts accelerating left

    //moves the character based on velocity
    public void handleKeyPress(KeyCode keyCode) {
        switch (keyCode) {
            case W:
                velocityY -= speed;
                break;
            case S:
                velocityY += speed;
                break;
            case A:
                velocityX -= speed;
                break;
            case D:
                velocityX += speed;
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

    public void update() {
        // Update position based on velocity
        setX(getX() + velocityX);
        setY(getY() + velocityY);
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
