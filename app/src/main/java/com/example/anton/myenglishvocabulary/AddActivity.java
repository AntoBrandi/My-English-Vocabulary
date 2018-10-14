package com.example.anton.myenglishvocabulary;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.anton.myenglishvocabulary.data.WordDBHelper;
import com.example.anton.myenglishvocabulary.data.WordsContract.wordEntry;

import com.example.anton.myenglishvocabulary.data.WordsContract;

public class AddActivity extends AppCompatActivity {

    private WordDBHelper mDBHelper = new WordDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);





        /**
         * Faccio in modo che al click sul bottone di conferma possa passare il contenuto attuale delle EditText
         * Dal momento che sto fornendo più extra allo stesso intent, è necessario creare un Bundle o cursore
         * allo stesso modo sto attento in ricezione a estrarre i dati da un bundle
         */
        ImageButton imageButton =(ImageButton) findViewById(R.id.checkButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Inserisco i valori letti nel database
                 */
                EditText englishEdit= (EditText) findViewById(R.id.english_word);
                EditText italianEdit= (EditText) findViewById(R.id.italian_word);

                String english=englishEdit.getText().toString();
                String italian=italianEdit.getText().toString();


                if (italian.matches(""))
                {
                    italian="Italian";
                }
                if (english.matches(""))
                {
                    english="English";
                }
                SQLiteDatabase database = mDBHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(WordsContract.wordEntry.COLUMN_ENGLISH_WORD,english);
                values.put(WordsContract.wordEntry.COLUMN_ITALIAN_WORD,italian);
                database.insert(WordsContract.wordEntry.TABLE_NAME,null,values);
                finish();
            }
        });
    }
}
