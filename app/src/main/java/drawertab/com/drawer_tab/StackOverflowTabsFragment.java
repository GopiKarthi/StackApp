package drawertab.com.drawer_tab;

import android.os.Bundle;
import android.support.design.widget.*;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by gopinath.munusamy on 8/13/2016.
 */
public class StackOverflowTabsFragment extends Fragment  {
//    PagerTitleStrip pagerTabStrip;
ViewPager viewPager;
    QuestionsSortPagerAdapter p;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View inflatedView = inflater.inflate(R.layout.stack_of_sort_tabs, container, false);


        final TabLayout tabLayout = (TabLayout) inflatedView.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Votes"));
        tabLayout.addTab(tabLayout.newTab().setText("Activity"));
        tabLayout.addTab(tabLayout.newTab().setText("Hot"));
        tabLayout.addTab(tabLayout.newTab().setText("Creation Date"));
        tabLayout.addTab(tabLayout.newTab().setText("Month"));

        viewPager = (ViewPager) inflatedView.findViewById(R.id.viewpager);
        p = new QuestionsSortPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(p);
//        viewPager.invalidate();
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                Toast.makeText(getContext(),"Me",Toast.LENGTH_SHORT).show();
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return inflatedView;


    }

}
