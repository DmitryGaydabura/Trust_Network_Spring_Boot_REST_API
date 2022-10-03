package com.example.trustnetwork.domain;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    String text;
    String topics;
    String fromPersonId;
    Integer minTrustLevel;
}
