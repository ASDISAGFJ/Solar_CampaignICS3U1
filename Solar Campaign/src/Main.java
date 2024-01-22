import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;



public class Main extends Application {

    private GameMenu gameMenu; //sets up the gameMenu for the main class

    private Platform[] platforms;

    // Added player instances
    private Player player;
    private Player player2;

    private int Level = 1;



    @Override
    public void start(Stage Mainstage){
        Group root = new Group();
        Scene scene = new Scene(root, 1200, 600);

        player = new Player();
        player2 = new Player();

        player.setVisible(false);
        player.stop();

        player2.setVisible(false);
        player2.stop();



        gameMenu = new GameMenu();
        player2.setFill(Color.RED);



        // this will be the Image for the menu, and the ImageView will change if the user is in the menu or not
        Image TitleScreen = new Image("Images/MenuBackground.gif");
        ImageView background = new ImageView(TitleScreen);
        background.setX(0); background.setY(0);
        background.setFitWidth(1200); background.setFitHeight(600);

        Image Level1 = new Image("Images/amazon.jpg");// the background for the first level
        Image Level2 = new Image("Images/amazon.jpg");// the background for the second level
        Image Level3 = new Image("Images/amazon.jpg");// the background for the third level
        Image Level4 = new Image("Images/amazon.jpg");// the background for the fourth level

        

        //adds the player and the background, the player is added later to overlap the background
        root.getChildren().addAll(background, gameMenu);

        //changes the background, the player.setVisible makes the player overlap with the level
        AnimationTimer setBackground = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameMenu.isVisible()){
                    Switch(Level){
                        case 1: 
                            background.setImage(level1);
                            break;
                        case 2:
                            background.setImage(level2);
                            break;
                        case 3:
                            background.setImage(level3);
                            break;
                        case 4:
                            background.setImage(level4);
                            break;
                        default:
                            throw new Exception("This ain't a level");
                            break;
                    }
                    player.setTranslateY(-800);
                    player2.setTranslateY(800);
            
                    player.setVisible(true);
                    player2.setVisible(true);
                    for (Platform platform : platforms) {
                        platform.setPlatformVisible(true);
                    }

                }else {
                    player.setTranslateY(800);
                    player2.setTranslateY(-800);
                    setBackground.stop();
                    background.setImage(TitleScreen);
                    player.setVisible(false);
                    player2.setVisible(false);
                    for (Platform platform : platforms) {
                        platform.setPlatformVisible(false);
                    }

                }
            }
        };
        platforms = new Platform[5];

        for (Platform platform : platforms) {
            System.out.println("X: " + platform.getX() + ", Y: " + platform.getY());
        }
       root.getChildren().addAll(platforms);



        setBackground.start();


        //sets up the key inputs, which includes the player movement, and the menu button
        scene.setOnKeyPressed(keyEvent -> {
            KeyCode keyCode = keyEvent.getCode();

            // Player 1 controls
            player.handleKeyPress(keyCode, root);

            // Player 2 controls
            player2.handleKeyPress2(keyCode, root);


            //Menu button
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                if (!gameMenu.isVisible()) {
                    //creates a fade transition and makes the players movement and visibily to none
                    FadeTransition ft = new FadeTransition(Duration.seconds(0.5), gameMenu);

                    ft.setFromValue(0);
                    ft.setToValue(1);
                    player.stop();
                    player.setVisible(false);
                    player2.stop();
                    player2.setVisible(false);
                    gameMenu.setVisible(true);

                    ft.play();
                } else {
                    FadeTransition ft = new FadeTransition(Duration.seconds(0.5), gameMenu);
                    ft.setFromValue(0);
                    ft.setToValue(1);
                    player.start();
                    player2.start();
                    gameMenu.setVisible(false);
                    player.setVisible(true);
                    player2.setVisible(true);
                    ft.play();
                }
            }
        });
        scene.setOnKeyReleased(KeyEvent -> {
            KeyCode keyCode = KeyEvent.getCode();
            player.handleKeyRelease(keyCode);
            player2.handleKeyRelease2(keyCode);
        });





        //updates the player location
        AnimationTimer PlayerTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                player.update(platforms);
                player2.update(platforms);
                player.bulletTouched(player, player2);
                player2.bulletTouched(player, player2);
                player.GameOver();
                player2.GameOver();
            }
        };
        PlayerTimer.start();



        root.getChildren().addAll(player, player2);

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
            HBox map = new HBox(20);



            //the default location for the first object
            menu0.setTranslateX(40);
            menu0.setTranslateY(100);
            menu1.setTranslateX(40);
            menu1.setTranslateY(200);
            map.setTranslateX(40);
            map.setTranslateY(300);



            //offseet for different menus
            final int offset = 800;
            menu1.setTranslateX(offset);
            map.setTranslateX(offset);



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
            MenuButton btnMap = new MenuButton("MAPS");
            btnMap.setOnMouseClicked(event -> {
                getChildren().addAll(map);
                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), map);
                tt.setToX(menu1.getTranslateX() - offset);
                TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), map);
                tt1.setToX(menu1.getTranslateX());
                tt.play();
                tt1.play();


                tt.setOnFinished(evt -> getChildren().remove(menu1));
                    }

            );
            MenuButton btnBack2 = new MenuButton("BACK");
            btnBack2.setOnMouseClicked(event -> {
                getChildren().add(menu1);
                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), map);
                tt.setToX(map.getTranslateX() + offset);

                TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu1);
                tt1.setToX(map.getTranslateX());

                tt.play();
                tt1.play();

                tt.setOnFinished(evt -> getChildren().remove(map));
            });



            //TBA
            MenuButton btnSpecials = new MenuButton("N/A");





            //adds the buttons to their percpective menu
            menu0.getChildren().addAll(btnResume, btnOptions, btnExit);
            menu1.getChildren().addAll(btnBack, btnMap, btnSpecials);
            map.getChildren().addAll(btnBack2);



            //background hue
            Rectangle bg = new Rectangle(1200, 900);
            bg.setFill(Color.PURPLE);
            bg.setOpacity(0.3);
            getChildren().addAll(bg, menu0);


        }
    }

    // Method to create an image button
    private static StackPane createMapButton(String imageUrl) {
        Image image = new Image(imageUrl);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);

        Rectangle bg = new Rectangle(300, 100);
        bg.setOpacity(0.6);
        bg.setFill(Color.PURPLE);
        bg.setEffect(new GaussianBlur(3.5));

        DropShadow drop = new DropShadow(50, Color.WHITE);
        drop.setInput(new Glow());

        imageView.setEffect(null);
        bg.setFill(Color.PURPLE);

        imageView.setOnMouseEntered(event -> {
            bg.setTranslateX(10);
            imageView.setTranslateX(10);
            imageView.setEffect(drop);
            bg.setFill(Color.WHITE);
        });

        imageView.setOnMouseExited(event -> {
            bg.setTranslateX(0);
            imageView.setTranslateX(0);
            imageView.setEffect(null);
            bg.setFill(Color.PURPLE);
        });

        imageView.setOnMousePressed(event -> imageView.setEffect(drop));
        imageView.setOnMouseReleased(event -> imageView.setEffect(null));

        StackPane buttonPane = new StackPane(bg, imageView);

        return buttonPane;
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
