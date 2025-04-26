package fr.ubx.poo.ubgarden.game.engine;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;
import fr.ubx.poo.ubgarden.game.view.ImageResource;
import fr.ubx.poo.ubgarden.game.view.Sprite;
import fr.ubx.poo.ubgarden.game.view.SpriteFactory;
import fr.ubx.poo.ubgarden.game.view.SpriteGardener;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.*;

public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final Game game;
    private final Gardener gardener;
    private final List<Sprite> sprites = new LinkedList<>();
    private final Set<Sprite> cleanUpSprites = new HashSet<>();
    private final Scene scene;
    private final Pane rootPane = new Pane();
    private final Group root = new Group();
    private final Pane layer = new Pane();
    private Input input;
    private StatusBar statusBar;

    public GameEngine(Game game, Scene scene) {
        this.game = game;
        this.scene = scene;
        this.gardener = game.getGardener();
        initialize();
        buildAndSetGameLoop();
    }

    public Pane getRoot() {
        return rootPane;
    }

    private void initialize() {
        int height = game.world().getGrid().height();
        int width = game.world().getGrid().width();
        int sceneWidth = width * ImageResource.size;
        int sceneHeight = height * ImageResource.size;
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm());
        input = new Input(scene);

        root.getChildren().clear();
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight);

        rootPane.getChildren().clear();
        rootPane.setPrefSize(sceneWidth, sceneHeight + StatusBar.height);
        rootPane.getChildren().add(root);

        sprites.clear();
        for (var decor : game.world().getGrid().values()) {
            sprites.add(SpriteFactory.create(layer, decor));
            if (decor.getBonus() != null) {
                sprites.add(SpriteFactory.create(layer, decor.getBonus())); // <-- ВАЖНО: добавляем бонусы отдельно
            }
        }
        sprites.add(new SpriteGardener(layer, gardener));
        resizeScene(sceneWidth, sceneHeight);
    }


    private void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                checkLevel();
                processInput();
                update(now);
                cleanupSprites();
                render();
                statusBar.update(game);
            }
        };
    }

    private void checkLevel() {
        // Not used yet
    }

    private void processInput() {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        } else if (input.isMoveDown()) {
            gardener.requestMove(Direction.DOWN);
        } else if (input.isMoveLeft()) {
            gardener.requestMove(Direction.LEFT);
        } else if (input.isMoveRight()) {
            gardener.requestMove(Direction.RIGHT);
        } else if (input.isMoveUp()) {
            gardener.requestMove(Direction.UP);
        }
        input.clear();
    }

    private void update(long now) {
        game.world().getGrid().values().forEach(decor -> decor.update(now));
        gardener.update(now);
    }

    private void cleanupSprites() {
        Iterator<Sprite> iterator = sprites.iterator();
        while (iterator.hasNext()) {
            Sprite sprite = iterator.next();
            if (sprite.getGameObject().isDeleted()) {
                sprite.remove();
                iterator.remove();
            }
        }
    }

    private void render() {
        sprites.forEach(Sprite::render);
    }

    public void start() {
        gameLoop.start();
    }

    private void resizeScene(int width, int height) {
        rootPane.setPrefSize(width, height + StatusBar.height);
        layer.setPrefSize(width, height);
        Platform.runLater(() -> scene.getWindow().sizeToScene());
    }
}
