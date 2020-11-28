package me.replydev.notes_android.utils;

public class PasswordChecker {
    private final static PasswordStandard goodPasswordStandard = new PasswordStandard()
            .setMinimumPasswordLength(8)
            .setMinimumUpperChars(1)
            .setMinimumNumberChars(2)
            .setMinimumSpecialChars(1)
            .setMinimumLowerChars(1);

    public static boolean checkPwd(String pwd){

        PasswordStandard thisPasswordStandard = new PasswordStandard()
                .setMinimumPasswordLength(pwd.length());

        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerAlphabet = upperAlphabet.toLowerCase();
        String numberAlphabet = "0123456789";

        for(int i = 0; i < pwd.length(); i++){
            char c = pwd.charAt(i);
            if(containsChar(upperAlphabet,c)){
                thisPasswordStandard.incUpperChars();
            }
            else if (containsChar(lowerAlphabet,c)){
                thisPasswordStandard.incLowerChars();
            }
            else if(containsChar(numberAlphabet,c)){
                thisPasswordStandard.incNumberChars();
            }
            else{
                thisPasswordStandard.incSpecialChars();
            }
        }
        return thisPasswordStandard.compareTo(goodPasswordStandard) > 0;
    }

    private static boolean containsChar(String s, char c){
        for(int i = 0; i < s.length(); i++){
            if (c == s.charAt(i)){
                return true;
            }
        }
        return false;
    }
}
