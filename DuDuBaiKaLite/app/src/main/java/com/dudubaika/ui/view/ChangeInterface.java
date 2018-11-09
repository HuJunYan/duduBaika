package com.dudubaika.ui.view;

import android.text.Editable;

public interface ChangeInterface {
    void change(CharSequence s, int start, int before, int count);
    void changeBefore(CharSequence s, int start, int count, int after);
    void chageAfter(Editable s);
}