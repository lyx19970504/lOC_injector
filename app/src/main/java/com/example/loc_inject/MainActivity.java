package com.example.loc_inject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.ContentView;
import com.example.mylibrary.InjectManager;
import com.example.mylibrary.InjectView;
import com.example.mylibrary.onClick;
import com.example.mylibrary.onLongClick;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.btn)
    private Button button;
    @InjectView(R.id.tv)
    private TextView textView;

    @Override
    protected void onResume() {
        super.onResume();
        InjectManager.injectInto(this);

        textView.setText("中奥");
        button.setText("名雅居");

    }

    @onClick({R.id.btn,R.id.tv})
    public void show(){
        Toast.makeText(this, "AirPods", Toast.LENGTH_SHORT).show();
    }

    @onLongClick(R.id.tv)
    public boolean show(View view){
        Toast.makeText(this, "AirPods2", Toast.LENGTH_SHORT).show();
        return true;
    }
}
