package Domain;

public class Site {

    private String address;

    public Site(String address, String contactName, String contactPhoneNumber) {
        this.address = address;
        this.contactName = contactName;
        this.contactPhoneNumber = contactPhoneNumber;
    }

    private String contactName;

    public String getAddress() {
        return address;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    private String contactPhoneNumber;

}
