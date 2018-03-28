package com.ganlen.compartamosviaje.ruleta_file;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import com.ganlen.compartamosviaje.R;
import java.util.ArrayList;

public class RuletaView extends SurfaceView implements SurfaceHolder.Callback {
	private static final int MIN_FORCE = 100;
	private static final int FRAME = 5;
	private int mFPS = 20;
	private long mFrameTime = 1000 / mFPS;
	private final Paint mPaint = new Paint();
	private final RectF mRectF = new RectF();
	private final Path mPath = new Path();
	private int mStroke;
	private int mCenterY;
	private int mCenterX;
	private int mRadius;
	private int mSectors;
	private float mSweepAngle;
	private int mAngleOffset = 0;
	private int mLastPointAngle;
	private ArrayList<Coordinate> mPoints = new ArrayList<Coordinate>();
	private int[] mSectorColors;
	private int[] mColors = { 0xFFFA5882, 0xFFFA58F4, 0xFFAC58FA, 0xFF5858FA, 0xFF58ACFA, 0xFF58FAF4, 0xFF58FAAC, 0xFF58FA58, 0xFFACFA58, 0xFFF4FA58, 0xFFFAAC58, 0xFFFA5858, };
	private SurfaceHolder mSurfaceHolder;
	private Context mContext;
	private int mAngleDiff = 0;
	private MediaPlayer mediaPlayer;
	private String[] mAnswers;
	private Cursor mSectorsCursor;
	private RuletaSectorsDbAdapter mDbHelper;

	public RuletaView(Context context) {
		super(context);
		mContext = context;
		initRoulette();
	}

