package com.example.platform;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;


public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener,View.OnClickListener,RadioGroup.OnCheckedChangeListener {
    public static User user;
    private FragmentManager fragmentManager;
    private TranslateFragment translateFragment=null;
    private FragmentTransaction transaction;
    private BottomNavigationBar bottomNavigationBar;
    private ListFragment listFragment=null;
    private ProfileFragment profileFragment=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user=(User)getIntent().getSerializableExtra("user");
        fragmentManager=getSupportFragmentManager();
        transaction=fragmentManager.beginTransaction();
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_launcher_background, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.ic_launcher_background, "练习"))
                .addItem(new BottomNavigationItem(R.drawable.ic_launcher_background, "翻译"))
                .addItem(new BottomNavigationItem(R.drawable.ic_launcher_background, "活动"))
                .addItem(new BottomNavigationItem(R.drawable.ic_launcher_background, "我的"))
                .initialise();
    }
    @Override
    public void onTabSelected(int position) {
  //      Toast.makeText(this, position, Toast.LENGTH_SHORT).show();
     //   ori.setText(position);
        fragmentManager=getSupportFragmentManager();
        transaction=fragmentManager.beginTransaction();
        switch (position) {
            case 0:
                if(listFragment==null)
                    listFragment=listFragment.newInstance("aaa");
                transaction.replace(R.id.container,listFragment);
                transaction.commit();
                break;
            case 1:
                break;
            case 2:
                if(translateFragment==null)
                    translateFragment=translateFragment.newInstance("aaa");
                transaction.replace(R.id.container,translateFragment);
                transaction.commit();
                break;
            case 4:
                if(profileFragment==null)
                    profileFragment=profileFragment.newInstance("aaa");
                transaction.replace(R.id.container,profileFragment);
                transaction.commit();
                break;
            default:
                break;
        }

    }

    @Override
    public void onTabUnselected(int position) {
        Log.e("dongtaiFragment", "onTabUnselected() called with: " + "position = [" + position + "]");
    }

    @Override
    public void onTabReselected(int position) {

    }
    @Override
    public void onClick(View v){
    }
    @Override
    public void onCheckedChanged(RadioGroup group,int checkedId) {
    }
}
