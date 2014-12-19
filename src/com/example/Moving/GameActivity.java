package com.example.Moving;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends Activity implements OnTouchListener {
	
//	private Map map;
	
	int screenWidth;  
    int screenHeight;  
    int lastX;  
    int lastY; 
	private TextView timeText;
	private ImageView image;
	private Timer timer = new Timer();
	private Timer TimerMove;

	private final static int TIME_1S = 1;
	private final static int RESET_TIME = 2;
	private final static int ACCELERATOR_START = 3;
	private final static int ACCELERATOR_STOP = 4;
	private final static int MOVE_IMAGE = 5;
	private final static int COLLISION = 6;
	private final static int NEXT_LEVEL = 7;
	
	private Builder gameOver ,nextLevel;
	
	private SensorManager SManager ;
	private Sensor sensor ;
	private float acceleratorX = 0, acceleratorY = 0;
	private float speedX = 0, speedY = 0;
	private SensorEventListener listener;
	
	
	
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


		DisplayMetrics dm = getResources().getDisplayMetrics();

        screenWidth = dm.widthPixels;  
        screenHeight = dm.heightPixels - 50;

        timeText=(TextView)findViewById(R.id.time);
        timeText.setText("60");
        image = (ImageView)findViewById(R.id.image);
        image.setOnTouchListener(this);
        
        timer.schedule(timerTask, 1000,1000);
          
        gameOver = new Builder(GameActivity.this);
        gameOver.setTitle("Game T_T Over!");
        gameOver.setCancelable(false);
        gameOver.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
		
			@Override
			public void onClick(DialogInterface gameOver, int which) {
				
				MessageSend(RESET_TIME);
//				BallStart();
			}
		});
        
        gameOver.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface gameOver, int which) {
				finish();
				
			}
		});
        
        nextLevel = new Builder(GameActivity.this);
        nextLevel.setTitle("Congratulations!^_^");
        nextLevel.setCancelable(false);
        nextLevel.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface nextLevel, int which) {
//				NewLevel();
				MessageSend(RESET_TIME);
//				BallStart();
				
			}
		});
        nextLevel.setNegativeButton("Rest", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface nextLevel, int which) {
				finish();
			}
		});
        
        SManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor = SManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

//      NewLevel();
//      BallStart();

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
		TimerMove.schedule(moveTask, 5, 5);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.gamemenu, menu);
		return true;
	}

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
	            
//	            if(IsCollision())
//	            	MessageSend(COLLISION);
	            
//	            if(Arrive()){
//	            	MessageSend(NEXT_LEVEL);
//	            }
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
	            v.layout(left, top, right, bottom);  
	            lastX = (int) event.getRawX();  
	            lastY = (int) event.getRawY();                    
	            break;  
	        case MotionEvent.ACTION_UP:  
	            break;                
	        }  
	        return false;     
	 }


//	Message msg= new Message();
//	msg.what = ACCELERATOR_START;
//
	    private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			int paddingTop=findViewById(R.id.title).getHeight()+10;
			int paddingSide=10;
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
	    			timeText.setText("10");
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
	    		case ACCELERATOR_START:
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
					TimerMove.schedule(moveTask, 5, 5);
	    			break;
	    		case ACCELERATOR_STOP:
	    			SManager.unregisterListener(listener);
	    			TimerMove.cancel();
	    			speedX = speedY = acceleratorX = acceleratorY = 0;
	    			break;
	    		case MOVE_IMAGE:
	    			if((speedX > 0 && acceleratorX < 0) || (speedX < 0 && acceleratorX >0))
	    				speedX -= 0.5*acceleratorX;
	    			else speedX -= 0.5* acceleratorX;
	    			if((speedY > 0 && acceleratorY < 0) || (speedY < 0 && acceleratorY >0))
	    				speedY += 0.5*acceleratorY;
	    			else speedY += 0.5 * acceleratorY;
					int dx = (int)(speedX * 0.05);
		            int dy = (int)(speedY * 0.05);
		            View v = findViewById(R.id.image);
		            int left = v.getLeft() + dx;  
		            int top = v.getTop() + dy;  
		            int right = v.getRight() + dx;  
		            int bottom = v.getBottom() + dy;
		            
		            if(left < paddingSide){  
		                left = paddingSide;  
		                right = left + v.getWidth();
		                speedX = 0;
		            }                     
		            if(right > screenWidth-paddingSide){  
		                right = screenWidth-paddingSide;  
		                left = right - v.getWidth();  
		                speedX = 0;
		            }                     
		            if(top < paddingTop){
		                top = paddingTop;
		                bottom = top + v.getHeight();  
		                speedY = 0;
		            }                     
		            if(bottom > screenHeight-paddingSide){  
		                bottom = screenHeight-paddingSide;  
		                top = bottom - v.getHeight();
		                speedY = 0;
		            }   
		            v.layout(left, top, right, bottom);
	    			break;
	    		
	    		case COLLISION:
	    			timer.cancel();
	    			gameOver.setMessage("You failed! Try again?");
	    			break;
	    			
	    		case NEXT_LEVEL:
	    			nextLevel.show();
	    			break;
	    			
	    		}
	    	}
	    };
		
	public void MessageSend(int what){
			Message msg = new Message();
			msg.what = what;
			handler.sendMessage(msg);
	}

	public static int convertDpToPx(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

}
