package Domain;

public class Role {
    public String Role_Name;
    //constructor
    public  Role (String Role_Name){
        this.Role_Name=Role_Name;
    }

    //Getter Role Name
    public String get_Role_Name ()
    {
        return Role_Name ;
    }

    //Setter Role Name
    public void set_Role_Name()
    {
        this.Role_Name=Role_Name;
    }

/*
    //Storekeeper class extending Role class
    public class Storekeeper extends Role {
        public Storekeeper(String roleName) {
            super(roleName);

        }
    }

 */


}
