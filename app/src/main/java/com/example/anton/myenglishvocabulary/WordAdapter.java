package com.example.anton.myenglishvocabulary;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.example.anton.myenglishvocabulary.data.WordDBHelper;
import com.example.anton.myenglishvocabulary.data.WordsContract.wordEntry;
import com.example.anton.myenglishvocabulary.data.WordDBHelper;
import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {


    public WordAdapter(Activity context, ArrayList<Word> words)
    {
        super(context,0,words);
    }



    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        Word word = getItem(position);

        View listItemView = convertView;

        if(listItemView==null)
        {
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        TextView english_word = (TextView) listItemView.findViewById(R.id.english_textView);
        english_word.setText(word.getEnglish_word());

        TextView italian_word = (TextView) listItemView.findViewById(R.id.italian_textView);
        italian_word.setText(word.getItalian_word());

        return listItemView;
    }
}

