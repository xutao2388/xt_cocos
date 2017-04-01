package com.ly.cocos2d2.flappybird.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;

import com.ly.cocos2d2.flappybird.util.Constants;
import com.ly.cocos2d2.flappybird.util.Tools;

public class WorldRenderer implements Disposable {  
	
	private SpriteBatch batch;  
    private OrthographicCamera camera;  
    private WorldController worldController;  
    
    public WorldRenderer(WorldController worldController) { 
    	this.worldController = worldController;
    	init();
    }  

    private void init() {
    	batch = new SpriteBatch();
    	
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
    }  
      
    public void render() { 
      renderWorld(batch);
      renderGui(batch);
    }  
    
    // ��Ϸ����
    private void renderWorld (SpriteBatch batch) { 
    	batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(worldController.background,
				-camera.viewportWidth / 2, -camera.viewportHeight / 2, 
				camera.viewportWidth, camera.viewportHeight);
		worldController.pipes.render(batch);
		worldController.land.render(batch);
		worldController.bird.render(batch);
		batch.end();
    }
    
    // ��ϷGUI��Ⱦ����
 	private void renderGui(SpriteBatch batch) {
 		if(!worldController.isGameOver) {
	 		batch.setProjectionMatrix(camera.combined);
	 		batch.begin();
	 		renderScore(batch);
	 		batch.end();
 		}
		worldController.dialog.act();
		worldController.dialog.draw();
 	}
 	
 	// ����
 	private void renderScore(SpriteBatch batch) {
 		int[] score = Tools.splitInteger(worldController.score);
 		float h = 4.5f;
 		float w = 0, totalWidth = 0;
 		for(int i = 0; i < score.length; i++) {
 			AtlasRegion reg = Assets.instance.number.numbers_font.get(score[i]);
 			w = h * reg.getRegionWidth() / reg.getRegionHeight();			
 			totalWidth += w;
 		}
 		
 		float x = -totalWidth / 2;
 		float y = Constants.VIEWPORT_HEIGHT / 2 * 0.6f;
 		w = 0;
 		for(int i = 0; i < score.length; i++) {
 			AtlasRegion reg = Assets.instance.number.numbers_font.get(score[i]);
 			w = h * reg.getRegionWidth() / reg.getRegionHeight();	
 			batch.draw(reg.getTexture(), x, y, w/2, h/2, w, h, 1, 1, 0, 
 					reg.getRegionX(), reg.getRegionY(), 
 					reg.getRegionWidth()-(i != (score.length - 1) ? 3 : 0), 
 					reg.getRegionHeight(),false, false);
 			x += w; 
 			}
 	}
 	
    public void resize(int width, int height) { 
    	// ����camera�ӿڳߴ�
		camera.viewportWidth = Constants.VIEWPORT_HEIGHT * width / height; 
		camera.update();
    }
    
    @Override  
    public void dispose() { 
    	batch.dispose();
    }    
}  