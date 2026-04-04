package tfg.psygcv.dto.pet.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import tfg.psygcv.entity.pet.Breed;
import tfg.psygcv.entity.pet.Sex;
import tfg.psygcv.entity.pet.Species;

@Getter
@Setter
@NoArgsConstructor
public class UpdatePetRequest {

  @NotBlank(message = "El nombre de la mascota es obligatorio")
  @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
  private String name;

  @NotNull(message = "El sexo es obligatorio")
  private Sex sex;

  @NotNull(message = "La raza es obligatoria")
  private Breed breed;

  @NotNull(message = "La especie es obligatoria")
  private Species species;

  @NotNull(message = "La fecha de nacimiento es obligatoria")
  @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate birthDate;

  @NotNull(message = "El peso es obligatorio")
  @PositiveOrZero(message = "El peso debe ser mayor o igual a cero")
  private Float weight;
}
