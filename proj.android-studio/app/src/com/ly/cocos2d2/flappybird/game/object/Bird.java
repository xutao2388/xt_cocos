package com.ly.cocos2d2.flappybird.game.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import com.ly.cocos2d2.flappybird.game.Assets;
import com.ly.cocos2d2.flappybird.util.Constants;

public class Bird extends AbstractGameObject {
	protected static final float BIRD_MAX_FLAP_ANGLE = 20;	 	// �������Ƕ�
	protected static final float BIRD_MAX_DROP_ANGLE = -90; 	// �������Ƕ�
	protected static final float FLAP_ANGLE_DRAG = 9.0f; 		// ����Ƕ������ٶ�
	protected static final float BIRD_FLAP_ANGLE_POWER = 6.0f; 	// ����Ƕ������ٶ�
	protected static final float DIED_DROP_DRAG = 0.5f; 		// �����������ٶ�
    protected static final float SPEED_THRESHOLD = -20f;		// �ٶȷ�ֵ
	
    protected enum WAVE_STATE {
		WAVE_FALLING, WAVE_RISE
	}
	
	private Array<AtlasRegion> birds;
	
	private float animDuration;
	private Animation birdAnimation;
	private TextureRegion currentFrame;

	private float max_wave_height;
	private float min_wave_height;

	private boolean contacted;
	private boolean flashed;
	private WAVE_STATE waveState = WAVE_STATE.WAVE_RISE;

	public Bird(float x, float y) {
		init((int) (Math.random() * 3), x, y);
	}

	// ��ʼ��
	public void init(int selected, float x, float y) {
		super.init();					// ��֤�Ը���ĳ�ʼ��
		
		contacted = false;
		flashed = false;
		
		if (selected == 1) {
			birds = Assets.instance.bird.bird0;
		} else if (selected == 2) {
			birds = Assets.instance.bird.bird1;
		} else {
			birds = Assets.instance.bird.bird2;
		}
		
		birdAnimation = new Animation(0.1f, birds);
		birdAnimation.setPlayMode(Animation.PlayMode.LOOP);
		
		dimension.set(3.72f, 2.64f);
		position.set(x, y);
		max_wave_height = position.y + 0.7f;
		min_wave_height = position.y - 0.7f;
	}
	
	@Override
	public void beginToSimulate(World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody; // must be dynamic body
		bodyDef.fixedRotation = true; 		 // fixed angle
		bodyDef.position.set(position);

		body = world.createBody(bodyDef);
		body.setUserData(this);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(dimension.x / 2, dimension.y / 2); // position is
														  // rectangle bounds
														  // center
		shape.setRadius(-0.4f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 0.1f;
		fixtureDef.friction = 0f;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData(this);
	}
	
	// ������Ծ
	public void setJumping() {
		if(body != null) {
			body.setLinearVelocity(0, 35f);
			Assets.instance.sounds.wing.play();
		}
	}
	
	// ֪ͨ��ײ�¼�
	public void contact() {
		contacted = true;
		Assets.instance.sounds.hit.play();
		Assets.instance.sounds.die.play();
	}
	 
	public boolean isContact() {
		return contacted;
	}
	
	public boolean isGameOver() {
		return contacted && 
			  (position.y <= Land.LAND_HEIGHT - 
			  Constants.VIEWPORT_HEIGHT / 2 + dimension.y / 2) && 
			  (rotation == BIRD_MAX_DROP_ANGLE);
	}
	
	@Override
	public void update(float deltaTime) {
		if (body == null) {
			if (waveState == WAVE_STATE.WAVE_FALLING)
				position.y -= 0.05f;
			else if (waveState == WAVE_STATE.WAVE_RISE) {
				position.y += 0.05f;
			}
			if (position.y < min_wave_height) {
				waveState = WAVE_STATE.WAVE_RISE;
			} else if (position.y > max_wave_height) {
				waveState = WAVE_STATE.WAVE_FALLING;
			}
		} else {
			super.update(deltaTime);
			// �����ٶȼ���������ת�Ƕ�
			if (body.getLinearVelocity().y < SPEED_THRESHOLD) {
				rotation -= BIRD_FLAP_ANGLE_POWER;
			} else {
				rotation += FLAP_ANGLE_DRAG;
			}
			// �������D�Ƕ���20��-90��֮�g
			rotation = MathUtils.clamp(rotation, BIRD_MAX_DROP_ANGLE, BIRD_MAX_FLAP_ANGLE);
			body.setTransform(position, rotation * MathUtils.degreesToRadians);
		}
	}
	
	@Override
	public void render(SpriteBatch batch) {
		// ���������ײ���ٲ���֡����
		if(! contacted) {
			animDuration += Gdx.graphics.getDeltaTime();
			currentFrame = birdAnimation.getKeyFrame(animDuration);
		}
		// ������ײ����Ϸ��û�н���ʱС��Ҫ������ض���
		else {
			position.y -= DIED_DROP_DRAG;
			rotation -= BIRD_FLAP_ANGLE_POWER;
			position.y = Math.max(position.y, Land.LAND_HEIGHT - Constants.VIEWPORT_HEIGHT / 2 + dimension.y / 2);
			rotation = Math.max(rotation, BIRD_MAX_DROP_ANGLE);		
		}
		
		batch.draw(currentFrame.getTexture(), position.x - dimension.x / 2, position.y - dimension.y / 2,
			dimension.x / 2, dimension.y / 2, dimension.x, dimension.y, scale.x, scale.y, rotation,
			currentFrame.getRegionX(), currentFrame.getRegionY(), currentFrame.getRegionWidth(),
			currentFrame.getRegionHeight(), false, false);
		
		if (contacted && !flashed) {
			flashed = true;
			float w = Gdx.graphics.getWidth();
			float h = Gdx.graphics.getHeight();
			batch.draw(Assets.instance.decoration.white, -w / 2, -h / 2, w, h);
		}
		
	}
}
