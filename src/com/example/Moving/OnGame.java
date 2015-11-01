package com.example.Moving;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;

@SuppressLint("NewApi")
public class OnGame extends Activity implements OnTouchListener,OnClickListener {

	private final static String LOG_TAG="OnGame";

	private boolean musicOn=true;
	private boolean gameOn=true;

	private int onClick;
	private SoundPool soundPool;

	private int difficulty = MainList.difficulty-1;
	private int screenWidth;
	private int screenHeight;
	private int lastX;
	private int lastY;
	private TextView timeText;
	private ImageView image,firstBlock;
	private Timer timer = new Timer();
	private Timer TimerMove;

	private Button restart;
	private Button resume;
	private Button music;
	private MediaPlayer mp;

	private final static int TIME_1S = 1;
	private final static int RESET_TIME = 2;
	private final static int ACCELERATOR_START = 3;
	private final static int ACCELERATOR_STOP = 4;
	private final static int MOVE_IMAGE = 5;
	private final static int COLLISION = 6;
	private final static int NEXT_LEVEL = 7;
	private final static int BALL_START = 8;

	private int level = 0;

	private Builder gameOver ,nextLevel;

	private SensorManager SManager ;
	private Sensor sensor ;
	private float acceleratorX = 0, acceleratorY = 0;
	private float speedX = 0, speedY = 0;
	private SensorEventListener listener;

	private ArrayMap arrayMap;

	private TimerTask timerTask = new TimerTask(){
		public void run(){
			Message msg = new Message();
			msg.what = TIME_1S;
			handler.sendMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.game);

		soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,5);
		onClick=soundPool.load(this,R.raw.normalclick,1);

		mp=MediaPlayer.create(this,R.raw.main_music);
		mp.setLooping(true);
		mp.start();

