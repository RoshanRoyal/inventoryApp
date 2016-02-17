package in.periculum.ims.ds;

import java.io.Serializable;

/**
 * Created by ROYAL on 10/31/2015.
 */
public class StockRcv implements Serializable {

    private static final long serialVersionUID = 1L;
    private String RequisitionNo;
    private String WHNo;
    private String MaterialCode;
    private String MaterialDesc;
    private String Qty;
    private String QtyCarried;
    private String QtyReceived;
    private String ReceiptMismatchQty;
    private String QtyPendingToReceive;
    private String MaterialDeno;
    private String Srl;
    private String StockDeliveryID;

    public StockRcv(String requisitionNo, String WHNo, String materialCode, String materialDesc, String qty, String qtyCarried, String qtyReceived, String receiptMismatchQty, String qtyPendingToReceive, String materialDeno, String srl, String StockDeliveryID) {
        this.RequisitionNo = requisitionNo;
        this.WHNo = WHNo;
        this.MaterialCode = materialCode;
        this.MaterialDesc = materialDesc;
        this.Qty = qty;
        this.QtyCarried = qtyCarried;
        this.QtyReceived = qtyReceived;
        this.ReceiptMismatchQty = receiptMismatchQty;
        this.QtyPendingToReceive = qtyPendingToReceive;
        this.MaterialDeno = materialDeno;
        this.Srl = srl;
        this.StockDeliveryID = StockDeliveryID;
    }

    @Override
    public String toString() {
        return "StockRcv{" +
                "RequisitionNo='" + RequisitionNo + '\'' +
                ", WHNo='" + WHNo + '\'' +
                ", MaterialCode='" + MaterialCode + '\'' +
                ", MaterialDesc='" + MaterialDesc + '\'' +
                ", Qty='" + Qty + '\'' +
                ", QtyCarried='" + QtyCarried + '\'' +
                ", QtyReceived='" + QtyReceived + '\'' +
                ", ReceiptMismatchQty='" + ReceiptMismatchQty + '\'' +
                ", QtyPendingToReceive='" + QtyPendingToReceive + '\'' +
                ", MaterialDeno='" + MaterialDeno + '\'' +
                ", Srl='" + Srl + '\'' +
                ", StockDeliveryID='" + StockDeliveryID + '\'' +
                '}';
    }

    public String getRequisitionNo() {
        return RequisitionNo;
    }

    public void setRequisitionNo(String requisitionNo) {
        RequisitionNo = requisitionNo;
    }

    public String getWHNo() {
        return WHNo;
    }

    public void setWHNo(String WHNo) {
        this.WHNo = WHNo;
    }

    public String getMaterialCode() {
        return MaterialCode;
    }

    public void setMaterialCode(String materialCode) {
        MaterialCode = materialCode;
    }

    public String getMaterialDesc() {
        return MaterialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        MaterialDesc = materialDesc;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getQtyCarried() {
        return QtyCarried;
    }

    public void setQtyCarried(String qtyCarried) {
        QtyCarried = qtyCarried;
    }

    public String getQtyReceived() {
        return QtyReceived;
    }

    public void setQtyReceived(String qtyReceived) {
        QtyReceived = qtyReceived;
    }

    public String getReceiptMismatchQty() {
        return ReceiptMismatchQty;
    }

    public void setReceiptMismatchQty(String receiptMismatchQty) {
        ReceiptMismatchQty = receiptMismatchQty;
    }

    public String getQtyPendingToReceive() {
        return QtyPendingToReceive;
    }

    public void setQtyPendingToReceive(String qtyPendingToReceive) {
        QtyPendingToReceive = qtyPendingToReceive;
    }

    public String getMaterialDeno() {
        return MaterialDeno;
    }

    public void setMaterialDeno(String materialDeno) {
        MaterialDeno = materialDeno;
    }

    public String getSrl() {
        return Srl;
    }

    public void setSrl(String srl) {
        Srl = srl;
    }

    public String getStockDeliveryID() {
        return StockDeliveryID;
    }

    public void setStockDeliveryID(String StockDeliveryID) {
        this.StockDeliveryID = StockDeliveryID;
    }
}
