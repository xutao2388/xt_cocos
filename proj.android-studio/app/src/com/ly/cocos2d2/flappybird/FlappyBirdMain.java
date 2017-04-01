package com.ly.cocos2d2.flappybird;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;

import com.ly.cocos2d2.flappybird.game.Assets;
import com.ly.cocos2d2.flappybird.game.WorldController;
import com.ly.cocos2d2.flappybird.game.WorldRenderer;

public class FlappyBirdMain implements ApplicationListener {  
   // private static final String TAG =   
    //		FlappyBirdMain.class.getName();  
      
    private WorldController worldController;  
    private WorldRenderer worldRenderer;  
    
    public boolean paused;
    
    @Override 
    public void create() { 
    	Assets.instance.init(new AssetManager());
    	
    	// �趨��־��¼����
    	Gdx.app.setLogLevel(Application.LOG_DEBUG);
    //	worldController = new WorldController(this);
    	worldRenderer = new WorldRenderer(worldController);
    	
    	paused = false;
    }  
    
    @Override 
    public void resize(int width, int height) { 
    	worldRenderer.resize(width, height);
    }
    
    @Override 
    public void render() { 
    	if(!paused) {
	    	// �������һ֡����ǰ������ʱ�������Ϸ
    		float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 0.018f);
	    	worldController.update(deltaTime);
    	}
    	// �趨������ɫΪǳ��ɫ
    	Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);  
        //����  
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  
        //����Ϸ������Ⱦ����Ļ��  
        worldRenderer.render(); 
    }  
    
    @Override 
    public void pause() { 
    	paused = true;
    }
    
    @Override 
    public void resume() {
    	paused = false;
    	Assets.instance.init(new AssetManager());
    }  
    
    @Override 
    public void dispose() { 
    	worldRenderer.dispose();
    	worldController.dispose();
    }  
}