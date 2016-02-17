package in.periculum.ims.database;

import android.database.sqlite.SQLiteDatabase;

public class Tables {

    //for material
    public static final String TABLE_MATERIAL = "material";
    public static final String TABLE_CMATERIAL = "cmaterial";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PREFERED_LOC = "prefered_loc";
    public static final String COLUMN_BAY_INFO = "bay_info";
    public static final String COLUMN_BIN_INFO = "bin_info";
    public static final String COLUMN_SERVER_UPLOADED = "server_uploaded";
    public static final String COLUMN_DATEMODIFIED = "DateModified";
    //for stock
    public static final String TABLE_STOCK = "stock";
    public static final String COLUMN_STOCK_SRL = "stockSrl";
    public static final String COLUMN_BATCHNO = "batchNo";
    public static final String COLUMN_MATERIAL_CONDITIONCODE = "materialConditionCode";
    public static final String COLUMN_SMALLEST_ISSUABLE_PACKQTY = "smallestIssuablePackQty";
    public static final String COLUMN_PACKTYPE = "packtype";
    public static final String COLUMN_LOCATION_MARKING = "locationMarking";
    public static final String COLUMN_QTYGROUND = "qtyGround";
    public static final String COLUMN_GOODS_RECEIPTNO = "goodsReceiptNo";

    //for stockrcv
    public static final String TABLE_STOCK_RCV = "stock_rcv";
    public static final String COLUMN_REQUISTION_NO = "requisitionNo";
    public static final String COLUMN_MATERIAL_CODE = "materialCode";
    public static final String COLUMN_MATERIAL_DESC = "materialDesc";
    public static final String COLUMN_QTY = "qty";
    public static final String COLUMN_QTYCARRIED = "qtyCarried";
    public static final String COLUMN_QTY_RCV = "qtyreceived";
    public static final String COLUMN_RECEIPT_MISMATCHQTY = "receiptMismatchQty";
    public static final String COLUMN_QTY_PENDING_TO_RCV = "qtyPendingToReceive";
    public static final String COLUMN_MATERIAL_DENO = "materialDeno";
    public static final String COLUMN_SRL = "srl";
    public static final String COLUMN_STOCK_DELIVERY_ID = "StockDeliveryID";

    //for sites
    public static final String TABLE_SITE = "site";
    public static final String COLUMN_WHNo = "whno";
    public static final String COLUMN_WHDesc = "WHDesc";

    //for notification
    public static final String TABLE_NOTIFICATION = "notification";
    public static final String COLUMN_TRANSACTION_NO = "transactionNo";
    public static final String COLUMN_NOTI_SUB = "NotificationSubject";
    public static final String COLUMN_NOTI_REMARKS = "NotificationRemarks";
    public static final String COLUMN_NOTI_DATEMODIFIED = "DatetimeEntered";
    public static final String COLUMN_NOTI_IMAGEURL = "ImagePath";


    public static void onCreate(SQLiteDatabase db) {

        //for material
        String CREATE_MATERIAL_TABLE = "CREATE TABLE " + TABLE_MATERIAL + "(" + COLUMN_MATERIAL_CODE
                + " TEXT PRIMARY KEY," + COLUMN_DESCRIPTION + " TEXT," + COLUMN_MATERIAL_DENO
                + " TEXT," + COLUMN_PREFERED_LOC + " TEXT,"
                + COLUMN_BAY_INFO + " TEXT," + COLUMN_BIN_INFO + " TEXT," + COLUMN_DATEMODIFIED + " TEXT" + ")";

        String CREATE_CMATERIAL_TABLE = "CREATE TABLE " + TABLE_CMATERIAL + "(" + COLUMN_MATERIAL_CODE
                + " TEXT PRIMARY KEY," + COLUMN_DESCRIPTION + " TEXT," + COLUMN_MATERIAL_DENO
                + " TEXT," + COLUMN_PREFERED_LOC + " TEXT,"
                + COLUMN_BAY_INFO + " TEXT," + COLUMN_BIN_INFO + " TEXT," + COLUMN_SERVER_UPLOADED + " INTEGER," + COLUMN_DATEMODIFIED + " TEXT" + ")";

        //for stock
        String CREATE_STOCK_TABLE = "CREATE TABLE " + TABLE_STOCK + "("
                + COLUMN_MATERIAL_CODE + " TEXT NOT NULL," + COLUMN_STOCK_SRL + " TEXT NOT NULL,"
                + " TEXT," + COLUMN_BATCHNO + " TEXT,"
                + COLUMN_MATERIAL_CONDITIONCODE + " TEXT," + COLUMN_SMALLEST_ISSUABLE_PACKQTY + " TEXT," + COLUMN_PACKTYPE + " TEXT,"
                + COLUMN_LOCATION_MARKING + " TEXT," + COLUMN_QTYGROUND + " TEXT," + COLUMN_GOODS_RECEIPTNO + " TEXT,"
                + COLUMN_DATEMODIFIED + " TEXT," + "PRIMARY KEY (" + COLUMN_MATERIAL_CODE + "," + COLUMN_STOCK_SRL + "))";

        // FOR STOCKRCV
        String CREATE_STOCKRCV_TABLE = "CREATE TABLE " + TABLE_STOCK_RCV + "("
                + COLUMN_REQUISTION_NO + " TEXT PRIMARY KEY," + COLUMN_WHNo + " TEXT," + COLUMN_MATERIAL_CODE
                + " TEXT," + COLUMN_MATERIAL_DESC + " TEXT,"
                + COLUMN_QTY + " TEXT," + COLUMN_QTYCARRIED + " TEXT, " + COLUMN_QTY_RCV + " TEXT,"
                + COLUMN_RECEIPT_MISMATCHQTY + " TEXT," + COLUMN_QTY_PENDING_TO_RCV + " TEXT,"
                + COLUMN_MATERIAL_DENO + " TEXT," + COLUMN_SRL + " TEXT," + COLUMN_STOCK_DELIVERY_ID + " TEXT" + ")";

        String CREATE_SITE_TABLE = "CREATE TABLE " + TABLE_SITE + "("
                + COLUMN_WHNo + " TEXT PRIMARY KEY," + COLUMN_WHDesc + " TEXT" + ")";

        String CREATE_NOTIFICATION_TABLE = "CREATE TABLE " + TABLE_NOTIFICATION + "("
                + COLUMN_TRANSACTION_NO + " TEXT PRIMARY KEY," + COLUMN_NOTI_SUB + " TEXT," + COLUMN_NOTI_REMARKS + " TEXT,"
                + COLUMN_NOTI_DATEMODIFIED + " TEXT," + COLUMN_NOTI_IMAGEURL +   " TEXT"+")";

        db.execSQL(CREATE_SITE_TABLE);
        db.execSQL(CREATE_MATERIAL_TABLE);
        db.execSQL(CREATE_CMATERIAL_TABLE);
        db.execSQL(CREATE_STOCK_TABLE);
        db.execSQL(CREATE_STOCKRCV_TABLE);
        db.execSQL(CREATE_NOTIFICATION_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion,
                                 int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATERIAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CMATERIAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK_RCV);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SITE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        onCreate(db);
    }

}
