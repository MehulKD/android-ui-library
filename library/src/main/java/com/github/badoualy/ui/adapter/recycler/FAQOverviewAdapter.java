package com.github.badoualy.ui.adapter.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.badoualy.ui.R;
import com.github.badoualy.ui.model.FAQ;
import com.github.badoualy.ui.model.FAQSection;
import com.github.badoualy.ui.model.FAQSectionEnd;
import com.github.badoualy.ui.model.IFAQItem;


public class FAQOverviewAdapter extends BaseArrayRecyclerAdapter<IFAQItem, RecyclerView.ViewHolder> {

    // Variation without above shadow
    private static final int TYPE_SECTION_START_FIRST = 3;
    private static final int TYPE_SECTION_START = 0;
    private static final int TYPE_FAQ = 1;
    private static final int TYPE_SECTION_END = 2;

    private final int color;
    private final FAQOnClickListener listener;

    public FAQOverviewAdapter(Context context, IFAQItem[] items, int color, FAQOnClickListener listener) {
        super(context, items);
        this.color = color;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (viewType == TYPE_SECTION_START)
            return new FAQOverviewSectionViewHolder(layoutInflater
                                                            .inflate(R.layout.item_faq_overview_section_with_shadow, parent, false));
        else if (viewType == TYPE_SECTION_START_FIRST)
            return new FAQOverviewSectionViewHolder(layoutInflater
                                                            .inflate(R.layout.item_faq_overview_section, parent, false));
        else if (viewType == TYPE_SECTION_END)
            return new FAQOverviewSectionEndViewHolder(layoutInflater
                                                               .inflate(R.layout.item_faq_overview_section_end, parent, false));

        return new FAQOverviewViewHolder(layoutInflater.inflate(R.layout.item_faq_overview, parent, false), color);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderObject, int position) {
        IFAQItem item = getItem(position);
        int itemType = getItemViewType(position);
        if (itemType == TYPE_FAQ) {
            final FAQ faq = (FAQ) item;
            final FAQOverviewViewHolder holder = (FAQOverviewViewHolder) holderObject;

            holder.lblNumber.setText(getContext().getString(R.string.faq_number, faq.number));
            holder.lblQuestion.setText(faq.question);
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(@NonNull View view) {
                    listener.onFAQClicked(faq);
                }
            });
        } else if (itemType == TYPE_SECTION_START || itemType == TYPE_SECTION_START_FIRST) {
            final FAQSection section = (FAQSection) item;
            final FAQOverviewSectionViewHolder holder = (FAQOverviewSectionViewHolder) holderObject;
            holder.lblTitle.setText(section.title);
        }
    }

    @Override
    public int getItemViewType(int position) {
        IFAQItem item = getItem(position);
        if (item instanceof FAQSection)
            return position > 0 ? TYPE_SECTION_START : TYPE_SECTION_START_FIRST;
        else if (item instanceof FAQSectionEnd)
            return TYPE_SECTION_END;
        return TYPE_FAQ;
    }

    @Override
    public boolean hasDivider(int position) {
        return getItemViewType(position) == TYPE_FAQ
                && position < getItemCount() - 1
                && getItemViewType(position + 1) == TYPE_FAQ;
    }

    public static class FAQOverviewViewHolder extends RecyclerView.ViewHolder {

        public final ViewGroup root;
        public final TextView lblNumber, lblQuestion;

        public FAQOverviewViewHolder(View itemView, int color) {
            super(itemView);

            root = (ViewGroup) itemView.findViewById(R.id.root);
            root.setBackgroundResource(R.drawable.bg_selectable_item);
            root.getBackground().mutate();
            lblNumber = (TextView) itemView.findViewById(R.id.lbl_faq_number);
            lblQuestion = (TextView) itemView.findViewById(R.id.lbl_faq_question);

            lblNumber.setTextColor(color);
        }
    }

    public static class FAQOverviewSectionViewHolder extends RecyclerView.ViewHolder {

        public final ViewGroup root;
        public final TextView lblTitle;

        public FAQOverviewSectionViewHolder(View itemView) {
            super(itemView);

            root = (ViewGroup) itemView.findViewById(R.id.root);
            lblTitle = (TextView) root.findViewById(R.id.lbl_title);
        }
    }


    public static class FAQOverviewSectionEndViewHolder extends RecyclerView.ViewHolder {

        public FAQOverviewSectionEndViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface FAQOnClickListener {
        void onFAQClicked(FAQ faq);
    }
}
