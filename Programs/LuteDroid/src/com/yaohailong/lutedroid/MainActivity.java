package com.yaohailong.lutedroid;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Yaohailong
 */
public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	static SectionsPagerAdapter mSectionsPagerAdapter;
	static ViewPager mViewPager;
	
	static EditText et_IP;
	static Button b_start,b_stop;
	
	ProjectTable PT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		FragmentManager FM = getSupportFragmentManager();
		mSectionsPagerAdapter = new SectionsPagerAdapter(FM);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}
		
		PT = new ProjectTable(this);
	}
	
	@Override
	public void onBackPressed() {
		AlertDialog.Builder d = new AlertDialog.Builder(this);
		d.setTitle("離開?");
		d.setMessage("確定要結束程式嗎？");
		d.setIcon(R.drawable.ic_launcher);
		d.setCancelable(false);
		d.setPositiveButton("確定",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		d.setNegativeButton("取消",null);
		d.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onTabSelected(ActionBar.Tab tab,FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}
	@Override
	public void onTabUnselected(ActionBar.Tab tab,FragmentTransaction fragmentTransaction) {
	}
	@Override
	public void onTabReselected(ActionBar.Tab tab,FragmentTransaction fragmentTransaction) {
	}

	//分頁layout的承接器
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		Fragment ConnectFragment;
		Fragment SettingFragment;
		Fragment AboutFragment;
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new ConnectFragment();
				ConnectFragment = fragment;
				break;
			case 1:
				fragment = new SettingFragment();
				SettingFragment = fragment;
				break;
			case 2:
				fragment = new AboutFragment();
				AboutFragment = fragment;
				break;
			}
			return fragment;
		}
		@Override
		public int getCount() {
			return 3;
		}
		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_section1);
			case 1:
				return getString(R.string.title_section2);
			case 2:
				return getString(R.string.title_section3);
			}
			return null;
		}		
	}
	
	//各分頁定義
	public static class ConnectFragment extends Fragment {
		static EditText et_IP;
		static Button b_start,b_stop;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_connect,container, false);
			et_IP = (EditText)view.findViewById(R.id.et_IP);
			b_start = (Button)view.findViewById(R.id.b_start);
			b_stop = (Button)view.findViewById(R.id.b_stop);
			b_stop.setEnabled(false);
			b_start.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					b_start.setEnabled(false);
					b_stop.setEnabled(true);
				}
			});
			b_stop.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					b_start.setEnabled(true);
					b_stop.setEnabled(false);
				}
			});
			return view;
		}
	}

	public static class SettingFragment extends Fragment {
		public SettingFragment() {
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_setting,container, false);
			return view;
		}
	}
	
	public static class AboutFragment extends Fragment {
		public AboutFragment() {
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_about,container, false);
			return view;
		}
	}
}
