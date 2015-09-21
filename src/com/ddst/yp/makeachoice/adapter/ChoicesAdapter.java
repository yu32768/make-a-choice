package com.ddst.yp.makeachoice.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ddst.yp.makeachoice.R;
import com.ddst.yp.makeachoice.MainActivity;
import com.ddst.yp.makeachoice.model.Choice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ChoicesAdapter extends BaseAdapter {
	
	private List<Choice> choices;
	private Context context;
	private Map<Integer, TextView> textview_map;
	private int selectedPosition = -1, clickedPosition = -1;
	
	public ChoicesAdapter(List<Choice> choices, Context context) {
		this.choices = choices;
		this.context = context;
		this.textview_map = new HashMap<Integer, TextView>();
	}

	@Override
	public int getCount() {
		return this.choices.size();
	}

	@Override
	public Object getItem(int position) {
		
		return null;
	}
	
	public TextView getItemTextView(int position) {
		TextView ret = null;
		
		if (this.textview_map.containsKey(position)) {
			ret = this.textview_map.get(position);
		}
		
		return ret;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(this.context);
			convertView = inflater.inflate(R.layout.listview_item, null);
			viewHolder = new ViewHolder();
			viewHolder.textview_choice = (TextView) convertView.findViewById(R.id.textview_choice);
			viewHolder.button_delete = (Button) convertView.findViewById(R.id.button_delete_choice);
			convertView.setTag(viewHolder);
			
			this.textview_map.put(position, viewHolder.textview_choice);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Choice choice = this.choices.get(this.choices.size() - position - 1); // bottom up displaying
		viewHolder.textview_choice.setText(choice.getChoiceName());
		if (position == selectedPosition) {
			viewHolder.textview_choice.setTextColor(context.getResources().getColor(R.color.decided_text_color));
		} else {
			viewHolder.textview_choice.setTextColor(context.getResources().getColor(R.color.default_text_color));
		}
		if (position == clickedPosition) {
			viewHolder.button_delete.setVisibility(View.VISIBLE);
		} else {
			viewHolder.button_delete.setVisibility(View.GONE);
		}
		viewHolder.button_delete.setOnClickListener(new ChoiceOnClickListener(position));
		
		return convertView;
	}
	
	private class ChoiceOnClickListener implements OnClickListener {
		private int position;
		
		public ChoiceOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			((MainActivity)context).deleteChoice(position);
		}
		
	}
	
	public class ViewHolder {
		public TextView textview_choice;
		public Button button_delete;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	public int getClickedPosition() {
		return clickedPosition;
	}

	public void setClickedPosition(int clickedPosition) {
		this.clickedPosition = clickedPosition;
	}

}
