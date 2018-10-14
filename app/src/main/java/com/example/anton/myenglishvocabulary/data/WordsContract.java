/**
 *
 * CLASSE CONTRACT
 *
 * Si occupa di tenere un riferimento a tutti i nomi di tabelle colonne o keyword utili
 * che sranno usate nella comunicazione con il database.
 * Questo viene fatto al fine di evitare eventuali errori di scrittura nell'invio di comandi
 * al database.
 */


package com.example.anton.myenglishvocabulary.data;

import android.provider.BaseColumns;

public final class WordsContract {

    private WordsContract(){}


    public abstract class wordEntry implements BaseColumns
    {

        public static final String TABLE_NAME="Words_Table";

        public static final String COLUMN_ID = "_id";

        public static final String COLUMN_ENGLISH_WORD = "english";

        public static final String COLUMN_ITALIAN_WORD = "italian";

    }



}
