package com.hackfintech.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrgTokenRequest {
    @NotBlank(message = "El RUT del ciudadano es obligatorio")
    private String rutCiudadano;

    @NotBlank(message = "La finalidad del tratamiento es obligatoria")
    private String finalidad;
}
