package com.kelevnor.weightwatchers_ex1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.kelevnor.weightwatchers_ex1.ADAPTER.Adapter_ListItem;
import com.kelevnor.weightwatchers_ex1.MODELS.Item;
import com.kelevnor.weightwatchers_ex1.REST.PullData;
import com.kelevnor.weightwatchers_ex1.UTILITY.Config;
import com.kelevnor.weightwatchers_ex1.UTILITY.Permission_Request_Helper;

import java.util.List;

public class Splash extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
