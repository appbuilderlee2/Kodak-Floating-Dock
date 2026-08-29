package com.alphalee.kodakdock;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DockService extends Service {
    private static final String CHANNEL = "kodak_dock";
    private static final int NOTIFICATION_ID = 1001;
    private static final String LOCALSEND = "org.localsend.localsend_app";
    private static final String KODAK = "com.cloudhearing.digital.kodakframe.android";

    private WindowManager windowManager;
    private View dock;
    private WindowManager.LayoutParams params;
    private float downX;
    private float downY;
    private int startX;
    private int startY;
    private boolean dragged;

    @Override
    public void onCreate() {
        super.onCreate();
        createChannel();
        startForeground(NOTIFICATION_ID, notification());
        showDock();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (dock == null) showDock();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (dock != null && windowManager != null) {
            windowManager.removeView(dock);
            dock = null;
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showDock() {
        if (!android.provider.Settings.canDrawOverlays(this) || dock != null) return;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setPadding(dp(6), dp(5), dp(6), dp(5));

        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.argb(105, 10, 18, 16));
        background.setCornerRadius(dp(28));
        bar.setBackground(background);

        bar.addView(appButton(LOCALSEND), buttonParams());
        bar.addView(appButton(KODAK), buttonParams());
        dock = bar;

        SharedPreferences prefs = getSharedPreferences("dock", MODE_PRIVATE);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= 26
                        ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                        : WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        params.x = prefs.getInt("x", dp(12));
        params.y = prefs.getInt("y", dp(12));

        bar.setOnTouchListener(this::handleDrag);
        windowManager.addView(dock, params);
    }

    private ImageButton appButton(String packageName) {
        ImageButton button = new ImageButton(this);
        button.setBackgroundColor(Color.TRANSPARENT);
        button.setPadding(dp(7), dp(7), dp(7), dp(7));
        try {
            Drawable icon = getPackageManager().getApplicationIcon(packageName);
            button.setImageDrawable(icon);
        } catch (Exception ignored) {
            button.setImageResource(android.R.drawable.sym_def_app_icon);
        }
        button.setContentDescription(packageName.equals(LOCALSEND) ? "LocalSend" : "Kodak");
        button.setOnClickListener(v -> launch(packageName));
        return button;
    }

    private LinearLayout.LayoutParams buttonParams() {
        return new LinearLayout.LayoutParams(dp(54), dp(54));
    }

    private boolean handleDrag(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getRawX();
                downY = event.getRawY();
                startX = params.x;
                startY = params.y;
                dragged = false;
                return true;
            case MotionEvent.ACTION_MOVE:
                float dx = event.getRawX() - downX;
                float dy = event.getRawY() - downY;
                if (Math.abs(dx) > dp(5) || Math.abs(dy) > dp(5)) dragged = true;
                params.x = Math.max(0, startX - (int) dx);
                params.y = Math.max(0, startY - (int) dy);
                windowManager.updateViewLayout(dock, params);
                return true;
            case MotionEvent.ACTION_UP:
                if (dragged) {
                    getSharedPreferences("dock", MODE_PRIVATE).edit()
                            .putInt("x", params.x)
                            .putInt("y", params.y)
                            .apply();
                }
                return true;
            default:
                return false;
        }
    }

    private void launch(String packageName) {
        Intent launch = getPackageManager().getLaunchIntentForPackage(packageName);
        if (launch == null) {
            Toast.makeText(this, "未能開啟程式", Toast.LENGTH_SHORT).show();
            return;
        }
        launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(launch);
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL, "Kodak Dock", NotificationManager.IMPORTANCE_MIN);
            channel.setDescription("保持浮動按鈕運行");
            channel.setShowBadge(false);
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                    .createNotificationChannel(channel);
        }
    }

    private Notification notification() {
        Intent open = new Intent(this, MainActivity.class);
        PendingIntent pending = PendingIntent.getActivity(
                this, 0, open, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = Build.VERSION.SDK_INT >= 26
                ? new Notification.Builder(this, CHANNEL)
                : new Notification.Builder(this);
        return builder
                .setSmallIcon(R.drawable.ic_dock)
                .setContentTitle("Kodak Dock")
                .setContentText("LocalSend／Kodak 快捷按鈕運行中")
                .setContentIntent(pending)
                .setOngoing(true)
                .build();
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
