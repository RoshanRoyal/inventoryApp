package in.periculum.ims.ds;

/**
 * Created by ROYAL on 12/23/2015.
 */
public class NotificationDs {

    private String TransactionNo;
    private String EmployeeId;
    private String NotificationSubject;
    private String NotificationRemarks;
    private String DatetimeEntered;
    private String ImagePath;

    public NotificationDs(String transactionNo, String notificationSubject, String notificationRemarks, String datetimeEntered, String imagePath) {
        TransactionNo = transactionNo;

        NotificationSubject = notificationSubject;
        NotificationRemarks = notificationRemarks;
        DatetimeEntered = datetimeEntered;
        ImagePath = imagePath;
    }

    @Override
    public String toString() {
        return "NotificationDs{" +
                "TransactionNo='" + TransactionNo + '\'' +
                ", EmployeeId='" + EmployeeId + '\'' +
                ", NotificationSubject='" + NotificationSubject + '\'' +
                ", NotificationRemarks='" + NotificationRemarks + '\'' +
                ", DatetimeEntered='" + DatetimeEntered + '\'' +
                ", ImagePath='" + ImagePath + '\'' +
                '}';
    }

    public String getTransactionNo() {
        return TransactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        TransactionNo = transactionNo;
    }

    public String getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        EmployeeId = employeeId;
    }

    public String getNotificationSubject() {
        return NotificationSubject;
    }

    public void setNotificationSubject(String notificationSubject) {
        NotificationSubject = notificationSubject;
    }

    public String getNotificationRemarks() {
        return NotificationRemarks;
    }

    public void setNotificationRemarks(String notificationRemarks) {
        NotificationRemarks = notificationRemarks;
    }

    public String getDatetimeEntered() {
        return DatetimeEntered;
    }

    public void setDatetimeEntered(String datetimeEntered) {
        DatetimeEntered = datetimeEntered;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}
