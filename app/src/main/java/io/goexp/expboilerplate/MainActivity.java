package io.goexp.expboilerplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.scala.exp.android.sdk.Exp;
import com.scala.exp.android.sdk.Utils;
import com.scala.exp.android.sdk.model.Content;
import com.scala.exp.android.sdk.model.SearchResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private String password = "5715031Com@";
    private String user = "cesar.oyarzun@scala.com";

    private String org = "";
    //    public static final String host = "https://api.goexp.io";
    public static final String host =  "https://api-staging.goexp.io";
    public static final String networkUuid = "1213ecfa-3c41-4b94-b047-d1c887f96400";
    public static final String apiKey = "0c4c6f4bc040ebe37f40ba2429f96e88de0072dbc92b4c656453272fe16302e096769234c86ff573adef7696ac93542f";
    private ArrayList<Content> folderArray = new ArrayList<>();
    private ArrayList<Content> contentArray = new ArrayList<>();
    private RecyclerView folderRecycleView;
    private RecyclerView.Adapter folderAdapter;
    private RecyclerView.LayoutManager folderLayoutManager;
    private RecyclerView contentRecycleView;
    private RecyclerView.Adapter contentAdapter;
    private RecyclerView.LayoutManager contentLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Map<String,Object> startOptions = new HashMap<>();
        startOptions.put(Utils.HOST,host);
        startOptions.put(Utils.NETWORK_UUID,networkUuid);
        startOptions.put(Utils.API_KEY,apiKey);
        Exp.start(startOptions).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                Log.i(LOG_TAG, "...START EXP SDK COMPLETED...");
            }
            @Override
            public void onError(Throwable e) {
                Log.e(LOG_TAG, "...SDK ERROR START...", e);
            }
            @Override
            public void onNext(Boolean aBoolean) {
                Log.i(LOG_TAG,"...SDK ONNEXT...");
                Exp.getContent("root").then(new Subscriber<Content>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {}
                    @Override
                    public void onNext(Content content) {
                        content.getChildren().then(new Subscriber<SearchResults<Content>>() {
                            @Override
                            public void onCompleted() {
                                setAdapters();
                            }
                            @Override
                            public void onError(Throwable e) {}

                            @Override
                            public void onNext(SearchResults<Content> contents) {
                                Log.e(LOG_TAG, "...CONTENT  ...");
                                for (Content child : contents) {
                                    Log.e(LOG_TAG, "...SUBTYPE  ..." +child.get("subtype"));
                                    if (child.getString("subtype").equalsIgnoreCase("scala:content:folder")) {
                                        folderArray.add(child);
                                    } else {
                                        contentArray.add(child);
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
  }


    private void setAdapters() {
//        //folders
        folderRecycleView = (RecyclerView) findViewById(R.id.recycler_view_folders);
        folderRecycleView.setHasFixedSize(true);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.default_gap);
        folderRecycleView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0));

        // use a linear layout manager
        folderLayoutManager = new GridLayoutManager(this,3);
        folderRecycleView.setLayoutManager(folderLayoutManager);
        folderAdapter = new FolderAdapter(this,folderArray);
        folderRecycleView.setAdapter(folderAdapter);


        //content
        contentRecycleView = (RecyclerView) findViewById(R.id.recycler_view_files);
        contentRecycleView.setHasFixedSize(true);
        int spacingInPixels1 = getResources().getDimensionPixelSize(R.dimen.default_gap);
        contentRecycleView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels1, true, 0));

        // use a linear layout manager
        contentLayoutManager = new GridLayoutManager(this,3);
        contentRecycleView.setLayoutManager(contentLayoutManager);
        contentAdapter = new ImageAdapter(this,contentArray);
        contentRecycleView.setAdapter(contentAdapter);
    }


}
