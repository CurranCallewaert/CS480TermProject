package com.course.example.menuinflater;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.net.Uri;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Locale;

public class MenuInflaterActivity extends Activity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, TextToSpeech.OnInitListener{

	private Spinner spin;
	private TabHost tabs;

	private ListView list1;

	private ArrayList<String> todo = new ArrayList<String>();
	private ArrayAdapter<String> adapt1;
	private int post=-1; //set to this for control of deleting list items
	private TextToSpeech speaker;

	private RelativeLayout layout;
	private ImageView image;

	//file name
	private String todoFile = "todo.txt";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		layout = (RelativeLayout)findViewById(R.id.layout);
		image = (ImageView) findViewById(R.id.img);
		image.setImageResource(R.drawable.shape);

		// Load the appropriate animation
		Animation an =  AnimationUtils.loadAnimation(this, R.anim.shakeitup);
		// Register a listener, so we can disable and re-enable buttons
		an.setAnimationListener(new MyAnimationListener());
		// Start the animation
		layout.startAnimation(an);



		//hide title and icon in action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);

		//create the spinner
		spin = (Spinner) findViewById(R.id.spinner);
		spin.setOnItemSelectedListener(this);

		String[] items = new String[]{"**Create new task**", "Call", "Text", "Email", "Address"};
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);

		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(aa);

		// set up list views
		list1 = (ListView) findViewById(R.id.list1); // to do list
		list1.setOnItemClickListener(this);

		//set up the array adapter for list
		adapt1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todo);

		//connect adapter to listview
		list1.setAdapter(adapt1);

		//Initialize Text to Speech engine (context, listener object)
		speaker = new TextToSpeech(this, this);


		try {
			InputStream in = openFileInput(todoFile);
			InputStreamReader ins = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(ins);
			String str = null;

			while ((str = reader.readLine()) != null) {
				todo.add(str);
			}

			//close stream
			reader.close();


		} catch (FileNotFoundException e) {
			Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
		} catch (IOException io) {
			Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
		}

		for(int i=0; i<todo.size();i++){ //remove null items
			if(todo.get(i).indexOf("null") != -1){
				todo.remove(i);
			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Uri uri;
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.execute:
				if(post!=-1){ //prevents it from doing anything if nothing was selected
					if(todo.get(post).contains("Call: ")){
						//send intent to dialler to pull it up, filling in the number
						String dialup = todo.get(post).substring((todo.get(post).indexOf("@"))+1);
						Log.i("Test", ""+dialup);

						uri= Uri.parse("tel:"+dialup);
						Intent dial = new Intent(Intent.ACTION_DIAL,uri);
						startActivity(dial);
					}
					if(todo.get(post).contains("Text: ")){
						String textup = todo.get(post).substring((todo.get(post).indexOf("@"))+1);
						Log.i("Test", ""+textup);

						uri = Uri.parse("sms:"+textup);
						Intent text = new Intent(Intent.ACTION_VIEW,uri);
						startActivity(text);
					}

					if(todo.get(post).contains("Email: ")){
						String emailup = todo.get(post).substring((todo.get(post).indexOf("@"))+1);
						Log.i("Test", emailup);

						uri = Uri.parse("mailto:"+emailup);
						Intent email = new Intent(Intent.ACTION_SENDTO,uri);
						startActivity(email);
					}

					if(todo.get(post).contains("Visit: ")){
						String visitup = todo.get(post).substring((todo.get(post).indexOf("@"))+1);
						Log.i("Test",visitup);

						uri = Uri.parse("geo:0,0?q="+visitup);
						Intent address = new Intent(Intent.ACTION_VIEW,uri);
						startActivity(address);
					}
				}
				return true;
			case R.id.delete:
				if(post!=(-1)) {
					todo.remove(post);
					adapt1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todo);
					list1.setAdapter(adapt1);
					post=-1;
				}
				return true;
			case R.id.notify:
				//sends a notification to the phone, so the user can leave the app and be reminded.
				if(post!=-1) {
					NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					Intent notifyIntent = new Intent(this, MenuInflaterActivity.class);
					PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

					Notification notifyDetails = new Notification.Builder(this)
							.setContentIntent(pendingIntent)
							.setContentTitle("To-Do")
							.setContentText(todo.get(post))
							.setSmallIcon(R.drawable.ic_launcher)
							.setTicker("Reminder!")
							.setAutoCancel(true)
							.setVibrate(new long[]{1000, 1000, 1000, 1000})
							.build();

					nm.notify(123, notifyDetails);

				}

				return true;
			case R.id.save:
				try {
					OutputStreamWriter out = new OutputStreamWriter(openFileOutput(todoFile,MODE_PRIVATE));

					for(int i=0;i<todo.size();i++){
						out.write(todo.get(i)+"\n");

					}

					Toast.makeText(this,"List saved", Toast.LENGTH_SHORT).show(); //checks to make sure we get here

					//close stream
					out.close();
				}
				catch(FileNotFoundException e){
					Toast.makeText(this,"File not found",Toast.LENGTH_SHORT).show();
				}

				catch(IOException i){
					Toast.makeText(this,"An error occurred",Toast.LENGTH_SHORT).show();
				}


				return true;
			case R.id.close:
				try {
					OutputStreamWriter out = new OutputStreamWriter(openFileOutput(todoFile,MODE_PRIVATE));

					for(int i=0;i<todo.size();i++){
						out.write(todo.get(i)+"\n");

					}

					//close stream
					out.close();
				}
				catch(FileNotFoundException e){
					Toast.makeText(this,"File not found",Toast.LENGTH_SHORT).show();
				}

				catch(IOException i){
					Toast.makeText(this,"An error occurred",Toast.LENGTH_SHORT).show();
				}
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position, long id){ //for spinner
		//based on position
		switch(position){
			case 1: //start at 1 so the first item does nothing
				Intent callIntent = new Intent(this, CallActivity.class); //starts the call activity
				startActivity(callIntent);
				break;
			case 2:
				Intent textIntent = new Intent(this, TextActivity.class); //starts the text activity
				startActivity(textIntent);
				break;
			case 3:
				Intent emailIntent = new Intent(this,EmailActivity.class);
				startActivity(emailIntent);
				break;
			case 4:
				Intent addressIntent = new Intent(this, AddressActivity.class);
				startActivity(addressIntent);
				break;

		}

		String who = getIntent().getStringExtra("who");
		String num = getIntent().getStringExtra("num");
		String type = getIntent().getStringExtra("type");
		boolean check = getIntent().getBooleanExtra("check", true);

		Log.i("Test", ""+who);
		Log.i("Test", ""+num);
		Log.i("Test", ""+check);

		if(check){
			todo.add("**"+type + who + ". . . . . . @" + num);
		}

		else {
			todo.add(type + who + ". . . . . . @" + num);

		}


		for(int i=0; i<todo.size();i++){ //remove all the null valued ones
			if(todo.get(i).indexOf("null") != -1){
				todo.remove(i);
			}

		}

		LinkedHashSet lhs = new LinkedHashSet(todo); //remove duplicate values
		todo= new ArrayList<>(lhs);


		try {
			OutputStreamWriter out = new OutputStreamWriter(openFileOutput(todoFile,MODE_PRIVATE));

			for(int i=0;i<todo.size();i++){
				out.write(todo.get(i)+"\n");

			}

			//close stream
			out.close();
		}
		catch(FileNotFoundException e){
			Toast.makeText(this,"File not found",Toast.LENGTH_SHORT).show();
		}

		catch(IOException i){
			Toast.makeText(this,"An error occurred",Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent){ //for spinner

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) { //for handling selection of list items
		if(todo.get(position).substring(0,2).equals("**")) {

			speak(todo.get(position).substring(2, todo.get(position).indexOf(".")) ); //speak the selected list item
		}
		else{
			speak(todo.get(position).substring(0, todo.get(position).indexOf("."))); //speak the selected list item

		}
		post = position; //used to let the rest of the program know which item was clicked.

	}

	//speaks the contents of output
	public void speak(String output){
		//	speaker.speak(output, TextToSpeech.QUEUE_FLUSH, null);  //for APIs before 21
		speaker.speak(output, TextToSpeech.QUEUE_FLUSH, null, "Id 0");
	}

	// Implements TextToSpeech.OnInitListener.
	public void onInit(int status) {
		// status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
		if (status == TextToSpeech.SUCCESS) {
			// Set preferred language to US english.
			// If a language is not be available, the result will indicate it.
			int result = speaker.setLanguage(Locale.US);

			//int result = speaker.setLanguage(Locale.FRANCE);
			if (result == TextToSpeech.LANG_MISSING_DATA ||
					result == TextToSpeech.LANG_NOT_SUPPORTED) {
				// Language data is missing or the language is not supported.
			} else {
				// The TTS engine has been successfully initialized
				speak("");
			}
		} else {
			// Initialization failed.
		}
	}

	class MyAnimationListener implements Animation.AnimationListener {

		public void onAnimationEnd(Animation animation) {
			// what to do when animation ends
		}
		public void onAnimationRepeat(Animation animation) {
			// what to do when animation loops
		}

		public void onAnimationStart(Animation animation) {
			// what to do when animation starts
		}

	}



}