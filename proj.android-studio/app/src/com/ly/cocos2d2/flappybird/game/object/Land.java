package com.ly.cocos2d2.flappybird.game.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import com.ly.cocos2d2.flappybird.game.Assets;
import com.ly.cocos2d2.flappybird.util.Constants;

/*陆地*/
public class Land extends AbstractGameObject {
    private static final float LAND_VELOCITY = -15f;    // 恒定向左以15m/s的速度移动
    public static final float LAND_HEIGHT = 10.3f;

    private AtlasRegion land;
    private float viewWidth;
    private float leftMoveDist;

    public Land() {
        land = Assets.instance.land.land;
        viewWidth = Constants.VIEWPORT_HEIGHT * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        init();
    }

    public void init() {
        super.init();    // 保证对父类的初始化
        float startPosX = -viewWidth / 2;
        dimension.set(viewWidth, LAND_HEIGHT);
        position.set(startPosX, -Constants.VIEWPORT_HEIGHT / 2);
    }

    @Override
    public void beginToSimulate(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.KinematicBody;            // 运动物体
        bodyDef.position.set(position);                    // 初始位置
        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(dimension.x * 1.5f, dimension.y / 2, new Vector2(dimension.x * 1.5f, dimension.y / 2), 0);    // 矩形边界
        shape.setRadius(-0.1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
        body.setLinearVelocity(LAND_VELOCITY, 0);
    }

    private void wrapLand() {
        if (leftMoveDist >= viewWidth) {
            position.x = -viewWidth / 2 + leftMoveDist - viewWidth;
            leftMoveDist = 0;
            if (body != null) {
                body.setTransform(position, 0);
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        wrapLand();
        super.update(deltaTime);                                // 跟新应用body对象
        if (body == null) {                                    // 如果没有body对象自动更新
            position.x += LAND_VELOCITY * deltaTime;
        }
        // 转换land
        leftMoveDist += Math.abs(LAND_VELOCITY * deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(land.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x,
                scale.y, rotation, land.getRegionX(), land.getRegionY(), land.getRegionWidth(), land.getRegionHeight(),
                false, false);
        batch.draw(land.getTexture(), position.x + viewWidth, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x,
                scale.y, rotation, land.getRegionX(), land.getRegionY(), land.getRegionWidth(), land.getRegionHeight(),
                false, false);
        batch.draw(land.getTexture(), position.x + 2 * viewWidth, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x,
                scale.y, rotation, land.getRegionX(), land.getRegionY(), land.getRegionWidth(), land.getRegionHeight(),
                false, false);
    }

}
