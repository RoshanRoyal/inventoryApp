package in.periculum.ims.ds;

/**
 * Created by ROYAL on 11/3/2015.
 */
public class Sites {

  private  String WHNo;
    private  String WHDesc;

    @Override
    public String toString() {
        return "Sites{" +
                "WHNo='" + WHNo + '\'' +
                ", WHDesc='" + WHDesc + '\'' +
                '}';
    }

    public Sites() {
    }

    public Sites(String WHNo, String WHDesc) {
        this.WHNo = WHNo;
        this.WHDesc = WHDesc;
    }

    public String getWHNo() {
        return WHNo;
    }

    public void setWHNo(String WHNo) {
        this.WHNo = WHNo;
    }

    public String getWHDesc() {
        return WHDesc;
    }

    public void setWHDesc(String WHDesc) {
        this.WHDesc = WHDesc;
    }
}
