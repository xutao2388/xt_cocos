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
	
	@Override
	public void show() {
		Image background = new Image(new Texture(Gdx.files.internal("images/splash.png")));
		background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		Action fadeIn = Actions.fadeIn(1);
		background.addAction(fadeIn);
		
		stage = new Stage(new StretchViewport(
							Gdx.graphics.getWidth(), 
							Gdx.graphics.getHeight()));
		stage.addActor(background);
		
		// Ԥ������Դ
		Assets.instance.init(new AssetManager());
	}

	@Override
	public void hide() {
		stage.dispose();
	}
	
	private void update() {
		if(Assets.instance.isLoaded()) 
			game.setScreen(new PreviewScreen(game));
	}
	
	@Override
	public void render(float delta) {
		update();
		stage.act();
		stage.draw();
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

	@Override
	public void dispose() {
		
	}

}
