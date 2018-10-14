package com.example.anton.myenglishvocabulary;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.anton.myenglishvocabulary.data.WordDBHelper;
import com.example.anton.myenglishvocabulary.data.WordsContract.wordEntry;
import java.util.ArrayList;
import java.util.Random;



public class QuizActivity extends AppCompatActivity {

    private WordDBHelper mDBHelper = new WordDBHelper(this);

    public int progress;
    public int actual_question=0;
    public int random;
    public ArrayList<String> questions=new ArrayList<String>();
    public ArrayList<String> Correctanswers=new ArrayList<String>();
    public ArrayList<String> answers = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int max = (int) DatabaseUtils.queryNumEntries(db, wordEntry.TABLE_NAME);


        // Take the information from the intent
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            progress= extras.getInt("progress");
        }
        else
        {
            progress=-1;
            Toast.makeText(getApplicationContext(),"Errore nell' avviare l'applicazione",Toast.LENGTH_SHORT);
            finish();
        }


        // Setto la domanda interrogando il db
        String[] projection = {wordEntry.COLUMN_ENGLISH_WORD,wordEntry.COLUMN_ITALIAN_WORD};
        for(int i=1;i<=(progress+2);i++)
        {
            // In realtà valuto il random su tutta la dimensione della tabella
            random = new Random().nextInt((max + 1));
            String where = wordEntry.COLUMN_ID+" = "+random+" ;";
            SQLiteDatabase database = mDBHelper.getWritableDatabase();
            Cursor c = database.query(wordEntry.TABLE_NAME,
                                      projection,
                                      where,
                                      null,
                                      null,
                                      null,
                                      null
                    );
            try {
                int englishColumnIndex = c.getColumnIndex(wordEntry.COLUMN_ENGLISH_WORD);
                int italianColumnIndex = c.getColumnIndex(wordEntry.COLUMN_ITALIAN_WORD);

                while (c.moveToNext())
                {
                    //Estraggo colonna x colonna

                    String english = c.getString(englishColumnIndex);
                    String italian = c.getString(italianColumnIndex);
                    questions.add((""+english).toUpperCase());
                    Correctanswers.add((""+italian).toUpperCase());
                }
            }
            finally {
                c.close();
            }
        }

        refreshView(actual_question);


        ImageButton quiz_next= (ImageButton) findViewById(R.id.quiz_next_button);
        quiz_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(actual_question<(progress)) {

                    // Prelevo la risposta e la inserisco nell'array di parole
                    EditText quizWordtoRead = (EditText) findViewById(R.id.quiz_word_to_read);
                    answers.add(quizWordtoRead.getText().toString().toUpperCase());
                    actual_question++;
                    if(actual_question<(progress)) {
                        refreshView(actual_question);
                    }
                    else
                    {
                        scoreCalculation();
                    }
                }
            }
        });
    }


    /**
     * Delete from EditText and set text for the title and for the question word
     * @param actual_question is the actual question that we are evaluating
     */
    public void refreshView(int actual_question)
    {
        TextView quizWordtoShow = (TextView) findViewById(R.id.quiz_word_to_show);
        TextView quizTitle = (TextView) findViewById(R.id.activity_quiz_title);
        EditText quizWordtoRead = (EditText) findViewById(R.id.quiz_word_to_read);

        quizWordtoRead.getText().clear();
        quizTitle.setText("Domanda "+(actual_question+1)+" di "+progress);
        if(actual_question<progress) {
            quizWordtoShow.setText(questions.get(actual_question));
        }
        else
        {
            scoreCalculation();
        }
    }


    /**
     * Confrontation between two arrayLists
     * @return the score
     */
    public void scoreCalculation()
    {
        int score=0;

        for (int pos=0;pos<(progress);pos++)
        {
            if(answers.get(pos).equals(Correctanswers.get(pos)))
            {
                score++;
            }
        }

        Toast.makeText(getApplicationContext(),"Il tuo punteggio: "+score,Toast.LENGTH_LONG).show();
        Intent j = new Intent(QuizActivity.this,MainActivity.class);
        startActivity(j);
    }

}
