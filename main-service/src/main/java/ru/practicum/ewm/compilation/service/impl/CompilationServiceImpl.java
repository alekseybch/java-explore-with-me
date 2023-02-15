package ru.practicum.ewm.compilation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.bd.model.Compilation;
import ru.practicum.ewm.compilation.bd.repository.CompilationRepository;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.service.CompilationService;
import ru.practicum.ewm.event.bd.model.Event;
import ru.practicum.ewm.event.bd.repository.EventRepository;
import ru.practicum.ewm.global.exception.NotFoundException;
import ru.practicum.ewm.global.utility.ViewsCreator;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.ewm.global.utility.PageableConverter.getPageable;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;
    private final ViewsCreator viewsCreator;
    private final EntityManager entityManager;


    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        var compilation = compilationRepository.save(compilationMapper.toCompilation(compilationDto));
        log.info("Compilation with id = {} is saved {}.", compilation.getId(), compilation);
        var responseDto = compilationMapper.toCompilationDto(compilation);
        viewsCreator.addViewsToEvents(responseDto.getEvents());
        return responseDto;
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilationDto) {
        var compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id = %d not found", compId)));
        if (compilationDto.getEvents() != null && !compilationDto.getEvents().isEmpty()) {
            Set<Event> events = eventRepository.getEventByIds(compilationDto.getEvents());
            compilation.setEvents(events);
        }
        if (compilationDto.getPinned() != null) {
            compilation.setPinned(compilationDto.getPinned());
        }
        if (compilationDto.getTitle() != null) {
            compilation.setTitle(compilationDto.getTitle());
        }
        var updatedCompilation = compilationRepository.save(compilation);
        log.info("Compilation with id = {} is changed {}.", updatedCompilation.getId(), updatedCompilation);
        var responseDto = compilationMapper.toCompilationDto(compilation);
        viewsCreator.addViewsToEvents(responseDto.getEvents());
        return responseDto;
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
        log.info("Compilation with id = {} is deleted.", compId);
    }

    @Override
    public List<CompilationDto> findCompilations(Boolean pinned, Integer from, Integer size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Compilation> criteriaQuery = cb.createQuery(Compilation.class);
        Root<Compilation> compilationRoot = criteriaQuery.from(Compilation.class);
        criteriaQuery.select(compilationRoot);

        if (pinned != null) {
            Predicate predicate = cb.equal(compilationRoot.get("pinned"), pinned);
            criteriaQuery.where(predicate);
        }

        TypedQuery<Compilation> typedQuery = entityManager.createQuery(criteriaQuery);
        PageRequest pageable = getPageable(from, size);
        typedQuery.setFirstResult(pageable.getPageNumber());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Compilation> compilations = typedQuery.getResultList();

        var compilationDto = compilations.stream()
                .map(compilationMapper::toCompilationDto)
                .collect(Collectors.toList());

        compilationDto.forEach(o -> viewsCreator.addViewsToEvents(o.getEvents()));
        return compilationDto;
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        var compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id = %d not found", compId)));
        var compilationDto = compilationMapper.toCompilationDto(compilation);
        viewsCreator.addViewsToEvents(compilationDto.getEvents());
        return compilationDto;
    }
}
