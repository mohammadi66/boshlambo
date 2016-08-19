package ir.xzn.internetwan.boshlambo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class database extends SQLiteOpenHelper {

    public final String   path = "data/data/ir.xzn.internetwan.boshlambo/databases/";
    public final String   Name = "dic.db";
    public SQLiteDatabase mydb;
    private final Context mycontext;


    public database(Context context) {
        super(context, "dic.db", null, 1);
        mycontext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase arg0) {

    }


    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }


    public void useable() {
        boolean checkdb = checkdb();
        if (checkdb) {
        	//db.close();
        } else {
            this.getReadableDatabase();
            try {
                copydatabase();
            }
            catch (IOException e) {	
            
            }
        }
    }


    public void open() {
        mydb = SQLiteDatabase.openDatabase(path + Name, null, SQLiteDatabase.OPEN_READWRITE);
    }


    @Override
    public void close() {
        mydb.close();
    }


    public boolean checkdb() {
        SQLiteDatabase db = null;
        try {
            db = SQLiteDatabase.openDatabase(path + Name, null, SQLiteDatabase.OPEN_READONLY);
        }
        catch (SQLException e) {

        }
        return db != null ? true : false;
    }


    public void copydatabase() throws IOException {

        OutputStream myOutput = new FileOutputStream(path + Name);
        byte[] buffer = new byte[1024];
        int lenght;
        InputStream myInput = mycontext.getAssets().open(Name);
        while ((lenght = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, lenght);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();
    }
   //***********************  count and show in IntelliDic ***************************
    public Integer shomaresh_field(String table,String field,String entered,String field1){
    	Cursor Cursor = mydb.rawQuery("SELECT * FROM "+table+" WHERE "+field+" LIKE '"+entered+"' OR "+field1+" LIKE '"+entered+"%' LIMIT 5", null);
    	int i = Cursor.getCount();
    	return i ;
    }
    public String namayesh_fasl(String table , int row,String field,String word,String field1,int getstr){
    	Cursor Cursor = mydb.rawQuery("SELECT * FROM "+table+" WHERE "+field+" LIKE '"+word+"' OR "+field1+" LIKE '"+word+"%' LIMIT 5", null);
    	Cursor.moveToPosition(row);
    	String s = Cursor.getString(getstr);
    	return s ;
    }
    //********************************* count and show in WidgetsWindow  *********************************************
    public Integer shomaresh_field2(String table,String field,String entered){
    	Cursor Cursor = mydb.rawQuery("SELECT * FROM "+table+" WHERE "+field+" LIKE '"+entered+"%' LIMIT 5", null);
    	int i = Cursor.getCount();
    	return i ;
    }
    
    public String search_en(String table , int row, String field, String entered,int str){
    	Cursor Cursor = mydb.rawQuery("SELECT * FROM "+table+" WHERE "+field+" LIKE '"+entered+"%' LIMIT 5", null);
    	Cursor.moveToPosition(row);
    	String s = Cursor.getString(str);
    	return s ;
    }
    public String get_word(int col,String table, String field, String datapos){
    	Cursor cursor=mydb.rawQuery("SELECT * FROM "+table+" WHERE "+field+"='"+ datapos+ "'", null);
    	cursor.moveToFirst();
    	String faword=cursor.getString(col);
    	return faword;
    }
    //********* count and show in listview *********************************************************
    //  SELECT * FROM dictionary WHERE fav=1
    public Integer searchlist(){
    	Cursor cursor=mydb.rawQuery("SELECT * FROM dictionary WHERE fav=1 LIMIT 500", null);
    	int i=cursor.getCount();
    	return i;
    	
    }
    
    public Integer searchlist_fa2artbl(){
    	Cursor cursor=mydb.rawQuery("SELECT * FROM fa2ar WHERE fav=1 LIMIT 500", null);
    	int i=cursor.getCount();
    	return i;
    	
    }
    
    public String showList (String table, int row , int field){
    	Cursor Cursor = mydb.rawQuery("SELECT * FROM "+table+" WHERE fav=1 LIMIT 500", null);
    	Cursor.moveToPosition(row);
    	String save = Cursor.getString(field);
    	return save;
    	
    }
    //***************************************************************************************
    public Integer shomaresh_dastan (String table , String season){
    	Cursor Cursor = mydb.rawQuery("SELECT * FROM "+table+" where season='"+season+"' group by name", null);
    	int s = Cursor.getCount();
    	return s;
    	
    }
    
    public String namayesh_dastan (String table , int row , String season , int field){
    	Cursor Cursor = mydb.rawQuery("select * from "+table+" where season='"+season+"' group by name",null);
    	Cursor.moveToPosition(row);
    	String save = Cursor.getString(field);
    	return save;
    	
    }
    
    public Integer shomaresh_safhe_dastan (String table , String season , String story){
    	Cursor Cursor = mydb.rawQuery("select * from "+table+" where season='"+season+"' and name='"+story+"'",null);
    	int save = Cursor.getCount();
    	return save;
    }
    public String namayesh_matn (String table,String season,String name,int page){
    	Cursor Cursor = mydb.rawQuery("select * from "+table+" where season='"+season+"' and name='"+name+"' and page="+page,null);
    	Cursor.moveToFirst();
    	String save = Cursor.getString(2);
    	return save;
    }
    
    public String jostojoo(int row , int col , String word , String field){
    	Cursor cursor;
    	if (field.equals("name")) {
    		cursor = mydb.rawQuery("select * from datastorys where "+field+" like '%"+word+"%' group by name", null);
		}else {
    		cursor = mydb.rawQuery("select * from datastorys where "+field+" like '%"+word+"%'", null);
		}
    	cursor.moveToPosition(row);
    	String save = cursor.getString(col);	
    	return save;
    }
          
    
    public Integer shmaresh_jostojoo(String word , String field){
    	Cursor cursor;
    	if (field.equals("name")) {
    		cursor = mydb.rawQuery("select * from datastorys where "+field+" like '%"+word+"%' group by name", null);
		}else {
    		cursor = mydb.rawQuery("select * from datastorys where "+field+" like '%"+word+"%'", null);
		}
    	int save = cursor.getCount();
    	return save;
    }
    
    public void beroozresani_doostdashtaniha (String table ,String field, String word , String value){
    	ContentValues cv = new ContentValues();
    	
    	cv.put("fav", value);
    	mydb.update(table, cv, ""+field+"='"+word+"'", null);
    }
    
    public Integer shomaresh_doosrdashtaniha(String table){
    	Cursor Cursor = mydb.rawQuery("select * from "+table+" where star=1 group by name", null);
    	int save = Cursor.getCount();
    	return save;
    }
    
    public String namayesh_doosrdashtaniha(String table , int row , int field){
    	Cursor Cursor = mydb.rawQuery("select * from "+table+" where star=1 group by name", null);
    	Cursor.moveToPosition(row);
    	String save = Cursor.getString(field);
    	return save;
    }
    
}
