package ru.practicum.ewm.db.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hits")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 254, nullable = false)
    private String app;

    @Column(length = 254, nullable = false)
    private String uri;

    @Column(length = 15, nullable = false)
    private String ip;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
