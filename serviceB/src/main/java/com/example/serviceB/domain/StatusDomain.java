package com.example.serviceB.domain;

import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusDomain {
    private Long id;
    private String uuid;
    private Integer status;
}
