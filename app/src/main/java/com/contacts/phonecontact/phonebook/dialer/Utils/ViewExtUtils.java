package com.contacts.phonecontact.phonebook.dialer.Utils;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class ViewExtUtils {
    private static int getCharKeyCode(char c) {
        if (c == '0') {
            return 7;
        }
        if (c == '1') {
            return 8;
        }
        if (c == '2') {
            return 9;
        }
        if (c == '3') {
            return 10;
        }
        if (c == '4') {
            return 11;
        }
        if (c == '5') {
            return 12;
        }
        if (c == '6') {
            return 13;
        }
        if (c == '7') {
            return 14;
        }
        if (c == '8') {
            return 15;
        }
        if (c == '9') {
            return 16;
        }
        if (c == '*') {
            return 17;
        }
        return c == '+' ? 81 : 18;
    }

    public static void addCharacter(EditText editText, char c) {
        Log.e("fatal4", "addCharacter: " + getCharKeyCode(c) );
        editText.dispatchKeyEvent(getKeyEvent(editText, getCharKeyCode(c)));
    }

    public static KeyEvent getKeyEvent(EditText editText, int i) {
        Log.e("fatal4", "getKeyEvent: " + i );
        return new KeyEvent(0, 0, 0, i, 0);
    }

    public static boolean performHapticFeedback(View view) {
        return view.performHapticFeedback(1);
    }

}
