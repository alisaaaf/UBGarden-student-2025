package fr.ubx.poo.ubgarden.game.engine;

import fr.ubx.poo.ubgarden.game.*;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.bonus.InsectBomb;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.decor.Hedgehog;
import fr.ubx.poo.ubgarden.game.go.enemy.Enemy;
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
import java.util.Map;

public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final Game game;
    private final Gardener gardener;
    private final List<Sprite> sprites = new LinkedList<>();
    private final Map<Enemy, Sprite> enemySprites = new HashMap<>();
    private final Set<Sprite> cleanUpSprites = new HashSet<>();
    private final Scene scene;
    private StatusBar statusBar;
    private final Pane rootPane = new Pane();
    private final Group root = new Group();
    private final Pane layer = new Pane();
    private Input input;

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

        // Create sprites
        for (var decor : game.world().getGrid().values()) {
            sprites.add(SpriteFactory.create(layer, decor));
            decor.setModified(true);
            var bonus = decor.getBonus();
            if (bonus != null) {
                sprites.add(SpriteFactory.create(layer, bonus));
                bonus.setModified(true);
            }
        }

        // Create sprites for existing enemies
        for (Enemy enemy : game.getEnemies()) {
            Sprite sprite = SpriteFactory.create(layer, enemy);
            enemySprites.put(enemy, sprite);
        }

        sprites.add(new SpriteGardener(layer, gardener));
        resizeScene(sceneWidth, sceneHeight);
    }

    private void updateEnemySprites() {
        // Remove sprites for deleted enemies
        enemySprites.keySet().removeIf(enemy -> {
            if (enemy.isDeleted() || !game.getEnemies().contains(enemy)) {
                Sprite sprite = enemySprites.get(enemy);
                sprite.remove();
                return true;
            }
            return false;
        });

        // Add sprites for new enemies
        for (Enemy enemy : game.getEnemies()) {
            if (!enemySprites.containsKey(enemy)) {
                Sprite sprite = SpriteFactory.create(layer, enemy);
                enemySprites.put(enemy, sprite);
            }
        }
    }

    private void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                checkLevel();
                processInput();
                update(now);
                checkCollision();
                cleanupSprites();
                render();
                statusBar.update(game);
            }
        };
    }

    private void checkLevel() {
        if (game.isSwitchLevelRequested()) {
            game.saveCurrentLevelState();
            int newLevel = game.getSwitchLevel();

            // Charger le nouvel état ou initialiser
            LevelState savedState = game.getLevelState(newLevel);
            if (savedState != null) {
                ((Level)game.world().getGrid(newLevel)).applyState(savedState);
            } else {
                ((Level)game.world().getGrid(newLevel)).initializeNests();
                game.initCarrots();
            }

            game.world().setCurrentLevel(newLevel);
            initialize(); // Réinitialiser l'affichage
            game.clearSwitchLevel();
        }
    }

    private void checkCollision() {
        Position gardenerPos = gardener.getPosition();
        Decor decor = game.world().getGrid().get(gardenerPos);

        if (decor == null) return;

        // Vérifier le hérisson en premier
        if (decor instanceof Hedgehog) {
            decor.pickUpBy(gardener); // Cela doit appeler win() dans Hedgehog
            return;
        }

        // Vérifier les bonus
        if (decor.getBonus() != null) {
            decor.getBonus().pickUpBy(gardener);
            decor.setBonus(null);
            decor.setModified(true);
            // Important: mettre à jour la grille
            game.world().getGrid().put(gardenerPos, decor);
        }

        // Vérifier les ennemis
        for (Enemy enemy : new ArrayList<>(game.getEnemies())) {
            if (enemy.getPosition().equals(gardenerPos)) {
                gardener.hurt(enemy.stingDamage());
                if (enemy.isDeadAfterSting()) {
                    enemy.remove();
                    game.removeEnemy(enemy);
                }
            }
        }
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

        // Update enemies
        for (Enemy enemy : new ArrayList<>(game.getEnemies())) {
            enemy.update(now);
            if (enemy.isDeleted()) {
                game.removeEnemy(enemy);
            }
        }

        // Gestion des collisions
        checkCollision();

        gardener.useBombIfNeeded();
        updateEnemySprites();
        gardener.update(now);

        if (gardener.getEnergy() < 0) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }
    }

    private void showMessage(String msg, Color color) {
        Text message = new Text(msg);
        message.setTextAlignment(TextAlignment.CENTER);
        message.setFont(new Font(60));
        message.setFill(color);

        StackPane pane = new StackPane(message);
        pane.setPrefSize(rootPane.getWidth(), rootPane.getHeight());
        rootPane.getChildren().clear();
        rootPane.getChildren().add(pane);

        new AnimationTimer() {
            public void handle(long now) {
                processInput();
            }
        }.start();
    }

    public void cleanupSprites() {
        sprites.forEach(sprite -> {
            if (sprite.getGameObject().isDeleted()) {
                cleanUpSprites.add(sprite);
            }
        });
        cleanUpSprites.forEach(Sprite::remove);
        sprites.removeAll(cleanUpSprites);
        cleanUpSprites.clear();
    }

    private void render() {
        sprites.forEach(Sprite::render);
        enemySprites.values().forEach(Sprite::render);
        game.world().getGrid().values().forEach(decor -> {
            if (decor.isModified() && decor.getBonus() != null) {
                Sprite sprite = SpriteFactory.create(layer, decor.getBonus());
                sprites.add(sprite);
                decor.setModified(false);
            }
        });
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

