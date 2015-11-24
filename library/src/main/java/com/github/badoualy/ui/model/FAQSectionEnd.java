package com.github.badoualy.ui.model;

import android.os.Parcel;
import android.support.annotation.NonNull;

public class FAQSectionEnd implements IFAQItem {

    public FAQSectionEnd() {

    }

    public FAQSectionEnd(Parcel in) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }

    public static final Creator<FAQSectionEnd> CREATOR = new Creator<FAQSectionEnd>() {

        public FAQSectionEnd createFromParcel(@NonNull Parcel in) {
            return new FAQSectionEnd(in);
        }

        @NonNull
        public FAQSectionEnd[] newArray(int size) {
            return new FAQSectionEnd[size];
        }
    };
}
