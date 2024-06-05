package Domain;

import java.time.LocalDate;

public class ShiftPair {
    private final LocalDate date;
    private final ShiftType type;


    public ShiftPair(LocalDate date, ShiftType type) {
        this.date = date;
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public ShiftType getType() {
        return type;
    }

    public boolean equals(LocalDate date, ShiftType type) {
        return (this.date.compareTo(date) == 0) && (type == this.type);
    }
}