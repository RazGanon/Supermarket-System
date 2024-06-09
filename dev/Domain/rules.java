package Domain;

public interface rules {
    int getMorningManagerCount();

    int getMorningEmployeeCount();

    int getEveningManagerCount();

    int getEveningEmployeeCount();

    void setMorningManagerCount(int morningManagerCount);

    void setMorningEmployeeCount(int morningEmployeeCount);

    //the admin decide how much managers and employees will be in each shift
    int getManagerCount();
    int getEmployeeCount();
}