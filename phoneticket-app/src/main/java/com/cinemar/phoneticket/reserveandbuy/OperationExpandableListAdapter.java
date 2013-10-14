package com.cinemar.phoneticket.reserveandbuy;

import com.cinemar.phoneticket.R;

import android.app.Activity;
import android.content.Intent;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;


public class OperationExpandableListAdapter extends BaseExpandableListAdapter {
	
	private final SparseArray<GroupOperation> groups;
	private LayoutInflater inflater;
	private Activity activity;
	
	public OperationExpandableListAdapter(Activity activity, SparseArray<GroupOperation> groups) {
		
		this.activity = activity;
	    this.groups = groups;
	    this.inflater = activity.getLayoutInflater();
	}
	
	public Object getChild(int groupPosition, int childPosition) {
	    return groups.get(groupPosition).getItems().get(childPosition);
	}
	
	public long getChildId(int groupPosition, int childPosition) {
	    return 0;
	}
	
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		  
		final OperationView children = (OperationView) getChild(groupPosition, childPosition);
	    TextView textTitle = null, textCinemaAndDate = null;
	    
	    if (convertView == null) {
	      convertView = inflater.inflate(R.layout.operation_listrow_details, null);
	    }
	    
	    textTitle = (TextView) convertView.findViewById(R.id.accountItemTitle);
	    textTitle.setText(children.getItem().getTitle());
	    
	    textCinemaAndDate = (TextView) convertView.findViewById(R.id.accountItemCinemaAndDate);
	    textCinemaAndDate.setText(children.getItem().getCinema() + " - " + children.getItem().getDateToString() );
	    
	    convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showActivity(children);
			}
		});
	    
	    return convertView;
	  }
	
	private void showActivity(OperationView operation) {
		
		Intent intent = new Intent(activity, operation.getActivityClass());
		operation.setInformationToIntent(intent);
		activity.startActivity(intent);
	}
	
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).getItems().size();
	}
	
	public Object getGroup(int groupPosition) {
	    return groups.get(groupPosition);
	}
	
	public int getGroupCount() {
	    return groups.size();
	}
	
	@Override
	public void onGroupCollapsed(int groupPosition) {
	    super.onGroupCollapsed(groupPosition);
	}
	
	public void onGroupExpanded(int groupPosition) {
	    super.onGroupExpanded(groupPosition);
	}
	
	public long getGroupId(int groupPosition) {
	    return 0;
	}
	
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		  
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.operation_listrow_group, null);
		}
		  
		GroupOperation group = (GroupOperation) getGroup(groupPosition);
		((CheckedTextView) convertView).setText(group.getName());
		((CheckedTextView) convertView).setChecked(isExpanded);
		  
		return convertView;
	}

	public boolean hasStableIds() {
	    return false;
	}
	
	public boolean isChildSelectable(int groupPosition, int childPosition) {
	    return false;
	}
}