package in.periculum.ims.ds;

/**
 * Created by ROYAL on 10/30/2015.
 */
public class Employee {
private String EmployeeId;
    private String Name;
    private String Desig;
    private String MobileNo;
    private String AuthenticationCode;
    private String EmpImagePath;
    private String EMailId;

    @Override
    public String toString() {
        return "Employee{" +
                "EmployeeId='" + EmployeeId + '\'' +
                ", Name='" + Name + '\'' +
                ", Desig='" + Desig + '\'' +
                ", MobileNo='" + MobileNo + '\'' +
                ", AuthenticationCode='" + AuthenticationCode + '\'' +
                ", EmpImagePath='" + EmpImagePath + '\'' +
                ", EMailId='" + EMailId + '\'' +
                '}';
    }

    public String getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        EmployeeId = employeeId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDesig() {
        return Desig;
    }

    public void setDesig(String desig) {
        Desig = desig;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getAuthenticationCode() {
        return AuthenticationCode;
    }

    public void setAuthenticationCode(String authenticationCode) {
        AuthenticationCode = authenticationCode;
    }

    public String getEmpImagePath() {
        return EmpImagePath;
    }

    public void setEmpImagePath(String empImagePath) {
        EmpImagePath = empImagePath;
    }

    public String getEMailId() {
        return EMailId;
    }

    public void setEMailId(String EMailId) {
        this.EMailId = EMailId;
    }
}
