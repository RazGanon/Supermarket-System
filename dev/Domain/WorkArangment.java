package Domain;

public class WorkArangment {
    private String arrangementDetails;

    // Constructor, getters, and setters
    public WorkArangment(String arrangementDetails) {
        this.arrangementDetails = arrangementDetails;
    }

    @Override
    public String toString() {
        return "WorkArrangement{" +
                "arrangementDetails='" + arrangementDetails + '\'' +
                '}';
    }
}
