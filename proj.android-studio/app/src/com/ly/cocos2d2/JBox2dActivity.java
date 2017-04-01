package com.ly.cocos2d2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

/*使用jbox2d 引擎实现 球状自由落体*/
public class JBox2dActivity extends Activity {
    private final static int RATE = 10;//屏幕到现实世界的比例 10px：1m;
    AABB worldAABB;//一个坐标系统==axis aligned bounding box，大致概念首先是一个box，这个框框的4边与坐标轴平行，也就是一个正方形或者长方形区域。
    private World world;   //创建一个管理碰撞的世界
    private float timeStep = 1 / 60;//模拟的的频率
    private int iterations = 10;//迭代越大，模拟越精确，但性能越低
    private Body body;
    private MyView myView;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        worldAABB = new AABB();//创建一个类似坐标的范围
        //上下界，以屏幕的左上方为 原点，如果创建的物体到达屏幕的边缘，会停止模拟
        //范围大总比范围小要好,因为里面创建的物体不能大于当前世界的大小范围
        worldAABB.lowerBound.set(-100.0f, -100.0f);//最低位置
        worldAABB.upperBound.set(100.0f, 100.0f);//最高位置

        //创建一个重力方向向量,第一个参数(x)表示水平方向,负数为向左方向,整数为向右方向
        //第二个参数(y)表示垂直方向,负数向上,整数向下，本例中0.0f说明水平方向的重力为0, 10.0f说明垂直方向的重力为10.，故(Vec2(0.0f,10.0f))是球体垂直向下运动；当然你也可以改成你想要的值， 不过要注意比例，在Box2D中 1单元=1米。
        //参数三：是否休眠,如果为false,则系统休眠,休眠可以减少cpu的使用
        world = new World(worldAABB, new Vec2(0.0f, 10.0f), true);//创建世界

        createGround(160, 470, 160, 10);//创建一个地面
        createSprite(160, 100, 10);//创建一个圆球体=精灵
        timeStep = 1.0f / 60.0f;
        iterations = 10;

        myView = new MyView(this);
        setContentView(myView);
        mHandler = new Handler();
        mHandler.post(update);
    }

    private Runnable update = new Runnable() {
        public void run() {
            world.step(timeStep, iterations);//开始模拟 ==  需要不停的调用Step()方法来模拟物理世界的运行。
            Vec2 position = body.getPosition();//获取圆球体不断变化的轨迹的向量
            myView.x = position.x * RATE;//赋值view界面的位置,并按比例缩放实际值成为view的值
            myView.y = position.y * RATE;
            myView.update();
            mHandler.postDelayed(update, (long) timeStep * 1000);
        }
    };

    //创建一个地面的实体
    private void createGround(float x, float y, float half_width, float half_height) {
        PolygonDef shape = new PolygonDef();//定义一个多边形
        shape.density = 0;//定义密度,因为是地面,不会移动的,所以密度为0,
        shape.friction = 0.2f;//定义定义地面的摩擦力
        shape.restitution = 0.5f;//定义地面的弹性,弹性系数为0.0~1.0,系数越大弹性越大
        shape.setAsBox(half_width / RATE, half_height / RATE);//设置地面的为

        BodyDef bodyDef = new BodyDef();//定义一个物体,就是地面的对象,上面定义的是地面的属性
        bodyDef.position.set(x / RATE, y / RATE);//定义这个物体的位置
        Body mBody = world.createBody(bodyDef);//正式在上面的世界中创建地面,即将地面添加到一个世界中
        mBody.createShape(shape);//将上面定义的多边形属性添加到地面中去
        mBody.setMassFromShapes();//计算质量
    }

    //创建一个圆球体,因为与上面地板的创建过程极为相似,注释省略
    private void createSprite(float x, float y, float radius) {
        CircleDef shape = new CircleDef();
        shape.density = 0.1f;
        shape.friction = 0.2f;
        shape.radius = radius / RATE;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x / RATE, y / RATE);
        body = world.createBody(bodyDef);//这里用全局变量是因为在模拟过程中,计算球体的轨迹用到球体的坐标
        body.createShape(shape);
        body.setMassFromShapes();
    }

    //更新的View
    class MyView extends View {
        Canvas canvas;
        public float x = 160, y = 100;//圆球体初始化的位置

        public MyView(Context context) {
            super(context);
        }

        public void drawGround() {//绘制地面
            Paint mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.BLUE);
            canvas.drawRect(0, 460, 320, 480, mPaint);  //其实这个绘制的地面与实际地面的值关系不大，只是为了显示大概的位置
        }

        //绘制圆球体
        public void drawCircle(float x, float y) {
            Paint mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.GREEN);
            canvas.drawCircle(x, y, 10, mPaint);
        }

        public void update() {
            postInvalidate();
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            this.canvas = canvas;
            drawGround();//绘地面
            drawCircle(x, y);//绘圆球体
        }
    }

}