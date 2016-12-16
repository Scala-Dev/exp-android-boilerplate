package io.goexp.expboilerplate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.scala.exp.android.sdk.Exp;
import com.scala.exp.android.sdk.model.Content;
import com.scala.exp.android.sdk.model.SearchResults;

import java.io.Serializable;
import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by Cesar Oyarzun on 11/10/16.
 */
public class FolderActivity extends FragmentActivity {

    private ArrayList<Content> contentArray = new ArrayList<>();
    private ArrayList<Content> folderArray = new ArrayList<>();
    private RecyclerView folderRecycleView;
    private RecyclerView.Adapter folderAdapter;
    private RecyclerView.LayoutManager folderLayoutManager;
    private RecyclerView contentRecycleView;
    private RecyclerView.Adapter contentAdapter;
    private RecyclerView.LayoutManager contentLayoutManager;

    private final String LOG_TAG = FolderActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Serializable content = getIntent().getSerializableExtra("content");
        Exp.getContent(content.toString()).then(new Subscriber<Content>() {
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