		restart=(Button)findViewById(R.id.restart_btn);
		resume=(Button)findViewById(R.id.game_btn);
		music=(Button)findViewById(R.id.voice_btn);
		restart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO:restart the game
			}
		});

		resume.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(gameOn)
					findViewById(R.id.game_btn).setBackgroundResource(R.drawable.resume);
				else
					findViewById(R.id.game_btn).setBackgroundResource(R.drawable.ongoing);
				//TODO:pause the game
				gameOn=!gameOn;
			}
		});

		music.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(musicOn) {
					mp.pause();
					findViewById(R.id.voice_btn).setBackgroundResource(R.drawable.voice_off);
				}else {
					mp.start();
					findViewById(R.id.voice_btn).setBackgroundResource(R.drawable.voice_on);
				}
				musicOn=!musicOn;
			}
		});


		DisplayMetrics dm = getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels - 50;
		timeText=(TextView)findViewById(R.id.time);
		timeText.setText("60");

		image = (ImageView)findViewById(R.id.image);
		image.setOnTouchListener(this);

		timer.schedule(timerTask, 1000,1000);

		gameOver = new Builder(OnGame.this);
		gameOver.setTitle("Game T_T Over!");
		gameOver.setCancelable(false);
		gameOver.setPositiveButton("Retry", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface gameOver, int which) {

				reStart();
				SManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_FASTEST);

				MessageSend(RESET_TIME);
			}
		});

		gameOver.setNegativeButton("Quit", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface gameOver, int which) {
				onBackPressed();
			}
		});

		nextLevel = new Builder(OnGame.this);
		nextLevel.setTitle("Congratulations!^_^");
		nextLevel.setCancelable(false);
		nextLevel.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface nextLevel, int which) {
				NewLevel();
				MessageSend(RESET_TIME);
				BallStart();

			}
		});
		nextLevel.setNegativeButton("Rest", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface nextLevel, int which) {
				finish();
			}
		});

		restart = (Button)findViewById(R.id.restart_btn);
		restart.setOnClickListener(OnGame.this);

		SManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		sensor = SManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		listener = new SensorEventListener() {

			@Override
			public void onSensorChanged(SensorEvent event) {
				acceleratorX = event.values[0];
				acceleratorY = event.values[1];
			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {

			}
		};
		SManager.registerListener(listener, sensor,SensorManager.SENSOR_DELAY_FASTEST);

		TimerMove = new Timer();
		TimerTask moveTask = new TimerTask() {

			@Override
			public void run() {

				Message msg = new Message();
				msg.what = MOVE_IMAGE;
				handler.sendMessage(msg);

			}
		};
		TimerMove.schedule(moveTask, 25, 25);

		NewLevel();



	}

	public static void setLayout(View view,int x,int y)
	{
		ViewGroup.MarginLayoutParams margin=new ViewGroup.MarginLayoutParams(view.getLayoutParams());
		margin.setMargins(x,y, x+margin.width, y+margin.height);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
		view.setLayoutParams(layoutParams);
	}




	public void onWindowFocusChanged(boolean hasFocus) {

		//TODO Auto-generated method stub

		super.onWindowFocusChanged(hasFocus);
		TableLayout map1 = (TableLayout) findViewById(R.id.map);


		int left = map1.getLeft();
		int top = map1.getTop();
		arrayMap.setPara(left, top);

//			BallStart();
	}
	@Override
	protected void onStart() {
		super.onDestroy();
		BallStart();
	};


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {

		int action=event.getAction();
		switch(action){
			case MotionEvent.ACTION_DOWN:
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				int dx =(int)event.getRawX() - lastX;
				int dy =(int)event.getRawY() - lastY;

				int left = v.getLeft() + dx;
				int top = v.getTop() + dy;
				int right = v.getRight() + dx;
				int bottom = v.getBottom() + dy;

				if(left < 20){
					left = 20;
					right = left + v.getWidth();
				}
				if(right > screenWidth-20){
					right = screenWidth-20;
					left = right - v.getWidth();
				}
				if(top < 100){
					top = 100;
					bottom = top + v.getHeight();
				}
				if(bottom > screenHeight-20){
					bottom = screenHeight-20;
					top = bottom - v.getHeight();
				}
				if(arrayMap.isCollision(left,top,right,bottom)!=0){
					MessageSend(COLLISION);
					if(arrayMap.isCollision(left,top,right,bottom) == 2){
						speedY = 0;
						top = v.getTop() ;
						bottom = v.getBottom() ;
					}
					else if(arrayMap.isCollision(left,top,right,bottom) == 1){
						left = v.getLeft() ;
						right = v.getRight() ;
						speedX = 0;
					}
				}
				else if(arrayMap.isArrive(left,top,right,bottom)){
					MessageSend(NEXT_LEVEL);
					left = v.getLeft() ;
					top = v.getTop() ;
					right = v.getRight() ;
					bottom = v.getBottom() ;
					speedX = speedY = 0;
				}
				else if(arrayMap.isDead(left,top,right,bottom)){
//					SManager.registerListener(null, sensor,SensorManager.SENSOR_DELAY_FASTEST);
					left = v.getLeft() ;
					top = v.getTop() ;
					right = v.getRight() ;
					bottom = v.getBottom() ;
					speedX = speedY = 0;
				}
				v.layout(left, top, right, bottom);
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				break;
			case MotionEvent.ACTION_UP:
				break;
		}
		return false;
	}

	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
				case TIME_1S:
					int timeLeft = Integer.parseInt(timeText.getText().toString()) - 1;
					timeText.setText(Integer.toString(timeLeft));
					if(timeLeft == 0){
						timer.cancel();
						gameOver.setMessage("Time is up! Again ?");
						gameOver.show();
					}
					break;
				case RESET_TIME:
					timeText.setText("100");
					timer = new Timer();


					timerTask = new TimerTask(){
						public void run(){
							Message msg = new Message();
							msg.what = TIME_1S;
							handler.sendMessage(msg);
						}
					};
					timer.schedule(timerTask, 1000,1000);
					break;

				case MOVE_IMAGE:
					if((speedX > 0 && acceleratorX < 0) || (speedX < 0 && acceleratorX >0))
						speedX -= 2*acceleratorX;
					else speedX -= 0.1* acceleratorX;

					if((speedY > 0 && acceleratorY < 0) || (speedY < 0 && acceleratorY >0))
						speedY += 2*acceleratorY;
					else speedY += 0.1 * acceleratorY;

					int dx = (int)(speedX * 0.05);
					int dy = (int)(speedY * 0.05);
					View v = findViewById(R.id.image);
					int left = v.getLeft() + dx;
					int top = v.getTop() + dy;
					int right = v.getRight() + dx;
					int bottom = v.getBottom() + dy;

					if(left < 20){
						left = 20;
						right = left + v.getWidth();
						speedX = 0;
					}
					if(right > screenWidth-20){
						right = screenWidth-20;
						left = right - v.getWidth();
						speedX = 0;
					}
					if(top < 100){
						top = 100;
						bottom = top + v.getHeight();
						speedY = 0;
					}
					if(bottom > screenHeight-20){
						bottom = screenHeight-20;
						top = bottom - v.getHeight();
						speedY = 0;
					}

					if(arrayMap.isCollision(left,top,right,bottom)!=0){

						MessageSend(COLLISION);
						top = v.getTop() ;
						bottom = v.getBottom() ;

						if(arrayMap.isCollision(left,top,right,bottom) == 1){
							speedY = 0;
							top = v.getTop() ;
							bottom = v.getBottom() ;
						}
						else if(arrayMap.isCollision(left,top,right,bottom) == 2){
							speedX = 0;
							left = v.getLeft() ;
							right = v.getRight() ;

						}
					}
					else if(arrayMap.isArrive(left,top,right,bottom)){
						MessageSend(NEXT_LEVEL);
						left = v.getLeft() ;
						top = v.getTop() ;
						right = v.getRight() ;
						bottom = v.getBottom() ;

						LinearLayout map = (LinearLayout) findViewById(R.id.map);
						map.removeAllViews();

						speedX = speedY = 0;
					}
					else if(arrayMap.isDead(left,top,right,bottom)){
						left = v.getLeft() ;
						top = v.getTop() ;
						right = v.getRight() ;
						bottom = v.getBottom() ;
						speedX = speedY = 0;
						removeImage();
						gameOver.show();
						MessageSend(ACCELERATOR_STOP);
					}

					v.layout(left, top, right, bottom);
					break;
				case NEXT_LEVEL:
					LevelDatabaseHelper dbhelper = new LevelDatabaseHelper(OnGame.this,"LevelData",1);
					SQLiteDatabase db = dbhelper.getReadableDatabase();
