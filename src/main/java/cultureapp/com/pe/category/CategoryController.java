package cultureapp.com.pe.category;


import cultureapp.com.pe.event.EventResponse;
import cultureapp.com.pe.event.EventService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("category")
@RequiredArgsConstructor
@Tag(name = "Category")
public class CategoryController {

    private final EventService eventService;

    @PostMapping("/by-categories")
    public List<EventResponse> getEventsByCategories(@RequestBody List<Integer> categoryIds) {
        System.out.println("categories: " + categoryIds);
        return eventService.getRandomEventsByCategoryIds(categoryIds);
    }
}
