import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;
/**
*
*
* the player rectangle 
* @author: Abiru
 **/   
public class Player extends Rectangle {

    private double velocityX = 0;// the velocity in the x axis
    private double velocityY = 0;//the velocity in the y axis
    private double speedX = 6;//the run speed
    private double speedY = 5;//the jump speed
    private double damping = 0.45; // Damping factor to reduce velocity over time

    private double storeX;//store the velocityX for the start method

    private double storeY;//store the velocityY for the start method

    private double gravity = 0.1;//gravity which constantly brings the playerY down




    private long lastSuperJumpTime; // Time of the last super jump
    private long superJumpCooldown = 2000000000L; // Cooldown time in nanoseconds (2 seconds)
    private long lastShootTime; // Time of the last bullet shoot
    private long shootCooldown = 1000000000L; // Cooldown time in nanoseconds (1 second)



    private double minX = 0; // Minimum X-coordinate for the game border
    private double minY = 0; // Minimum Y-coordinate for the game border
    private double maxX = 1200; // Maximum X-coordinate for the game border
    private double maxY = 600; // Maximum Y-coordinate for the game border

    // Set the knockback speed
    private double knockback =10;
    private double knockback2 = 10;

    //check if the players are looking left or right
    private boolean isFacingRight = true;
    private boolean isFacingRight2 = true;
    //the speed for the bullet(and direction using negative value)
    private double amnt;
    private double amnt2;


    //players life
    public double Life = 5;

    //create a group to make things visible 
    Group root = new Group();

    //gives the player a size, colour, and location
    public Player() {
        super(200, 200, 50, 50);
        setFill(Color.BLUE);
    }


    //bullets for each player
    private static Bullet bullet;
    private  static Bullet bullet2;

    //the player will move in a linear speed, they have have a 2 second cooldown for jumps, and 2 second cooldown for shooting

    //moves the character
    public void handleKeyPress(KeyCode keyCode, Group root) {
        switch (keyCode) {

            case A:
                moveXLeft.start();
                isFacingRight = false;
                break;
            case D:
                moveXRight.start();
                isFacingRight = true;
                break;
            case E:
                //checks if the bullet has reachd the time limit and removes it
                //then creates a new bullet, and make it move in the direction the player is facing
                long currentTime = System.nanoTime();
                if (bullet == null || currentTime - lastShootTime > shootCooldown) {
                    if (bullet != null) {
                        root.getChildren().remove(bullet);
                    }

                    bullet = new Bullet(getX(), getY(), Color.RED, currentTime);
                    root.getChildren().add(bullet);
                    lastShootTime = currentTime;
                    if (isFacingRight) {
                        amnt = 5;
                    } else {
                        amnt = -5;
                    }
                    moveBullet.start();
                }

                break;

            case Q:
                // Check if enough time has passed since the last super jump
                long currentTime2 = System.nanoTime();
                if (currentTime2 - lastSuperJumpTime > superJumpCooldown) {
                    velocityY -= speedY;
                    lastSuperJumpTime = currentTime2;
                }
                break;
            default:
                break;
        }
    }

    //makes the bullet move for player 1
    AnimationTimer moveBullet = new AnimationTimer() {
        @Override
        public void handle(long now) {
            bullet.move(amnt);
        }
    };




    //slows down when the mouse is released
    public void handleKeyRelease(KeyCode keyCode) {
        switch (keyCode) {

            case A:
                velocityY *= damping;
                moveXLeft.stop();
                break;
            case D:
                velocityX *= damping;
                moveXRight.stop();
                break;
            default:
                break;
        }
    }

    //makes the player move right
    AnimationTimer moveXRight = new AnimationTimer() {
        @Override
        public void handle(long now) {
            velocityX = speedX;
        }
    };
    //makes the player move left
    AnimationTimer moveXLeft = new AnimationTimer() {
        @Override
        public void handle(long now) {
            velocityX =  -speedX;
        }
    };



    //same as player 1 movement just with different varibales and keys
    public void handleKeyPress2(KeyCode keyCode, Group root) {
        switch (keyCode) {

            case J:
                moveXLeft2.start();
                isFacingRight2 = false;
                break;
            case L:
                moveXRight2.start();
                isFacingRight2 = true;
                break;
            case O:
                //same as player 1
                long currentTime = System.nanoTime();
                if (bullet2 == null || currentTime - lastShootTime > shootCooldown) {
                    if (bullet2 != null) {
                        root.getChildren().remove(bullet2);
                    }

                    bullet2 = new Bullet(getX(), getY(), Color.BLUE, currentTime);
                    root.getChildren().add(bullet2);
                    lastShootTime = currentTime;
                    if (isFacingRight2) {
                        amnt2 = 5;
                    } else {
                        amnt2 = -5;
                    }
                    moveBullet2.start();
                }

                break;
            case U:
                // same as player 1
                long currentTime2 = System.nanoTime();
                if (currentTime2 - lastSuperJumpTime > superJumpCooldown) {
                    velocityY -= speedY;
                    lastSuperJumpTime = currentTime2;
                }
                break;
            default:
                break;
        }
    }



    //slows down when the mouse is released
    public void handleKeyRelease2(KeyCode keyCode) {
        switch (keyCode) {
            case I:
            case K:
            case J:
                moveXLeft2.stop();
                velocityY *= damping;
                break;
            case L:
                moveXRight2.stop();
                velocityX *= damping;
                break;
            default:
                break;
        }
    }
    //same bullet speed, and movement as player 1
    AnimationTimer moveXRight2 = new AnimationTimer() {
        @Override
        public void handle(long now) {
            velocityX = speedX;
        }
    };
    AnimationTimer moveXLeft2 = new AnimationTimer() {
        @Override
        public void handle(long now) {
            velocityX =  -speedX;
        }
    };
    AnimationTimer moveBullet2 = new AnimationTimer() {
        @Override
        public void handle(long now) {
            bullet2.move(amnt2);
        }
    };

    //creates the method for knockback, added it in such a way where the bullet won't knock the player shooting
    //and knocks back the opposing player
    //stops any movement when the bullet touches otherwise the knockback effect is ignored
    public void bulletTouched(Player player, Player player2) {
        if (bullet != null && bullet.getBoundsInParent().intersects(player2.getBoundsInParent())) {
            // Bullet 1 hits player 2
            player2.applyKnockback(knockback2);
            moveXRight2.stop();
            moveXLeft2.stop();

        }

        if (bullet2 != null && bullet2.getBoundsInParent().intersects(player.getBoundsInParent())) {
            // Bullet 2 hits player 1
            player.applyKnockback(knockback);
            moveXRight.stop();
            moveXLeft.stop();
        }
    }


    //knocks the player in whatever direction they are looking at
    private void applyKnockback(double knockbackValue) {
        if (isFacingRight) {
            velocityX = knockbackValue + speedX;
        } else {
            velocityX = -knockbackValue + speedX;
        }

    }

    //ends the game if a player runs out of health, TBA
    public void GameOver(){
        if(Life <= 0){
            System.exit(0);
        }
    }



    public void update(Platform[] platforms) {
        //changes player location
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

        //checks if the player has touched the bottom
        if (velocityX > 0) {
            velocityX -= 0.05;
        } else {
            velocityX += 0.05;
        }
        if(getY() > 590 - getHeight()){
            setX(600);
            setY(200);
            Life--;
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
