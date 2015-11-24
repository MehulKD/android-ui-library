package com.github.badoualy.ui.model;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.text.TextUtils;

public class FAQSection implements IFAQItem {

    public final CharSequence title;

    public FAQSection(CharSequence title) {
        this.title = title;
    }

    public FAQSection(Parcel in) {
        title = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
    }

    @Override
    public String toString() {
        return title.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        TextUtils.writeToParcel(title, dest, flags);
    }

    public static final Creator<FAQSection> CREATOR = new Creator<FAQSection>() {

        public FAQSection createFromParcel(@NonNull Parcel in) {
            return new FAQSection(in);
        }

        @NonNull
        public FAQSection[] newArray(int size) {
            return new FAQSection[size];
        }
    };
}
