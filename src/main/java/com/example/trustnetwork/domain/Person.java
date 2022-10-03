package com.example.trustnetwork.domain;

import lombok.*;

import javax.persistence.*;
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "person1")
public class Person {

    String id;
    String topics;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer code;
    String trust_connections;
    String message;
}
