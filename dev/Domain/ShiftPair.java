package Domain;

import java.time.LocalDate;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShiftPair shiftPair = (ShiftPair) o;
        return date.equals(shiftPair.date) && type == shiftPair.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, type);
    }
    @Override
    public String toString() {
        return "ShiftPair{" +
                "date=" + date +
                ", type=" + type +
                '}';
    }
}
