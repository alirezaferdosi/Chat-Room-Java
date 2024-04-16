package com.Datareview;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command{

    private static Pattern pattern;
    private static String[] str = {
            "^([gG][rR][oO][uU][pP])\\s+(-[cC])\\s+(\\S+)\\s+(\\S+)\\s*$",
            "^([gG][rR][oO][uU][pP])\\s+(-[cC])\\s+(\\S+)\\s*$",
            "^([gG][rR][oO][uU][pP])\\s+(-[aA][uU])\\s+(\\S+)\\s+(\\d+)\\s*$",
            "^([gG][rR][oO][uU][pP])\\s+(-[rR][uU])\\s+(\\S+)\\s+(\\d+)\\s*$",
            "^([gG][rR][oO][uU][pP])\\s+(-[aA]{2})\\s+(\\S+)\\s+(\\d+)\\s*$",
            "^([gG][rR][oO][uU][pP])\\s+(-[rR][aA])\\s+(\\d+)\\s*$",
            "^([gG][rR][oO][uU][pP])\\s+(-[eE][nN])\\s+(\\d+)\\s*$",
            "^([rR][eE][gG][iI][sS][tT][rR][aA][tT][iI][oO][nN]|[lL][oO][gG][iI][nN])\\s+(\\S+)\\s+(\\S+)\\s*$",
            "^([eE][dD][iI][tT])\\s+(-[uU])\\s+(\\S+)\\s*$",
            "^([eE][dD][iI][tT])\\s+(-[bB])\\s+(\\S+\\s*)$",
            "^([eE][dD][iI][tT])\\s+(-[pP])\\s+(\\S+)\\s*$",
            "^([cC][hH][aA][tT])\\s+(\\S+)\\s*$",
            "^([mM][aA][nN])\\s*$",
            "^([mM][aA][nN])\\s+(\\S+)\\s*$",
            "^([gG][rR][oO][uU][pP])\\s+(-[lL]\\s*$)",
            "^([gG][rR][oO][uU][pP])\\s+(-[eE][xX])\\s*$",

    };

    public static Matcher Checker(Integer number,String input){
        pattern = Pattern.compile(str[number]);
        return pattern.matcher(input);
    }
}
// Groupm  -