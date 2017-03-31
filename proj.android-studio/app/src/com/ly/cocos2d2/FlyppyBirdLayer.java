package com.ly.cocos2d2;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGSize;

import java.util.Iterator;
import java.util.Random;

public class FlyppyBirdLayer extends CCLayer implements ContactListener {
    protected static final float PTM_RATIO = 32.0f;
    protected static final float FPS = (float) CCDirector.sharedDirector().getAnimationInterval();//Frames Per Second,每秒传输帧数
    private World world;//世界，第三方库libGDX，== initWorld
    private CCSprite bird;//游戏背景、NPC、人物、道具等。在cocos2d-x引擎中，只要是用图片展示的，基本上需要使用精灵类。== addBird
    private CGSize screenSize;
    private Context mContext;
    private int score;

    //                 设置屏幕接受点击事件
    public FlyppyBirdLayer(Context context) {
        this.mContext = context;
        this.setIsTouchEnabled(true);//设置点击事件
        this.setIsAccelerometerEnabled(true);//设置加速度
        screenSize = CCDirector.sharedDirector().winSize();//winSize=window size,获取屏大小
        initWorld();
        startGame();
    }

    //    首先设置当前场景的物理环境，这里我们使用Box2D，=libs中的box2d.jar；
    private void initWorld() {
        Vector2 gravity = new Vector2(0f, -9.8f);//向量的X组件。向量的Y组件。
        world = new World(gravity, true);//创建世界
        world.setContinuousPhysics(true);//允许物理现象
        world.setContactListener(this);//设置接触监听
    }

    private void startGame() {
        score = 0;
        addBird();
        addGround();
        scheduleUpdate();//需要每次重绘小鸟位置，否则小鸟表现为不会运动，我们调用scheduleUpdate()方法，来执行默认的update(float dt)方法（Cocos2d机制）==可理解为更新函数，
        // 对应的是:// unscheduleUpdate()=停止更新；
        schedule("addBar", 2);//参数1是目标更新函数（对应addBar方法）,参数2的单位是秒；即每隔2秒更新一次addBar函数；
        // schedule的作用与scheduleUpdate()函数相同，但是scheduleUpdate()默认每一帧都会调用update函数，而schedule则可以自定义调用更新函数的时间间隔。
//  <1>     schedule(SEL_SCHEDULE selector,float interval)参数1： 目标更新函数。参数2： 刷新间隔。
//  <2>     schedule(SEL_SCHEDULE selector)作用： 此函数默认每一帧都调用目标函数。 参数： 目标更新函数。注意： 刷新间隔时间的单位是s（秒）。
//          unschedule(SEL_SCHEDULE selector);停止自定义更新函数。
//          unscheduleAllSelectors() 停止所有更新函数。
    }

    //添加小鸟
    private void addBird() {
        bird = CCSprite.sprite("bird.png");//创建精灵
        CGSize s = bird.getContentSize();//获取图片尺寸

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(300 / PTM_RATIO, 800 / PTM_RATIO);

        PolygonShape dynamicBox = new PolygonShape(); // Define another box shape for our dynamic body.
        dynamicBox.setAsBox(s.width / 2 / PTM_RATIO, s.height / 2 / PTM_RATIO);

        Body body = world.createBody(bodyDef); // Define the dynamic body fixture and set mass so it's dynamic. 创建body
        body.setUserData(bird);


        FixtureDef fixtureDef = new FixtureDef();//        设置材料

//        fixtureDef.SetAsBox(3,3);//将多边形设为矩形
        fixtureDef.shape = dynamicBox;//定形
        fixtureDef.density = 1.0f;//密度
        fixtureDef.friction = 0.3f;//定义摩擦力

        body.createFixture(fixtureDef);//将已经定形的材料加进body中= 给物体定义形状以及物理材质
        addChild(bird);
    }

