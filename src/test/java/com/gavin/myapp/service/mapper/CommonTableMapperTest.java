package com.gavin.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommonTableMapperTest {

    private CommonTableMapper commonTableMapper;

    @BeforeEach
    public void setUp() {
        commonTableMapper = new CommonTableMapperImpl();
    }
}
