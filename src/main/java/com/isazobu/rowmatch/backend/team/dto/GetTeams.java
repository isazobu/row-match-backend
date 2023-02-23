package com.isazobu.rowmatch.backend.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetTeams {

    private Long id;
    private String name;
    private int num_members;

}
