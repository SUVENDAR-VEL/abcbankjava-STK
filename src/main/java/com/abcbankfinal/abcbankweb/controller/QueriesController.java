package com.abcbankfinal.abcbankweb.controller;

import com.abcbankfinal.abcbankweb.dto.*;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.QueriesResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queriesResponse")
@RequiredArgsConstructor
@CrossOrigin("*")
public class QueriesController {

    private final QueriesResponseService queriesService;

    @PostMapping("/save")
    public ApiResponse<QueriesResponseDto> saveQuery(
            @RequestBody QueriesSaveDto dto) {

        return queriesService.saveQuery(dto);
    }

    @PostMapping("/queriesList")
    public ApiResponse<List<QueriesResponseDto>>
    getByAccountNumber(
            @RequestBody QueriesListByAccountRequestDto request) {

        return queriesService
                .getByAccountNumber(
                        request.getAccountNumber());
    }

    @PostMapping("/adminQueriesList")
    public ApiResponse<PageResponse<QueriesResponseDto>>
    listQueries(
            @RequestBody QueriesListRequestDto request) {

        return queriesService.getAllQueries(request);
    }

    @GetMapping("/queryBy/{id}")
    public ApiResponse<QueriesResponseDto>
    getQueryById(@PathVariable Long id) {

        return queriesService.getQueryById(id);
    }

    @PostMapping("/queryUpdateAdmin/{id}")
    public ApiResponse<String> updateQueryStatus(
            @PathVariable Long id,
            @RequestBody QueriesUpdateRequestDto request) {

        return queriesService
                .updateQueryStatus(id, request);
    }

    @GetMapping("/count")
    public ApiResponse<RequestCountDto>
    getQueriesCounts() {

        return queriesService.getQueriesCounts();
    }
}