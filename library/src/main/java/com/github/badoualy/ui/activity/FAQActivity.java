package com.github.badoualy.ui.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.github.badoualy.ui.R;
import com.github.badoualy.ui.fragment.FAQOverviewFragment;
import com.github.badoualy.ui.listener.ActionBarHandler;
import com.github.badoualy.ui.model.IFAQItem;

import static com.github.badoualy.ui.fragment.FAQOverviewFragment.FAQ_COLOR_KEY;
import static com.github.badoualy.ui.fragment.FAQOverviewFragment.FAQ_LIST_KEY;

public class FAQActivity extends BaseActivity implements ActionBarHandler {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_above);

        Bundle extras = getIntent().getExtras();
        Parcelable[] faqParcelable = extras.getParcelableArray(FAQ_LIST_KEY);
        IFAQItem[] faqList = new IFAQItem[faqParcelable.length];
        System.arraycopy(faqParcelable, 0, faqList, 0, faqParcelable.length);
        int color = extras.getInt(FAQ_COLOR_KEY);

        toolbar = viewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(color);
        setupActionBar();

        displayFragment(FAQOverviewFragment.builder(this)
                                           .withColor(color)
                                           .withItems(faqList)
                                           .build(),
                        false, true);
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Log.e(TAG, "SupportActionBar is null, this shouldn't happen, couldn't setup activity");
            finish();
            return;
        }
        actionBar.setTitle(getTitle());
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME
                                            | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM);
        if (toolbar.getNavigationIcon() != null)
            toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.container_fragment;
    }

    @Override
    public void setHomeAsUpEnabled(boolean enabled) {
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }
}
