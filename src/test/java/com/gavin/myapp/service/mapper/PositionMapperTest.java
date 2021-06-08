package com.gavin.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PositionMapperTest {

    private PositionMapper positionMapper;

    @BeforeEach
    public void setUp() {
        positionMapper = new PositionMapperImpl();
    }
}
