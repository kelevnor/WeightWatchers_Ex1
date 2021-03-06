package com.kelevnor.weightwatchers_ex1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.kelevnor.weightwatchers_ex1.ADAPTER.Adapter_ListItem;
import com.kelevnor.weightwatchers_ex1.ImageLoader.ImageLoader;
import com.kelevnor.weightwatchers_ex1.MODELS.Item;
import com.kelevnor.weightwatchers_ex1.REST.PullData;
import com.kelevnor.weightwatchers_ex1.UTILITY.Config;
import com.kelevnor.weightwatchers_ex1.UTILITY.Permission_Request_Helper;
import com.kelevnor.weightwatchers_ex1.UTILITY.UtilityHelper;
import java.util.List;


/**
 * Created by kelevnor on 08/25/2018.
 */


public class MainActivity extends AppCompatActivity implements PullData.OnAsyncResult, Permission_Request_Helper.OnAsyncResult, Adapter_ListItem.onItemClickListener, Switch.OnCheckedChangeListener, View.OnClickListener{
    Permission_Request_Helper permissionHelper;
    RecyclerView list;
    Switch enablePerm;
    Switch enableInternet;
    LinearLayout layout;
    Adapter_ListItem listAdapter;
    ConstraintLayout clOuter;
    Typeface openSansRegular, openSansSemiBold, fontAwesome;
    TextView actionbarTitle;
    RelativeLayout fullScreenLayout;
    ImageView imageFullScreen;
    TextView closeFullScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        View view = getLayoutInflater().inflate(R.layout.actionbar_layout, null);

        actionbarTitle = view.findViewById(R.id.tvTitle);
        layout = findViewById(R.id.ll_layout);
        clOuter = findViewById(R.id.cl_outer);
        list = findViewById(R.id.list);
        enablePerm = findViewById(R.id.sw_enableperm);
        enableInternet = findViewById(R.id.sw_enableinternet);

        fullScreenLayout = findViewById(R.id.rl_fullscreen);
        imageFullScreen = findViewById(R.id.iv_imagefullscreen);
        closeFullScreen = findViewById(R.id.tv_close);

        openSansRegular = Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Regular.ttf");
        openSansSemiBold = Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Semibold.ttf");
        fontAwesome = Typeface.createFromAsset(getAssets(),"fonts/fontawesome-webfont.ttf");

        actionbarTitle.setTypeface(openSansSemiBold);
        closeFullScreen.setTypeface(fontAwesome);

        enablePerm.setTypeface(openSansSemiBold);
        enableInternet.setTypeface(openSansSemiBold);

        enablePerm.setOnCheckedChangeListener(this);
        enableInternet.setOnCheckedChangeListener(this);

        permissionHelper = new Permission_Request_Helper(this, this);
        permissionHelper.CheckGenericPermissions();

        closeFullScreen.setOnClickListener(this);

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
    }

    //Permission Request Interface Listener
    @Override
    public void onInternetForPermissionSuccess(int resultCode, String message) {
        new PullData(this,this);
    }

    @Override
    public void onInternetForPermissionFail(int resultCode, String errorMessage) {

        layout.setVisibility(View.VISIBLE);

        if(resultCode == Config.RESULT_EXT_STORAGE_FAIL){
            enableInternet.setVisibility(View.GONE);
            enablePerm.setVisibility(View.VISIBLE);
        }
        else if(resultCode == Config.RESULT_EXT_STORAGE_SUCCESS_INTERNET_FAIL){
            enablePerm.setVisibility(View.GONE);
            enableInternet.setVisibility(View.VISIBLE);
        }
        else if(resultCode == Config.RESULT_EXT_STORAGE_INTERNET_FAIL){
            enablePerm.setVisibility(View.VISIBLE);
            enableInternet.setVisibility(View.VISIBLE);
        }
    }

    //Activity implements generic OnRequestPermission result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        permissionHelper.onRequestPermissionAction(requestCode, permissions, grantResults);
    }

    //Activity implements Recycler View's interface listener
    @Override
    public void onItemClick(final Item item) {
        Snackbar snackbar = Snackbar
                .make(clOuter, "Title Picked: "+ item.getTitle(), Snackbar.LENGTH_LONG)
                .setAction(getResources().getString(R.string.bottombtn_view), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImageLoader imageLoader = new ImageLoader(MainActivity.this);
                        imageLoader.DisplayImage(Config.BASE_URL+item.getImage(), imageFullScreen, MainActivity.this);
                        fullScreenLayout.setVisibility(View.VISIBLE);



                    }
                });

        TextView textViewText = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        TextView textViewAction = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);

        setSnackBarTextAction(textViewText, textViewAction);    //Setting style on SnackBar

        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorWhite));
        snackbar.show();
    }

    //Activity implements generic onCheckedChanged Listener
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
                    public void run() {}
                });

            }
            public void onFinish() {
                new PullData(act, dataOnAsync);}
        };
        timer.start();

    }
    //Activity implements generic View.OnClick Listener
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_close:
                fullScreenLayout.setVisibility(View.GONE);
                break;
        }
    }

    public void setSnackBarTextAction(TextView text, TextView action){
        text.setTextColor(getResources().getColor(R.color.colorMaxDark));
        text.setTypeface(openSansSemiBold);
        action.setTextColor(getResources().getColor(R.color.colorAccent));
        action.setTypeface(fontAwesome);
        action.setText(getResources().getString(R.string.fa_3_6_0_icon_resize));
    }
}
