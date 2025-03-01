package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@ToString
public class Capec {

    private Integer id;
    private String name;
    private String likelihood;
}
