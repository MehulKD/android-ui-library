package com.github.badoualy.ui.listener;


import android.app.Fragment;
import android.app.FragmentManager;

public interface FragmentTransactionHandler {

    void executeTransaction(FragmentTransactionRunnable transactionRunnable);

    void displayFragment(Fragment fragment);

    void displayFragment(Fragment fragment, boolean addToBackStack);

    void displayFragment(Fragment fragment, boolean addToBackStack, boolean clearStack);

    /** A custom runnable that will operate a FragmentTransaction */
    interface FragmentTransactionRunnable {
        void run(FragmentManager fm, int fragmentId);
    }
}
