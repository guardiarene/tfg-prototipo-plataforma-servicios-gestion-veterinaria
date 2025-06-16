package tfg.psygcv.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "VETERINARY_CLINIC")
public class VeterinaryClinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @NotBlank
    @Column(name = "PHONE", nullable = false)
    private String phone;

    @NotBlank
    @Email
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MedicalService> services = new ArrayList<>();

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VETERINARIAN_ID", nullable = false)
    private User veterinarian;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECEPTIONIST_ID", nullable = false, unique = true)
    private User receptionist;

    @NotNull
    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = true;

    public VeterinaryClinic(String name, String address, String phone, String email, User veterinarian, User receptionist) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.setVeterinarian(veterinarian);
        this.setReceptionist(receptionist);
        this.active = true;
    }

    public void addService(MedicalService service) {
        if (service != null) {
            services.add(service);
            service.setClinic(this);
        }
    }

    public void removeService(MedicalService service) {
        if ((service != null) && (services.remove(service))) {
            service.setClinic(null);
        }
    }

    public void addAppointment(Appointment appointment) {
        if (appointment != null) {
            appointments.add(appointment);
            appointment.setVeterinaryClinic(this);
        }
    }

    public void removeAppointment(Appointment appointment) {
        if ((appointment != null) && (appointments.remove(appointment))) {
            appointment.setVeterinaryClinic(null);
        }
    }

    public void setVeterinarian(User veterinarian) {
        this.veterinarian = veterinarian;
        if ((veterinarian != null) && (!veterinarian.getClinicsOwned().contains(this))) {
            veterinarian.getClinicsOwned().add(this);
        }
    }

    public void setReceptionist(User receptionist) {
        this.receptionist = receptionist;
        if (receptionist != null) {
            receptionist.setClinicAsReceptionist(this);
        }
    }

}
