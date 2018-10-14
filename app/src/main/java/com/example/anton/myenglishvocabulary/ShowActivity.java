package com.example.anton.myenglishvocabulary;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.anton.myenglishvocabulary.data.WordsContract.wordEntry;
import com.example.anton.myenglishvocabulary.data.WordDBHelper;

import org.w3c.dom.Text;


public class ShowActivity extends AppCompatActivity {

    private WordDBHelper mDBHelper = new WordDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        EditText englishEditText = (EditText) findViewById(R.id.show_english);
        EditText italianEditText = (EditText) findViewById(R.id.show_italian);
        ImageButton saveButton = (ImageButton) findViewById(R.id.show_save);

        final int position;
        String englishText;
        String italianText;

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            position= extras.getInt("position");
            englishText = extras.getString("english_word");
            italianText = extras.getString("italian_word");
        }
        else
        {
            position=-1;
            englishText="English Word";
            italianText="Italian Word";
        }

        if ((englishText == null)||(englishText=="")||(englishText==" "))
        {
            englishEditText.setText("English Word",TextView.BufferType.EDITABLE);
        }
        else
            englishEditText.setText(englishText);

        if ((italianText == null)||(italianText=="")||(italianText==" "))
        {
            italianEditText.setText("Italian Word",TextView.BufferType.EDITABLE);
        }
        else
            italianEditText.setText(italianText);




        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText engEdit = (EditText) findViewById(R.id.show_english);
                EditText itaEdit = (EditText) findViewById(R.id.show_italian);

                String eng = engEdit.getText().toString();
                String ita = itaEdit.getText().toString();

                if(eng.matches(""))
                {
                    eng="English";
                }
                if (ita.matches(""))
                {
                    ita="Italian";
                }

                SQLiteDatabase database = mDBHelper.getWritableDatabase();
                database.execSQL("UPDATE "+wordEntry.TABLE_NAME+" SET "+wordEntry.COLUMN_ENGLISH_WORD+" = '"+eng+"' , "+wordEntry.COLUMN_ITALIAN_WORD+" = '"+ita+"' WHERE "+wordEntry.COLUMN_ID+" = "+(position+1)+" ;");
                finish();
            }
        });
    }
}
