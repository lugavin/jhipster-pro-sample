package com.gavin.myapp.config;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bstek.ureport.console.UReportServlet;
import com.bstek.ureport.definition.datasource.BuildinDatasource;
import com.bstek.ureport.exception.ReportException;
import com.bstek.ureport.provider.report.ReportFile;
import com.bstek.ureport.provider.report.ReportProvider;
import com.gavin.myapp.service.UReportFileService;
import com.gavin.myapp.service.dto.UReportFileDTO;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.*;
import javax.sql.DataSource;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.util.StringUtils;

@Configuration
@ImportResource("classpath:ureport-console-context.xml")
public class UReportConfig {

    private final DataSource dataSource;

    private final UReportFileService uReportFileService;

    public UReportConfig(DataSource dataSource, UReportFileService uReportFileService) {
        this.dataSource = dataSource;
        this.uReportFileService = uReportFileService;
    }

    @Bean
    public ServletRegistrationBean ureportServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new UReportServlet());
        bean.addUrlMappings("/ureport/*");
        return bean;
    }

    @Bean
    public ReportProvider reportProvider() {
        return new ReportProvider() {
            private String prefix = "db:";
            private boolean disabled;

            @Override
            public InputStream loadReport(String file) {
                if (file.startsWith(prefix)) {
                    file = file.substring(prefix.length(), file.length());
                }
                Optional<UReportFileDTO> uReportFileDTO = uReportFileService.getByName(file);
                if (uReportFileDTO.isPresent()) {
                    return new ByteArrayInputStream(uReportFileDTO.get().getContent().getBytes());
                } else {
                    throw new ReportException("file is not exist.");
                }
            }

            @Override
            public void deleteReport(String file) {
                if (file.startsWith(prefix)) {
                    file = file.substring(prefix.length(), file.length());
                }
                uReportFileService.deleteByName(file);
            }

            @Override
            public List<ReportFile> getReportFiles() {
                List<ReportFile> list = new ArrayList<ReportFile>();
                IPage<UReportFileDTO> uReportFileDTOS = uReportFileService.findAll(null);
                uReportFileDTOS
                    .getRecords()
                    .forEach(
                        uReportFileDTO -> {
                            list.add(new ReportFile(uReportFileDTO.getName(), Date.from(uReportFileDTO.getUpdateAt().toInstant())));
                        }
                    );
                return list;
            }

            @Override
            public void saveReport(String file, String content) {
                if (file.startsWith(prefix)) {
                    file = file.substring(prefix.length(), file.length());
                }
                UReportFileDTO uReportFileDTO = uReportFileService.getByName(file).orElse(new UReportFileDTO());
                if (StringUtils.hasText(uReportFileDTO.getName())) {
                    uReportFileDTO.setContent(content);
                } else {
                    uReportFileDTO.setName(file);
                    uReportFileDTO.setContent(content);
                    uReportFileDTO.setCreateAt(ZonedDateTime.now());
                    uReportFileDTO.setUpdateAt(ZonedDateTime.now());
                }
                uReportFileService.save(uReportFileDTO);
            }

            @Override
            public String getName() {
                return "数据库文件系统";
            }

            @Override
            public boolean disabled() {
                return false;
            }

            @Override
            public String getPrefix() {
                return prefix;
            }
        };
    }

    @Bean
    public BuildinDatasource buildinDatasource() {
        return new BuildinDatasource() {
            @Override
            public String name() {
                return "内置";
            }

            @Override
            public Connection getConnection() {
                try {
                    return dataSource.getConnection();
                } catch (SQLException e) {
                    throw new ReportException(e);
                }
            }
        };
    }
}
