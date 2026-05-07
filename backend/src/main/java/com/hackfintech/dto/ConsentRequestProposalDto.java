package com.hackfintech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ConsentRequestProposalDto {

    @Valid
    @NotNull(message = "El header es obligatorio")
    @JsonProperty("request_header")
    private RequestHeaderDto requestHeader;

    @Valid
    @NotNull(message = "La declaración legal es obligatoria")
    @JsonProperty("legal_declaration")
    private LegalDeclarationDto legalDeclaration;

    @Valid
    @NotEmpty(message = "Debe solicitar al menos una estructura de datos")
    @JsonProperty("requested_data_structure")
    private List<RequestedDataStructureDto> requestedDataStructure;

    // Campos añadidos internamente para el ciudadano destino
    private String rutCiudadano;

    @Data
    public static class RequestHeaderDto {
        @NotEmpty
        @JsonProperty("organization_id")
        private String organizationId;

        @NotEmpty
        @JsonProperty("organization_name")
        private String organizationName;

        @JsonProperty("request_timestamp")
        private String requestTimestamp;

        @JsonProperty("request_id")
        private String requestId;
        
        @JsonProperty("request_type")
        private String requestType = "NORMAL"; // NORMAL o PORTABILITY

        @JsonProperty("source_organization_id")
        private java.util.UUID sourceOrganizationId;
    }

    @Data
    public static class LegalDeclarationDto {
        @NotEmpty
        @JsonProperty("base_legal")
        private String baseLegal;

        @NotEmpty
        @JsonProperty("finalidad_tratamiento")
        private String finalidadTratamiento;

        @NotEmpty
        @JsonProperty("clausula_privacidad_url")
        private String clausulaPrivacidadUrl;

        @NotNull
        @JsonProperty("tiempo_retencion_dias")
        private Integer tiempoRetencionDias;

        @NotNull
        @JsonProperty("transferencia_internacional")
        private Boolean transferenciaInternacional;

        @NotEmpty
        @JsonProperty("texto_legal_completo")
        private String textoLegalCompleto;
    }

    @Data
    public static class RequestedDataStructureDto {
        @NotEmpty
        @JsonProperty("categoria")
        private String categoria;

        @NotEmpty
        @JsonProperty("campos")
        private List<String> campos;

        @NotEmpty
        @JsonProperty("justificacion")
        private String justificacion;
    }
}
