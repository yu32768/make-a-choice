package com.ddst.yp.makeachoice.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Choice implements Parcelable{
	private String choiceName;

	public Choice(String choiceName) {
		super();
		this.choiceName = choiceName;
	}

	public String getChoiceName() {
		return choiceName;
	}

	public void setChoiceName(String choiceName) {
		this.choiceName = choiceName;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(choiceName);
		
	}
	
	
}
