import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Platform extends Rectangle {

    public Platform(double x, double y, double width, double height) {
        super(x, y, width, height);
        setFill(Color.GREY); // You can customize the color
    }
    public void setPlatformVisible(boolean visible) {
        this.setVisible(visible);
    }
}
