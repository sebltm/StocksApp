package com.example.Team8.ui.main;

import java.util.ArrayList;

import java.util.List;

import android.content.Context;

public class analyseBean
{ ModelFacade model = null;

  private List errors = new ArrayList();

  public analyseBean(Context _c) { model = ModelFacade.getInstance(_c); }

  public void resetData()
  { }

  public boolean isanalyseerror()
  { errors.clear(); 
    return errors.size() > 0;
  }

  public String errors() { return errors.toString(); }

  public GraphDisplay analyse()
  { return model.analyse(); }

}

