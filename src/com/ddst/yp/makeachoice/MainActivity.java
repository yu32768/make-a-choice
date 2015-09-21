package com.ddst.yp.makeachoice;

import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;
import java.util.Random;

import com.ddst.yp.makeachoice.R;
import com.ddst.yp.makeachoice.adapter.ChoicesAdapter;
import com.ddst.yp.makeachoice.adapter.ChoicesAdapter.ViewHolder;
import com.ddst.yp.makeachoice.model.Choice;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity implements OnClickListener, OnItemClickListener {
	
	private ArrayList<Choice> choices;
	private boolean addButtonChangeToSave;
	private Choice clickedChoice;
	private EditText edittext_choice_input;
	private ListView listview_choices;
	private ChoicesAdapter choiceAdapter;
	private Button button_add_save, button_randomize;
	private ViewHolder clickedViewHolder;
	//private int clickedPosition = -1; // saved for screen rotation
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		choices = new ArrayList<Choice>();
		addButtonChangeToSave = false;
		
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("choices")) {
				choices = savedInstanceState.getParcelableArrayList("choices");
			}
			
			if (savedInstanceState.containsKey("addButtonChangeToSave")) {
				addButtonChangeToSave = savedInstanceState.getBoolean("addButtonChangeToSave");
			}
		}
		
		button_add_save = (Button) findViewById(R.id.button_add);
		button_randomize = (Button) findViewById(R.id.button_randomize);
		edittext_choice_input = (EditText) findViewById(R.id.edittext_choice_input);
		listview_choices = (ListView) findViewById(R.id.listview_choices);
		button_add_save.setOnClickListener(this);
		button_randomize.setOnClickListener(this);
		listview_choices.setOnItemClickListener(this);
		listview_choices.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					v.performClick();
					break;

				default:
					break;
				}
				MainActivity.this.hideKeyBoard();
				
				return false;
			}
		});
		this.choiceAdapter = new ChoicesAdapter(this.choices, this);
		this.listview_choices.setAdapter(choiceAdapter);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		this.edittext_choice_input.getText().clear();
		
		super.onSaveInstanceState(outState);
		
		//outState.putBoolean("addButtonChangeToSave", addButtonChangeToSave);
		outState.putParcelableArrayList("choices", choices);
		
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position < this.choices.size()) {
			addButtonChangeToSave = true;
			
			int newPosition = this.choices.size() - position - 1;
			this.clickedChoice = this.choices.get(newPosition);
			this.edittext_choice_input.setText(this.clickedChoice.getChoiceName());
			
			this.button_add_save.setText(R.string.save);
			
			if (clickedViewHolder != null) {
				clickedViewHolder.button_delete.setVisibility(View.GONE);
			}
			clickedViewHolder = (ViewHolder) view.getTag();
			
			// show delete button
			clickedViewHolder.button_delete.setVisibility(View.VISIBLE);
			
			// refresh
			this.choiceAdapter.setClickedPosition(position);

			//this.updateListView();
		}
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			this.hideKeyBoard();
		}
	}

	private void updateListView() {
		this.choiceAdapter.setSelectedPosition(-1);
		this.choiceAdapter.notifyDataSetChanged();
	}
	
	public void deleteChoice(int position) {
		int newPosition = this.choices.size() - position - 1;
		this.choices.remove(newPosition);
		

		this.button_add_save.setText(R.string.add);
		addButtonChangeToSave = false;
		this.edittext_choice_input.getText().clear();
		
		// refresh
		this.choiceAdapter.setClickedPosition(-1);
		this.updateListView();
	}
	
	private void selectChoice(int position) {
		this.edittext_choice_input.getText().clear();
		this.updateListView();
		this.listview_choices.smoothScrollToPosition(position);
		this.choiceAdapter.setSelectedPosition(position);
//		TextView textView = this.choiceAdapter.getItemTextView(position);
//		if (textView != null) {
//			textView.setTextColor(getResources().getColor(R.color.decided_text_color));
//		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_add:
			String choiceName = edittext_choice_input.getText().toString();
			if (choiceName == null || choiceName.isEmpty()) {
				return;
			}
			if (addButtonChangeToSave) {
				if (this.clickedChoice != null) {
					this.clickedChoice.setChoiceName(choiceName);
				}
				
				this.button_add_save.setText(R.string.add);
				addButtonChangeToSave = false;
				
			} else {
				Choice c = new Choice(choiceName);
				this.choices.add(c);
			}

			// refresh
			this.choiceAdapter.setClickedPosition(-1);
			this.edittext_choice_input.getText().clear();
			this.updateListView();
			
			break;
			
		case R.id.button_randomize:
			if (this.choices.size() > 0) {
				this.hideKeyBoard();
				
				int result = new Random(System.currentTimeMillis()).nextInt(this.choices.size());
				
				// select list item
				this.selectChoice(result);
				
			}
			break;

		default:
			break;
		}
		
	}
	
	private void hideKeyBoard() {
		// Check if no view has focus:
		View view = this.getCurrentFocus();
		if (view != null) {  
		    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
}
