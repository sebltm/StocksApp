package com.example.Team8.ui.main;

import java.util.ArrayList;

import java.util.List;

import android.content.Context;

public class findQuoteBean
{ ModelFacade model = null;

  private String date = "";
  private List errors = new ArrayList();

  public findQuoteBean(Context _c) { model = ModelFacade.getInstance(_c); }

  public void setdate(String datex)
  { date = datex; }

  public void resetData()
  { date = "";
    }

  public boolean isfindQuoteerror()
  { errors.clear(); 
    return errors.size() > 0;
  }

  public String errors() { return errors.toString(); }

  public String findQuote()
  { return model.findQuote(date); }

}

