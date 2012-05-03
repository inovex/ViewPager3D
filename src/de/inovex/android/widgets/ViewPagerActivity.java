package de.inovex.android.widgets;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class ViewPagerActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ViewPager pager = (ViewPager) findViewById(R.id.awesomepager);
        pager.setAdapter(new AwesomePagerAdapter());
    }
    
    private class AwesomePagerAdapter extends PagerAdapter{

        
        @Override
        public int getCount() {
                return 4;
        }

    /**
     * Create the page for the given position.  The adapter is responsible
     * for adding the view to the container given here, although it only
     * must ensure this is done by the time it returns from
     * {@link #finishUpdate()}.
     *
     * @param container The containing View in which the page will be shown.
     * @param position The page position to be instantiated.
     * @return Returns an Object representing the new page.  This does not
     * need to be a View, but can be some other container of the page.
     */
        @Override
        public Object instantiateItem(View collection, int position) {
        		ImageView tv = new ImageView(getApplicationContext());
                int r = (int) (Math.random()*255);
                int g = (int) (Math.random()*255);
                int b = (int) (Math.random()*255);
                ViewGroup.LayoutParams params;
               	params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                tv.setBackgroundColor(Color.argb(255, 255-r, 255-g, 255-b));
                switch(position){
                case 0:
                    tv.setImageDrawable(getResources().getDrawable(R.drawable.p1)); break;
                case 1:
                    tv.setImageDrawable(getResources().getDrawable(R.drawable.p2)); break;
                case 2:
                    tv.setImageDrawable(getResources().getDrawable(R.drawable.p3)); break;
                case 3:
                    tv.setImageDrawable(getResources().getDrawable(R.drawable.p4)); break;
                }
                tv.setScaleType(ScaleType.CENTER);
                //tv.setCompoundDrawables(left, top, right, bottom)
                ((ViewPager) collection).addView(tv,params);
                tv.setTag(position);
                return tv;
                
            
        }

    /**
     * Remove a page for the given position.  The adapter is responsible
     * for removing the view from its container, although it only must ensure
     * this is done by the time it returns from {@link #finishUpdate()}.
     *
     * @param container The containing View from which the page will be removed.
     * @param position The page position to be removed.
     * @param object The same object that was returned by
     * {@link #instantiateItem(View, int)}.
     */
        @Override
        public void destroyItem(View collection, int position, Object view) {
                ((ViewPager) collection).removeView((ImageView) view);
        }

        
        
        @Override
        public boolean isViewFromObject(View view, Object object) {
                return view==((ImageView)object);
        }

        
    /**
     * Called when the a change in the shown pages has been completed.  At this
     * point you must ensure that all of the pages have actually been added or
     * removed from the container as appropriate.
     * @param container The containing View which is displaying this adapter's
     * page views.
     */
        @Override
        public void finishUpdate(View arg0) {}
        

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {}

        @Override
        public Parcelable saveState() {
                return null;
        }

        @Override
        public void startUpdate(View arg0) {}

}
}