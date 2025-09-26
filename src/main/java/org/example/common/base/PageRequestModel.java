package org.example.common.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageRequestModel {
    private int page = 1;
    private int limit = 10;
    private String search = "";
}
