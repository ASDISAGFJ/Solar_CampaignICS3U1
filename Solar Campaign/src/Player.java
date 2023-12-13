import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends Rectangle {

    private double velocityX = 0;// the velocity in the x axis
    private double velocityY = 0;//the velocity in the y axis
    private double speed = 1;//the rate of change for the player
    private double damping = 0.9; // Damping factor to reduce velocity over time

    private double storeX;//store the velocityX for the start method

    private double storeY;//store the velocityY for the start method



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

    public void update(Platform[] gameBoard) {
        // Update position based on velocity
        double newX = getX() + velocityX;
        double newY = getY() + velocityY;

        // Check if the new position collides with the GameBoard object
        if (newX < gameBoard.getX() + gameBoard.getWidth() &&
                newX + getWidth() > gameBoard.getX() &&
                newY < gameBoard.getY() + gameBoard.getHeight() &&
                newY + getHeight() > gameBoard.getY()) {
            // Collision detected, adjust the player's position
            if (velocityX > 0) {
                newX = gameBoard.getX() - getWidth();
            } else if (velocityX < 0) {
                newX = gameBoard.getX() + gameBoard.getWidth();
            }

            if (velocityY > 0) {
                newY = gameBoard.getY() - getHeight();
            } else if (velocityY < 0) {
                newY = gameBoard.getY() + gameBoard.getHeight();
            }
        }

        // Check if the new position is within the game border
        if (newX >= minX && newX <= maxX - getWidth()) {
            setX(newX);
        }

        if (newY >= minY && newY <= maxY - getHeight()) {
            setY(newY);
        }
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
