//package com.ly.cocos2d2;
//
//import android.app.Activity;
//import android.os.Bundle;
//
//import org.cocos2d.layers.CCScene;
//import org.cocos2d.nodes.CCDirector;
//import org.cocos2d.opengl.CCGLSurfaceView;
//
///*
//* 使用libgdx引擎开发的小鸟飞跃
//* http://www.tuicool.com/articles/qINfmm
//* */
//public class GbxActivity extends Activity {
//
//    static {
//        System.loadLibrary("gdx");//表示先加载libgdx.so,Java的JNI机制
//    }
//
//    CCGLSurfaceView view;//是GLSurfaceView子类，GLSurfaceView是一个视图，继承至SurfaceView，它内嵌的surface专门负责OpenGL渲染。
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        view = new CCGLSurfaceView(this);
////        设置android UI；
//        setContentView(view);
//
//        CCDirector director = CCDirector.sharedDirector();// 获得导演类==设置OpenGL视图，设置是否显示每帧时间，设置每帧时间，设置运行场景，设置或控制类；
//
////        设置导演类的属性
//        director.setDeviceOrientation(CCDirector.kCCDeviceOrientationPortrait);//设置设备方向，竖或横屏；
//        director.attachInView(view);//导演绑定OpenGL渲染
//        director.setAnimationInterval(1 / 30.0);//设置每帧时间
//        director.setDisplayFPS(true);//设置是否显示每帧时间
//
//        CCScene scene = CCScene.node(); //初始化场景类
//        FlyppyBirdLayer layer = new FlyppyBirdLayer(this);//初始化场景类
//        scene.addChild(layer);//增加子场景
//
//        director.runWithScene(scene);//运行场景
//    }
//}
