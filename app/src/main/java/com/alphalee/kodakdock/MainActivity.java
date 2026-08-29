package com.alphalee.kodakdock;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        showSetup();
        requestOrStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Settings.canDrawOverlays(this)) {
            startService(new Intent(this, DockService.class));
        }
    }

    private void requestOrStart() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } else {
            startService(new Intent(this, DockService.class));
        }
    }

    private void showSetup() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(48, 48, 48, 48);
        root.setBackgroundColor(Color.rgb(246, 248, 247));

        TextView title = new TextView(this);
        title.setText("Kodak Floating Dock");
        title.setTextSize(30);
        title.setTextColor(Color.rgb(22, 36, 33));
        title.setGravity(Gravity.CENTER);

        TextView body = new TextView(this);
        body.setText("\n右下角會顯示 LocalSend 及 Kodak 按鈕。\n拖動按鈕列可移動位置。");
        body.setTextSize(18);
        body.setTextColor(Color.DKGRAY);
        body.setGravity(Gravity.CENTER);

        Button start = new Button(this);
        start.setText("顯示浮動按鈕");
        start.setOnClickListener(v -> requestOrStart());

        root.addView(title, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        root.addView(body, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        root.addView(start, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(root);
    }
}