    //    添加滑块，并且设置速度，这里我们仍然使用schedule方法，来隔一段时间添加滑块，并且设置从右往左运动。 这里添加了上下2部分滑块，中间留出空隙，让小鸟可以通过。
//    被schedule调用
    public void addBar(float dt) {
        float offset = -(new Random().nextInt(5));
        CCSprite downBar = CCSprite.sprite("downBar.png");
        CGSize downBarSize = downBar.getContentSize();
        BodyDef def = new BodyDef();
        def.type = BodyType.KinematicBody;
        def.position.set(screenSize.width / PTM_RATIO + 2, downBarSize.height / PTM_RATIO + offset);
        def.linearVelocity.set(-5, 0);

        Body downBarBody = world.createBody(def);
        PolygonShape downPolygonShape = new PolygonShape();
        downPolygonShape.setAsBox(downBarSize.width / 2 / PTM_RATIO, downBarSize.height / 2 / PTM_RATIO);
        FixtureDef downBarFixtureDef = new FixtureDef();
        downBarFixtureDef.shape = downPolygonShape;
        downBarBody.createFixture(downBarFixtureDef);
        addChild(downBar);
        downBarBody.setUserData(downBar);

        CCSprite upBar = CCSprite.sprite("downBar.png");
        CGSize upBarSize = upBar.getContentSize();
        BodyDef upBardef = new BodyDef();
        upBardef.type = BodyType.KinematicBody;
        upBardef.position.set(screenSize.width / PTM_RATIO + 2, upBarSize.height / PTM_RATIO + offset + upBarSize.height * 2 / PTM_RATIO);
        upBardef.linearVelocity.set(-5, 0);

        Body upBarBody = world.createBody(upBardef);
        PolygonShape upPolygonShape = new PolygonShape();
        upPolygonShape.setAsBox(upBarSize.width / 2 / PTM_RATIO, upBarSize.height / 2 / PTM_RATIO);
        FixtureDef upBarFixtureDef = new FixtureDef();
        upBarFixtureDef.shape = upPolygonShape;
        upBarBody.createFixture(upBarFixtureDef);
        addChild(upBar);
        upBarBody.setUserData(upBar);
    }

    //     给屏幕添加点击事件，当点击屏幕时，改变小鸟速度为向上，初始速度为10
    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        Iterator<Body> it = world.getBodies();
        while (it.hasNext()) {
            Body b = it.next();
            Object userData = b.getUserData();
            if (userData != null && userData instanceof CCSprite && userData == bird) {
                b.setLinearVelocity(0, 10);//添加的速度会覆盖刚体原有的速度,x轴不变，Y轴+10
            }
        }
        return super.ccTouchesBegan(event);
    }

    //    添加地板
    private void addGround() {
        CCSprite ground = CCSprite.sprite("ground.png");
        CGSize s = ground.getContentSize();
        BodyDef def = new BodyDef();
        def.type = BodyType.StaticBody;
        def.position.set(s.width / 2 / PTM_RATIO, s.height / 2 / PTM_RATIO);
        Body groundBody = world.createBody(def);

        FixtureDef fDef = new FixtureDef();
        PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(s.width / 2 / PTM_RATIO, s.height / 2 / PTM_RATIO);
        fDef.shape = pShape;
        fDef.density = 1.0f;
        fDef.friction = 0.3f;
        groundBody.createFixture(fDef);
        groundBody.setUserData(ground);
        addChild(ground);
    }

    public void update(float dt) {
        System.out.println(world.getBodyCount());
        world.step(FPS, 8, 1);
        Iterator<Body> it = world.getBodies(); // Iterate over the bodies in the physics world
        while (it.hasNext()) {
            Body b = it.next();
            Object userData = b.getUserData();
            if (userData != null && userData instanceof CCSprite) {
                // Synchronize the Sprites position and rotation with the corresponding body
                final CCSprite sprite = (CCSprite) userData;
                final Vector2 pos = b.getPosition();
                if (pos.x < 0) {
                    sprite.removeFromParentAndCleanup(true);
                    world.destroyBody(b);
                    score++;
                } else {
                    sprite.setPosition(pos.x * PTM_RATIO, pos.y * PTM_RATIO);
                }
            }
        }
    }

    private void stopGame() {
        unscheduleUpdate();
        unschedule("addBar");
        Iterator<Body> it = world.getBodies();
        while (it.hasNext()) {
            Body b = it.next();
            Object userData = b.getUserData();
            if (userData != null) {
                world.destroyBody(b);
                CCSprite sprite = (CCSprite) userData;
                sprite.removeFromParentAndCleanup(true);
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Builder builder = new Builder(mContext);
                builder.setTitle("GameOver!");
                builder.setMessage("得分:" + score / 2);
                builder.setCancelable(false);
                builder.setPositiveButton("再玩一次", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startGame();
                    }
                });
                builder.setNegativeButton("结束", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CCDirector.sharedDirector().end();
                    }
                });
                builder.show();
            }
            super.handleMessage(msg);
        }
    };

    //             最后，我们需要碰撞检测
    @Override
    public void beginContact(Contact contact) {
        if (contact.getFixtureA().getBody().getUserData() == bird || contact.getFixtureB().getBody().getUserData() == bird) {
            stopGame();
            handler.sendEmptyMessage(0);
        }

    }

    @Override
    public void endContact(Contact arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void postSolve(Contact arg0, ContactImpulse arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void preSolve(Contact arg0, Manifold arg1) {
        // TODO Auto-generated method stub

    }
}
