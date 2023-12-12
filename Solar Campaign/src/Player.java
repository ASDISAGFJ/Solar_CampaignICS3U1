import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends Rectangle {

    private double velocityX = 0;
    private double velocityY = 0;
    private double speed = 0.5;
    private double damping = 0.9; // Damping factor to reduce velocity over time

    private double storeX;

    private double storeY;

    public Player() {
        super(200, 200, 50, 50);
        setFill(Color.BLUE);
    }

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

    public void stop(){
        storeX = velocityX;
        storeY = velocityY;
        velocityX = 0;
        velocityY = 0;
    }
    public void start(){
        velocityX = storeX;
        velocityY = storeY;
    }
}