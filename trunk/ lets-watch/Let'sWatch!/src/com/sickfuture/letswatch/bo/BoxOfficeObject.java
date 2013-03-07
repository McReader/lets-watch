package com.sickfuture.letswatch.bo;

import org.json.JSONObject;

import com.sickfuture.letswatch.bo.common.JSONModel;

public class BoxOfficeObject extends JSONModel {

	private static final String CRITICS_CONSENSUS = "critics_consensus";
	
	public BoxOfficeObject(JSONObject jsonObject) {
		super(jsonObject);
	}

	public String getId(){
		return getString(ID);
	}
	
	public String getTitle(){
		return getString(TITLE);
	}
	
	public String getSynopsis(){
		return getString(SYNOPSIS);
	}
	
	public String getCriticConsensus() {
		return getString(CRITICS_CONSENSUS);
	}
}
