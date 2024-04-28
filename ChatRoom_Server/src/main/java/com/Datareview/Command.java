package com.Datareview;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command{

    private static Pattern pattern;
    private static String[] str = {
            "^([gG][rR][oO][uU][pP])\\s+(-[cC])\\s+(\\S+)\\s+(\\S+)\\s*$",//0
            "^([gG][rR][oO][uU][pP])\\s+(-[cC])\\s+(\\S+)\\s*$",//1
            "^([gG][rR][oO][uU][pP])\\s+(-[aA][uU])\\s+(\\S+)\\s+(\\d+)\\s*$",//2
            "^([gG][rR][oO][uU][pP])\\s+(-[rR][uU])\\s+(\\S+)\\s+(\\d+)\\s*$",//3
            "^([gG][rR][oO][uU][pP])\\s+(-[aA]{2})\\s+(\\S+)\\s+(\\d+)\\s*$",//4
            "^([gG][rR][oO][uU][pP])\\s+(-[rR][aA])\\s+(\\d+)\\s*$",//5
            "^([gG][rR][oO][uU][pP])\\s+(-[eE][nN])\\s+(\\d+)\\s*$",//6
            "^([gG][rR][oO][uU][pP])\\s+(-[eE][xX])\\s*$",//7
            "^([gG][rR][oO][uU][pP])\\s+(-[lL]\\s*$)",//8
            "^([eE][dD][iI][tT])\\s+(-[uU])\\s+(\\S+)\\s*$",//9
            "^([eE][dD][iI][tT])\\s+(-[bB])\\s+(\\S+\\s*)$",//10
            "^([eE][dD][iI][tT])\\s+(-[pP])\\s+(\\S+)\\s*$",//11
            "^([cC][hH][aA][tT])\\s+(-[eE][nN])\\s+(\\S+)\\s*$",//12
            "^([cC][hH][aA][tT])\\s+(-[eE][xX])\\s*$",//13
            "^([mM][aA][nN])\\s*$",//14
            "^([mM][aA][nN])\\s+(\\S+)\\s*$",//15
            "^([rR][eE][gG][iI][sS][tT][rR][aA][tT][iI][oO][nN]|[lL][oO][gG][iI][nN])\\s+(\\S+)\\s+(\\S+)\\s*$",//16
            "^([lL][oO][gG]\\s+[oO][uU][tT]\\s*)$"//17
    };

    public static Matcher Checker(Integer number,String input){
        pattern = Pattern.compile(str[number]);
        return pattern.matcher(input);
    }

}