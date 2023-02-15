package ru.practicum.ewm.event.bd.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.category.bd.model.Category;
import ru.practicum.ewm.event.bd.model.enums.EventStatus;
import ru.practicum.ewm.request.bd.model.Request;
import ru.practicum.ewm.request.bd.model.enums.RequestStatus;
import ru.practicum.ewm.user.bd.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 120, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String annotation;

    @Column(length = 7000, nullable = false)
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "participant_limit", nullable = false)
    private Long participantLimit;

    @Column(nullable = false)
    private Boolean paid;

    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", referencedColumnName = "id", nullable = false)
    private User initiator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", referencedColumnName = "id", nullable = false)
    private Location location;

    @ToString.Exclude
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private Set<Request> requests = new HashSet<>();

    public Long getConfirmedRequestsCount() {
        return this.getRequests().stream()
                .filter(o -> o.getStatus().equals(RequestStatus.CONFIRMED)).count();
    }

}
