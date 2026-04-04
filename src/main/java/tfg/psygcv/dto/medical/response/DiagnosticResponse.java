package tfg.psygcv.dto.medical.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DiagnosticResponse {

  private Long id;
  private List<String> problems = new ArrayList<>();
}
