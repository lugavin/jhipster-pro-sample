package com.gavin.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DataDictionaryMapperTest {

    private DataDictionaryMapper dataDictionaryMapper;

    @BeforeEach
    public void setUp() {
        dataDictionaryMapper = new DataDictionaryMapperImpl();
    }
}
