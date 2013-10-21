package de.inovex.android.widgets.example;

import java.lang.ref.WeakReference;
import java.util.Stack;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ViewPagerActivity extends Activity {

	LayoutInflater mInflater;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.main);
		mInflater = getLayoutInflater();
		ViewPager pager = (ViewPager) findViewById(R.id.awesomepager);
		pager.setAdapter(new AwesomePagerAdapter());
		// Bind the title indicator to the adapter
		// CirclePageIndicator circleIndicator = (CirclePageIndicator)
		// findViewById(R.id.circles);
		// circleIndicator.setViewPager(pager);
		// TabPageIndicator tabIndicator = (TabPageIndicator)
		// findViewById(R.id.tabs);
		// tabIndicator.setViewPager(pager);
		// TitlePageIndicator titleIndicator = (TitlePageIndicator)
		// findViewById(R.id.titles);
		//titleIndicator.setViewPager(pager);
	}

	private class AwesomePagerAdapter extends PagerAdapter {

		private Stack<WeakReference<View>> mViews = new Stack<WeakReference<View>>();

		@Override
		public int getCount() {
			return 5;
		}

		/**
		 * Create the page for the given position. The adapter is responsible
		 * for adding the view to the container given here, although it only
		 * must ensure this is done by the time it returns from
		 * {@link #finishUpdate()}.
		 * 
		 * @param container
		 *            The containing View in which the page will be shown.
		 * @param position
		 *            The page position to be instantiated.
		 * @return Returns an Object representing the new page. This does not
		 *         need to be a View, but can be some other container of the
		 *         page.
		 */
		@Override
		public Object instantiateItem(View collection, int position) {
			WeakReference<View> refView = null;
			View v = null;
			while (v == null && mViews.size() > 0) {
				refView = mViews.pop();
				v = refView.get();
			}
			View tv;
			if (v != null) {
				refView.clear();
				tv = v;
			} else {
				tv = mInflater.inflate(R.layout.page_item, null);
			}
			final int r = (int) (Math.random() * 80);
			final int g = (int) (Math.random() * 50);
			final int b = (int) (Math.random() * 100);
			tv.setBackgroundColor(Color.argb(255, r, g, b));
			// tv.setCompoundDrawables(left, top, right, bottom)
			((ViewPager) collection).addView(tv);
			return tv;

		}

		/**
		 * Remove a page for the given position. The adapter is responsible for
		 * removing the view from its container, although it only must ensure
		 * this is done by the time it returns from {@link #finishUpdate()}.
		 * 
		 * @param container
		 *            The containing View from which the page will be removed.
		 * @param position
		 *            The page position to be removed.
		 * @param object
		 *            The same object that was returned by
		 *            {@link #instantiateItem(View, int)}.
		 */
		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((View) view);
			mViews.push(new WeakReference<View>((View) view));

		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}

		/**
		 * Called when the a change in the shown pages has been completed. At
		 * this point you must ensure that all of the pages have actually been
		 * added or removed from the container as appropriate.
		 * 
		 * @param container
		 *            The containing View which is displaying this adapter's
		 *            page views.
		 */
		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}
}