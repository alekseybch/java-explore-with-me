package ru.practicum.ewm.global.utility;

import org.springframework.data.domain.PageRequest;

public class PageableConverter {

    private PageableConverter() {
    }

    public static PageRequest getPageable(Integer from, Integer size) {
        return PageRequest.of((from / size), size);
    }

}
