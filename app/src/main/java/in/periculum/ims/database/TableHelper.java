package in.periculum.ims.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import in.periculum.ims.ds.Material;
import in.periculum.ims.ds.NotificationDs;
import in.periculum.ims.ds.Sites;
import in.periculum.ims.ds.Stock;
import in.periculum.ims.ds.StockRcv;
import in.periculum.ims.utility.CommonUtility;
import in.periculum.ims.utility.ImsUtility;

/**
 * Created by roshan.kumar1 on 12/7/2015.
 */
public class TableHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static TableHelper sInstance;
    private SQLiteDatabase database = null;

    public TableHelper(Context context) {
        super(context, ImsUtility.DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    public static synchronized TableHelper getInstance(Context context) {
        // Use the application context, which will ensure that you 
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new TableHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Tables.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Tables.onUpgrade(db, oldVersion, newVersion);
    }

    public void emptyMaterials() {
        database = getWritableDatabase();
        database.delete(Tables.TABLE_MATERIAL, null, null);
        database.close();
    }

    public void emptyCMaterials() {
        database = getWritableDatabase();
        database.delete(Tables.TABLE_CMATERIAL, null, null);
        database.close();
    }
    public String getMaterialName(String materialcode) {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select "+Tables.COLUMN_DESCRIPTION+" from "
                + Tables.TABLE_MATERIAL + " where " + Tables.COLUMN_MATERIAL_CODE + " = '" + materialcode.trim() + "'", null);
        String materialname = null;

        if (cursor.moveToFirst()) {
            materialname= cursor.getString(cursor.getColumnIndexOrThrow(Tables.COLUMN_DESCRIPTION)) ;
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return materialname;
    }
    public Material getMaterial(String materialcode) {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from "
                + Tables.TABLE_MATERIAL + " where " + Tables.COLUMN_MATERIAL_CODE + " = '" + materialcode.trim() + "'", null);
        Material material = null;

        if (cursor.moveToFirst()) {
            material = new Material(cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CODE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Tables.COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_DENO)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_BAY_INFO)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_PREFERED_LOC)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_BIN_INFO)), "0", cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_DATEMODIFIED)));
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return material;
    }

    public boolean dbMaterialHasData(String searchKey) {
        String query = "Select * from " + Tables.TABLE_MATERIAL + " where " + Tables.COLUMN_MATERIAL_CODE + " = ?";
        database = getReadableDatabase();
        boolean c = database.rawQuery(query, new String[]{searchKey}).moveToFirst();
        database.close();
        return c;
    }

    public boolean dbCMaterialHasData(String searchKey) {
        String query = "Select * from " + Tables.TABLE_CMATERIAL + " where " + Tables.COLUMN_MATERIAL_CODE + " = ?";
        database = getReadableDatabase();
        boolean c = database.rawQuery(query, new String[]{searchKey}).moveToFirst();
        database.close();
        return c;
    }

    public ArrayList<Material> getAllMaterials() {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from "
                + Tables.TABLE_MATERIAL, null);
        ArrayList<Material> materials = new ArrayList<Material>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                materials.add(new Material(cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Tables.COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_DENO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_BAY_INFO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_PREFERED_LOC)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_BIN_INFO)), "0", cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_DATEMODIFIED))));
                cursor.moveToNext();
            }
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return materials;
    }


    public ArrayList<Material> getClientAddedMaterials() {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from "
                + Tables.TABLE_CMATERIAL, null);
        ArrayList<Material> materials = new ArrayList<Material>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                materials.add(new Material(cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Tables.COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_DENO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_PREFERED_LOC)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_BAY_INFO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_BIN_INFO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_SERVER_UPLOADED)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_DATEMODIFIED))));
                cursor.moveToNext();
            }
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return materials;
    }

    public void addMaterial(Material material) {
        database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Tables.COLUMN_MATERIAL_CODE, material.getMaterialCode().trim());
        values.put(Tables.COLUMN_DESCRIPTION, material.getMaterialDesc());
        values.put(Tables.COLUMN_BAY_INFO, material.getBay());
        values.put(Tables.COLUMN_BIN_INFO, material.getBin());
        values.put(Tables.COLUMN_MATERIAL_DENO, material.getMaterialDeno());
        values.put(Tables.COLUMN_PREFERED_LOC, material.getPreferredLocation());
        values.put(Tables.COLUMN_DATEMODIFIED, material.getDateModified());

        values.put(Tables.COLUMN_SERVER_UPLOADED, material.getServer_uploaded());
        database.insert(Tables.TABLE_MATERIAL, null, values);

        if (database != null)
            database.close();

    }

    public void addCMaterial(Material material) {
        database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Tables.COLUMN_MATERIAL_CODE, material.getMaterialCode());
        values.put(Tables.COLUMN_DESCRIPTION, material.getMaterialDesc());
        values.put(Tables.COLUMN_BAY_INFO, material.getBay());
        values.put(Tables.COLUMN_BIN_INFO, material.getBin());
        values.put(Tables.COLUMN_MATERIAL_DENO, material.getMaterialDeno());
        values.put(Tables.COLUMN_PREFERED_LOC, material.getPreferredLocation());
        values.put(Tables.COLUMN_SERVER_UPLOADED, material.getServer_uploaded());
        values.put(Tables.COLUMN_DATEMODIFIED, CommonUtility.getDate());

        database.insert(Tables.TABLE_CMATERIAL, null, values);

        if (database != null)
            database.close();

    }

    public void addAllMaterial(List<Material> materials) {
        database = getWritableDatabase();
        // you can use INSERT only
        String sql = "INSERT OR REPLACE INTO " + Tables.TABLE_MATERIAL + " ( " + Tables.COLUMN_MATERIAL_CODE + ","
                + Tables.COLUMN_DESCRIPTION + "," + Tables.COLUMN_BAY_INFO + "," + Tables.COLUMN_BIN_INFO + "," + Tables.COLUMN_MATERIAL_DENO + ","
                + Tables.COLUMN_PREFERED_LOC + "," + Tables.COLUMN_DATEMODIFIED + " ) VALUES ( ?, ? ,?,?,?,?,?)";

        SQLiteDatabase db = this.getWritableDatabase();

        /*
         * According to the docs http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html
         * Writers should use beginTransactionNonExclusive() or beginTransactionWithListenerNonExclusive(SQLiteTransactionListener)
         * to start a transaction. Non-exclusive mode allows database file to be in readable by other threads executing queries.
         */
        db.beginTransactionNonExclusive();
        // db.beginTransaction();

        SQLiteStatement stmt = db.compileStatement(sql);

        for (Material material : materials) {

            stmt.bindString(1, material.getMaterialCode());
            if (material.getMaterialDesc() != null)
                stmt.bindString(2, material.getMaterialDesc());
            else
                stmt.bindString(2, "");
            stmt.bindString(3, material.getBay());
            stmt.bindString(4, material.getBin());
            stmt.bindString(5, material.getMaterialDeno());
            stmt.bindString(6, material.getPreferredLocation());

            stmt.bindString(7, material.getDateModified());

            stmt.execute();
            stmt.clearBindings();

        }

        db.setTransactionSuccessful();
        db.endTransaction();


        if (database != null)
            database.close();

    }

    public void updateMaterial(Material material) {
        database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Tables.COLUMN_MATERIAL_CODE, material.getMaterialCode());
        values.put(Tables.COLUMN_DESCRIPTION, material.getMaterialDesc());
        values.put(Tables.COLUMN_BAY_INFO, material.getBay());
        values.put(Tables.COLUMN_BIN_INFO, material.getBin());
        values.put(Tables.COLUMN_MATERIAL_DENO, material.getMaterialDeno());
        values.put(Tables.COLUMN_SERVER_UPLOADED, material.getServer_uploaded());
        database.update(Tables.TABLE_MATERIAL, values,
                Tables.COLUMN_MATERIAL_CODE + "=?", new String[]{material.getMaterialCode()});
        if (database != null)
            database.close();

    }

    public void removeMaterial(Material material) {
        database = getWritableDatabase();

        database.delete(Tables.TABLE_MATERIAL, Tables.COLUMN_MATERIAL_CODE
                + "=? ", new String[]{material.getMaterialCode()});

        if (database != null)
            database.close();

    }


    //for site

    public void emptySites() {
        database = getWritableDatabase();
        database.delete(Tables.TABLE_SITE, null, null);
        database.close();
    }

    public Sites getSite(String whno) {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from "
                + Tables.TABLE_SITE + " where " + Tables.COLUMN_WHNo + " = " + whno, null);
        Sites site = null;
        if (cursor != null && cursor.getCount() > 0) {

            site = new Sites(cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_WHNo)),
                    cursor.getString(cursor
                            .getColumnIndexOrThrow(Tables.COLUMN_WHDesc)));


        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return site;
    }

    public boolean dbSiteHasData(String searchKey) {
        database = getReadableDatabase();
        String query = "Select * from " + Tables.TABLE_SITE + " where " + Tables.COLUMN_WHNo + " = ?";
        boolean c = database.rawQuery(query, new String[]{searchKey}).moveToFirst();
        database.close();
        return c;
    }

    public ArrayList<Sites> getAllSites() {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from "
                + Tables.TABLE_SITE, null);
        ArrayList<Sites> sites = new ArrayList<Sites>();
        if (cursor.moveToFirst()) {
            do {
                sites.add(new Sites(cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_WHNo)),
                        cursor.getString(cursor
                                .getColumnIndexOrThrow(Tables.COLUMN_WHDesc))));
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return sites;
    }

    public void addSite(Sites site) {
        database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Tables.COLUMN_WHNo, site.getWHNo());
        values.put(Tables.COLUMN_WHDesc, site.getWHDesc());

        database.insert(Tables.TABLE_SITE, null, values);

        if (database != null)
            database.close();

    }

    public void updateSite(Sites site) {
        database = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Tables.COLUMN_WHDesc, site.getWHDesc());

        database.update(Tables.TABLE_SITE, values,
                Tables.COLUMN_WHNo + "=?", new String[]{site.getWHNo()});
        if (database != null)
            database.close();

    }

    public void removeSite(Sites site) {
        database = getWritableDatabase();

        database.delete(Tables.TABLE_SITE, Tables.COLUMN_WHNo
                + "=? ", new String[]{site.getWHNo()});

        if (database != null)
            database.close();

    }


    //for stock

    public void emptyStock() {
        database = getWritableDatabase();
        database.delete(Tables.TABLE_STOCK, null, null);
        database.close();
    }

    public boolean dbStockHasData(String mcode, String stockSrl) {
        String query = "Select * from " + Tables.TABLE_STOCK + " where " + Tables.COLUMN_MATERIAL_CODE + " = ? AND " + Tables.COLUMN_STOCK_SRL + " =?";
        return getReadableDatabase().rawQuery(query, new String[]{mcode, stockSrl}).moveToFirst();
    }

    public Stock getStock(String materialCode, String stockSrl) {
        database = getReadableDatabase();
        String query = "Select * from " + Tables.TABLE_STOCK + " where " + Tables.COLUMN_MATERIAL_CODE + " = ? AND " + Tables.COLUMN_STOCK_SRL + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{materialCode.trim(), stockSrl.trim()});

        Stock stock = null;
        if (cursor.moveToFirst()) {
            stock = new Stock(cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CODE)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_STOCK_SRL)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_WHNo)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_BATCHNO)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CONDITIONCODE)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_SMALLEST_ISSUABLE_PACKQTY)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_PACKTYPE)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_LOCATION_MARKING)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_QTYGROUND)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_GOODS_RECEIPTNO)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_DATEMODIFIED)));
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return stock;
    }


    public ArrayList<Stock> getAllStock() {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from "
                + Tables.TABLE_STOCK, null);
        ArrayList<Stock> stocks = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                stocks.add(new Stock(cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_STOCK_SRL)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_WHNo)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_BATCHNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CONDITIONCODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_SMALLEST_ISSUABLE_PACKQTY)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_PACKTYPE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_LOCATION_MARKING)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_QTYGROUND)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_GOODS_RECEIPTNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_DATEMODIFIED))));
                cursor.moveToNext();
            }
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return stocks;
    }

    public ArrayList<Stock> getStockByMcode(String mcode) {
        if (mcode.length() != 0) {
            mcode = "%" + mcode + "%";
        }
        String selectQuery = " select *  from " + Tables.TABLE_STOCK + " where " + Tables.COLUMN_MATERIAL_CODE + " like  '"
                + mcode + "' ORDER BY " + Tables.COLUMN_MATERIAL_CODE + " DESC";
        database = getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);
        ArrayList<Stock> stocks = new ArrayList<>();
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                stocks.add(new Stock(cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_STOCK_SRL)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_WHNo)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_BATCHNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CONDITIONCODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_SMALLEST_ISSUABLE_PACKQTY)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_PACKTYPE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_LOCATION_MARKING)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_QTYGROUND)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_GOODS_RECEIPTNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_DATEMODIFIED))));
                cursor.moveToNext();
            }
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return stocks;
    }

    public ArrayList<Stock> getStockByCondition(String siteId) {
        if (siteId.length() != 0) {
            siteId = "%" + siteId + "%";
        }
        String selectQuery = " select *  from " + Tables.TABLE_STOCK + " where " + Tables.COLUMN_WHNo + " like  '"
                + siteId + "' ORDER BY " + Tables.COLUMN_MATERIAL_CODE + " DESC";
        database = getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);
        ArrayList<Stock> stocks = new ArrayList<>();
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                stocks.add(new Stock(cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_STOCK_SRL)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_WHNo)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_BATCHNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CONDITIONCODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_SMALLEST_ISSUABLE_PACKQTY)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_PACKTYPE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_LOCATION_MARKING)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_QTYGROUND)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_GOODS_RECEIPTNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_DATEMODIFIED))));
                cursor.moveToNext();
            }
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return stocks;
    }

    public ArrayList<Stock> getStockByCondition(String siteId, String mcode) {
        if (siteId.length() != 0) {
            siteId = "%" + siteId + "%";
        }
        if (mcode.length() != 0)
            mcode = "%" + mcode + "%";

        String selectQuery = " select *  from " + Tables.TABLE_STOCK + " where " + Tables.COLUMN_WHNo + " like  '"
                + siteId + "' AND " + Tables.COLUMN_MATERIAL_CODE + " like '" + mcode + "' ORDER BY " + Tables.COLUMN_MATERIAL_CODE + " DESC";
        database = getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);
        ArrayList<Stock> stocks = new ArrayList<>();
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                stocks.add(new Stock(cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_STOCK_SRL)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_WHNo)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_BATCHNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CONDITIONCODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_SMALLEST_ISSUABLE_PACKQTY)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_PACKTYPE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_LOCATION_MARKING)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_QTYGROUND)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_GOODS_RECEIPTNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_DATEMODIFIED))));
                cursor.moveToNext();
            }
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return stocks;
    }

    public ArrayList<Stock> getStockByCondition(String siteId, String mcode, String materialName) {
        if (siteId.length() != 0) {
            siteId = "%" + siteId + "%";
        }
        if (mcode.length() != 0)
            mcode = "%" + mcode + "%";
        if (materialName.length() != 0)
            materialName = "%" + materialName + "%";

        String selectQuery = " select *  from " + Tables.TABLE_STOCK + " where " + Tables.COLUMN_WHNo + " like  '"
                + siteId + "' AND " + Tables.COLUMN_MATERIAL_CODE + " like '" + mcode + "' AND " + Tables.COLUMN_MATERIAL_DESC + " like '" + materialName + "' ORDER BY " + Tables.COLUMN_MATERIAL_CODE + " DESC";
        database = getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);
        ArrayList<Stock> stocks = new ArrayList<>();
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                stocks.add(new Stock(cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_STOCK_SRL)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_WHNo)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_BATCHNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CONDITIONCODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_SMALLEST_ISSUABLE_PACKQTY)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_PACKTYPE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_LOCATION_MARKING)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_QTYGROUND)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_GOODS_RECEIPTNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_DATEMODIFIED))));
                cursor.moveToNext();
            }
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return stocks;
    }

    public ArrayList<Stock> getStockByCondition(String siteId, String mcode, String materialName, String stockqty) {
        if (siteId.length() != 0) {
            siteId = "%" + siteId + "%";
        }
        if (mcode.length() != 0)
            mcode = "%" + mcode + "%";
        if (materialName.length() != 0)
            materialName = "%" + materialName + "%";

        String selectQuery = " select *  from " + Tables.TABLE_STOCK + " where " + Tables.COLUMN_WHNo + " like  '"
                + siteId + "' AND " + Tables.COLUMN_MATERIAL_CODE + " like '" + mcode + "' AND " + Tables.COLUMN_MATERIAL_DESC + " like '" + materialName +
                "' AND " + Tables.COLUMN_QTYGROUND + " >= " + stockqty ;
        database = getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);
        ArrayList<Stock> stocks = new ArrayList<>();
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                stocks.add(new Stock(cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_STOCK_SRL)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_WHNo)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_BATCHNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CONDITIONCODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_SMALLEST_ISSUABLE_PACKQTY)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_PACKTYPE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_LOCATION_MARKING)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_QTYGROUND)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_GOODS_RECEIPTNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_DATEMODIFIED))));
                cursor.moveToNext();
            }
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return stocks;
    }

    public ArrayList<Stock> getStockByConditionNoSite( String mcode, String materialName, String stockqty) {

        if (mcode.length() != 0)
            mcode = "%" + mcode + "%";
        if (materialName.length() != 0)
            materialName = "%" + materialName + "%";

        String selectQuery = " select *  from " + Tables.TABLE_STOCK + " where "+ Tables.COLUMN_MATERIAL_CODE + " like '" + mcode + "' AND " + Tables.COLUMN_MATERIAL_DESC + " like '" + materialName +
                "' AND " + Tables.COLUMN_QTYGROUND + " >= " + stockqty + " ORDER BY " + Tables.COLUMN_MATERIAL_CODE + " DESC";
        database = getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);
        ArrayList<Stock> stocks = new ArrayList<>();
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                stocks.add(new Stock(cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_STOCK_SRL)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_WHNo)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_BATCHNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CONDITIONCODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_SMALLEST_ISSUABLE_PACKQTY)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_PACKTYPE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_LOCATION_MARKING)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_QTYGROUND)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_GOODS_RECEIPTNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_DATEMODIFIED))));
                cursor.moveToNext();
            }
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return stocks;
    }
    public ArrayList<Stock> getStockByConditionNoSiteMcode(String materialName, String stockqty) {

        if (materialName.length() != 0)
            materialName = "%" + materialName + "%";

        String selectQuery = " select *  from " + Tables.TABLE_STOCK + " where " + Tables.COLUMN_MATERIAL_DESC + " like '" + materialName +
                "' AND " + Tables.COLUMN_QTYGROUND + " >= " + stockqty + " ORDER BY " + Tables.COLUMN_MATERIAL_CODE + " DESC";
        database = getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);
        ArrayList<Stock> stocks = new ArrayList<>();
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                stocks.add(new Stock(cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_STOCK_SRL)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_WHNo)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_BATCHNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CONDITIONCODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_SMALLEST_ISSUABLE_PACKQTY)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_PACKTYPE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_LOCATION_MARKING)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_QTYGROUND)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_GOODS_RECEIPTNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_DATEMODIFIED))));
                cursor.moveToNext();
            }
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return stocks;
    }
    public ArrayList<Stock> getStockByStockQty(String stockqty) {

        String selectQuery = " select *  from " + Tables.TABLE_STOCK + " where " + Tables.COLUMN_QTYGROUND + " >= " + stockqty + " ORDER BY " + Tables.COLUMN_MATERIAL_CODE + " DESC";
        database = getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);
        ArrayList<Stock> stocks = new ArrayList<>();
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                stocks.add(new Stock(cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_STOCK_SRL)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_WHNo)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_BATCHNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CONDITIONCODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_SMALLEST_ISSUABLE_PACKQTY)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_PACKTYPE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_LOCATION_MARKING)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_QTYGROUND)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_GOODS_RECEIPTNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_DATEMODIFIED))));
                cursor.moveToNext();
            }
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return stocks;
    }
    public ArrayList<Stock> getStockByMName(String mname) {

        if (mname.length() != 0)
            mname = "%" + mname + "%";

        String selectQuery = " select *  from " + Tables.TABLE_STOCK + " where " +Tables.COLUMN_MATERIAL_DESC + " like '" + mname + " ORDER BY " + Tables.COLUMN_MATERIAL_CODE + " DESC";
        database = getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);
        ArrayList<Stock> stocks = new ArrayList<>();
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                stocks.add(new Stock(cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_STOCK_SRL)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_WHNo)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_BATCHNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CONDITIONCODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_SMALLEST_ISSUABLE_PACKQTY)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_PACKTYPE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_LOCATION_MARKING)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_QTYGROUND)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_GOODS_RECEIPTNO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_DATEMODIFIED))));
                cursor.moveToNext();
            }
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return stocks;
    }

    public ArrayList<String> getSearchStocksMcode(String materialCode,String siteid) {
        if (materialCode.length() != 0) {
            materialCode = "%" + materialCode + "%";
        }
        String selectQuery = " select DISTINCT " + Tables.COLUMN_MATERIAL_CODE + " from " + Tables.TABLE_STOCK + " where " + Tables.COLUMN_MATERIAL_CODE + " like  '"
                + materialCode + "' AND "+Tables.COLUMN_WHNo +" = "+ siteid+" ORDER BY " + Tables.COLUMN_MATERIAL_CODE + " DESC";
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        ArrayList<String> stocks = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                stocks.add(cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CODE)));
                cursor.moveToNext();
            }
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return stocks;
    }


    public void addStock(Stock stock) {
        database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Tables.COLUMN_MATERIAL_CODE, stock.getMaterialCode());
        values.put(Tables.COLUMN_STOCK_SRL, stock.getStockSrl());
        values.put(Tables.COLUMN_WHNo, stock.getWHNo());
        values.put(Tables.COLUMN_BATCHNO, stock.getBatchNo());
        values.put(Tables.COLUMN_MATERIAL_CONDITIONCODE, stock.getMaterialConditionCode());
        values.put(Tables.COLUMN_GOODS_RECEIPTNO, stock.getGoodsReceiptNo());
        values.put(Tables.COLUMN_LOCATION_MARKING, stock.getLocationMarking());
        values.put(Tables.COLUMN_PACKTYPE, stock.getPackType());
        values.put(Tables.COLUMN_QTYGROUND, stock.getQtyGround());
        values.put(Tables.COLUMN_SMALLEST_ISSUABLE_PACKQTY, stock.getSmallestIssuablePackQty());
        values.put(Tables.COLUMN_DATEMODIFIED, stock.getDateModified());


        database.insert(Tables.TABLE_STOCK, null, values);

        if (database != null)
            database.close();

    }

    public void addAllStock(List<Stock> stocks) {
        database = getWritableDatabase();

        // you can use INSERT only
        String sql = "INSERT OR REPLACE INTO " + Tables.TABLE_STOCK + " ( " + Tables.COLUMN_MATERIAL_CODE + ","
                + Tables.COLUMN_STOCK_SRL + "," + Tables.COLUMN_WHNo + "," + Tables.COLUMN_BATCHNO + "," + Tables.COLUMN_MATERIAL_CONDITIONCODE + ","
                + Tables.COLUMN_SMALLEST_ISSUABLE_PACKQTY + "," + Tables.COLUMN_PACKTYPE + "," + Tables.COLUMN_LOCATION_MARKING + ","
                + Tables.COLUMN_QTYGROUND + "," + Tables.COLUMN_GOODS_RECEIPTNO + "," + Tables.COLUMN_DATEMODIFIED + " ) VALUES ( ?, ? ,?,?,?,?,?,?,?,?,?)";

        SQLiteDatabase db = this.getWritableDatabase();

        /*
         * According to the docs http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html
         * Writers should use beginTransactionNonExclusive() or beginTransactionWithListenerNonExclusive(SQLiteTransactionListener)
         * to start a transaction. Non-exclusive mode allows database file to be in readable by other threads executing queries.
         */
        db.beginTransactionNonExclusive();
        // db.beginTransaction();

        SQLiteStatement stmt = db.compileStatement(sql);

        for (Stock stock : stocks) {

            stmt.bindString(1, stock.getMaterialCode());
            stmt.bindString(2, stock.getStockSrl());
            stmt.bindString(3, stock.getWHNo());
            stmt.bindString(4, stock.getBatchNo());
            stmt.bindString(5, stock.getMaterialConditionCode());


            stmt.bindString(6, "");

            stmt.bindString(7, stock.getLocationMarking());
            stmt.bindString(8, stock.getPackType());
            stmt.bindString(9, stock.getQtyGround());
            stmt.bindString(10, stock.getSmallestIssuablePackQty());
            stmt.bindString(11, stock.getDateModified());

            stmt.execute();
            stmt.clearBindings();

        }

        db.setTransactionSuccessful();
        db.endTransaction();


        if (database != null)
            database.close();

    }

    public void updateStock(Stock stock) {
        database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Tables.COLUMN_MATERIAL_CODE, stock.getMaterialCode());
        values.put(Tables.COLUMN_STOCK_SRL, stock.getStockSrl());
        values.put(Tables.COLUMN_WHNo, stock.getWHNo());
        values.put(Tables.COLUMN_BATCHNO, stock.getBatchNo());
        values.put(Tables.COLUMN_MATERIAL_CONDITIONCODE, stock.getMaterialConditionCode());
        values.put(Tables.COLUMN_GOODS_RECEIPTNO, stock.getGoodsReceiptNo());
        values.put(Tables.COLUMN_LOCATION_MARKING, stock.getLocationMarking());
        values.put(Tables.COLUMN_PACKTYPE, stock.getPackType());
        values.put(Tables.COLUMN_QTYGROUND, stock.getQtyGround());
        values.put(Tables.COLUMN_SMALLEST_ISSUABLE_PACKQTY, stock.getSmallestIssuablePackQty());
        values.put(Tables.COLUMN_DATEMODIFIED, stock.getDateModified());


        database.update(Tables.TABLE_STOCK, values,
                Tables.COLUMN_MATERIAL_CODE + "=? AND " + Tables.COLUMN_STOCK_SRL + " =?", new String[]{stock.getMaterialCode(), stock.getStockSrl()});
        if (database != null)
            database.close();

    }

    public void removeStock(Stock stock) {
        database = getWritableDatabase();

        database.delete(Tables.TABLE_STOCK, Tables.COLUMN_MATERIAL_CODE
                + "=? ", new String[]{stock.getMaterialCode()});

        if (database != null)
            database.close();

    }


    //for stockrecv

    public void emptyStockrcv() {
        database = getWritableDatabase();
        database.delete(Tables.TABLE_STOCK_RCV, null, null);
        database.close();
    }

    public StockRcv getStockToRcv(String reqno) {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from "
                + Tables.TABLE_STOCK_RCV + " where " + Tables.COLUMN_REQUISTION_NO + " = " + reqno, null);
        StockRcv stockRcv = null;
        if (cursor != null && cursor.getCount() > 0) {
            stockRcv = new StockRcv(cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_REQUISTION_NO)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_WHNo)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CODE)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_DESC)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_QTY)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_QTYCARRIED)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_QTY_RCV)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_RECEIPT_MISMATCHQTY)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_QTY_PENDING_TO_RCV)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_DENO)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_SRL)), cursor.getString(cursor
                    .getColumnIndexOrThrow(Tables.COLUMN_STOCK_DELIVERY_ID)));
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return stockRcv;
    }

    public boolean dbStockRcvHasData(String searchKey) {
        String query = "Select * from " + Tables.TABLE_STOCK_RCV + " where " + Tables.COLUMN_REQUISTION_NO + " = ?";
        return getReadableDatabase().rawQuery(query, new String[]{searchKey}).moveToFirst();
    }

    public ArrayList<StockRcv> getAllStockRcvs() {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from "
                + Tables.TABLE_STOCK_RCV, null);
        ArrayList<StockRcv> stockRcvs = new ArrayList<StockRcv>();
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                stockRcvs.add(new StockRcv(cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_REQUISTION_NO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_WHNo)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_DESC)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_QTY)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_QTYCARRIED)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_QTY_RCV)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_RECEIPT_MISMATCHQTY)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_QTY_PENDING_TO_RCV)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_DENO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_SRL)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_STOCK_DELIVERY_ID))));
                cursor.moveToNext();
            }
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return stockRcvs;
    }

    public ArrayList<StockRcv> getAllStockRcvsBySite(String whNo) {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from "
                + Tables.TABLE_STOCK_RCV + " where " + Tables.COLUMN_WHNo + " = ?", new String[]{whNo});
        ArrayList<StockRcv> stockRcvs = new ArrayList<StockRcv>();
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                stockRcvs.add(new StockRcv(cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_REQUISTION_NO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_WHNo)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_CODE)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_DESC)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_QTY)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_QTYCARRIED)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_QTY_RCV)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_RECEIPT_MISMATCHQTY)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_QTY_PENDING_TO_RCV)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_MATERIAL_DENO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_SRL)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_STOCK_DELIVERY_ID))));
                cursor.moveToNext();
            }
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return stockRcvs;
    }

    public void addStockRcvs(StockRcv stockRcv) {
        database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Tables.COLUMN_REQUISTION_NO, stockRcv.getRequisitionNo());
        values.put(Tables.COLUMN_WHNo, stockRcv.getWHNo());

        values.put(Tables.COLUMN_MATERIAL_CODE, stockRcv.getMaterialCode());
        values.put(Tables.COLUMN_MATERIAL_DESC, stockRcv.getMaterialDesc());
        values.put(Tables.COLUMN_QTY, stockRcv.getQty());
        values.put(Tables.COLUMN_QTY_RCV, stockRcv.getQtyReceived());
        values.put(Tables.COLUMN_RECEIPT_MISMATCHQTY, stockRcv.getReceiptMismatchQty());
        values.put(Tables.COLUMN_QTY_PENDING_TO_RCV, stockRcv.getQtyPendingToReceive());
        values.put(Tables.COLUMN_MATERIAL_DENO, stockRcv.getMaterialDeno());
        values.put(Tables.COLUMN_SRL, stockRcv.getSrl());
        values.put(Tables.COLUMN_STOCK_DELIVERY_ID, stockRcv.getStockDeliveryID());


        database.insert(Tables.TABLE_STOCK_RCV, null, values);

        if (database != null)
            database.close();

    }

    public void addAllStockRcv(List<StockRcv> stocksRcvs) {
        database = getWritableDatabase();

        // you can use INSERT only
        String sql = "INSERT OR REPLACE INTO " + Tables.TABLE_STOCK_RCV + " ( " + Tables.COLUMN_REQUISTION_NO + ","
                + Tables.COLUMN_WHNo + "," + Tables.COLUMN_MATERIAL_CODE + "," + Tables.COLUMN_MATERIAL_DESC + "," + Tables.COLUMN_QTY + ","
                + Tables.COLUMN_QTYCARRIED + "," + Tables.COLUMN_QTY_RCV + "," + Tables.COLUMN_RECEIPT_MISMATCHQTY + ","
                + Tables.COLUMN_QTY_PENDING_TO_RCV + "," + Tables.COLUMN_MATERIAL_DENO + "," + Tables.COLUMN_SRL + ","
                + Tables.COLUMN_STOCK_DELIVERY_ID + " ) VALUES ( ?, ? ,?,?,?,?,?,?,?,?,?,?)";

        SQLiteDatabase db = this.getWritableDatabase();

        /*
         * According to the docs http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html
         * Writers should use beginTransactionNonExclusive() or beginTransactionWithListenerNonExclusive(SQLiteTransactionListener)
         * to start a transaction. Non-exclusive mode allows database file to be in readable by other threads executing queries.
         */
        db.beginTransactionNonExclusive();
        // db.beginTransaction();

        SQLiteStatement stmt = db.compileStatement(sql);

        for (StockRcv stock : stocksRcvs) {

            stmt.bindString(1, stock.getRequisitionNo());
            stmt.bindString(2, stock.getWHNo());
            stmt.bindString(3, stock.getMaterialCode());
            if (stock.getMaterialDesc() != null)
                stmt.bindString(4, stock.getMaterialDesc());
            else
                stmt.bindString(4, "");
            stmt.bindString(5, stock.getQty());
            stmt.bindString(6, stock.getQtyCarried());
            stmt.bindString(7, stock.getQtyReceived());
            stmt.bindString(8, stock.getReceiptMismatchQty());
            stmt.bindString(9, stock.getQtyPendingToReceive());
            stmt.bindString(10, stock.getMaterialDeno());
            stmt.bindString(11, stock.getSrl());
            stmt.bindString(12, stock.getStockDeliveryID());
            stmt.execute();
            stmt.clearBindings();
        }

        db.setTransactionSuccessful();
        db.endTransaction();


        if (database != null)
            database.close();

    }

    public void updateStockRcvs(StockRcv stockRcv) {
        database = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Tables.COLUMN_REQUISTION_NO, stockRcv.getRequisitionNo());
        values.put(Tables.COLUMN_WHNo, stockRcv.getWHNo());

        values.put(Tables.COLUMN_MATERIAL_CODE, stockRcv.getMaterialCode());
        values.put(Tables.COLUMN_MATERIAL_DESC, stockRcv.getMaterialDesc());
        values.put(Tables.COLUMN_QTY, stockRcv.getQty());
        values.put(Tables.COLUMN_QTY_RCV, stockRcv.getQtyReceived());
        values.put(Tables.COLUMN_RECEIPT_MISMATCHQTY, stockRcv.getReceiptMismatchQty());
        values.put(Tables.COLUMN_QTY_PENDING_TO_RCV, stockRcv.getQtyPendingToReceive());
        values.put(Tables.COLUMN_MATERIAL_DENO, stockRcv.getMaterialDeno());
        values.put(Tables.COLUMN_SRL, stockRcv.getSrl());
        values.put(Tables.COLUMN_STOCK_DELIVERY_ID, stockRcv.getStockDeliveryID());

        database.update(Tables.TABLE_STOCK_RCV, values,
                Tables.COLUMN_REQUISTION_NO + "=?", new String[]{stockRcv.getRequisitionNo()});
        if (database != null)
            database.close();

    }

    public void removeStockRcvs(StockRcv stockRcv) {
        database = getWritableDatabase();

        database.delete(Tables.TABLE_STOCK_RCV, Tables.COLUMN_REQUISTION_NO
                + "=? ", new String[]{stockRcv.getRequisitionNo()});

        if (database != null)
            database.close();

    }

    public List<NotificationDs> getAllNotification() {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from "
                + Tables.TABLE_NOTIFICATION, null);
        List<NotificationDs> notificationDses = new ArrayList<NotificationDs>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                notificationDses.add(new NotificationDs(cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_TRANSACTION_NO)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_NOTI_SUB)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_NOTI_REMARKS)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_NOTI_DATEMODIFIED)), cursor.getString(cursor
                        .getColumnIndexOrThrow(Tables.COLUMN_NOTI_IMAGEURL))));
                cursor.moveToNext();
            }
        }
        if (cursor != null)
            cursor.close();
        if (database != null)
            database.close();
        return notificationDses;
    }

    public void addAllNotification(List<NotificationDs> notificationDses) {
        database = getWritableDatabase();

        // you can use INSERT only
        String sql = "INSERT OR REPLACE INTO " + Tables.TABLE_NOTIFICATION + " ( " + Tables.COLUMN_TRANSACTION_NO + ","
                + Tables.COLUMN_NOTI_SUB + "," + Tables.COLUMN_NOTI_REMARKS + "," + Tables.COLUMN_NOTI_DATEMODIFIED + "," + Tables.COLUMN_NOTI_IMAGEURL
                + " ) VALUES ( ?, ? ,?,?,?)";

        SQLiteDatabase db = this.getWritableDatabase();

        /*
         * According to the docs http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html
         * Writers should use beginTransactionNonExclusive() or beginTransactionWithListenerNonExclusive(SQLiteTransactionListener)
         * to start a transaction. Non-exclusive mode allows database file to be in readable by other threads executing queries.
         */
        db.beginTransactionNonExclusive();
        // db.beginTransaction();

        SQLiteStatement stmt = db.compileStatement(sql);

        for (NotificationDs notificationDs : notificationDses) {
            stmt.bindString(1, notificationDs.getTransactionNo());
            stmt.bindString(2, notificationDs.getNotificationSubject());
            stmt.bindString(3, notificationDs.getNotificationRemarks());
            stmt.bindString(4, notificationDs.getDatetimeEntered());
            if(null!=notificationDs.getImagePath())
            stmt.bindString(5, notificationDs.getImagePath());
            else
                stmt.bindString(5, "");
            stmt.execute();
            stmt.clearBindings();
        }

        db.setTransactionSuccessful();
        db.endTransaction();


        if (database != null)
            database.close();

    }

}
