package com.flyer.chat.listener;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by mike.li on 2018/5/2.
 */

public abstract class EditTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        OnTextChange(s);
    }
    public abstract void OnTextChange(Editable s);
}