//	    			db.execSQL("update levellock set lock=? where difficulty=? and level=?",
//	    					new Object[]{0,difficulty,level+1});
					LevelDatabaseHelper.Unlock(db,level,difficulty);
					nextLevel.show();
					break;
				case BALL_START:
//	    			BallStart();
					break;
				case ACCELERATOR_STOP:
					SManager.unregisterListener(listener);
					acceleratorX=0;
					acceleratorY=0;
					speedX=0;
					speedY=0;
					break;
			}
		}
	};



	@Override
	public void onClick(View v) {

		int[] location = new int[2];
		firstBlock.getLocationOnScreen(location);
		LinearLayout map = (LinearLayout) findViewById(R.id.map);
		Log.v("Left",""+location[0]);
		Log.v("Top",""+location[1]);
		Log.v("Left",""+map.getLeft());
		Log.v("Top",""+map.getTop());
//				case R.id.restart:
//					BallStart();
	}

	public void NewLevel(){
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		//int height = size.y;

		int b_size =(int) ((20 * width  / 320)  * 0.9);//直接通过分辨率和bp算px
		if(level==5){
			Toast.makeText(OnGame.this, "You win!", Toast.LENGTH_SHORT).show();
			return;
		}
		else
			arrayMap = new ArrayMap((difficulty+1)*8,level++,b_size);;

		removeImage();
		putImage(b_size);

		//Toast.makeText(getApplicationContext(), Integer.toString(b_size), Toast.LENGTH_SHORT).show();

	}

	public void putImage(int b_size){
		LinearLayout map = (LinearLayout) findViewById(R.id.map);
		int [][] things = arrayMap.getArray();//获得二维数组
		ImageView[] hole = new ImageView[16];
		//图片直接使用位图缩放

		Bitmap test2 = Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(this.getBaseContext().getResources(),R.drawable.testbmp),
				b_size , b_size, true);
		Bitmap test1 =
				Bitmap.createScaledBitmap(
						BitmapFactory.decodeResource(this.getBaseContext().getResources(), R.drawable.testbmp2),
						b_size, b_size, true);
		Bitmap test3 =
				Bitmap.createScaledBitmap(
						BitmapFactory.decodeResource(this.getBaseContext().getResources(), R.drawable.testbmp3),
						b_size, b_size, true);


		for(int rowC = 0; rowC < 21; rowC++)
		{
			TableRow tr = new TableRow(this);
			for(int colC=0 ; colC< hole.length; colC++)
			{
				hole[colC] = new ImageView(this);
				LayoutParams lparam = (LayoutParams)hole[colC].getLayoutParams();
				hole[colC].setLayoutParams(new LayoutParams(32, 32));
				hole[colC].setAdjustViewBounds(false);//设置边界对齐
				hole[colC].setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
				hole[colC].setPadding(0, 0, 0, 0);//设置间距
				if(rowC==0 && colC==0){
					firstBlock = hole[colC];
				}

				hole[0].setId(rowC);
				int temp;

				switch(things[rowC][colC])
				{
					case 0://没有东西
						//hole[colC].setImageResource(R.drawable.test5);
						hole[colC].setImageBitmap(test1);
						break;
					case 1://墙
						//hole[colC].setImageResource(R.drawable.test3);
						break;
					case 2://洞
						//hole[colC].setImageResource(R.drawable.test2);
						hole[colC].setImageBitmap(test2);
						break;
					case 3://终点
						//hole[colC].setImageResource(R.drawable.test4);
						hole[colC].setImageBitmap(test3);
						break;
				}

				TableRow.LayoutParams lp = new TableRow.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
				tr.addView(hole[colC],lp);
				//size = hole[colC].getRight() - hole[colC].getLeft() ;
			}
			map.addView(tr);
		}
		map = (LinearLayout)findViewById(R.id.map);
		map.bringChildToFront(image);
	}


	public void removeImage(){
		LinearLayout map = (LinearLayout) findViewById(R.id.map);
		map.removeAllViews();
	}


	public void MessageSend(int what){
		Message msg = new Message();
		msg.what = what;
		handler.sendMessage(msg);
	}

	public void reStart(){
		level--;
		NewLevel();
	}

	public void BallStart(){
		int height = image.getBottom() - image.getTop();
		int width  = image.getRight() - image.getLeft();
		Log.v("hehe",""+arrayMap.getStart());
		image.layout(arrayMap.getStart().x - width/2, arrayMap.getStart().y - height /2,arrayMap.getStart().x - width/2 + width , arrayMap.getStart().y - height/2 + height);

	}

