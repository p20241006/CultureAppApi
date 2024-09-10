package cultureapp.com.pe.event;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EventRequest(
        Integer id,
        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String title,
        @NotNull(message = "101")
        @NotEmpty(message = "101")
        String description,
        @NotNull(message = "102")
        @NotEmpty(message = "102")
        String urlEvent,
        @NotNull(message = "103")
        @NotEmpty(message = "103")
        String imgEvent,
        boolean shareable
) {
}
