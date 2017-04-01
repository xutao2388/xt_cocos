package com.ly.cocos2d2.flappybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import com.ly.cocos2d2.flappybird.game.Assets;

public class WelcomeScreen implements Screen {

    private Stage stage;
    private FlappyBirdGame game;

    public WelcomeScreen(FlappyBirdGame game) {
        this.game = game;
    }

    /**
     * 当该场景被设置到 Game 中成为 Game 的当前场景时被调用
     */
    @Override
    public void show() {
        //纹理
        Image background = new Image(new Texture(Gdx.files.internal("images/splash.png")));//texture：图片从原始格式解码并上传到GPU  就被称为纹理。

//        然后来说一下为什么要将图片放在assets文件夹中。 Gdx.files是libgdx的文件模块，主要提供以下5大功能。
//        1、读取文件  2、写文件  3、复制文件4、移动文件 5、列出文件和目录

//        而获取操作文件的FileHandle有4种方法。
//        1、Classpath 路径相对于classpath，文件通常为只读。
//        2、Internal 内部文件路径相对于程序根目录或者android 的assets文件夹。
//        3、External 外部文件路径是相对于SD卡根目录。
//        4、Absolute assets文件夹本身就是存储资源的文件夹，而且相比resource文件夹，它其中的资源不会生成R中的ID，用来放图片很是合适。
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Action fadeIn = Actions.fadeIn(1);
        background.addAction(fadeIn);

        //Stage==舞台，和Actor演员一个级别==类是一个集合，其中包括一个camera，一个SpriteBatch，一个root Group，可以处理子对象的绘制和输入事件的分发;
        stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        stage.addActor(background);

        // 预加载资源
        Assets.instance.init(new AssetManager());
    }

    /**
     * 当该场景需要被渲染时被调用
     */
    @Override
    public void render(float delta) {
        update();
        stage.act();
        stage.draw();
    }

    private void update() {
        if (Assets.instance.isLoaded())
            game.setScreen(new PreviewScreen(game));
    }

    /**
     * 当有另一个场景被设置为 Game 的当前场景时（即该场景被覆盖/移出当前场景）被调用
     */
    @Override
    public void hide() {
        stage.dispose();
    }


    @Override
    public void resize(int width, int height) {

    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    /**
     * 当场景需要被释放所有资源时调用,
     * 注意: 该方法不会自动被调用, 在需要释放场景的所有资源时手动进行调用
     */
    @Override
    public void dispose() {

    }

}
