package com.ly.cocos2d2.flappybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import com.ly.cocos2d2.flappybird.FlappyBirdScreen;
import com.ly.cocos2d2.flappybird.game.UI.GameDialog;
import com.ly.cocos2d2.flappybird.game.object.AbstractGameObject;
import com.ly.cocos2d2.flappybird.game.object.Bird;
import com.ly.cocos2d2.flappybird.game.object.Land;
import com.ly.cocos2d2.flappybird.game.object.Pipes;
import com.ly.cocos2d2.flappybird.game.object.Pipes.Pipe;
import com.ly.cocos2d2.flappybird.util.Constants;

/*控制类*/
public class WorldController extends InputAdapter implements Disposable {
    private static final String TAG = WorldController.class.getName();

    public Bird bird;
    public Land land;
    public int score;
    public Pipes pipes;
    public World world;
    public boolean isStart;
    public Preferences prefs;
    public GameDialog dialog;
    public boolean isGameOver;
    public FlappyBirdScreen screen;
    public AtlasRegion background;

    public WorldController(FlappyBirdScreen screen) {
        this.screen = screen;
        init();
    }

    private void init() {
        background = Assets.instance.decoration.bg.random();

        score = 0;
        isStart = false;
        isGameOver = false;

        initWorld();
        initBird();
        initLand();
        initPipes();
        initDialog();
        InputMultiplexer multiplexer = new InputMultiplexer(dialog, this);
        Gdx.input.setInputProcessor(multiplexer);
        prefs = Gdx.app.getPreferences(Constants.BEST_SCORE_FILE);
    }

    private void initWorld() {
        if (world != null) world.dispose();
        world = new World(new Vector2(0, -110.8f), false);

        // 为world添加碰撞检测监听器
        world.setContactListener(new BirdContactListener());
    }

    private void initBird() {
        if (bird == null) {
            bird = new Bird(-4.5f, 1.3f);
        } else {
            bird.init((int) (Math.random() * 3), -4.5f, 1.3f);
        }
    }

    private void initLand() {
        if (land == null) {
            land = new Land();
        } else {
            land.init();
        }
    }

    private void initPipes() {
        if (pipes == null) {
            pipes = new Pipes(world);
        } else {
            pipes.init(world);
        }
    }

    private void initDialog() {
        if (dialog == null)
            dialog = new GameDialog(this);
        dialog.startView();
    }

    // 计算分数
    private void calculateScore() {
        for (Pipe pipe : pipes.pipes) {
            if (pipe.position.x < bird.position.x) {
                if (pipe.getScore() == 1) {
                    score += 1;
                    Assets.instance.sounds.point.play();
                    Gdx.app.debug(TAG, "Your current score:" + score);
                }
            }
        }
    }

    private void saveBestScore() {
        int bestScore = prefs.getInteger(Constants.BEST_SCORE_KEY);
        if (bestScore < score) {
            prefs.putInteger(Constants.BEST_SCORE_KEY, score);
            prefs.flush();
        }
    }

    public void pauseOrResume() {
        screen.paused = !screen.paused;
    }

    public void restart() {
        if (isGameOver)
            init();
    }

    public void update(float deltaTime) {
        if (!isGameOver) {
            if (isGameOver = bird.isGameOver()) {//先赋值，再判断是否为true；
                saveBestScore();
                dialog.endView();
                Gdx.app.debug(TAG, "GAME OVER!");
            }
        }

        if (bird.isContact()) return;

        // 如果游戏开始则开始模拟
        if (isStart) {
            world.step(deltaTime, 8, 3);
        }

        bird.update(deltaTime);
        land.update(deltaTime);
        pipes.update(deltaTime);

        calculateScore();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Buttons.LEFT) {
            if (!isStart) {
                isStart = true;
                dialog.gameView();
                bird.beginToSimulate(world);
                land.beginToSimulate(world);
            }
            bird.setJumping();
        }
        return true;
    }

    @Override
    public void dispose() {
        world.dispose();
        dialog.dispose();
    }

    private void collisionDetection(AbstractGameObject a, AbstractGameObject b) {
        if (a instanceof Bird) {
            ((Bird) a).contact();
        } else if (b instanceof Bird) {
            ((Bird) b).contact();                            // if game over
        }
        Gdx.app.debug(TAG, "Player Character Contected!");
    }

    // world contact listener
    private class BirdContactListener implements ContactListener {

        @Override
        public void beginContact(Contact arg0) {
            AbstractGameObject a = (AbstractGameObject) arg0.getFixtureA().getBody().getUserData();
            AbstractGameObject b = (AbstractGameObject) arg0.getFixtureB().getBody().getUserData();
            collisionDetection(a, b);
        }

        @Override
        public void endContact(Contact arg0) {
        }

        @Override
        public void postSolve(Contact arg0, ContactImpulse arg1) {
        }

        @Override
        public void preSolve(Contact arg0, Manifold arg1) {
        }
    }
}