package com.my.agents.utils;

/**
 * 转成帧数
 * */
public class FrameUtils {

	public static int timeStr2Frame(String timeStr,int framebite){
		String[] time = timeStr.split(":|\\."); 
		return Integer.valueOf(time[0]) * 60 * 60 * framebite +
				Integer.valueOf(time[1]) * 60 * framebite+
				Integer.valueOf(time[2]) * framebite+
				Integer.valueOf(time[3]) * framebite /1000 ;
	}

	public static int getFrames(String startStr,String stopStr,int framebite){
		return timeStr2Frame(stopStr, framebite) - timeStr2Frame(startStr, framebite);
	}
	
	public static void main(String[] args) {
		String time = "00:00:10.100";
		System.out.println(FrameUtils.timeStr2Frame(time, 25));
	}

}
