package com.example.anton.myenglishvocabulary;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.FloatingActionButton;
import com.example.anton.myenglishvocabulary.data.WordDBHelper;
import java.util.ArrayList;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import java.util.ListIterator;

import com.example.anton.myenglishvocabulary.data.WordsContract;
import com.example.anton.myenglishvocabulary.data.WordsContract.wordEntry;



public class MainActivity extends AppCompatActivity {
    /**
     * Creazione dell'Array List o contenitore che dovrà contenere le parole aggiunte con il click
     * sul bottone add della view principale
     */
    private ArrayList<Word> words = new ArrayList<Word>();
    private ArrayList<Word> temporaryWords = new ArrayList<Word>();
    private WordAdapter wordAdapter;
    private WordDBHelper mDBHelper = new WordDBHelper(this);
    public AlertDialog dialog;
    public int max;
    public AlertDialog quizDialog;

    static final int WORD_RESULT=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordAdapter =new WordAdapter(this,words);

        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(wordAdapter);


        /**
         * Risposta al click sulla image button che lancia un intent e si aspetta una risposta
         * dalla activity che ho lanciato. Tale risposta sarà poi catturata dal metodo @onActivityResult
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,AddActivity.class);
                startActivityForResult(i,WORD_RESULT);
            }
        });


        /**
         * Gestione del click su un Item della textView
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                TextView englishTextView=(TextView) view.findViewById(R.id.english_textView);
                String englishText=englishTextView.getText().toString();
                TextView italianTextView=(TextView) view.findViewById(R.id.italian_textView);
                String italianText = italianTextView.getText().toString();
                Intent j = new Intent(MainActivity.this,ShowActivity.class);
                j.putExtra("position",position);
                j.putExtra("english_word",englishText);
                j.putExtra("italian_word",italianText);
                startActivity(j);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int position =i;

                AlertDialog.Builder mBuild = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.delete_dialog,null);

                mBuild.setView(mView);
                dialog = mBuild.create();

                Button confirmationButton = (Button) mView.findViewById(R.id.confirmationButton);
                Button annulationButton = (Button) mView.findViewById(R.id.annulationButton);

                annulationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                confirmationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Cancella dal database alla posizione selezionata
                        deleteItem(position);
                        dialog.dismiss();
                        UpdateView();
                    }
                });

                dialog.show();
                UpdateView();
                return true;
            }
        });


        UpdateView();
    }


    public void deleteItem (int position)
    {
        wordAdapter.clear();
        wordAdapter.notifyDataSetChanged();
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        String[] projection = {
                wordEntry.COLUMN_ENGLISH_WORD,
                wordEntry.COLUMN_ITALIAN_WORD
        };
        Cursor c = database.query(
                wordEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        try {
            int columnEnglishWord = c.getColumnIndex(wordEntry.COLUMN_ENGLISH_WORD);
            int columnItalianWord = c.getColumnIndex(wordEntry.COLUMN_ITALIAN_WORD);

            // Leggo il cursore
            while (c.moveToNext()) {
                String englishWord = c.getString(columnEnglishWord);
                String italianWord = c.getString(columnItalianWord);
                temporaryWords.add(new Word(englishWord, italianWord));

            }
        }
        finally {
            c.close();
        }

        temporaryWords.remove(position);

        deleteAllFromDB();

        int dim = temporaryWords.size();
        while(dim>0)
        {
            dim=dim-1;
            Word word = temporaryWords.get(dim);
            String english = word.getEnglish_word();
            String italian = word.getItalian_word();
            ContentValues values = new ContentValues();
            values.put(WordsContract.wordEntry.COLUMN_ENGLISH_WORD,english);
            values.put(WordsContract.wordEntry.COLUMN_ITALIAN_WORD,italian);
            database.insert(WordsContract.wordEntry.TABLE_NAME,null,values);
            words.add(new Word(english, italian));
        }

        wordAdapter.notifyDataSetChanged();
        temporaryWords.clear();
    }




    @Override
    protected void onStart()
    {
        super.onStart();
        UpdateView();
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        UpdateView();
    }
    @Override
    protected void onRestart()
    {
        super.onRestart();
        UpdateView();
    }



    /**
     * Si occupa di leggere dal database tutte le entry presenti in tabella
     * e di mostrarle all'utente in una ListView
     */
    public void UpdateView()
    {
        wordAdapter.clear();
        words.clear();
        wordAdapter.notifyDataSetChanged();
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        String[] projection = {
                wordEntry.COLUMN_ENGLISH_WORD,
                wordEntry.COLUMN_ITALIAN_WORD
        };


        Cursor c = database.query(
                wordEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        try {
            int columnEnglishWord = c.getColumnIndex(wordEntry.COLUMN_ENGLISH_WORD);
            int columnItalianWord = c.getColumnIndex(wordEntry.COLUMN_ITALIAN_WORD);

            // Leggo il cursore
            while (c.moveToNext()) {
                String englishWord = c.getString(columnEnglishWord);
                String italianWord = c.getString(columnItalianWord);
                words.add(new Word(englishWord, italianWord));
                wordAdapter.notifyDataSetChanged();
            }
        }
        finally {
            c.close();
        }
        if (wordAdapter.isEmpty())
        {
            deleteAllFromDB();
            words.clear();
        }
    }





    /**
     * Creazione del MENU e gestione del click su un elemento del menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_delete_all_items:
                deleteAllFromDB();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_settings:
                // do nothing for now
                return true;
            case R.id.action_quiz:
                // Mostro la Dialog che consenta di scegliere il numero di parole da mostrare nel quiz
                startQuiz();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void startQuiz()
    {
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        max = (int) DatabaseUtils.queryNumEntries(database, wordEntry.TABLE_NAME);

        AlertDialog.Builder mBuild = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.start_quiz_dialog,null);

        mBuild.setView(mView);
        quizDialog = mBuild.create();

        Button confirmationButton = (Button) mView.findViewById(R.id.quiz_confirmation);
        Button annulationButton = (Button) mView.findViewById(R.id.quiz_annulation);
        SeekBar seekBar = (SeekBar) mView.findViewById(R.id.quiz_seekbar);
        final TextView quantityText = (TextView) mView.findViewById(R.id.quiz_quantity);

        seekBar.setMax(max);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                quantityText.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Nothing for the moment
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            // Nothing for the moment
            }
        });


        annulationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quizDialog.dismiss();
            }
        });
        confirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int progress = Integer.parseInt(quantityText.getText().toString());
                Intent j = new Intent(MainActivity.this,QuizActivity.class);
                j.putExtra("progress",progress);
                quizDialog.dismiss();
                startActivity(j);
            }
        });

        quizDialog.show();
    }




    public void deleteAllFromDB()
    {
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        database.delete(wordEntry.TABLE_NAME,null,null);
        database.execSQL("DELETE FROM sqlite_sequence;");
        wordAdapter.clear();
        words.clear();
        wordAdapter.notifyDataSetChanged();
    }
}
