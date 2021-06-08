package com.gavin.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ViewPermissionMapperTest {

    private ViewPermissionMapper viewPermissionMapper;

    @BeforeEach
    public void setUp() {
        viewPermissionMapper = new ViewPermissionMapperImpl();
    }
}
