package cultureapp.com.pe.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;

public record EventRequest(
        Integer id,
        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String title,
        @NotNull(message = "101")
        @NotEmpty(message = "101")
        String description,
        LocalDate start_date,
        LocalDate end_date,
        Float price,
        @NotNull(message = "103")
        @NotEmpty(message = "103")
        String imgEvent,
        boolean shareable,
        @NotNull @Min(1)
        Integer categoryId,
        @NotNull @Min(1)
        Integer regionId
) {
}
