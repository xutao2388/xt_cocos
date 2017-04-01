package com.ly.cocos2d2.flappybird.game.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import com.ly.cocos2d2.flappybird.game.Assets;
import com.ly.cocos2d2.flappybird.util.Constants;

/*管子*/
public class Pipes extends AbstractGameObject {
    public static final float PIPE_DISTANCE = 17.24f;

    private Pipe curPipe;
    public Array<Pipe> pipes;
    private float viewWidth;
    private World word;

    public Pipes(World world) {
        viewWidth = Constants.VIEWPORT_HEIGHT * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        pipes = new Array<Pipes.Pipe>();
        init(world);
    }

    public void init(World world) {
        this.word = world;
        pipes.clear();
        pipes.add(curPipe = new Pipe());
    }

    private void testPipeNumberIsTooLarge(int amount) {
        if (pipes != null && pipes.size > amount) {
            pipes.get(0).destroy();
            pipes.removeIndex(0);
        }
    }

    private void wrapPipe() {
        if (curPipe.position.x <= viewWidth - PIPE_DISTANCE) {
            pipes.add(curPipe = new Pipe());
        }
    }

    @Override
    public void update(float deltaTime) {
        wrapPipe();
        for (Pipe pipe : pipes)
            pipe.update(deltaTime);
        testPipeNumberIsTooLarge(8);
    }

    @Override
    public void render(SpriteBatch batch) {
        for (Pipe pipe : pipes) {
            pipe.render(batch);
        }
    }

    public class Pipe extends AbstractGameObject {
        private static final float PIPE_WIDTH = 5.72f;
        private static final float CHANNEL_HEIGHT = 10.56f;
        public static final float MIN_PIPE_HEIGHT = 5.27f;
        private static final float PIPE_VELOCITY = -15f;    // 恒定向左以15m/s的速度移动

        private AtlasRegion pipe;
        private float dnPipeHeight;
        private boolean isCollected;

        public Pipe() {
            pipe = Assets.instance.pipe.pipeDownGreen;
            init();
        }

        public void init() {
            super.init();    // 保证对父类的初始化

            isCollected = false;
            position.set(viewWidth, Land.LAND_HEIGHT - Constants.VIEWPORT_HEIGHT / 2);
            dimension.set(PIPE_WIDTH, Constants.VIEWPORT_HEIGHT - Land.LAND_HEIGHT);

            dnPipeHeight = MathUtils.random(dimension.y - 2 * MIN_PIPE_HEIGHT - CHANNEL_HEIGHT) + MIN_PIPE_HEIGHT;
            beginToSimulate(Pipes.this.word);
        }

        public int getScore() {
            if (isCollected) return 0;
            else {
                isCollected = true;
                return 1;
            }
        }

        @Override
        public void beginToSimulate(World world) {
            // down
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.KinematicBody;
            bodyDef.position.set(position);

            body = world.createBody(bodyDef);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(dimension.x / 2, dnPipeHeight / 2,
                    new Vector2(dimension.x / 2, dnPipeHeight / 2), 0);
            shape.setRadius(-0.1f);

            FixtureDef fixtureDefDown = new FixtureDef();
            fixtureDefDown.shape = shape;

            body.createFixture(fixtureDefDown);
            body.setLinearVelocity(PIPE_VELOCITY, 0);

            // up
            float height = dimension.y - dnPipeHeight - CHANNEL_HEIGHT;
            shape.setAsBox(dimension.x / 2, height / 2,
                    new Vector2(dimension.x / 2, dnPipeHeight + CHANNEL_HEIGHT + height / 2), 0);
            shape.setRadius(-0.1f);
            FixtureDef fixtureDefUp = new FixtureDef();
            fixtureDefUp.shape = shape;
            body.createFixture(fixtureDefUp);
        }

        public void destroy() {
            for (Fixture fixture : body.getFixtureList()) {
                body.destroyFixture(fixture);
            }
            Pipes.this.word.destroyBody(body);
        }

        @Override
        public void render(SpriteBatch batch) {
            // down
            batch.draw(pipe.getTexture(), position.x, position.y + dnPipeHeight - Constants.VIEWPORT_HEIGHT / 1.5f,
                    origin.x, origin.y, dimension.x, Constants.VIEWPORT_HEIGHT / 1.5f, scale.x, scale.y,
                    rotation, pipe.getRegionX(), pipe.getRegionY(), pipe.getRegionWidth(),
                    pipe.getRegionHeight(), false, true);
            // up
            batch.draw(pipe.getTexture(), position.x, position.y + dnPipeHeight + CHANNEL_HEIGHT,
                    origin.x, origin.y, dimension.x, dimension.y / 1.5f,
                    scale.x, scale.y, rotation, pipe.getRegionX(), pipe.getRegionY(),
                    pipe.getRegionWidth(), pipe.getRegionHeight(), false, false);
        }
    }
}
