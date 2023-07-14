package com.iurd_com.AtividadeDeUsuarios;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.iurd_com.R;

public class Activity_Usuarios extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_usuarios );

        tabLayout = (TabLayout) findViewById( R.id.tabLayoutUsuarios );
        viewPager = (ViewPager) findViewById( R.id.viewPagerUsuarios );
        tabLayout.setupWithViewPager( viewPager );
        viewPager.setAdapter( new AdapterUsuarios( getSupportFragmentManager() ) );
        
        viewPager.addOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0)
                {
                    setTitle( "Fragment 1" );
                }
                else if(position == 1)
                {
                    setTitle( "Fragment 2" );
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        } );

    }
}
