package cultureapp.com.pe.preference;

import jakarta.validation.constraints.*;

public record PreferenceRequest(
        @Positive(message = "200")
        @Min(value = 0, message = "201")
        @Max(value = 5, message = "202")
        Double rating,
        @NotNull(message = "204")
        Integer eventId
) {
}
