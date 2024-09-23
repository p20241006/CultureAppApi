package cultureapp.com.pe.event;

import cultureapp.com.pe.category.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventResponse {

    private Integer id;
    private String title;
    private String description;
    private LocalDate start_date;
    private LocalDate end_date;
    private Float price;
    private String urlEvent;
    private String imgEvent;
    private String company;
    private String owner;
    private Integer categoria_id;
    private Integer region_id;
    private byte[] cover;
    private double rate;
    private boolean archived;
    private boolean shareable;

}
