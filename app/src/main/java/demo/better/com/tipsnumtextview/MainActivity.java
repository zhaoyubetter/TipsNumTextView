package demo.better.com.tipsnumtextview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import demo.better.com.tipsnumtextview.ui.TipsNumberView;

public class MainActivity extends AppCompatActivity {

    TipsNumberView tipsText;
    int num = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tipsText = (TipsNumberView) findViewById(R.id.tipsText);

        tipsText.setText(num + "");


        findViewById(R.id.btn_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                tipsText.setText(num + "");
            }
        });
    }
}
