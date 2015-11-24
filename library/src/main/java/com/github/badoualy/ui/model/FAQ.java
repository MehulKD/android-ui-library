package com.github.badoualy.ui.model;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.text.TextUtils;

public class FAQ implements IFAQItem {

    public CharSequence question, answer;
    public int number;

    public FAQ() {

    }

    public FAQ(@NonNull Parcel in) {
        question = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        answer = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        number = in.readInt();
    }

    public FAQ(CharSequence question, CharSequence answer, int number) {
        this.question = question;
        this.answer = answer;
        this.number = number;
    }

    @Override
    public String toString() {
        return question + " : " + answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        TextUtils.writeToParcel(question, dest, flags);
        TextUtils.writeToParcel(answer, dest, flags);
        dest.writeInt(number);
    }

    public static final Creator<FAQ> CREATOR = new Creator<FAQ>() {

        public FAQ createFromParcel(@NonNull Parcel in) {
            return new FAQ(in);
        }

        @NonNull
        public FAQ[] newArray(int size) {
            return new FAQ[size];
        }
    };
}
