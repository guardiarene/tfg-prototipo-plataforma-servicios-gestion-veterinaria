package tfg.psygcv.model.appointment;

import lombok.Getter;

@Getter
public enum AppointmentStatus {
    PENDING("Pending confirmation"),
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed"),
    NO_SHOW("Customer did not show up");

    private final String description;

    AppointmentStatus(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.name();
    }

}
