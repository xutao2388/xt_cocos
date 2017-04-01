package com.ly.cocos2d2.flappybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import com.ly.cocos2d2.flappybird.game.Assets;
import com.ly.cocos2d2.flappybird.game.UI.CustomButton;
import com.ly.cocos2d2.flappybird.game.object.Bird;
import com.ly.cocos2d2.flappybird.game.object.Land;
import com.ly.cocos2d2.flappybird.util.Constants;

public class PreviewScreen implements Screen {
    private Bird bird;                        // bird object
    private Land land;                        // land object
    private Stage stage;                    // stage
    private Image title;                    // text title image
    private Image copyRight;                // text copyright image
    private float viewWidth;                // last viewport width
    private SpriteBatch batch;                // sprite render batch
    private FlappyBirdGame game;            // main class
    private AtlasRegion background;            // random background texture region
    private OrthographicCamera camera;        // 正交投影相机
    private CustomButton play, score, rate;    // three buttons

    public PreviewScreen(FlappyBirdGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();//用来绘制2D的带有纹理或纹理区域的矩形的。 他可以把绘制的一些命令捆绑起来，然后一起发给GPU来处理。
        viewWidth = Constants.VIEWPORT_HEIGHT * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();

        // bird and land
        bird = new Bird(0, 2);
        land = new Land();

        // camera
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);//正交相机的操作非常简单，就像我们在现实世界中操作相机一样，
        // 相机的移动和旋转;相机的放到和缩小;改变相机的视窗大小;在窗口（widow）坐标系和（世界）坐标系之间切换点的位置；http://blog.csdn.net/zqiang_55/article/details/52155274
        camera.position.set(0, 0, 0);
        camera.update();

        // stage==舞台
        stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));

        // background texture region
        background = Assets.instance.decoration.bg.random();

        // title image
        title = new Image(Assets.instance.assetUI.textTitle);
        title.setBounds(93, 553, 295, 90);//x:组件在容器X轴上的起点 ;y:组件在容器Y轴上的起点 ;width:组件的长度 height:组件的高度

        // copyright image
        copyRight = new Image(Assets.instance.assetUI.copyRight);
        copyRight.setBounds(137, 110, 206, 21);

        // three buttons
        play = new CustomButton(Assets.instance.assetUI.buttonPlay);
        play.setBounds(43, 175, 173, 108);

        score = new CustomButton(Assets.instance.assetUI.buttonScore);
        score.setBounds(263, 175, 173, 108);

        rate = new CustomButton(Assets.instance.assetUI.buttonRate);
        rate.setBounds(189, 313, 102, 65);

        stage.addActor(title);
        stage.addActor(copyRight);
        stage.addActor(play);
        stage.addActor(score);
        stage.addActor(rate);

        Gdx.input.setInputProcessor(stage);//input handler

        // touch up event (touchDown must be return true)
        play.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new FlappyBirdScreen());
            }
        });
    }

    @Override
    public void hide() {
        batch.dispose();
        stage.dispose();
    }

    private void update(float deltaTime) {
        bird.update(deltaTime);            // wave animation
        land.update(deltaTime);            // move animation
    }

    @Override
    public void render(float delta) {
        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);//清屏
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(background, -viewWidth / 2, -Constants.VIEWPORT_HEIGHT / 2, viewWidth, Constants.VIEWPORT_HEIGHT);
        bird.render(batch);
        land.render(batch);

        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = Constants.VIEWPORT_HEIGHT * width / height;
        camera.update();
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
    }
}