/**
 *
 * CREAZIONE DEL DATABASE
 *
 * Mi occupo di cosa accade al momento della creazione di un nuovo database o di un aggiornamento su di esso
 */


package com.example.anton.myenglishvocabulary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.anton.myenglishvocabulary.data.WordsContract.wordEntry;

public class WordDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "EnglishVocabulary";

    public WordDBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase database)
    {
        String CREATE_WORDS_TABLE = "CREATE TABLE "+ wordEntry.TABLE_NAME + "( "+
                wordEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                wordEntry.COLUMN_ENGLISH_WORD + " TEXT NOT NULL, " +
                wordEntry.COLUMN_ITALIAN_WORD + " TEXT NOT NULL );";
        database.execSQL(CREATE_WORDS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion)
    {

    }

}
