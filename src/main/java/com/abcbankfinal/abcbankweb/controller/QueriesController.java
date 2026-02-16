package com.abcbankfinal.abcbankweb.controller;


import com.abcbankfinal.abcbankweb.dto.QueriesResponseDto;
import com.abcbankfinal.abcbankweb.dto.QueriesSaveDto;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.QueriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queriesRequest")
@RequiredArgsConstructor
@CrossOrigin("*")
public class QueriesController {

    @Autowired
    private QueriesService queriesService;

    @PostMapping("/save")
    public ApiResponse<QueriesResponseDto> save(
            @RequestBody QueriesSaveDto dto) {
        return queriesService.save(dto);
    }

    @GetMapping("/getBy/{accountNumber}")
    public ApiResponse<List<QueriesResponseDto>>
    getByAccountNumber(@PathVariable Long accountNumber) {
        return queriesService.getByAccountNumber(accountNumber);
    }

    @PutMapping("update/{id}")
    public ApiResponse<QueriesResponseDto>
    updateQueryStatus(@PathVariable Long id, @RequestParam String status) {
        return queriesService.updateQueryStatus(id, status);
    }

    @PostMapping("/adminQueriesList")
    public ApiResponse<List<QueriesResponseDto>> getAllQueries() {
        return queriesService.getAllQueries();
    }

    @GetMapping("/queryById/{id}")
    public ApiResponse<QueriesResponseDto> getQueryById(@PathVariable Long id) {
        return queriesService.getQueryById(id);
    }
}

