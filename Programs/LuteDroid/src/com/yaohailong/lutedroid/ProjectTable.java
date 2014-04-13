package com.yaohailong.lutedroid;

public class ProjectTable {
	MainActivity mainActivity;
	Service_SendView sendView;
	
	String pc_IP;
	Boolean isServiceStart = false;
	Boolean isConnected = false;
	
	public ProjectTable(MainActivity mainActivity){
		this.mainActivity = mainActivity;
	}
}
