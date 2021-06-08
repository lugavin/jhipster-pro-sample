package com.gavin.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UReportFileMapperTest {

    private UReportFileMapper uReportFileMapper;

    @BeforeEach
    public void setUp() {
        uReportFileMapper = new UReportFileMapperImpl();
    }
}