	public RuletaView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initRoulette();
	}

	public RuletaView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initRoulette();
	}

	private void initRoulette() {
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		mediaPlayer = MediaPlayer.create(mContext, R.raw.beat);
		mDbHelper = new RuletaSectorsDbAdapter(mContext);
		mDbHelper.open();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mCenterX = this.getWidth() / 2;
		mCenterY = this.getHeight() / 2;
		mRadius = Math.min(mCenterX, mCenterY);
		int rectRad = mRadius - FRAME;
		mRectF.set(mCenterX - rectRad, mCenterY - rectRad, mCenterX + rectRad, mCenterY + rectRad);
		mStroke = 2;
		mPaint.setStrokeWidth(mStroke);
		mPaint.setAntiAlias(true);
		updatePoints(0);
	}

	public void updateSectors() {
		mSectorsCursor = mDbHelper.fetchAllSectors();
		mSectors = mSectorsCursor.getCount();
		if (mSectors < 2) {
			RuletaInitialization initializer = new RuletaInitialization(mContext);
			Resources res = getResources();
			String[] answers = res.getStringArray(R.array.predefined);
			initializer.initRoulette(answers);
			mSectorsCursor = mDbHelper.fetchAllSectors();
			mSectors = mSectorsCursor.getCount();
		}
		mSweepAngle = 360 / mSectors;
		updateColors();
		updateAnswers();
		updatePoints(0);
	}

	private void updateAnswers() {
		mSectorsCursor = mDbHelper.fetchAllSectors();
		mAnswers = new String[mSectors];
		for (int i = 0; i < mSectors; i++) {
			mSectorsCursor.moveToPosition(i);
			mAnswers[i] = mSectorsCursor.getString(mSectorsCursor.getColumnIndexOrThrow(RuletaSectorsDbAdapter.KEY_BODY));
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Coordinate currentPoint = new Coordinate(event.getX() - mCenterX, event.getY() - mCenterY);
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
				int currentPointAngle = getAngle(currentPoint);
				mAngleDiff = currentPointAngle - mLastPointAngle;
				updatePoints(mLastPointAngle - currentPointAngle);
				repaint();
				mLastPointAngle = currentPointAngle;
			break;
		case MotionEvent.ACTION_UP:
				spin(-1*(int) Math.signum(mAngleDiff)* Math.min(Math.abs(mAngleDiff) * 25,360*3));
			break;
		case MotionEvent.ACTION_DOWN:
				mLastPointAngle = getAngle(currentPoint);
				mAngleDiff = 0;
				//Log.v("MotionEvent", "Action = ACTION_DOWN " + mLastPointAngle);
			break;
		}
		return true;
	}

	private int getAngle(Coordinate point) {
		return (int) Math.toDegrees(Math.atan2(-point.y, point.x));
	}

	public void repaint() {
		Canvas c = null;
		try {
			c = mSurfaceHolder.lockCanvas(null);
			synchronized (mSurfaceHolder) {
				doDraw(c);
			}
		} finally {
			if (c != null) {
				mSurfaceHolder.unlockCanvasAndPost(c);
			}
		}
	}

	protected void doDraw(Canvas canvas) {
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		for (int i = 0; i < mSectors; i++) {
			// Draw sector
			mPaint.setColor(mSectorColors[i]);
			Coordinate myPoint = mPoints.get(i);
			mPath.reset();
			mPath.moveTo(mCenterX, mCenterY);
			mPath.lineTo(myPoint.x, myPoint.y);
			mPath.addArc(mRectF, i * mSweepAngle + mAngleOffset, mSweepAngle);
			mPath.lineTo(mCenterX, mCenterY);
			mPath.close();
			canvas.drawPath(mPath, mPaint);
		}
		// Draw lines
		mPaint.setColor(Color.BLACK);
		for (int i = 0; i < mSectors; i++) {
			Coordinate myPoint = mPoints.get(i);
			canvas.drawLine(mCenterX, mCenterY, myPoint.x, myPoint.y, mPaint);
		}
		mPaint.setColor(Color.WHITE);
		mPaint.setStrokeWidth(mStroke * 15);
		canvas.drawLine(mCenterX + mRadius - FRAME * 15, mCenterY, mCenterX + mRadius + FRAME, mCenterY, mPaint);
		mPaint.setStrokeWidth(mStroke);
	}

	public void spin(int force) {
		boolean show_answer = true;
		int brake = 1;
		int clockwise = (force > 0) ? 1 : -1;
		force = Math.abs(force);
		if (force < MIN_FORCE) {
			show_answer = false;
		}
		while (force > 0) {
			int gradosPorFrame = (int) clockwise * force / mFPS;
			force = force - brake++;
			updatePoints(gradosPorFrame);
			repaint();
			SystemClock.sleep(1000 / mFrameTime);
		}
		showSectorSentence(show_answer);
	}

	private void showSectorSentence(boolean sentence) {
		String text;
		if (sentence) {
			text = mAnswers[getCurrentSector()];
		} else {
			//text = ("Vamos, gira un poco más fuerte");
			text = ("");
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Promo del Día");
		builder.setMessage(text);
		builder.setPositiveButton("Canjear", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
			}
		});
		builder.setNegativeButton("Volver a girar", null);
		Dialog dialog = builder.create();
		dialog.show();
	}

	public int getCurrentSector() {
		return mSectors - 1 - (int) (mAngleOffset / mSweepAngle);
	}

	private void updatePoints(int angle) {
		int currentSector = getCurrentSector();
		mAngleOffset = mAngleOffset + angle;
		while (mAngleOffset < 0) {
			mAngleOffset = mAngleOffset + 360;
		}
		while (mAngleOffset > 360) {
			mAngleOffset = mAngleOffset - 360;
		}
		mPoints.clear();
		float newpointX, newpointY;
		for (int i = 0; i < mSectors; i++) {
			newpointX = (float) Math.cos(Math.toRadians(i * mSweepAngle + mAngleOffset)) * mRadius + mCenterX;
			newpointY = (float) Math.sin(Math.toRadians(i * mSweepAngle + mAngleOffset)) * mRadius + mCenterY;
			mPoints.add(new Coordinate(newpointX, newpointY));
		}
		if (getCurrentSector() != currentSector) {
			mediaPlayer.start();
		}
	}

	private class Coordinate {
		public float x;
		public float y;
		public Coordinate(float newX, float newY) {
			x = newX;
			y = newY;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		repaint();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	private void updateColors() {
		mSectorColors = new int[mSectors];
		switch (mSectors) {
		case 2:
			mSectorColors = new int[] { mColors[3], mColors[8] };
			break;
		case 3:
			mSectorColors = new int[] { mColors[2], mColors[6], mColors[10] };
			break;
		case 4:
			mSectorColors = new int[] { mColors[2], mColors[4], mColors[7], mColors[10] };
			break;
		case 5:
			mSectorColors = new int[] { mColors[1], mColors[3], mColors[5], mColors[7], mColors[10] };
			break;
		case 6:
			mSectorColors = new int[] { mColors[1], mColors[3], mColors[5], mColors[7], mColors[9], mColors[11] };
			break;
		case 7:
			mSectorColors = new int[] { mColors[0], mColors[2], mColors[4], mColors[6], mColors[8], mColors[9], mColors[11] };
			break;
		case 8:
			mSectorColors = new int[] { mColors[0], mColors[2], mColors[3], mColors[5], mColors[7], mColors[8], mColors[9], mColors[11] };
			break;
		case 9:
			mSectorColors = new int[] { mColors[0], mColors[1], mColors[3], mColors[4], mColors[5], mColors[7], mColors[8], mColors[9], mColors[11] };
			break;
		case 10:
			mSectorColors = new int[] { mColors[0], mColors[1], mColors[2], mColors[4], mColors[5], mColors[6], mColors[7], mColors[9], mColors[10], mColors[11] };
			break;
		case 11:
			mSectorColors = new int[] { mColors[0], mColors[1], mColors[2], mColors[3], mColors[4], mColors[6], mColors[7], mColors[8], mColors[9], mColors[10], mColors[11] };
			break;
		case 12:
			mSectorColors = mColors;
			break;
		default:	mSectorColors = mColors;
		}
	}
}