package com.example.vincent_pc.svslidemonitor;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.vincent_pc.svslidemonitor.view.MyScrollView;

public class MainActivity extends AppCompatActivity implements MyScrollView.OnScrollListener {

    private MyScrollView myScrollView;
    private LinearLayout linearLayout;
    private WindowManager systemService;
    private int screenWidth;
    /**悬浮布局的高*/
    private int layoutHeight;
    private int layoutTop;
    private int scrollViewTop;

    /**
     * 悬浮框View
     */
    private static View suspendsionView;

    /**
     * 悬浮框的参数
     */
    private static WindowManager.LayoutParams suspendLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initData() {
        /**接口回调，滚动监听，回调y值*/
        myScrollView.setOnScrollListener(this);
        /**调用系统服务，管理打开的窗口程序*/
        systemService = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWidth = systemService.getDefaultDisplay().getWidth();
    }

    private void initView() {
        linearLayout = (LinearLayout) findViewById(R.id.layout);
        myScrollView = (MyScrollView) findViewById(R.id.sv);
    }

    /**当前Activity中的windows显示或者消失是调用该方法
     * 可以在此法中直接获取view的宽高
     * onCreate中获取值为0*/
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            layoutHeight = linearLayout.getHeight();
            layoutTop = linearLayout.getTop();

            scrollViewTop = myScrollView.getTop();
        }
    }

    @Override
    public void setOnScrollListener(int scrollY) {
        if (scrollY >= layoutTop) {
            if (suspendsionView == null) {
                /**满足条件显示悬浮*/
                showSuspension();
            }
        } else if (scrollY <= layoutTop + layoutHeight) {
            if (suspendsionView != null) {
                /**移除悬浮*/
                removeSuspension();
            }
        }
    }

    private void showSuspension() {
        if (suspendsionView == null) {
            /**悬浮的布局*/
            suspendsionView= LayoutInflater.from(this).inflate(R.layout.suspension_layout, null);

            if (suspendLayoutParams == null) {
                suspendLayoutParams = new WindowManager.LayoutParams();
                suspendLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;//悬浮窗的类型，一般设为2002，表示在所有应用程序之上，但在状态栏之下
                suspendLayoutParams.format = PixelFormat.RGBA_8888;
                suspendLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;  //悬浮窗的行为，比如说不可聚焦，非模态对话框等等
                suspendLayoutParams.gravity = Gravity.TOP;  //悬浮窗的对齐方式
                suspendLayoutParams.width = screenWidth;
                suspendLayoutParams.height = layoutHeight;
                suspendLayoutParams.x = 0;  //悬浮窗X的位置
                suspendLayoutParams.y = scrollViewTop;  ////悬浮窗Y的位置
            }
        }
        systemService.addView(suspendsionView, suspendLayoutParams);
    }

    private void removeSuspension() {
        if (suspendsionView != null) {
            systemService.removeView(suspendsionView);
            suspendsionView = null;
        }
    }
}
