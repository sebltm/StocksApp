package com.example.Team8.ui.main;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.Team8.R;
import android.content.Context;
import android.view.View.OnClickListener;
import android.view.View;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;


public class analyseFragment extends Fragment implements OnClickListener
{ View root;
  Context myContext;
  analyseBean analysebean;

  ImageView analyseResult;
  Button analyseOkButton;
  Button analysecancelButton;


 public analyseFragment() {}

  public static analyseFragment newInstance(Context c)
  { analyseFragment fragment = new analyseFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    fragment.myContext = c;
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  { super.onCreate(savedInstanceState); }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  { root = inflater.inflate(R.layout.analyse_layout, container, false);
    Bundle data = getArguments();
    analyseResult = (ImageView) root.findViewById(R.id.analyseResult);
    analysebean = new analyseBean(myContext);
    analyseOkButton = root.findViewById(R.id.analyseOK);
    analyseOkButton.setOnClickListener(this);
    analysecancelButton = root.findViewById(R.id.analyseCancel);
    analysecancelButton.setOnClickListener(this);
    return root;
  }



  public void onClick(View _v)
  { InputMethodManager _imm = (InputMethodManager) myContext.getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
    try { _imm.hideSoftInputFromWindow(_v.getWindowToken(), 0); } catch (Exception _e) { }
    if (_v.getId() == R.id.analyseOK)
    { analyseOK(_v); }
    else if (_v.getId() == R.id.analyseCancel)
    { analyseCancel(_v); }
  }

  public void analyseOK(View _v) 
  { 
    if (analysebean.isanalyseerror())
    { Log.w(getClass().getName(), analysebean.errors());
      Toast.makeText(myContext, "Errors: " + analysebean.errors(), Toast.LENGTH_LONG).show();
    }
    else
    { GraphDisplay _result = analysebean.analyse();
      analyseResult.invalidate();
      analyseResult.refreshDrawableState();
      analyseResult.setImageDrawable(_result);
    }
  }



  public void analyseCancel(View _v)
  { analysebean.resetData();
  }
}