//		public boolean Arrive(){
//			Point end = map.getEnd();
//			if(end.x < image.getRight() && end.x > image.getLeft() 
//					&& end.y <image.getBottom() && end.y > image.getTop())
//				return true;
//			else return false;
//		}

	@Override
	public void onBackPressed(){
		Log.d(LOG_TAG, "onBackPressed");
		soundPool.play(onClick, 1.0F, 1.0F, 0, 0, 1.0F);
		Intent intent=new Intent(OnGame.this,MainList.class);
		startActivity(intent);
		overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
		finish();
	}

	public void onResume(){
		super.onResume();
		if(musicOn==true)
		{
			mp.start();
			findViewById(R.id.voice_btn).setBackgroundResource(R.drawable.voice_on);
		}else{
			findViewById(R.id.voice_btn).setBackgroundResource(R.drawable.voice_off);

		}
		Log.d(LOG_TAG,"Game:Resume");
	}

	@Override
	public void onPause(){
		super.onPause();
		mp.pause();
		Log.d(LOG_TAG,"Game:Pause");
	}

	public void Passed(){
		//TODO:set animation and turn to the next stage if current level is not 6
		LevelDatabaseHelper databaseHelper;
		databaseHelper = new LevelDatabaseHelper(OnGame.this,"LevelData",1);
		SQLiteDatabase db;
		db=databaseHelper.getReadableDatabase();
		LevelDatabaseHelper.Unlock(db,0,0);
	}

}
