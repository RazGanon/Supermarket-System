package Domain;

public class Site {

    private String address;
    private String contactPhoneNumber;
    private String  contactName;

    public Site(String address, String contactName, String contactPhoneNumber) {
        this.address = address;
        this.contactName = contactName;
        this.contactPhoneNumber = contactPhoneNumber;
    }

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



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Site site = (Site) o;

        if (!address.equals(site.address)) return false;
        if (!contactName.equals(site.contactName)) return false;
        return contactPhoneNumber.equals(site.contactPhoneNumber);
    }

}
