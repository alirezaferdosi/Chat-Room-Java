package com.Datareview;

import java.util.regex.Pattern;

/**
 *  This class is for checking the correct pattern of input data .
 */
public class PatternCheck {

    public Boolean UsernameCheck(String str) {
        return Pattern.matches("(?:@)?[a-zA-Z0-9]+", str);
    }

    public Boolean PasswrodCheck(String str) {
        return Pattern.matches("[a-zA-Z0-9@#$%^&*+-_.]+", str);
    }
}
