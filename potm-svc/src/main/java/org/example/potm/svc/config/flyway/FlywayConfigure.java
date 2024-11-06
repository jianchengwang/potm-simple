package org.example.potm.svc.config.flyway;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Configuration
@EnableConfigurationProperties(FlywayProperties.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableTransactionManagement
public class FlywayConfigure {

    @Autowired
    public FlywayConfigure(DataSource dataSource, FlywayProperties config) {
        boolean enable = config.isEnable();
        if(!enable) {
            return;
        }
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        List<String> dataSourceNames = config.getDataSourceNames();
        if(CollectionUtils.isEmpty(dataSourceNames)) {
            log.warn("flyway dataSourceNames is empty");
            return;
        }
        for(String dataSourceName : dataSourceNames) {
            DataSource dsItem = ds.getDataSource(dataSourceName);
            if(dsItem == null){
                log.warn("flyway dataSourceNames is empty");
                continue;
            }
            String sqlLocation = "db/migration/" + dataSourceName;
            if(dataSourceName.equals("ck-ds")) {
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dsItem);
                String lastSql = getLastSql(sqlLocation);
                if(StringUtils.isNotEmpty(lastSql)) {
                    jdbcTemplate.execute(lastSql);
                }
                continue;
            }
            Flyway flyway = Flyway.configure()
                    .placeholderReplacement(false)
                    .dataSource(dsItem)
                    .locations("classpath:" + sqlLocation)
                    .sqlMigrationPrefix("V")
                    .sqlMigrationSeparator("__")
                    .sqlMigrationSuffixes(".sql")
                    .baselineOnMigrate(true)
                    .table("flyway_schema_history")
                    .outOfOrder(false)
                    .validateOnMigrate(false)
                    .validateMigrationNaming(true)
                    .load();
            flyway.migrate();
        }
    }

    private String getLastSql(String sqlLocation) {
        StringBuilder sqlBuilder = new StringBuilder();
        ClassLoader classLoader = FlywayConfigure.class.getClassLoader();
        URL resource = classLoader.getResource(sqlLocation);
        if (resource != null) {
            File folder = new File(resource.getFile());
            File[] files = folder.listFiles();

            if (files != null && files.length > 0) {
                // 使用文件名排序
                Arrays.sort(files, Comparator.comparing(File::getName));
                // 获取最后一个文件
                File lastFile = files[files.length - 1];
                // 读取文件内容并输出为文本
                try (BufferedReader reader = new BufferedReader(new FileReader(lastFile, StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sqlBuilder.append("\n").append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                log.error("No files found in the directory.");
            }
        } else {
            log.error("Resource folder not found.");
        }
        return sqlBuilder.toString();
    }

}
