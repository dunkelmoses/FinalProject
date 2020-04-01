package Guardian;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * For saving articles for later viewing SQLite database is used
 * through this class.
 * */
public class GuardianDB extends SQLiteOpenHelper {

    private String tableName = "articles";
    private String colTitle = "webTitle";
    private String colUrl = "webUrl";
    private String colId = "id";
    private String colSectionName = "sectionName";
    private String colDate = "webPublicationDate";

    public GuardianDB(Context context) {
        super(context, "guardianDB", null, 1);
    }

    /**
     * This method creates a table called Favorites to allow storing articles to a favorites list or
     * removing them.
     * @param db The name of the database, guardianDB
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + tableName + "(" +
                colId + " text primary key, " +
                colTitle + " text, " +
                colUrl + " text, " +
                colSectionName + " text, " +
                colDate + " text" +
                ")";
        db.execSQL(query);
    }

    /**
     * in case of upgrade, old tables should be deleted
     *
     * @param db object of SQLiteDatabase
     * */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + tableName);
    }

    /**
     * To save the article details in the table
     * @param article Article to be saved
     * */
    public void saveArticle(Article article) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(colId, article.getId());
        contentValues.put(colTitle, article.getTitle());
        contentValues.put(colUrl, article.getUrl());

        contentValues.put(colSectionName, article.getSectionName());
        database.insert(tableName, null, contentValues);
    }

    /**
     * Performs SELECT * FROM <table> query and
     * Returns all the articles saved in the table
     *
     * @return List of Articles
     * */
    public List<Article> getAllSavedArticle() {
        //for reading database values
        SQLiteDatabase database = getReadableDatabase();
        //performing query and save result(s) in cursor
        Cursor cursor = database.query(tableName, new String[]{
                        colId, colTitle, colUrl, colDate, colSectionName
                }, null, null,
                null, null, null);
        List<Article> articles = new ArrayList<>();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            Article article = new Article();
            String aId = cursor.getString(cursor.getColumnIndex(colId));
            String title = cursor.getString(cursor.getColumnIndex(colTitle));
            String url = cursor.getString(cursor.getColumnIndex(colUrl));
            String section = cursor.getString(cursor.getColumnIndex(colSectionName));
            String date = cursor.getString(cursor.getColumnIndex(colDate));
            article.setId(aId);
            article.setTitle(title);
            article.setUrl(url);
            article.setSectionName(section);


            articles.add(article);

            cursor.moveToNext();
        }

        cursor.close();
        return articles;
    }

    /**
     * Deletes the article from the table.
     * similar to DELETE FROM <table> WHERE ID = ?
     *
     * @param id id of an article
     * @return number of deleted articles [it would be 1 in all cases]
     * */
    public Integer deleteArticle(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(tableName,
                colId + " = ? ",
                new String[]{id});
    }

    /**
     * Checks whether the articles is already saved or not
     * @return true if article is already saved and false if not
     * */
    public boolean isSaved(String id){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(tableName, new String[]{
                        colId, colTitle, colUrl, colDate, colSectionName
                }, colId + " = ?", new String[]{id},
                null, null, null);
        return cursor.getCount() != 0;
    }
}
