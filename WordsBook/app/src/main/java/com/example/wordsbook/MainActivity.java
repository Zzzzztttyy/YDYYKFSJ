package com.example.wordsbook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements
        WordItemFragment.OnFragmentInteractionListener,
        WordDetailFragment.OnFragmentInteractionListener{
    private static final String TAG = "myTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(com.example.wordsbook.R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(com.example.wordsbook.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertDialog();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WordsDB wordsDB = WordsDB.getWordsDB();
        if (wordsDB != null)
            wordsDB.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.wordsbook.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                SearchDialog();
                return true;
            case R.id.action_insert:
                InsertDialog();
                return true;
            case R.id.action_translate1:
                Intent intent = new Intent(MainActivity.this, TranslateActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_help:
                HelpDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //当用户在单词详细Fragment中单击时回调此函数
    public void onWordDetailClick(Uri uri) { }

    //当用户在单词列表Fragment中单击某个单词时回调此函数,判断如果横屏的话，则需要在右侧单词详细Fragment中显示
    public void onWordItemClick(String id) {
        if(isLand()) {//横屏的话则在右侧的WordDetailFragment中显示单词详细信息
            ChangeWordDetailFragment(id);
        }else{
            Intent intent = new Intent(MainActivity.this,WordDetailActivity.class);
            intent.putExtra(WordDetailFragment.ARG_ID, id);
            startActivity(intent);
        }
    }

    private void RefreshWordItemFragment() {
        WordItemFragment wordItemFragment = (WordItemFragment) getFragmentManager()
                .findFragmentById(R.id.wordslist);
        wordItemFragment.refreshWordsList();
    }

    private void RefreshWordItemFragment(String strWord) {
        WordItemFragment wordItemFragment = (WordItemFragment) getFragmentManager()
                .findFragmentById(R.id.wordslist);
        wordItemFragment.refreshWordsList(strWord);
    }

    private void InsertDialog() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert
                , null);
        new AlertDialog.Builder(this)
                .setTitle("新增单词")
                .setView(tableLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strWord = ((EditText) tableLayout.findViewById(R.id.txtWord))
                                .getText().toString();
                        String strMeaning = ((EditText) tableLayout.findViewById(R.id.txtMeaning))
                                .getText().toString();
                        String strSample = ((EditText) tableLayout.findViewById(R.id.txtSample))
                                .getText().toString();
                        WordsDB wordsDB = WordsDB.getWordsDB();
                        wordsDB.InsertUserSql(strWord, strMeaning, strSample);
                        RefreshWordItemFragment();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }


    private void DeleteDialog(final String strId) {
        new AlertDialog.Builder(this).setTitle("删除单词").setMessage("是否真的删除单词?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        WordsDB wordsDB = WordsDB.getWordsDB();
                        wordsDB.DeleteUseSql(strId);


                        RefreshWordItemFragment();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }

    private void HelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle("这是一个帮助")

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();

    }

    //修改单词
    private void UpdateDialog(final String strId, final String strWord, final String strMeaning
            , final String strSample) {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater()
                .inflate(R.layout.insert, null);
        ((EditText) tableLayout.findViewById(R.id.txtWord)).setText(strWord);
        ((EditText) tableLayout.findViewById(R.id.txtMeaning)).setText(strMeaning);
        ((EditText) tableLayout.findViewById(R.id.txtSample)).setText(strSample);
        new AlertDialog.Builder(this)
                .setTitle("修改单词")
                .setView(tableLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strNewWord = ((EditText) tableLayout.findViewById(R.id.txtWord))
                                .getText().toString();
                        String strNewMeaning = ((EditText) tableLayout.findViewById(R.id.txtMeaning))
                                .getText().toString();
                        String strNewSample = ((EditText) tableLayout.findViewById(R.id.txtSample))
                                .getText().toString();
                        WordsDB wordsDB = WordsDB.getWordsDB();
                        wordsDB.UpdateUseSql(strId, strWord, strNewMeaning, strNewSample);

                        RefreshWordItemFragment();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();

    }

    //查找单词
    private void SearchDialog() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater()
                .inflate(R.layout.searchterm, null);
        new AlertDialog.Builder(this)
                .setTitle("查找单词")
                .setView(tableLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String txtSearchWord = ((EditText) tableLayout
                                .findViewById(R.id.txtSearchWord))
                                .getText().toString();
                        RefreshWordItemFragment(txtSearchWord);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();

    }


    private boolean isLand() {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return true;
        return false;
    }

    private void ChangeWordDetailFragment(String id) {
        Bundle arguments = new Bundle();
        arguments.putString(WordDetailFragment.ARG_ID, id);
        Log.v(TAG, id);

        WordDetailFragment fragment = new WordDetailFragment();
        fragment.setArguments(arguments);
        getFragmentManager().beginTransaction()
                .replace(R.id.worddetail, fragment).commit();
    }

    @Override
    public void onDeleteDialog(String strId) {
        DeleteDialog(strId);
    }

    @Override
    public void onUpdateDialog(String strId) {
        WordsDB wordsDB = WordsDB.getWordsDB();
        if (wordsDB != null && strId != null) {

            Words.WordDescription item = wordsDB.getSingleWord(strId);
            if (item != null) {
                UpdateDialog(strId, item.word, item.meaning, item.sample);
            }

        }

    }
}