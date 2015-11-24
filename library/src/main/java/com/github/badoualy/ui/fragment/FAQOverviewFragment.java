package com.github.badoualy.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.badoualy.ui.R;
import com.github.badoualy.ui.activity.FAQActivity;
import com.github.badoualy.ui.adapter.recycler.FAQOverviewAdapter;
import com.github.badoualy.ui.decoration.DividerItemDecoration;
import com.github.badoualy.ui.model.FAQ;
import com.github.badoualy.ui.model.FAQSection;
import com.github.badoualy.ui.model.FAQSectionEnd;
import com.github.badoualy.ui.model.IFAQItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FAQOverviewFragment extends BaseFragment implements FAQOverviewAdapter.FAQOnClickListener {

    public static final String FAQ_LIST_KEY = "faqList";
    public static final String FAQ_COLOR_KEY = "color";

    private RecyclerView recyclerFaq;

    private IFAQItem[] faqList;
    private int color;
    private boolean toolbarMargin = false;
    private boolean backArrow = false;

    @Override
    protected int getDisplayFlags() {
        if (backArrow)
            return DISPLAY_DRAWER_LOCKED | DISPLAY_HOME_AS_UP;

        return super.getDisplayFlags();
    }

    @Override
    protected String getTitle() {
        return getString(R.string.faq_title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() == null || !getArguments().containsKey(FAQ_LIST_KEY) || !getArguments()
                .containsKey(FAQ_COLOR_KEY)) {
            Log.e(TAG, "Missing extra, finishing");
            finish();
            return;
        }

        faqList = (IFAQItem[]) getArguments().getParcelableArray(FAQ_LIST_KEY);
        color = getArguments().getInt(FAQ_COLOR_KEY);
        toolbarMargin = getArguments().getBoolean("toolbarMargin", false);
        backArrow = getArguments().getBoolean("backArrow", false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater
                .inflate(toolbarMargin ? R.layout.fragment_faq_overview_wrapped : R.layout.fragment_faq_overview,
                         container, false);

        recyclerFaq = viewById(view, R.id.list_faq);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // getToolbar().setBackgroundColor(color);
        recyclerFaq.setHasFixedSize(true);
        recyclerFaq.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerFaq
                .addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, R.drawable.divider_faq));
        recyclerFaq.setAdapter(new FAQOverviewAdapter(getContext(), faqList, color, this));
    }

    @Override
    public void onFAQClicked(FAQ faq) {
        Log.d(TAG, "onFAQClicked: " + faq.toString());
        getTransactionHandler().displayFragment(new FAQFragment.Builder().withFAQ(faq)
                                                                         .withToolbarMargin(toolbarMargin)
                                                                         .build());
    }

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    public static class Builder {

        private static final String TAG = Builder.class.getSimpleName();

        private final Context context;
        private final List<IFAQItem> faqList = new ArrayList<>(10);
        private int color;
        private boolean toolbarMargin = false;
        private boolean backArrow = false;

        public Builder(Context context) {
            this.context = context;

            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
            color = typedValue.data;
        }

        public FAQOverviewFragment build() {
            Log.d(TAG, "Building FAQ fragment for " + faqList.size() + " faqs");
            if (faqList.isEmpty())
                Log.w(TAG, "FAQ list is empty");

            // Check list
            boolean hasSections = faqList.size() > 0 && faqList.get(0) instanceof FAQSection;
            for (int i = 0; i < faqList.size(); i++) {
                // Two following sections
                if (i > 0 && faqList.get(i) instanceof FAQSection && faqList.get(i - 1) instanceof FAQSection) {
                    throw new RuntimeException("Two consecutive sections: "
                                                       + faqList.get(i).toString()
                                                       + " and "
                                                       + faqList.get(i - 1).toString());
                }

                if (faqList.get(i) instanceof FAQSection && !hasSections) {
                    throw new RuntimeException("The first FAQ item is not a section, but the items contains a section");
                }

                if (i == faqList.size() - 1 && faqList.get(i) instanceof FAQSection) {
                    throw new RuntimeException("Empty last section: " + faqList.get(i).toString());
                }
            }

            // Add end section items
            if (hasSections) {
                // Can start at 2, 0 is section, 1 is item by validation
                for (int i = 2; i < faqList.size(); i++) {
                    if (faqList.get(i) instanceof FAQSection) {
                        faqList.add(i++, new FAQSectionEnd());
                    }
                }
                faqList.add(new FAQSectionEnd());
            }

            FAQOverviewFragment fragment = new FAQOverviewFragment();
            Bundle args = new Bundle();
            args.putParcelableArray(FAQ_LIST_KEY, faqList.toArray(new IFAQItem[faqList.size()]));
            args.putInt(FAQ_COLOR_KEY, color);
            args.putBoolean("toolbarMargin", toolbarMargin);
            args.putBoolean("backArrow", backArrow);
            fragment.setArguments(args);

            return fragment;
        }

        public void startActivity() {
            Log.d(TAG, "Building FAQ activity for " + faqList.size() + " faqs");
            if (faqList.isEmpty())
                Log.w(TAG, "FAQ list is empty");

            Intent intent = new Intent(context, FAQActivity.class);
            Bundle args = new Bundle();
            args.putParcelableArray(FAQ_LIST_KEY, faqList.toArray(new IFAQItem[faqList.size()]));
            args.putInt(FAQ_COLOR_KEY, color);
            args.putBoolean("toolbarMargin", toolbarMargin);
            args.putBoolean("backArrow", backArrow);
            intent.putExtras(args);
            context.startActivity(intent);
        }

        public Builder addFAQ(CharSequence question, CharSequence response) {
            faqList.add(new FAQ(question, response, faqList.size() + 1));
            return this;
        }

        public Builder addFAQ(@NonNull CharSequence questions[], @NonNull CharSequence responses[]) {
            for (int i = 0; i < questions.length && i < responses.length; i++)
                faqList.add(new FAQ(questions[i], responses[i], faqList.size() + 1));

            return this;
        }

        public Builder addFAQ(@ArrayRes int questionsArrayId, @ArrayRes int responsesArrayId) {
            Resources resources = context.getResources();
            return addFAQ(resources.getTextArray(questionsArrayId), resources.getTextArray(responsesArrayId));
        }

        public Builder addFAQ(FAQ... faqList) {
            this.faqList.addAll(Arrays.asList(faqList));
            return this;
        }

        public Builder addSection(@StringRes int titleId) {
            return addSection(context.getText(titleId));
        }

        public Builder addSection(CharSequence title) {
            faqList.add(new FAQSection(title));
            return this;
        }

        public Builder withItems(IFAQItem[] items) {
            if (faqList.size() > 0)
                faqList.clear();
            faqList.addAll(Arrays.asList(items));
            return this;
        }

        public Builder withColor(@ColorInt int color) {
            this.color = color;
            return this;
        }

        public Builder withColorId(@ColorRes int id) {
            this.color = context.getResources().getColor(id);
            return this;
        }

        public Builder withToolbarMargin(boolean toolbarMargin) {
            this.toolbarMargin = toolbarMargin;
            return this;
        }

        public Builder withBackArrow(boolean backArrow) {
            this.backArrow = backArrow;
            return this;
        }
    }
}
