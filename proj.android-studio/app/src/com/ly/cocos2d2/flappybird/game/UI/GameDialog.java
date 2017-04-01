package com.ly.cocos2d2.flappybird.game.UI;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import com.ly.cocos2d2.flappybird.game.Assets;
import com.ly.cocos2d2.flappybird.game.WorldController;
import com.ly.cocos2d2.flappybird.util.Constants;

public class GameDialog extends Stage {
	WorldController worldController;
	
	public GameDialog(WorldController worldController) {
		super(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT));
		this.worldController = worldController;
	}
	
	public void startView() {
		clear();
		// tutorial
		Image tutorial = new Image(Assets.instance.assetUI.tutorial);
		tutorial.setBounds(134, 327, 213, 185);
		// get ready
		Image textReady = new Image(Assets.instance.assetUI.textReady);
		textReady.setBounds(89, 553, 302, 90);
		addActor(tutorial);
		addActor(textReady);
	}
	
	public void gameView() {
		clear();
		// pause ��ť
		Button pause = new Button(new TextureRegionDrawable(Assets.instance.assetUI.buttonPause),
				new TextureRegionDrawable(Assets.instance.assetUI.buttonResume), 
				new TextureRegionDrawable(Assets.instance.assetUI.buttonResume));
		pause.setBounds(410, 784, 50, 50);
		pause.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			worldController.pauseOrResume();
			}
		});
		
		addActor(pause);
				
	}
	
	public void endView() {
		clear();
		
		// ���� 
		int medalLevel = (worldController.score < 10)   ? 0 : 
						 (worldController.score < 100)  ? 1 : 
						 (worldController.score < 1000) ? 2 : 3; 
		final Image medal = new Image(Assets.instance.assetUI.medals.get(medalLevel));
		medal.setBounds(93, 375, 75, 72);
		
		// ��������
		final ScoreActor scoreActor = new ScoreActor(worldController.score, 393, 430, false);
		
		// ��߷���
		int bestScore = worldController.prefs.getInteger(Constants.BEST_SCORE_KEY); 
		final ScoreActor bestScoreActor = new ScoreActor(bestScore, 393, 350, true);
				
		
		// game over �ı�
		Image textGameOver = new Image(Assets.instance.assetUI.textGameOver);
		textGameOver.setBounds(80, 563, 321, 80);
		// actions
		Action textGameAlphaAction = Actions.fadeIn(0.3f);
		Action textGameOverSeqAction = Actions.sequence(Actions.moveTo(80, 580, 0.1f), Actions.moveTo(80, 563, 0.1f));
		Action textGameOverParAction = Actions.parallel(textGameAlphaAction, textGameOverSeqAction);
		textGameOver.addAction(textGameOverParAction);
		
		// score ���
		Image scorePanel = new Image(Assets.instance.assetUI.scorePanel);
		scorePanel.setBounds(51, -215, 378, 215);
		// actions 
		Action endRunAction = Actions.run(new Runnable() {
			@Override public void run() {
				addActor(medal);
				addActor(scoreActor);
				addActor(bestScoreActor);
			}
		});
		Action scorePanelDelayAction = Actions.delay(0.5f);
		Action scorePanelSeqAction = Actions.sequence(
				scorePanelDelayAction, 
				Actions.moveTo(51, 320, 0.3f),
				endRunAction);
		scorePanel.addAction(scorePanelSeqAction);
		
		// play ��ť
		CustomButton play = new CustomButton(Assets.instance.assetUI.buttonPlay);
		play.setBounds(43, -108, 173, 108);
		// actions 
		Action playDelayAction = Actions.delay(1f);
		Action playSeqAction = Actions.sequence(playDelayAction, Actions.moveTo(43, 175, 0));
		play.addAction(playSeqAction);
		play.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				worldController.restart();
			}
		});
		
		// score ��ť
		CustomButton score = new CustomButton(Assets.instance.assetUI.buttonScore);
		score.setBounds(263, -108, 173, 108);
		// actions 
		DelayAction scoreDelayAction = Actions.delay(1f);
		Action scoreSeqAction = Actions.sequence(scoreDelayAction, Actions.moveTo(263, 175, 0));
		score.addAction(scoreSeqAction);

		addActor(textGameOver);
		addActor(scorePanel);
		addActor(play);
		addActor(score);
		
	}
	
}
