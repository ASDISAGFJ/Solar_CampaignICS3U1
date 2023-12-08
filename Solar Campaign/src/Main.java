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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;


public class Main extends Application {

    private GameMenu gameMenu;

    @Override
    public void start(Stage Mainstage){
        Group root = new Group();
        Scene scene = new Scene(root, 1200, 600);



        gameMenu = new GameMenu();

        // this will be the Image for the menu, and the ImageView will change if the user is in the menu or not
        Image TitleScreen = new Image("Images/MenuBackground.gif");
        ImageView background = new ImageView(TitleScreen);
        background.setX(0); background.setY(0);
        background.setFitWidth(1200); background.setFitHeight(600);

        root.getChildren().addAll(background, gameMenu);


        Mainstage.setTitle("Solar Campaign");
        Mainstage.setScene(scene);
        Mainstage.setResizable(false);
        Mainstage.show();


    }
    public static class GameMenu extends Parent {

        public GameMenu() {
            VBox menu0 = new VBox();
            VBox menu1 = new VBox();


            menu0.setTranslateX(40);
            menu0.setTranslateY(280);
            menu1.setTranslateX(280);
            menu1.setTranslateY(40);


            final int offset = 400;
            menu1.setTranslateX(offset);



            MenuButton btnResume = new MenuButton("START");
            btnResume.setOnMouseClicked(event ->
            {
                FadeTransition ft = new FadeTransition(Duration.seconds(0.5), this);
                ft.setFromValue(1);
                ft.setToValue(0);
                ft.setOnFinished(evt -> setVisible(false));
                ft.play();


            });

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
            btnOptions.setTranslateX(390);
            btnOptions.setTranslateY(-100);

            MenuButton btnExit = new MenuButton("EXIT");
            btnExit.setOnMouseClicked(event ->
                    System.exit(0));

            btnExit.setTranslateX(770);
            btnExit.setTranslateY(-200);

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
            btnBack.setTranslateY(240);

            MenuButton btnScaling = new MenuButton("SCALING");
            btnScaling.setTranslateY(140);
            btnScaling.setTranslateX(390);

            MenuButton btnSpecials = new MenuButton("SPECIALS");
            btnSpecials.setTranslateX(770);
            btnSpecials.setTranslateY(40);



            menu0.getChildren().addAll(btnResume, btnOptions, btnExit);
            menu1.getChildren().addAll(btnBack, btnScaling, btnSpecials);


            Rectangle bg = new Rectangle(1200, 900);
            bg.setFill(Color.PURPLE);
            bg.setOpacity(0.3);
            getChildren().addAll(bg, menu0);


        }
    }
    private static class MenuButton extends StackPane {
        private final Text text;

        public MenuButton(String name) {
            text = new Text(name);
            text.setFont(text.getFont().font(60));
            Rectangle bg = new Rectangle(300, 100);
            bg.setOpacity(0.6);
            bg.setFill(Color.PURPLE);


            bg.setEffect(new GaussianBlur(3.5));
            text.setFill(Color.WHITE);
            setRotate(-0.5);
            this.setAlignment(Pos.TOP_LEFT);
            getChildren().addAll(bg, text);


            setOnMouseEntered(event ->
            {
                bg.setTranslateX(10);
                text.setTranslateX(10);
                text.setFill(Color.PURPLE);
                bg.setFill(Color.WHITE);
            });


            setOnMouseExited(event ->
            {
                bg.setTranslateX(0);
                text.setTranslateX(0);
                bg.setFill(Color.PURPLE);
                text.setFill(Color.WHITE);
            });


            DropShadow drop = new DropShadow(50, Color.WHITE);
            drop.setInput(new Glow());


            setOnMousePressed(event -> setEffect(drop));
            setOnMouseReleased(event -> setEffect(null));
        }

    }

}