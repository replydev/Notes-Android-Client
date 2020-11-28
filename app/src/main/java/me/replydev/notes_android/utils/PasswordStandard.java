package me.replydev.notes_android.utils;

public class PasswordStandard implements Comparable<PasswordStandard>{
    
    private int upperChars;
    private int lowerChars;
    private int numberChars;
    private int specialChars;
    private int passwordLength;

    @Override
    public int compareTo(PasswordStandard o) {
        if(
                        this.upperChars == o.upperChars &&
                        this.lowerChars == o.lowerChars &&
                        this.numberChars == o.numberChars &&
                        this.specialChars == o.specialChars &&
                        this.passwordLength == o.passwordLength
        ){
            return 0;
        }

        else if(
                this.upperChars < o.upperChars ||
                        this.lowerChars < o.lowerChars ||
                        this.numberChars < o.numberChars ||
                        this.specialChars < o.specialChars ||
                        this.passwordLength < o.passwordLength
        ){
            return -1;
        }
        else return 1;
    }

    public void incUpperChars(){
        this.upperChars++;
    }

    public void incLowerChars(){
        this.lowerChars++;
    }

    public void incNumberChars(){
        this.numberChars++;
    }

    public void incSpecialChars(){
        this.specialChars++;
    }

    public PasswordStandard setMinimumUpperChars(int upperChars) {
        this.upperChars = upperChars;
        return this;
    }

    public PasswordStandard setMinimumLowerChars(int lowerChars) {
        this.lowerChars = lowerChars;
        return this;
    }

    public PasswordStandard setMinimumNumberChars(int numberChars) {
        this.numberChars = numberChars;
        return this;
    }

    public PasswordStandard setMinimumSpecialChars(int specialChars) {
        this.specialChars = specialChars;
        return this;
    }

    public PasswordStandard setMinimumPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
        return this;
    }
}
