package com.owen.tvrecyclerview.example;

import android.os.Bundle;
import android.util.TypedValue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.owen.focus.FocusBorder;
import com.owen.tab.TvTabLayout;
import com.owen.tvrecyclerview.example.fragment.BaseFragment;
import com.owen.tvrecyclerview.example.fragment.NestedFragment;
import com.owen.tvrecyclerview.utils.Loger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nikhil Gopi on 16-06-2021.
 */
public class ModfiedActivity extends AppCompatActivity implements BaseFragment.FocusBorderHelper{
    private final String LOGTAG = ModfiedActivity.class.getSimpleName();
    private final String[] tabNames = {
            "Home", "Movies", "Originals",
            "Live", "Support"
    };
    private final String[] fragments = {
            NestedFragment.class.getName(), NestedFragment.class.getName(), NestedFragment.class.getName(),
            NestedFragment.class.getName(), NestedFragment.class.getName()
    };

    @BindView(R.id.tab_layout)
    TvTabLayout mTabLayout;

    private FocusBorder mFocusBorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //是否打开TvRecyclerView的log打印
        Loger.isDebug = true;

        // 移动框
        if(null == mFocusBorder) {
            mFocusBorder = new FocusBorder.Builder()
                    .asColor()
                    .borderColorRes(R.color.actionbar_color)
                    .borderWidth(TypedValue.COMPLEX_UNIT_DIP, 3.2f)
                    .shadowColorRes(R.color.green_bright)
                    .shadowWidth(TypedValue.COMPLEX_UNIT_DIP, 22f)
                    .build(this);
        }

        mTabLayout.addOnTabSelectedListener(new TabSelectedListener());
        for(String tabName : tabNames) {
            mTabLayout.addTab(mTabLayout.newTab().setText(tabName));
        }
        mTabLayout.selectTab(0);
    }

    @Override
    public FocusBorder getFocusBorder() {
        return mFocusBorder;
    }

    public class TabSelectedListener implements TvTabLayout.OnTabSelectedListener {
        private Fragment mFragment;

        @Override
        public void onTabSelected(TvTabLayout.Tab tab) {
            final int position = tab.getPosition();
            mFragment = getSupportFragmentManager().findFragmentByTag(position + "");
            FragmentTransaction mFt = getSupportFragmentManager().beginTransaction();
            if (mFragment == null) {
                mFragment = Fragment.instantiate(ModfiedActivity.this, fragments[position]);
                mFt.add(R.id.content, mFragment, String.valueOf(position));
            } else {
                mFt.attach(mFragment);
            }
            mFt.commit();
        }

        @Override
        public void onTabUnselected(TvTabLayout.Tab tab) {
            if (mFragment != null) {
                FragmentTransaction mFt = getSupportFragmentManager().beginTransaction();
                mFt.detach(mFragment);
                mFt.commit();
            }
        }

        @Override
        public void onTabReselected(TvTabLayout.Tab tab) {

        }
    }
}