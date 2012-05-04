package de.inovex.android.widgets;

import android.content.Context;
import android.graphics.Camera;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;

public class ViewPager3D extends ViewPager {

	@SuppressWarnings("unused")
	private final static String DEBUG_TAG = ViewPager.class.getSimpleName();

	private final static int INVALID_POINTER_ID = -1;

	/**
	 * 
	 * @author renard
	 * 
	 */
	private class OverscrollEffect {
		private float mOverscroll;
		private Animator mAnimator;
		private final static int ANIMATION_DURATION = 350;

		/**
		 * @param deltaDistance
		 *            [0..1] 0->no overscroll, 1>full overscroll
		 */
		public void setPull(final float deltaDistance) {
			mOverscroll = deltaDistance;
			invalidate();
		}

		/**
		 * call when finger is released. starts to animate back to default
		 * position
		 */
		private void onRelease() {
			if (mAnimator != null && mAnimator.isRunning()) {
				mAnimator.addListener(new AnimatorListener() {

					@Override
					public void onAnimationStart(Animator animation) {
					}

					@Override
					public void onAnimationRepeat(Animator animation) {
					}

					@Override
					public void onAnimationEnd(Animator animation) {
						startAnimation(0);
					}

					@Override
					public void onAnimationCancel(Animator animation) {
					}
				});
				mAnimator.cancel();
			} else {
				startAnimation(0);
			}
		}

		private void startAnimation(final float target) {
			mAnimator = ObjectAnimator.ofFloat(this, "pull", mOverscroll, target);
			mAnimator.setInterpolator(new DecelerateInterpolator());
			final float scale = Math.abs(target - mOverscroll);
			mAnimator.setDuration((long) (ANIMATION_DURATION * scale));
			mAnimator.start();
		}

		private boolean isOverscrolling() {
			return mOverscroll != 0;
		}

		private float getOverscroll() {
			return mOverscroll;
		}
	}

	private OnPageChangeListener mScrollListener;
	private float mLastMotionX;
	private int mActivePointerId;
	final private OverscrollEffect mOverscrollEffect = new OverscrollEffect();
	private Camera mCamera = new Camera();
	private int mScrollPosition;
	private float mScrollPositionOffset;

	public ViewPager3D(Context context, AttributeSet attrs) {
		super(context, attrs);
		setStaticTransformationsEnabled(true);

		super.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mScrollListener = listener;
	};

	private class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			if (mScrollListener != null) {
				mScrollListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
			}
			mScrollPosition = position;
			mScrollPositionOffset = positionOffset;
		}

		@Override
		public void onPageSelected(int position) {
			if (mScrollListener != null) {
				mScrollListener.onPageSelected(position);
			}
		}

		@Override
		public void onPageScrollStateChanged(final int state) {
			if (mScrollListener != null) {
				mScrollListener.onPageScrollStateChanged(state);
			}
			if (state == SCROLL_STATE_IDLE) {
				mScrollPositionOffset = 0;
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		final int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			mLastMotionX = ev.getX();
			mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			if (mActivePointerId != INVALID_POINTER_ID) {
				// Scroll to follow the motion event
				final int activePointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
				final float x = MotionEventCompat.getX(ev, activePointerIndex);
				final float deltaX = mLastMotionX - x;
				final float oldScrollX = getScrollX();
				final int width = getWidth();
				final int widthWithMargin = width + getPageMargin();
				final int lastItemIndex = getAdapter().getCount() - 1;
				final int currentItemIndex = getCurrentItem();
				final float leftBound = Math.max(0, (currentItemIndex - 1) * widthWithMargin);
				final float rightBound = Math.min(currentItemIndex + 1, lastItemIndex) * widthWithMargin;
				final float scrollX = oldScrollX + deltaX;

				if (scrollX < leftBound) {
					if (leftBound == 0) {
						float over = deltaX;
						mOverscrollEffect.setPull(over / width);
					}
				} else if (scrollX > rightBound) {
					if (rightBound == lastItemIndex * widthWithMargin) {
						float over = scrollX - rightBound;
						mOverscrollEffect.setPull(over / width);
					}
				} else {
					mLastMotionX = x;
				}
			}
			break;
		}
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL: {
			mActivePointerId = INVALID_POINTER_ID;
			mOverscrollEffect.onRelease();
			break;
		}
		case MotionEventCompat.ACTION_POINTER_DOWN: {
			final int index = MotionEventCompat.getActionIndex(ev);
			final float x = MotionEventCompat.getX(ev, index);
			mLastMotionX = x;
			mActivePointerId = MotionEventCompat.getPointerId(ev, index);
			break;
		}
		case MotionEvent.ACTION_POINTER_UP: {
			final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
			if (pointerId == mActivePointerId) {
				// This was our active pointer going up. Choose a new
				// active pointer and adjust accordingly.
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mLastMotionX = ev.getX(newPointerIndex);
				mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
			}
			break;
		}
		}

		if (mOverscrollEffect.isOverscrolling()) {
			return true;
		} else {
			return super.onTouchEvent(ev);
		}
	}

	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		if (child.getWidth() == 0) {
			return false;
		}
		final int position = child.getLeft() / child.getWidth();
		final boolean isFirstOrLast = position == 0 || (position == getAdapter().getCount() - 1);
		if (mOverscrollEffect.isOverscrolling() && isFirstOrLast) {
			final float dx = getWidth() / 2;
			final int dy = getHeight() / 2;
			t.getMatrix().reset();
			mCamera.save();
			float translateZ = (float) (100 * Math.sin((Math.PI) * Math.abs(mOverscrollEffect.getOverscroll())));
			float degrees = 18 - (float) (((180f / Math.PI) * Math.acos(mOverscrollEffect.getOverscroll())) / 5);

			mCamera.rotateY(degrees);
			mCamera.translate(0, 0, translateZ);
			mCamera.getMatrix(t.getMatrix());
			mCamera.restore();
			t.getMatrix().preTranslate(-dx, -dy);
			t.getMatrix().postTranslate(dx, dy);

			return true;
		} else if (mScrollPositionOffset > 0) {

			float dx = getWidth() / 2;
			final int dy = getHeight() / 2;

			double degrees;
			if (position > mScrollPosition) {
				// right side
				degrees = -45 + ((180f / Math.PI) * Math.acos(1 - mScrollPositionOffset)) / 2;
			} else {
				// left side
				degrees = (90 - (180f / Math.PI) * Math.acos(mScrollPositionOffset)) / 2;
			}
			final float translateZ = (float) (100 * Math.sin((Math.PI) * mScrollPositionOffset));

			t.getMatrix().reset();
			mCamera.save();
			mCamera.rotateY((float) degrees);
			mCamera.translate(0, 0, translateZ);
			mCamera.getMatrix(t.getMatrix());
			mCamera.restore();
			// pivot point is center of child
			t.getMatrix().preTranslate(-dx, -dy);
			t.getMatrix().postTranslate(dx, dy);
			return true;
		}
		return false;
	}
}
