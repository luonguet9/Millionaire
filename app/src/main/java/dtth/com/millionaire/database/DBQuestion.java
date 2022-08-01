package dtth.com.millionaire.database;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import dtth.com.millionaire.models.Question;

public class DBQuestion extends SQLiteOpenHelper {
    private static String DB_PATH = "";
    private static String DB_NAME = "Question.sqlite";
    private SQLiteDatabase database;
    private final Context context;

    public static final String TABLE_NAME = "Question";
    public static final String TABLE_ID = "_id";
    public static final String TABLE_QUESTION = "Question";
    public static final String TABLE_CASE_A = "CaseA";
    public static final String TABLE_CASE_B = "CaseB";
    public static final String TABLE_CASE_C = "CaseC";
    public static final String TABLE_CASE_D = "CaseD";
    public static final String TABLE_TRUE_CASE = "TrueCase";
    public int iopen = -1, icreate = -1;

    public DBQuestion(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        DB_PATH = this.context.getDatabasePath(DB_NAME).toString();
    }

    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }
        catch (SQLiteException e) {
            Log.e("message", "" + e);
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            icreate = 11;
        }
        else {
            this.getWritableDatabase();
            try {
                icreate = 1;
                copyDataBase();
            }
            catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    public void openDataBase() throws java.sql.SQLException {
        // Open the database
        String myPath = DB_PATH;
        database = SQLiteDatabase.openDatabase(myPath, null,SQLiteDatabase.OPEN_READONLY);
        if(database != null) iopen = 1;
    }

    public synchronized void close() {
        if(database != null) database.close();
        super.close();
    }

    private void copyDataBase() throws IOException {
        // Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DB_NAME);

        String outFileName = DB_PATH;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public ArrayList<Question> getdata(Activity activity) {
        ArrayList<Question> arrQues = new ArrayList<>();
        for(int i = 1; i < 16; i++) {
            String table = TABLE_NAME + i + "";
            String sql = "SELECT * FROM " + table + " ORDER BY random() limit 1";
            SQLiteOpenHelper sqLiteOpenHelper = new DBQuestion(activity);
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            int indexID = cursor.getColumnIndex(TABLE_ID);
            int indexQuestion = cursor.getColumnIndex(TABLE_QUESTION);
            int indexCaseA = cursor.getColumnIndex(TABLE_CASE_A);
            int indexCaseB = cursor.getColumnIndex(TABLE_CASE_B);
            int indexCaseC = cursor.getColumnIndex(TABLE_CASE_C);
            int indexCaseD = cursor.getColumnIndex(TABLE_CASE_D);
            int indexTrueCase = cursor.getColumnIndex(TABLE_TRUE_CASE);
            cursor.moveToFirst();
            int id = cursor.getInt(indexID);
            String question = cursor.getString(indexQuestion);
            String caseA = cursor.getString(indexCaseA);
            String caseB = cursor.getString(indexCaseB);
            String caseC = cursor.getString(indexCaseC);
            String caseD = cursor.getString(indexCaseD);
            int trueCase = cursor.getInt(indexTrueCase);
            Question qt = new Question(id, question, caseA, caseB, caseC, caseD, trueCase);
            arrQues.add(qt);
        }
        return arrQues;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion) {
            try {
                copyDataBase();
            } catch (IOException e) {
            }
        }
    }
}
