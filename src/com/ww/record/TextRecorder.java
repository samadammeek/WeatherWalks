package com.ww.record;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.ww.start.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TextRecorder extends Activity{
	
	private EditText mainNoteField;
	
	String textString;
	Button submitButton;
	double location[];
	int URN = 0;
	ArrayList<String> audioFileLines = new ArrayList<String>();
	Context context;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.textrecorder);
		
		Bundle b = getIntent().getExtras();
		
		readInFile();
		
		location = new double[2];
		
		location = b.getDoubleArray("coords");
		
		mainNoteField = (EditText) findViewById(R.id.editText1);
		
		submitButton = (Button)findViewById(R.id.buttonRecordText);
		
		submitButton.setOnClickListener(listener);
		
		context = this;
		
		location = b.getDoubleArray("Coords");
		
	}
	
	private OnClickListener listener = new OnClickListener(){

		public void onClick(View v) {
			
		
			
			textString = mainNoteField.getEditableText().toString();
			
			
			
			System.out.println("TextString " + textString.length());
				
			
				String line = URN + "," + URN + ".txt"+ "," + location[0] + "," + location[1];
			
				if(textString.length()!=0){
					audioFileLines.add(line);
				
				
					for (int i = 0; i < audioFileLines.size(); i++){
						System.out.println("get " + audioFileLines.get(i));
					}
			
					writeOutFile();
			
					Timer timer = new Timer();
			
					timer.schedule(new TimerTask(){
						public void run(){
							onDestroy();
						}

				
				
						}, 1000);
			
			
				}
				else{
					Toast.makeText(context, "Please enter some text", Toast.LENGTH_SHORT).show();
				}
		}
			
			
			
		
			};
	
	 
	  public void readInFile(){
		  
		  File file = new File(Environment.getExternalStorageDirectory().getPath() +"/weather walks/recorded media/recorded media.txt");
		  
		  try {
			
			  BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while((line = br.readLine())!=null){
				
			
				audioFileLines.add(line);
				
	
			}
			
			URN = audioFileLines.size()+1;
			
			br.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found Exception @ textRecorder read in");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception @ text Recorder read in");
		}
		  catch (NullPointerException e){
			  System.out.println("Null Pointer Exception @ TextRecorder read in ");
		  }
		  
		System.out.println(audioFileLines.size());
		//URN = audioFileLines.size(); 
		  
	  }
	  private void writeOutFile(){
		  
		 
		  //write entry to text file
		  File file = new File(Environment.getExternalStorageDirectory().getPath() +"/weather walks/recorded media/recorded media.txt");
		  
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
		  
		  //
		  File textFile = new File(Environment.getExternalStorageDirectory().getPath()+ 
				  "/Weather Walks/recorded media/" + URN + ".txt");
		  
		  try{
			  FileOutputStream fos = new FileOutputStream(textFile, false);
			  PrintStream ps = new PrintStream(fos);
			  ps.println(textString);
			  Toast.makeText(context, "File written " + URN+".txt", Toast.LENGTH_SHORT).show();
			  ps.close();
			  
		  }
		  catch (FileNotFoundException d){
			  
			  Toast.makeText(context, "Text File not found", Toast.LENGTH_SHORT).show();
			  
		  }
		  

	  }
	  
	  protected void onDestroy(){
		  super.onDestroy();
		  
		  finish();
		  
	  }
}