package in.periculum.ims.ds;

/**
 * Created by ROYAL on 12/1/2015.
 */
public class Material {
    private String MaterialCode;
    private String MaterialDesc;
    private String MaterialDeno;
    private String PreferredLocation;
    private String Bay;
    private String Bin;
    private String server_uploaded;
    private String DateModified;

    public Material(String materialCode, String materialDesc, String materialDeno, String preferredLocation, String bay, String bin, String server_uploaded, String dateModified) {
        this.MaterialCode = materialCode;
        this.MaterialDesc = materialDesc;
        this.MaterialDeno = materialDeno;
        this.PreferredLocation = preferredLocation;
        this.Bay = bay;
        this.Bin = bin;
        this.server_uploaded = server_uploaded;
        this.DateModified = dateModified;
    }

    public String getMaterialCode() {
        return MaterialCode;
    }

    public void setMaterialCode(String materialCode) {
        MaterialCode = materialCode;
    }

    @Override
    public String toString() {
        return "Material{" +
                "MaterialCode='" + MaterialCode + '\'' +
                ", MaterialDesc='" + MaterialDesc + '\'' +
                ", MaterialDeno='" + MaterialDeno + '\'' +
                ", PreferredLocation='" + PreferredLocation + '\'' +
                ", Bay='" + Bay + '\'' +
                ", Bin='" + Bin + '\'' +
                ", server_uploaded='" + server_uploaded + '\'' +
                ", DateModified='" + DateModified + '\'' +
                '}';
    }

    public String getMaterialDesc() {
        return MaterialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        MaterialDesc = materialDesc;
    }

    public String getMaterialDeno() {
        return MaterialDeno;
    }

    public void setMaterialDeno(String materialDeno) {
        MaterialDeno = materialDeno;
    }

    public String getPreferredLocation() {
        return PreferredLocation;
    }

    public void setPreferredLocation(String preferredLocation) {
        PreferredLocation = preferredLocation;
    }

    public String getBay() {
        return Bay;
    }

    public void setBay(String bay) {
        Bay = bay;
    }

    public String getBin() {
        return Bin;
    }

    public void setBin(String bin) {
        Bin = bin;
    }

    public String getServer_uploaded() {
        return server_uploaded;
    }

    public void setServer_uploaded(String server_uploaded) {
        this.server_uploaded = server_uploaded;
    }

    public String getDateModified() {
        return DateModified;
    }

    public void setDateModified(String dateModified) {
        DateModified = dateModified;
    }
}
