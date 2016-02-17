package in.periculum.ims.ds;

import java.util.Date;

/**
 * Created by roshan.kumar1 on 12/3/2015.
 */
public class StockIssue {
    private String ConsumerCode;
    private String MaterialCode;
    private String DirectiveType;
    private String DirectiveRef;
    private Date DirectiveDate;
    private String RaisedBy;
    private String RemarksChecking;
    private String EndorsementRemarks;
    private String StockSrl;
    private String Qty;

    public StockIssue(String consumerCode, String materialCode, String directiveType, String directiveRef, Date directiveDate, String raisedBy, String remarksChecking, String endorsementRemarks, String stockSrl, String qty) {
        ConsumerCode = consumerCode;
        MaterialCode = materialCode;
        DirectiveType = directiveType;
        DirectiveRef = directiveRef;
        DirectiveDate = directiveDate;
        RaisedBy = raisedBy;
        RemarksChecking = remarksChecking;
        EndorsementRemarks = endorsementRemarks;
        StockSrl = stockSrl;
        Qty = qty;
    }

    @Override
    public String toString() {
        return "StockIssue{" +
                "ConsumerCode='" + ConsumerCode + '\'' +
                ", MaterialCode='" + MaterialCode + '\'' +
                ", DirectiveType='" + DirectiveType + '\'' +
                ", DirectiveRef='" + DirectiveRef + '\'' +
                ", DirectiveDate=" + DirectiveDate +
                ", RaisedBy='" + RaisedBy + '\'' +
                ", RemarksChecking='" + RemarksChecking + '\'' +
                ", EndorsementRemarks='" + EndorsementRemarks + '\'' +
                ", StockSrl='" + StockSrl + '\'' +
                ", Qty='" + Qty + '\'' +
                '}';
    }

    public String getConsumerCode() {
        return ConsumerCode;
    }

    public void setConsumerCode(String consumerCode) {
        ConsumerCode = consumerCode;
    }

    public String getMaterialCode() {
        return MaterialCode;
    }

    public void setMaterialCode(String materialCode) {
        MaterialCode = materialCode;
    }

    public String getDirectiveType() {
        return DirectiveType;
    }

    public void setDirectiveType(String directiveType) {
        DirectiveType = directiveType;
    }

    public String getDirectiveRef() {
        return DirectiveRef;
    }

    public void setDirectiveRef(String directiveRef) {
        DirectiveRef = directiveRef;
    }

    public Date getDirectiveDate() {
        return DirectiveDate;
    }

    public void setDirectiveDate(Date directiveDate) {
        DirectiveDate = directiveDate;
    }

    public String getRaisedBy() {
        return RaisedBy;
    }

    public void setRaisedBy(String raisedBy) {
        RaisedBy = raisedBy;
    }

    public String getRemarksChecking() {
        return RemarksChecking;
    }

    public void setRemarksChecking(String remarksChecking) {
        RemarksChecking = remarksChecking;
    }

    public String getEndorsementRemarks() {
        return EndorsementRemarks;
    }

    public void setEndorsementRemarks(String endorsementRemarks) {
        EndorsementRemarks = endorsementRemarks;
    }

    public String getStockSrl() {
        return StockSrl;
    }

    public void setStockSrl(String stockSrl) {
        StockSrl = stockSrl;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }
}
