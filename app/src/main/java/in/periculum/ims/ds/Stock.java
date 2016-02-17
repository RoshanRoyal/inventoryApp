package in.periculum.ims.ds;

/**
 * Created by ROYAL on 12/5/2015.
 */
public class Stock {

    private String MaterialCode;
    private String StockSrl;
    private String WHNo;
    private String BatchNo;
    private String MaterialConditionCode;
    private String SmallestIssuablePackQty;
    private String PackType;
    private String LocationMarking;
    private String QtyGround;
    private String GoodsReceiptNo;
    private String DateModified;

    public Stock(String materialCode, String stockSrl, String WHNo, String batchNo, String materialConditionCode, String smallestIssuablePackQty, String packType, String locationMarking, String qtyGround, String goodsReceiptNo, String DateModified) {
        this.MaterialCode = materialCode;
        this.StockSrl = stockSrl;
        this.WHNo = WHNo;
        this.BatchNo = batchNo;
        this.MaterialConditionCode = materialConditionCode;
        this.SmallestIssuablePackQty = smallestIssuablePackQty;
        this.PackType = packType;
        this.LocationMarking = locationMarking;
        this.QtyGround = qtyGround;
        this.GoodsReceiptNo = goodsReceiptNo;
        this.DateModified = DateModified;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "MaterialCode='" + MaterialCode + '\'' +
                ", StockSrl='" + StockSrl + '\'' +
                ", WHNo='" + WHNo + '\'' +
                ", BatchNo='" + BatchNo + '\'' +
                ", MaterialConditionCode='" + MaterialConditionCode + '\'' +
                ", SmallestIssuablePackQty='" + SmallestIssuablePackQty + '\'' +
                ", PackType='" + PackType + '\'' +
                ", LocationMarking='" + LocationMarking + '\'' +
                ", QtyGround='" + QtyGround + '\'' +
                ", GoodsReceiptNo='" + GoodsReceiptNo + '\'' +
                ", DateModified='" + DateModified + '\'' +
                '}';
    }

    public String getMaterialCode() {
        return MaterialCode;
    }

    public void setMaterialCode(String materialCode) {
        MaterialCode = materialCode;
    }

    public String getStockSrl() {
        return StockSrl;
    }

    public void setStockSrl(String stockSrl) {
        StockSrl = stockSrl;
    }

    public String getWHNo() {
        return WHNo;
    }

    public void setWHNo(String WHNo) {
        this.WHNo = WHNo;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    public String getMaterialConditionCode() {
        return MaterialConditionCode;
    }

    public void setMaterialConditionCode(String materialConditionCode) {
        MaterialConditionCode = materialConditionCode;
    }

    public String getSmallestIssuablePackQty() {
        return SmallestIssuablePackQty;
    }

    public void setSmallestIssuablePackQty(String smallestIssuablePackQty) {
        SmallestIssuablePackQty = smallestIssuablePackQty;
    }

    public String getPackType() {
        return PackType;
    }

    public void setPackType(String packType) {
        PackType = packType;
    }

    public String getLocationMarking() {
        return LocationMarking;
    }

    public void setLocationMarking(String locationMarking) {
        LocationMarking = locationMarking;
    }

    public String getQtyGround() {
        return QtyGround;
    }

    public void setQtyGround(String qtyGround) {
        QtyGround = qtyGround;
    }

    public String getGoodsReceiptNo() {
        return GoodsReceiptNo;
    }

    public void setGoodsReceiptNo(String goodsReceiptNo) {
        GoodsReceiptNo = goodsReceiptNo;
    }

    public String getDateModified() {
        return DateModified;
    }

    public void setDateModified(String dateModified) {
        this.DateModified = dateModified;
    }
}
