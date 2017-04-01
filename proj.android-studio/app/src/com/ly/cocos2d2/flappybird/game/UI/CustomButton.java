package com.ly.cocos2d2.flappybird.game.UI;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import com.ly.cocos2d2.flappybird.game.Assets;

public class CustomButton extends Button {
    AtlasRegion region;

    public CustomButton(AtlasRegion reg) {
        super(new TextureRegionDrawable(reg));
        this.region = reg;
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.instance.sounds.swooshing.play();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isPressed()) {
            batch.draw(region, getX(), getY() - 2f, getWidth(), getHeight());
        } else {
            super.draw(batch, parentAlpha);
        }
    }
}
