package com.sickfuture.letswatch.app.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.sickfuture.letswatch.R;
import com.sickfuture.letswatch.app.fragment.InfoFragment;

public class MovieDetailsActivity extends SherlockFragmentActivity implements
		TabListener {

	private static final int FRAGMENTS_COUNT = 1;

	public static final int INFO_FRAGMENT = 0;

	private FragmentPagerAdapter mPagerAdapter;

	private ViewPager mViewPager;
	
	private BroadcastReceiver mBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_details);
		mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return FRAGMENTS_COUNT;
			}

			@Override
			public Fragment getItem(int position) {
				switch (position) {
				case INFO_FRAGMENT:
					InfoFragment fr = new InfoFragment();
					fr.setArguments(getIntent().getBundleExtra(MainActivity.ARGUMENTS));
					return fr;
				default:
					break;
				}
				return null;
			}

			@Override
			public CharSequence getPageTitle(int position) {
				switch (position) {
				case INFO_FRAGMENT:
					return getString(R.string.title_info).toUpperCase(); 

				default:
					break;
				}
				return super.getPageTitle(position);
			}

		};
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);
		mViewPager = (ViewPager) findViewById(R.id.viewpager_fragment_movie_details);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});
		for (int i = 0; i < mPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
