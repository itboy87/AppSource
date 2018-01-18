package com.hopin.uniq.webapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hopin.uniq.sdk.WebViewActivity;

/**
 * A login screen that offers login via email/password.
 */
public class MainActivity extends AppCompatActivity {


    private EditText mLinkEditText;
    private EditText mColorEditText;
    private EditText mColorBackButtonEditText;
    private EditText mTitleEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLinkEditText = findViewById(R.id.edit_link);
        mColorEditText = findViewById(R.id.edit_color);
        mColorBackButtonEditText = findViewById(R.id.edit_back_button_color);
        mTitleEditText = findViewById(R.id.edit_text);
    }


    public void onclickStart(View view) {
        if (TextUtils.isEmpty(mColorEditText.getText().toString())) {
            Toast.makeText(this, "Enter color", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mColorBackButtonEditText.getText().toString())) {
            Toast.makeText(this, "Enter back button color", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mLinkEditText.getText().toString())) {
            Toast.makeText(this, "Enter link", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mTitleEditText.getText().toString())) {
            Toast.makeText(this, "Enter title", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
                Color.parseColor(mColorEditText.getText().toString());
            Color.parseColor(mColorBackButtonEditText.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "use color format #xxxxxx", Toast.LENGTH_SHORT).show();
            return;
        }

        WebViewActivity.start(this, mLinkEditText.getText().toString(), mColorEditText.getText().toString(), mColorBackButtonEditText.getText().toString(), mTitleEditText.getText().toString());
    }
}