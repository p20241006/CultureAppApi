package cultureapp.com.pe.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScoredEventResponse {

    private Integer id;
    private String title;
    private String description;  //authorName
    private String urlEvent;
    private double rate;
    private boolean returned;
    private boolean returnApproved;
}
