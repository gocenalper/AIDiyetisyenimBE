package org.example.request;

import lombok.Data;

import java.util.List;

@Data
public class GetCurrentDietResponse {

    private List<DietDay> days;
}
