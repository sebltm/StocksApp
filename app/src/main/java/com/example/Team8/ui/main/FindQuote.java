package com.example.Team8.ui.main;

import java.util.ArrayList;

class FindQuote { static ArrayList<FindQuote> FindQuote_allInstances = new ArrayList<FindQuote>();

  FindQuote() { FindQuote_allInstances.add(this); }

  static FindQuote createFindQuote() { FindQuote result = new FindQuote();
    return result; }


  public String findQuote(String date)
  {
    String result = "";
    return result;
  }

}

