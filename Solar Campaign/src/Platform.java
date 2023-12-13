import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Platform extends Rectangle {
    public Platform(double x, double y, double width, double height, Image image) {
        super(x, y, width, height);
        ImageView objectView = new ImageView(image);
    }

    public void setImage(Image image) {
        setFill(new ImagePattern(image));
    }
}
