package cultureapp.com.pe.preference;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreferenceResponse {
    private Double rating;
    private boolean ownFeedback;
}
