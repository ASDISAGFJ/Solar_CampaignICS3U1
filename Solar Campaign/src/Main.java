import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;


public class Main extends Application {

    private GameMenu gameMenu; //sets up the gameMenu for the main class

    @Override
    public void start(Stage Mainstage){
        Group root = new Group();
        Scene scene = new Scene(root, 1200, 600);

        Player  player= new Player();//sets up the player to be used
        player.setVisible(false);
        player.stop();




        gameMenu = new GameMenu();

        // this will be the Image for the menu, and the ImageView will change if the user is in the menu or not
        Image TitleScreen = new Image("Images/MenuBackground.gif");
        ImageView background = new ImageView(TitleScreen);
        background.setX(0); background.setY(0);
        background.setFitWidth(1200); background.setFitHeight(600);

        Image Level1 = new Image("Images/Level1.jpg");// the background for the first level

        //adds the player and the background, the player is added later to overlap the background
        root.getChildren().addAll(background, gameMenu);

        //changes the background, the player.setVisible makes the player overlap with the level
        AnimationTimer setBackground = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameMenu.isVisible()){
                    background.setImage(Level1);
                    player.setVisible(true);

                }else {
                    background.setImage(TitleScreen);
                    player.setVisible(false);

                }
            }
        };
        setBackground.start();

        //sets up the key inputs, which includes the player movement, and the menu button
        scene.setOnKeyPressed(keyEvent -> {
            KeyCode keyCode = keyEvent.getCode();

            //player movement
            player.handleKeyPress(keyCode);
            player.handleKeyRelease(keyCode);

            //Menu button
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                if (!gameMenu.isVisible()) {
                    //creates a fade transition and makes the players movement and visibily to none
                    FadeTransition ft = new FadeTransition(Duration.seconds(0.5), gameMenu);
                    ft.setFromValue(0);
                    ft.setToValue(1);
                    player.stop();
                    player.setVisible(false);
                    gameMenu.setVisible(true);

                    ft.play();
                } else {
                    FadeTransition ft = new FadeTransition(Duration.seconds(0.5), gameMenu);
                    ft.setFromValue(0);
                    ft.setToValue(1);
                    player.start();
                    gameMenu.setVisible(false);
                    player.setVisible(true);
                    ft.play();
                }
            }
        });


        //updates the player location
        AnimationTimer PlayerTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                player.update();
            }
        };
        PlayerTimer.start();

        root.getChildren().add(player);

        Mainstage.setTitle("Solar Campaign");
        Mainstage.setScene(scene);
        Mainstage.setResizable(false);
        Mainstage.show();


    }
    public static class GameMenu extends Parent {

        public GameMenu() {
            //resizes every new objects horizontally
            HBox menu0 = new HBox(100);
            HBox menu1 = new HBox(100);



            //the default location for the first object
            menu0.setTranslateX(40);
            menu0.setTranslateY(280);
            menu1.setTranslateX(40);
            menu1.setTranslateY(280);


            //offseet for different menus
            final int offset = 800;
            menu1.setTranslateX(offset);



            //the start button, sets the menu to be invisible
            MenuButton btnResume = new MenuButton("START");
            btnResume.setOnMouseClicked(event ->
            {
                FadeTransition ft = new FadeTransition(Duration.seconds(0.5), this);
                ft.setFromValue(1);
                ft.setToValue(0);
                ft.setOnFinished(evt -> setVisible(false));
                ft.play();


            });

            //changes to menu1
            MenuButton btnOptions = new MenuButton("OPTIONS");
            btnOptions.setOnMouseClicked(event ->
            {
                getChildren().addAll(menu1);
                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menu0);
                tt.setToX(menu0.getTranslateX() - offset);
                TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu1);
                tt1.setToX(menu0.getTranslateX());
                tt.play();
                tt1.play();


                tt.setOnFinished(evt -> getChildren().remove(menu0));
            });

            //exits the program
            MenuButton btnExit = new MenuButton("EXIT");
            btnExit.setOnMouseClicked(event ->
                    System.exit(0));


            //changes to menu0
            MenuButton btnBack = new MenuButton("BACK");
            btnBack.setOnMouseClicked(event ->
            {
                getChildren().add(menu0);
                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menu1);
                tt.setToX(menu1.getTranslateX() + offset);


                TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu0);
                tt1.setToX(menu1.getTranslateX());


                tt.play();
                tt1.play();


                tt.setOnFinished(evt -> getChildren().remove(menu1));


            });


            //TBA
            MenuButton btnScaling = new MenuButton("SCALING");


            //TBA
            MenuButton btnSpecials = new MenuButton("SPECIALS");




            //adds the buttons to their percpective menu
            menu0.getChildren().addAll(btnResume, btnOptions, btnExit);
            menu1.getChildren().addAll(btnBack, btnScaling, btnSpecials);


            //background hue
            Rectangle bg = new Rectangle(1200, 900);
            bg.setFill(Color.PURPLE);
            bg.setOpacity(0.3);
            getChildren().addAll(bg, menu0);


        }
    }
    //the buttons
    private static class MenuButton extends StackPane {

        private final Text text;


        public MenuButton(String name) {
            //sets the button name as the perimeter, sets the font, and adds the rectangle for the button
            text = new Text(name);
            text.setFont(text.getFont().font(60));
            Rectangle bg = new Rectangle(300, 100);
            bg.setOpacity(0.6);
            bg.setFill(Color.PURPLE);


            //creates a blur effect, makes the text colour white, slight rotate, and adds the text
            bg.setEffect(new GaussianBlur(3.5));
            text.setFill(Color.WHITE);
            setRotate(-0.5);
            this.setAlignment(Pos.TOP_LEFT);
            getChildren().addAll(bg, text);


            //changes the button when mouse enters
            setOnMouseEntered(event ->
            {
                bg.setTranslateX(10);
                text.setTranslateX(10);
                text.setFill(Color.PURPLE);
                bg.setFill(Color.WHITE);
            });


            //changes back after exiting
            setOnMouseExited(event ->
            {
                bg.setTranslateX(0);
                text.setTranslateX(0);
                bg.setFill(Color.PURPLE);
                text.setFill(Color.WHITE);
            });

            //create a glow effect for the buttons
            DropShadow drop = new DropShadow(50, Color.WHITE);
            drop.setInput(new Glow());

            //makes it so that the glow only happens when the mouse is on the button
            setOnMousePressed(event -> setEffect(drop));
            setOnMouseReleased(event -> setEffect(null));
        }

    }

}
