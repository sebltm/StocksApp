package com.example.Team8.ui.main;

import java.util.ArrayList;

class Analyse { static ArrayList<Analyse> Analyse_allInstances = new ArrayList<Analyse>();

  Analyse() { Analyse_allInstances.add(this); }

  static Analyse createAnalyse() { Analyse result = new Analyse();
    return result; }


  public GraphDisplay analyse()
  {
    GraphDisplay result = null;
    return result;
  }

}

