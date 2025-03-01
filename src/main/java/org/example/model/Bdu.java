package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@AllArgsConstructor
@ToString
public class Bdu {

    private String id;
    private String name;
    private String description;
}
