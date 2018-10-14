package com.example.anton.myenglishvocabulary;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class Word extends ArrayList {
    private String english_word;
    private String italian_word;

    public Word(String english_word,String italian_word)
    {
       this.english_word=english_word;
       this.italian_word=italian_word;
    }

    public String getEnglish_word()
    {
        return english_word;
    }

    public String getItalian_word() {
        return italian_word;
    }
}
