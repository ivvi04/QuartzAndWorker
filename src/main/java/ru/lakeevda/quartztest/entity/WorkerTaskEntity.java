package ru.lakeevda.quartztest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "worker_tasks")
@ToString
public class WorkerTaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "status", nullable = false, length = Integer.MAX_VALUE)
    private String status;

    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "count_of_iterations")
    private Long countOfIterations;


}