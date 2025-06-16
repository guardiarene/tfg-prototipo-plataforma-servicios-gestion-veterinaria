package tfg.psygcv.model;

import lombok.Getter;

@Getter
public enum ReproductiveStatus {
    INTACT("Not spayed/neutered"),
    NEUTERED("Spayed or neutered"),
    PREGNANT("Currently pregnant"),
    LACTATING("Currently lactating"),
    UNKNOWN("Status not determined");

    private final String description;

    ReproductiveStatus(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.name();
    }

}
