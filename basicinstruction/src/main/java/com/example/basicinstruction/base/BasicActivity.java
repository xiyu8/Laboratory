package com.example.basicinstruction.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.basicinstruction.R;
import com.example.basicinstruction.Tool.LL;

public abstract class BasicActivity extends AppCompatActivity {

    //the container of this activity layout and sub-activity layout
    private LinearLayout parentLinearLayout;
    private TextView toolbarTitle;
    private TextView toolbarRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      ActivityCollector.addActivity(this);
      LL.e(""+getClass().getName());

      initContentView(R.layout.activity_basic);  //加入含有toolbar的基础布局
      setContentView(getLayoutId());      //setcontentView将view加到含toolbar的基础View下面
      initBasicToolbar();

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected abstract int getLayoutId();

    private void initBasicToolbar() {
      this.setSupportActionBar(getToolbar());
      if(getSupportActionBar()!=null)  getSupportActionBar().setDisplayShowTitleEnabled(false);
      toolbarTitle = (TextView) findViewById(R.id.tv_title);
      toolbarRight = (TextView) findViewById(R.id.tv_right);
      if (null != getToolbar() && isShowBacking()) {
//        getToolbar().setNavigationIcon(R.drawable.back);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            onBackPressed();
          }
        });
      }
    }


    private void initContentView(@LayoutRes int layoutResID) {
      ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
      viewGroup.removeAllViews();
      parentLinearLayout = new LinearLayout(this);
      parentLinearLayout.setOrientation(LinearLayout.VERTICAL);
      viewGroup.addView(parentLinearLayout);
      LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);
    }
    public void setContentView(@LayoutRes int layoutResID) {
      if(!isShowToolbar())  {super.setContentView(layoutResID);  return;}
      LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);
    }


    public Toolbar getToolbar() { return (Toolbar) findViewById(R.id.toolbar); }
    protected boolean isShowBacking() {  return true; }
    public TextView getToolbarTitle() { return toolbarTitle; }
    public TextView getToolbarSubTitle() {   return toolbarRight; }
    public void setToolbarBg(String colorString) {   findViewById(R.id.toolbarArea).setBackgroundColor(Color.parseColor(colorString)); }
    public void setToolbarBoundary(String colorString){ findViewById(R.id.toolbar_boundary).setBackgroundColor(Color.parseColor(colorString));}
    public Boolean isShowToolbar() { return true; }
    public void setToolBarTitle(CharSequence title) {
      if (toolbarTitle != null) {
        toolbarTitle.setText(title);
      } else {
        this.getToolbar().setTitle(title);
      }
    }
    ///////////////////////////////////////////////////////////ReadyGo///////////////////////////////////////////////////////////
    protected <T extends Activity> void readyGo(Class<T> clz, Bundle bundle) {
      Intent intent = new Intent(this, clz);
      if (bundle != null)
        intent.putExtras(bundle);
      startActivity(intent);
    }
    protected <T extends Activity> void readyGo(Class<T> clz) { readyGo(clz, null); }
    protected <T extends Activity> void readyGo(Class<T> clz, String key,String value) {
      Intent intent = new Intent(this, clz);
      intent.putExtra(key, value);
      startActivity(intent);
    }
    protected <T extends Activity> void readyGoForResult(Class<T> clz, Bundle bundle, int code) {
      Intent intent = new Intent(this, clz);
      if (bundle != null)
        intent.putExtras(bundle);
      startActivityForResult(intent, code);
    }
    protected <T extends Activity> void readyGoForResult(Class<T> clz, int code) {
      readyGoForResult(clz, null, code);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void showErrorPage(String error,View view){

    }
    public void toastErrorMassage(Error error){
      LL.t(this,error.getMessage());
    }




    @Override
    protected void onDestroy() {
      ActivityCollector.removeActivity(this);
      super.onDestroy();
    }




























}
