package com.example.trabajofct.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.trabajofct.Adapters.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trabajo1.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabsAlumno extends Fragment {
    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private com.example.trabajofct.Adapters.PagerAdapter adapter;    public TabsAlumno() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tabs_alumno, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Usuarios"));
        tabLayout.addTab(tabLayout.newTab().setText("Grupos"));
        tabLayout.addTab(tabLayout.newTab().setText("Asignaturas"));

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        adapter = new PagerAdapter(getChildFragmentManager(),  tabLayout.getTabCount(), inflater.getContext());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Inflate the layout for this fragment
        return view;
    }


}
