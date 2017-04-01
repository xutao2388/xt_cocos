package com.ly.cocos2d2.flappybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import com.ly.cocos2d2.flappybird.util.Constants;


public class Assets implements Disposable, AssetErrorListener {

	public static final String TAG = Assets.class.getName();
	
	public static final Assets instance = new Assets();
	
	TextureAtlas atlas;
	private AssetManager assetManager;
	
	public AssetFonts fonts;
	public AssetBird bird;
	public AssetPipe pipe;
	public AssetLand land;
	public AssetUI assetUI;
	public AssetNumber number;
	public AssetSounds sounds;
	public AssetDecoration decoration;
	
	// ��̬��:��ֹ����������ʵ����
	private Assets () {}
	
	public void init (AssetManager assetManager) {
		this.assetManager = assetManager;
		
		// �趨��Դ�������Ĵ����������
		assetManager.setErrorListener(this);
		
		// ��������
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
			
		// ���������ļ�
		assetManager.load("sounds/sfx_die.ogg", Sound.class);
		assetManager.load("sounds/sfx_hit.ogg", Sound.class);
		assetManager.load("sounds/sfx_point.ogg", Sound.class);
		assetManager.load("sounds/sfx_swooshing.ogg", Sound.class);
		assetManager.load("sounds/sfx_wing.ogg", Sound.class);
	}
	
	public boolean isLoaded() {
		if(!assetManager.update()) return false;
		
	    // ��ӡ��Դ��Ϣ
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for(String a : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + a);
		}
		
		atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		fonts = new AssetFonts();
		bird = new AssetBird(atlas);
		pipe = new AssetPipe(atlas);
		land = new AssetLand(atlas);
		number = new AssetNumber(atlas);
		assetUI = new AssetUI(atlas);
		sounds = new AssetSounds(assetManager);
		decoration = new AssetDecoration(atlas);
		
		return true;
	}
	
	@Override
	@SuppressWarnings("rawtypes") 
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.debug(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
	}
	
	@Override
	public void dispose () {
		sounds.dispose();
		atlas.dispose();
		assetManager.dispose();
	}
	
	public class AssetBird {
		public final Array<AtlasRegion> bird0;
		public final Array<AtlasRegion> bird1;
		public final Array<AtlasRegion> bird2;
		
		public AssetBird (TextureAtlas atlas) {
			bird0 = atlas.findRegions("bird0");
			bird1 = atlas.findRegions("bird1");
			bird2 = atlas.findRegions("bird2");
		}
	}
	
	public class AssetPipe {
		public final AtlasRegion pipeUpGreen;
		public final AtlasRegion pipeUpBrown;
		public final AtlasRegion pipeDownGreen;		
		public final AtlasRegion pipeDownBrown;
		
		public AssetPipe (TextureAtlas atlas) {
			pipeUpGreen = atlas.findRegion("pipe_up");
			pipeDownGreen = atlas.findRegion("pipe_down");
			pipeUpBrown = atlas.findRegion("pipe2_up");
			pipeDownBrown = atlas.findRegion("pipe2_down");
		}
	}
	
	public class AssetLand {
		public final AtlasRegion land;
		public AssetLand (TextureAtlas atlas) {
			land = atlas.findRegion("land");
		}
	}
	
	public class AssetNumber {
		public final Array<AtlasRegion> numbers_score;
		public final Array<AtlasRegion> numbers_context;
		public final Array<AtlasRegion> numbers_font;
		public AssetNumber(TextureAtlas atlas) {
			numbers_score = atlas.findRegions("number_score");
			numbers_context = atlas.findRegions("number_context");
			numbers_font = atlas.findRegions("font");
		}
	}
	
	public class AssetUI {
		public final AtlasRegion textGameOver;
		public final AtlasRegion scorePanel;
		public final AtlasRegion buttonMenu;
		public final AtlasRegion buttonOk;
		public final AtlasRegion buttonPause;
		public final AtlasRegion buttonPlay;
		public final AtlasRegion buttonRate;
		public final AtlasRegion buttonResume;
		public final AtlasRegion buttonScore;
		public final AtlasRegion buttonShare;
		public final AtlasRegion tutorial;
		public final AtlasRegion textReady;
		public final AtlasRegion textTitle;
		public final AtlasRegion copyRight;
		public final Array<AtlasRegion> medals;
		
		public AssetUI(TextureAtlas atlas) {
			textGameOver = atlas.findRegion("text_game_over");
			scorePanel = atlas.findRegion("score_panel");
			buttonMenu = atlas.findRegion("button_menu");
			buttonOk = atlas.findRegion("button_ok");
			buttonPause = atlas.findRegion("button_pause");
			buttonPlay = atlas.findRegion("button_play");
			buttonRate = atlas.findRegion("button_rate");
			buttonResume = atlas.findRegion("button_resume");
			buttonScore = atlas.findRegion("button_score");
			buttonShare = atlas.findRegion("button_share");
			tutorial = atlas.findRegion("tutorial");
			medals = atlas.findRegions("medals");
			textReady = atlas.findRegion("text_ready");
			textTitle = atlas.findRegion("title");
			copyRight = atlas.findRegion("brand_copyright");
		}
	}
	
	public class AssetSounds { 
		public final Sound die;
		public final Sound hit;
		public final Sound point;
		public final Sound swooshing;
		public final Sound wing;
		
		public AssetSounds(AssetManager am) {
			die = am.get("sounds/sfx_die.ogg", Sound.class);
			hit = am.get("sounds/sfx_hit.ogg", Sound.class);
			point = am.get("sounds/sfx_point.ogg", Sound.class);
			swooshing = am.get("sounds/sfx_swooshing.ogg", Sound.class);
			wing = am.get("sounds/sfx_wing.ogg", Sound.class);
		}
		public void dispose() {
			die.dispose();
			hit.dispose();
			point.dispose();
			swooshing.dispose();
			wing.dispose();
		}
	}
	
	public class AssetDecoration {
		public final Array<AtlasRegion> bg;
		public final AtlasRegion white;
		public AssetDecoration (TextureAtlas atlas) {
			bg = new Array<AtlasRegion>();
			bg.add(atlas.findRegion("bg_day"));
			bg.add(atlas.findRegion("bg_night"));
			white = atlas.findRegion("white");
		}
	}	
	
	public class AssetFonts {
		
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;
		
		public AssetFonts() {
			// ʹ��libgdx��15pxλͼ�����ļ�������������
			defaultSmall = new BitmapFont(
					Gdx.files.internal("images/arial-15.fnt"), false);
			defaultNormal = new BitmapFont(
					Gdx.files.internal("images/arial-15.fnt"), false);
			defaultBig = new BitmapFont(
					Gdx.files.internal("images/arial-15.fnt"), false);
			// �趨����ߴ�
			defaultSmall.setScale(0.75f);
			defaultNormal.setScale(1.0f);
			defaultBig.setScale(2.0f);
			// �趨�������ģʽΪ����ƽ��
			defaultSmall.getRegion().getTexture().setFilter(
					TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(
					TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(
					TextureFilter.Linear, TextureFilter.Linear);
		}
	}	
}
