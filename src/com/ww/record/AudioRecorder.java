package com.ww.record;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import com.ww.start.R;
import com.ww.start.SensorControl;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;



public class AudioRecorder  extends Activity {
	
	
	MediaRecorder recorder;
	SensorControl sc;
	Context context;
	String path;
	Button audioRecord;
	int URN;
	Boolean recording;
	AlertDialog.Builder builder;
	ArrayList<String> audioFileLines = new ArrayList<String>();
	double [] location = new double[2];
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audiorecorder);
		
		recorder = new MediaRecorder();
		context = this;
		
		sc = new SensorControl(context);
		
		recording = false;
		
		Bundle b = getIntent().getExtras();
		
		location = b.getDoubleArray("Coords");
		
		System.out.println("location double array " + location[0] + " " + location[1]);
		
		//alertdialog builder for saving audio files
		
		builder = new AlertDialog.Builder(context);
		builder.setMessage("Do you want to save your recording?")
		.setCancelable(false)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface d, int id){
				Toast.makeText(context, "Audio File Saved", Toast.LENGTH_SHORT).show();
				
				audioFileLines.add(URN + "," + URN + ".mp3" + "," + location[0] + ","+location[1]);
//				System.out.println(audioFileLines.get(URN));
				writeOutFile();
				
				try {
					stop();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				finish();
				
				
			}

		})
		
		.setNegativeButton("No, don't save", new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				
				Toast.makeText(context, "File not saved", Toast.LENGTH_SHORT).show();
			//delete file here	
				
				File file = new File(Environment.getExternalStorageDirectory().getPath() +"/weather walks/recorded media/"+ URN + ".mp3");
				
				
					file.delete();
					
					try{
						stop();
					}
					catch(IllegalStateException t){
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			
				
			}
			
		});
		
		audioRecord = (Button) findViewById(R.id.starRecordAudioButton);
		
		audioRecord.setBackgroundColor(Color.RED);
		
		audioRecord.setOnClickListener(new OnClickListener(){

			public void onClick(View view) {
			
				if(recording == false){
					
					audioRecord.setBackgroundColor(Color.GREEN);
					
					System.out.println("audioFile size " + audioFileLines.size());
					
					recording = true;
					try {
						start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				else{
					
					AlertDialog alert = builder.create();
					
					audioRecord.setBackgroundColor(Color.RED);
					//recorder.stop();
					
					//recorder.release();
					alert.show();
					
					
					recording = false;
				}
				
				
			}
			
		});
		
		readInFile();
		System.out.println("audioRecordSize " + audioFileLines.size());
		
	}
	
	 public void start() throws IOException {
		    String state = android.os.Environment.getExternalStorageState();
		    if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
		        throw new IOException("SD Card is not mounted.  It is " + state + ".");
		    }

		    // make sure the directory we plan to store the recording in exists
		  // File directory = new File(path).getParentFile();
		 //   if (!directory.exists() && !directory.mkdirs()) {
		   //   throw new IOException("Path to file could not be created.");
		  //  }
		    
		    String path = Environment.getExternalStorageDirectory().getPath() +"/weather walks/recorded media/";

		    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		    recorder.setOutputFile(path+ URN +".mp3");
		    recorder.prepare();
		    recorder.start();
		  }

		  /**
		   * Stops a recording that has been previously started.
		   */
		  public void stop() throws IOException, IllegalStateException {
			  
		    recorder.stop();
		    recorder.release();
		  }
		  
		  
		  public void readInFile(){
			  
			  File file = new File(Environment.getExternalStorageDirectory().getPath() +"/weather walks/recorded media/recorded media.txt"
			  		);
			  
			  try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				
				String line;
				
				while((line = br.readLine())!=null){
					
					audioFileLines.add(line);
		
					
				}
				URN = audioFileLines.size()+1;
				br.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  
			//URN = audioFileLines.size()+1; 
			System.out.println("URN " + URN);
			  
		  }
		  
		  public void writeOutFile(){
			  
			  File file = new File(Environment.getExternalStorageDirectory().getPath() +"/weather walks/recorded media/recorded media.txt");
			  System.out.println("arraySize " + audioFileLines.size());
			  try {
				FileOutputStream fos = new FileOutputStream(file, false);
				PrintStream ps = new PrintStream(fos);
				
				for(int i = 0; i < audioFileLines.size();i++){
					ps.println(audioFileLines.get(i));
				}
				
				ps.close();
				
				
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			  
			  
			  
			  
			  
			
			  
		  }

}
