package com.someone.drawingboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private MyView mView;
    private Button mCleanBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mView = (MyView) findViewById(R.id.id_myview);
        mCleanBtn = (Button) findViewById(R.id.id_clean_btn);

        mCleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.clear();
            }
        });
    }
}
