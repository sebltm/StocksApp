package com.example.Team8.ui.main;

import java.util.Date;
import java.text.DateFormat; 
import java.text.SimpleDateFormat; 


public class DateComponent
{ public static long getEpochSeconds(String date)
  { DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
    try
	{ Date d = df.parse(date); 
      long time = d.getTime()/1000; 
	  return time;
	} catch (Exception _e) { return -1; } 
  }

  public static long getEpochMilliseconds(String format, String date)
  { DateFormat df = new SimpleDateFormat(format); 
    try
	{ Date d = df.parse(date); 
      long time = d.getTime(); 
	  return time;
	} catch (Exception _e) { return -1; } 
  }

  public static long getTime()
  { Date d = new Date(); 
    return d.getTime(); 
  }

}