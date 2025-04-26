package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Sprite {

    public static final int size = 40;
    private final Pane layer;
    private final GameObject gameObject;
    private ImageView imageView;
    private Image image;
    private Effect effect;

    public Sprite(Pane layer, Image image, GameObject gameObject) {
        this.layer = layer;
        this.image = image;
        this.gameObject = gameObject;
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(size);
        this.imageView.setFitHeight(size);
        this.imageView.setX(getPosition().x() * size);
        this.imageView.setY(getPosition().y() * size);
        layer.getChildren().add(imageView);
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Position getPosition() {
        return getGameObject().getPosition();
    }

    public void setImage(Image image) {
        this.image = image;
        if (imageView != null) {
            imageView.setImage(image);
        }
    }

    public void remove() {
        if (imageView != null) {
            layer.getChildren().remove(imageView);
            imageView = null;
        }
    }

    public void render() {
        if (gameObject.isModified()) {
            remove();
            this.imageView = new ImageView(this.image);
            this.imageView.setFitWidth(size);
            this.imageView.setFitHeight(size);
            this.imageView.setX(getPosition().x() * size);
            this.imageView.setY(getPosition().y() * size);
            layer.getChildren().add(imageView);
            gameObject.setModified(false);
        }
    }
}
