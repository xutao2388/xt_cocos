package com.ly.cocos2d2.flappybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import com.ly.cocos2d2.flappybird.game.WorldController;
import com.ly.cocos2d2.flappybird.game.WorldRenderer;

/*游戏界面，由WorldController 来控制*/
public class FlappyBirdScreen implements Screen {

    private WorldController worldController;
    private WorldRenderer worldRenderer;
    public boolean paused;

    @Override
    public void show() {
        paused = false;
        worldController = new WorldController(this);
        worldRenderer = new WorldRenderer(worldController);
    }

    @Override
    public void hide() {
        worldController.dispose();
        worldRenderer.dispose();
    }

    @Override
    public void render(float delta) {
        if (!paused) {
            float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 0.018f);
            worldController.update(deltaTime);
        }

        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        worldRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        paused = false;
    }

    @Override
    public void dispose() {
    }
}
