package com.github.badoualy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.badoualy.ui.R;
import com.github.badoualy.ui.model.FAQ;


public class FAQFragment extends BaseFragment {

    private TextView lblQuestion, lblAnswer;
    private ImageView img;

    private FAQ faq;
    private boolean toolbarMargin = false;

    @Override
    protected String getTitle() {
        return "Q" + faq.number;
    }

    @Override
    protected int getDisplayFlags() {
        return DISPLAY_DRAWER_LOCKED | DISPLAY_HOME_AS_UP;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() == null || !getArguments().containsKey("faq")) {
            Log.e(TAG, "Missing extra, finishing");
            finish();
            return;
        }

        faq = getArguments().getParcelable("faq");
        toolbarMargin = getArguments().getBoolean("toolbarMargin", false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(toolbarMargin ? R.layout.fragment_faq_wrapped : R.layout.fragment_faq,
                                     container, false);

        lblQuestion = viewById(root, R.id.lbl_faq_question);
        lblAnswer = viewById(root, R.id.lbl_faq_answer);
        img = viewById(root, R.id.img_faq);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lblQuestion.setText(faq.question);
        lblAnswer.setText(faq.answer);
        img.setVisibility(View.GONE);
    }

    public static class Builder {

        private FAQ faq;
        private boolean toolbarMargin = false;

        public Builder builder() {
            return new Builder();
        }

        public Builder withFAQ(FAQ faq) {
            this.faq = faq;
            return this;
        }

        public Builder withToolbarMargin(boolean toolbarMargin) {
            this.toolbarMargin = toolbarMargin;
            return this;
        }

        public FAQFragment build() {
            FAQFragment fragment = new FAQFragment();
            Bundle args = new Bundle();
            args.putParcelable("faq", faq);
            args.putBoolean("toolbarMargin", toolbarMargin);
            fragment.setArguments(args);
            return fragment;
        }
    }
}
