package com.example.plugnapp;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.qihoo360.replugin.RePlugin;

public class MainActivity extends AppCompatActivity {

    private static final String PLUGN_PACkage_NAME = "com.example.plugndemo01";
    private FragmentManager fragmentManager;
    private Fragment showFragment;
    private RadioGroup rg;
    private RadioButton rb_home, rb_my;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rg = findViewById(R.id.rg);
        rb_home = findViewById(R.id.rb_home);
        rb_my = findViewById(R.id.rb_my);

        rb_home.setChecked(true);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        ClassLoader classLoader = RePlugin.fetchClassLoader(PLUGN_PACkage_NAME);
                        try {
                            Fragment fragment = classLoader.loadClass(PLUGN_PACkage_NAME + ".HomePlugnFragment").asSubclass(Fragment.class).newInstance();
                            showFragment(R.id.layout_content, fragment);
                        } catch (Exception e) {
                            Log.e("e", e.toString());
                        }
                        break;
                    case R.id.rb_my:
                        break;
                }
            }
        });


        fragmentManager =getFragmentManager();


        findViewById(R.id.btn01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RePlugin.createIntent(PLUGN_PACkage_NAME, PLUGN_PACkage_NAME + ".MainActivity");
                if (!RePlugin.startActivity(MainActivity.this, intent)) {
                    Toast.makeText(getBaseContext(), "启动失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    /**
     * 显示隐藏Fragment
     */
    protected void showFragment(int resid, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //隐藏正在暂时的Fragment
        if (showFragment != null) {
            fragmentTransaction.hide(showFragment);
        }
        //展示需要显示的Fragment对象
        Fragment mFragment = fragmentManager.findFragmentByTag(fragment.getClass().getName());
        if (mFragment != null) {
            fragmentTransaction.show(mFragment);
            showFragment = (Fragment) mFragment;
        } else {
            fragmentTransaction.add(resid, fragment, fragment.getClass().getName());
            showFragment = fragment;
        }
        fragmentTransaction.commit();

    }


}