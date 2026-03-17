package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.constants.ApiConstants;
import com.spec.kit.exam.system.util.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class BaseController {

    protected Map<String, Object> buildStatistics(
        Supplier<Integer> totalSupplier,
        Supplier<Integer> activeSupplier,
        Supplier<Integer> inactiveSupplier
    ) {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", totalSupplier.get());
        statistics.put("active", activeSupplier.get());
        statistics.put("inactive", inactiveSupplier.get());
        return statistics;
    }

    protected <T> Result<T> success(T data) {
        return Result.success(data, ApiConstants.SUCCESS_MESSAGE);
    }

    protected <T> Result<T> success(T data, String message) {
        return Result.success(data, message);
    }

    protected <T> Result<T> successList(T data) {
        return Result.success(data, ApiConstants.LIST_SUCCESS_MESSAGE);
    }

    protected <T> Result<T> successDetail(T data) {
        return Result.success(data, ApiConstants.DETAIL_SUCCESS_MESSAGE);
    }

    protected <T> Result<T> successCreate(T data) {
        return Result.success(data, ApiConstants.CREATE_SUCCESS_MESSAGE);
    }

    protected <T> Result<T> successUpdate(T data) {
        return Result.success(data, ApiConstants.UPDATE_SUCCESS_MESSAGE);
    }

    protected Result<Void> successDelete() {
        return Result.success(null, ApiConstants.DELETE_SUCCESS_MESSAGE);
    }

    protected Result<Map<String, Object>> successStatistics(Map<String, Object> statistics) {
        return Result.success(statistics, ApiConstants.STATISTICS_SUCCESS_MESSAGE);
    }
}