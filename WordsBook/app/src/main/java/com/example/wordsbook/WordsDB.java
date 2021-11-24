package com.example.wordsbook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordsDB {
    private static final String TAG = "myTag";
    private static WordsDBHelper mDbHelper;

    //采用单例模式
    private static WordsDB instance=new WordsDB();
    public static WordsDB getWordsDB(){
        return WordsDB.instance;
    }

    private WordsDB() {
        if (mDbHelper == null) {
            mDbHelper = new WordsDBHelper(WordsApplication.getContext());
        }
    }

    public void close() {
        if (mDbHelper != null)
            mDbHelper.close();
    }

    //获得单个单词的全部信息
    public Words.WordDescription getSingleWord(String id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String sql = "select * from words where _ID=?";
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        if (cursor.moveToNext()) {
            Words.WordDescription item = new Words.WordDescription(cursor.getString(cursor.getColumnIndex(Words.Word._ID)),
                    cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_WORD)),
                    cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_MEANING)),
                    cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_SAMPLE)));
            return item;
        }
        return null;
    }

    //得到全部单词列表
    public ArrayList<Map<String, String>> getAllWords() {
        if (mDbHelper == null) {
            Log.v(TAG, "WordsDB::getAllWords()");
            return null;
        }

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                Words.Word._ID,
                Words.Word.COLUMN_NAME_WORD
        };

        //排序
        String sortOrder =
                Words.Word.COLUMN_NAME_WORD + " ASC";


        Cursor c = db.query(
                Words.Word.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        return ConvertCursor2WordList(c);
    }

    //将游标转化为单词列表
    private ArrayList<Map<String, String>> ConvertCursor2WordList(Cursor cursor) {
        ArrayList<Map<String, String>> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put(Words.Word._ID, String.valueOf(cursor.getString(cursor.getColumnIndex(Words.Word._ID))));
            map.put(Words.Word.COLUMN_NAME_WORD, cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_WORD)));
            result.add(map);
        }
        return result;
    }

    //增加单词
    public void InsertUserSql(String strWord, String strMeaning, String strSample) {
        String sql = "insert into  words(_id,word,meaning,sample) values(?,?,?,?)";

        //Gets the data repository in write mode*/
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(sql, new String[]{GUID.getGUID(),strWord, strMeaning, strSample});
    }
    public void Insert(String strWord, String strMeaning, String strSample) {}

    //删除单词
    public void DeleteUseSql(String strId) {
        String sql = "delete from words where _id='" + strId + "'";
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL(sql);
    }
    public void Delete(String strId) {}

    //更新单词
    public void UpdateUseSql(String strId, String strWord, String strMeaning, String strSample) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "update words set word=?,meaning=?,sample=? where _id=?";
        db.execSQL(sql, new String[]{strWord,strMeaning, strSample, strId});
    }
    public void Update(String strId, String strWord, String strMeaning, String strSample) { }

    //查找
    public ArrayList<Map<String, String>> SearchUseSql(String strWordSearch) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String sql = "select * from words where word like ? order by word desc";
        Cursor c = db.rawQuery(sql, new String[]{"%" + strWordSearch + "%"});

        return ConvertCursor2WordList(c);
    }
    /*public ArrayList<Map<String, String>> Search(String strWordSearch) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                Words.Word._ID,
                Words.Word.COLUMN_NAME_WORD
        };

        String sortOrder =
                Words.Word._ID + " DESC";

        String selection = Words.Word._ID + " LIKE ?";
        String[] selectionArgs = {"%" + strWordSearch + "%"};

        Cursor c = db.query(
                Words.Word.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        return ConvertCursor2WordList(c);
    }*/

}
