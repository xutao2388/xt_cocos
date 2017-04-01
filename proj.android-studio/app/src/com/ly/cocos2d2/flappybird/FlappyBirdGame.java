package com.ly.cocos2d2.flappybird;

import com.badlogic.gdx.Game;

import com.ly.cocos2d2.flappybird.game.Assets;

public class FlappyBirdGame extends Game {

    @Override
    public void create() {
        setScreen(new WelcomeScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        Assets.instance.dispose();
    }
}
