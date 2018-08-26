package com.kelevnor.weightwatchers_ex1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.kelevnor.weightwatchers_ex1.ADAPTER.Adapter_ListItem;
import com.kelevnor.weightwatchers_ex1.MODELS.Item;
import com.kelevnor.weightwatchers_ex1.REST.PullData;
import com.kelevnor.weightwatchers_ex1.UTILITY.Config;
import com.kelevnor.weightwatchers_ex1.UTILITY.Permission_Request_Helper;
import com.kelevnor.weightwatchers_ex1.UTILITY.UtilityHelper;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PullData.OnAsyncResult, Permission_Request_Helper.OnAsyncResult, Adapter_ListItem.onItemClickListener, Switch.OnCheckedChangeListener{
    Permission_Request_Helper permissionHelper;
    RecyclerView list;
    Switch enablePerm;
    Switch enableInternet;
    LinearLayout layout;
    Adapter_ListItem listAdapter;
    Typeface openSansRegular, openSansSemiBold;
    TextView actionbarTitle, actionbarLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        View view = getLayoutInflater().inflate(R.layout.actionbar_layout, null);

        actionbarTitle = view.findViewById(R.id.tvTitle);
        actionbarLoader = view.findViewById(R.id.tvloader);
        layout = findViewById(R.id.ll_layout);
        list = findViewById(R.id.list);
        enablePerm = findViewById(R.id.sw_enableperm);
        enableInternet = findViewById(R.id.sw_enableinternet);

        openSansRegular = Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Regular.ttf");
        openSansSemiBold = Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Semibold.ttf");

        actionbarTitle.setTypeface(openSansSemiBold);
        actionbarLoader.setTypeface(openSansSemiBold);

        enablePerm.setTypeface(openSansSemiBold);
        enableInternet.setTypeface(openSansSemiBold);

        enablePerm.setOnCheckedChangeListener(this);
        enableInternet.setOnCheckedChangeListener(this);

        permissionHelper = new Permission_Request_Helper(this, this);
        permissionHelper.CheckGenericPermissions();
    }

    //PullData Interface Listener
    @Override
    public void onResultSuccess(int resultCode, List<Item> objList) {
        listAdapter = new Adapter_ListItem(this, objList, this);
        list.setAdapter(listAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        list.setLayoutAnimation(animation);
        list.setLayoutManager(mLayoutManager);
//        list.setItemAnimator(new DefaultItemAnimator());
//
//        final Context context = list.getContext();
//        final LayoutAnimationController controller =
//                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
//
//        list.setLayoutAnimation(controller);
//        list.getAdapter().notifyDataSetChanged();
//        list.scheduleLayoutAnimation();
    }

    @Override
    public void onResultFail(int resultCode, String errorMessage) {
        if(resultCode==Config.RESULT_INTERNET_FAIL){
            UtilityHelper.displayDialogOneButton(this,getResources().getString(R.string.error_label),getResources().getString(R.string.error_internet),getResources().getString(R.string.bottombtn_dismiss));

            if(!UtilityHelper.isWifiAvailable(this)){
                layout.setVisibility(View.VISIBLE);
                enablePerm.setVisibility(View.GONE);
                enableInternet.setVisibility(View.VISIBLE);
            }
        }
        else if(resultCode==Config.RESULT_PULLDATA_FAIL){
            UtilityHelper.displayDialogOneButton(this,getResources().getString(R.string.error_label),getResources().getString(R.string.error_pull_data),getResources().getString(R.string.bottombtn_dismiss));
        }
        Log.e("Pull Data","FAIL");
    }

    //Permission Request Interface Listener
    @Override
    public void onInternetForPermissionSuccess(int resultCode, String message) {
        Log.d("SUCCESS", "INTERNET_STORAGE_ALLOWED");
        new PullData(this,this);
    }

    @Override
    public void onInternetForPermissionFail(int resultCode, String errorMessage) {

        layout.setVisibility(View.VISIBLE);

        if(resultCode == Config.RESULT_EXT_STORAGE_FAIL){
            Log.d("FAIL", "RESULT_EXT_STORAGE_FAIL");
            enableInternet.setVisibility(View.GONE);
            enablePerm.setVisibility(View.VISIBLE);
        }
        else if(resultCode == Config.RESULT_EXT_STORAGE_SUCCESS_INTERNET_FAIL){
            Log.d("FAIL", "RESULT_EXT_STORAGE_SUCCESS_INTERNET_FAIL");
            enablePerm.setVisibility(View.GONE);
            enableInternet.setVisibility(View.VISIBLE);
        }
        else if(resultCode == Config.RESULT_EXT_STORAGE_INTERNET_FAIL){
            Log.d("FAIL", "RESULT_EXT_STORAGE_INTERNET_FAIL");
            enablePerm.setVisibility(View.VISIBLE);
            enableInternet.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        permissionHelper.onRequestPermissionAction(requestCode, permissions, grantResults);
    }

    @Override
    public void onItemClick(Item item) {

    }

    //
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        switch (compoundButton.getId()){
            case R.id.sw_enableperm:
                enablePerm.setChecked(false);
                enablePerm.setVisibility(View.GONE);
                permissionHelper.CheckGenericPermissions();
                break;

            case R.id.sw_enableinternet:
                enableInternet.setChecked(false);
                enableInternet.setVisibility(View.GONE);
                WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);

                actionbarLoader.setVisibility(View.VISIBLE);
                setTimerUntilFetch(this, this);
                break;
        }
    }

    //Method that uses CoundDownTimer to start PullData Rest after 3 secods
    public void setTimerUntilFetch(final Activity act, final PullData.OnAsyncResult dataOnAsync){
        CountDownTimer timer = new CountDownTimer(3000, 1000)
        {
            public void onTick(final long millisUntilFinished){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        actionbarLoader.setText((int)millisUntilFinished/1000);
                    }
                });

            }
            public void onFinish() {
                actionbarLoader.setVisibility(View.GONE);
                new PullData(act, dataOnAsync);}
        };
        timer.start();

    }
}