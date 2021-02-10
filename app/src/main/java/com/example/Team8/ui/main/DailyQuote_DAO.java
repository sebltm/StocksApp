package com.example.Team8.ui.main;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class DailyQuote_DAO
{ public static String getURL(String command, ArrayList<String> pars, ArrayList<String> values)
  { String res = "https://query1.finance.yahoo.com/v7/finance/download/";
    if (command != null)
    { res = res + command; }
    if (pars.size() == 0)
    { return res; }
    res = res + "?";
    for (int i = 0; i < pars.size(); i++)
    { String par = pars.get(i); 
      String val = values.get(i); 
      res = res + par + "=" + val;
      if (i < pars.size() - 1)
      { res = res + "&"; }
    }
    return res;
  }

  public static boolean isCached(String id)
  { DailyQuote _x = DailyQuote.DailyQuote_index.get(id);
    if (_x == null) { return false; }
    return true;
  }



  public static DailyQuote getCachedInstance(String id)
  { return DailyQuote.DailyQuote_index.get(id); }



  public static DailyQuote parseCSV(String _line)
  { if (_line == null) { return null; }
    ArrayList<String> _line1vals = Ocl.tokeniseCSV(_line);
    DailyQuote dailyquotex;
    dailyquotex = DailyQuote.DailyQuote_index.get((String) _line1vals.get(0));
    if (dailyquotex == null)
    { dailyquotex = DailyQuote.createByPKDailyQuote((String) _line1vals.get(0)); }
    dailyquotex.date = (String) _line1vals.get(0);
    dailyquotex.open = Double.parseDouble((String) _line1vals.get(1));
    dailyquotex.high = Double.parseDouble((String) _line1vals.get(2));
    dailyquotex.low = Double.parseDouble((String) _line1vals.get(3));
    dailyquotex.close = Double.parseDouble((String) _line1vals.get(4));
    dailyquotex.adjclose = Double.parseDouble((String) _line1vals.get(5));
    dailyquotex.volume = Double.parseDouble((String) _line1vals.get(6));
    return dailyquotex;
  }



  public static DailyQuote parseJSON(JSONObject obj)
  { if (obj == null) { return null; }

    try {
      String date = obj.getString("date");
      DailyQuote _dailyquotex = DailyQuote.DailyQuote_index.get(date);
      if (_dailyquotex == null) { _dailyquotex = DailyQuote.createByPKDailyQuote(date); }
      
      _dailyquotex.date = obj.getString("date");
      _dailyquotex.open = obj.getDouble("open");
      _dailyquotex.high = obj.getDouble("high");
      _dailyquotex.low = obj.getDouble("low");
      _dailyquotex.close = obj.getDouble("close");
      _dailyquotex.adjclose = obj.getDouble("adjclose");
      _dailyquotex.volume = obj.getDouble("volume");
      return _dailyquotex;
    } catch (Exception _e) { return null; }
  }



  public static ArrayList<DailyQuote> makeFromCSV(String lines)
  { ArrayList<DailyQuote> result = new ArrayList<DailyQuote>();

    if (lines == null)
    { return result; }

    ArrayList<String> rows = Ocl.parseCSVtable(lines);

    for (int i = 1; i < rows.size(); i++)
    { String row = rows.get(i);
      if (row == null || row.trim().length() == 0)
      { }
      else
      { DailyQuote _x = parseCSV(row);
        if (_x != null)
        { result.add(_x); }
      }
    }
    return result;
  }



  public static ArrayList<DailyQuote> parseJSON(JSONArray jarray)
  { if (jarray == null) { return null; }
    ArrayList<DailyQuote> res = new ArrayList<DailyQuote>();

    int len = jarray.length();
    for (int i = 0; i < len; i++)
    { try { JSONObject _x = jarray.getJSONObject(i);
        if (_x != null)
        { DailyQuote _y = parseJSON(_x); 
          if (_y != null) { res.add(_y); }
        }
      }
      catch (Exception _e) { }
    }
    return res;
  }



  public static JSONObject writeJSON(DailyQuote _x)
  { JSONObject result = new JSONObject();
    try {
       result.put("date", _x.date);
       result.put("open", _x.open);
       result.put("high", _x.high);
       result.put("low", _x.low);
       result.put("close", _x.close);
       result.put("adjclose", _x.adjclose);
       result.put("volume", _x.volume);
      } catch (Exception _e) { return null; }
    return result;
  }



  public static JSONArray writeJSONArray(ArrayList<DailyQuote> es)
  { JSONArray result = new JSONArray();
    for (int _i = 0; _i < es.size(); _i++)
    { DailyQuote _ex = es.get(_i);
      JSONObject _jx = writeJSON(_ex);
      if (_jx == null) { } 
      else 
      { try { result.put(_jx); }
        catch (Exception _ee) { }
      }
    }
    return result;
  }


}
