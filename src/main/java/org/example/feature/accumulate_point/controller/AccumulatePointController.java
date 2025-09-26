package org.example.feature.accumulate_point.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.base.PageRequestModel;
import org.example.feature.accumulate_point.dto.request.SubtractPointDto;
import org.example.feature.accumulate_point.service.AccumulatePointService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RequestMapping(value = "/api/accumulate_point")
@RestController
@RequiredArgsConstructor
public class AccumulatePointController {
    private final AccumulatePointService accumulatePointService;

    @PostMapping("/checkin")
    public Object checkin() {
        return accumulatePointService.checkIn();
    }

    @PostMapping("/subtract")
    public Object subTract(@RequestBody @Validated SubtractPointDto subtractPointDto) {
        return accumulatePointService.subTractPoint(subtractPointDto);
    }

    @GetMapping("/history")
    public Object history(@ModelAttribute PageRequestModel pageRequestModel,
                          @RequestParam("startTime") @DateTimeFormat(pattern = "dd-MM-yyyy") Date startTime,
                          @RequestParam("endTime") @DateTimeFormat(pattern = "dd-MM-yyyy") Date endTime) {
       return accumulatePointService.historyAccumulationPoint(pageRequestModel, startTime, endTime);
    }

    @GetMapping("/day_checkin")
    public Object dayCheckin() {
        return accumulatePointService.listDayCheckin();
    }
}
