package com.sheep.generator.utils;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;

/**
 * @author : sheep669
 * @description : TODO
 * @created at 2022/9/21 17:03
 */
@SuppressWarnings("ALL")
public class CreateDbDoc {
    //生成数据库文档路径
    private static String FILE_OUTPUT_DIR = "src\\main\\resources\\outdir";

    public static void main(String[] args) {
        documentGeneration();
    }

    public static void documentGeneration() {
        //数据源  hikari
        HikariConfig hikariConfig = new HikariConfig();
        // MYSQL 8 驱动
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        //指定数据库
        hikariConfig.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/emo_admin");
        //数据库账号
        hikariConfig.setUsername("root");
        //数据库密码
        hikariConfig.setPassword("root");
        //设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        DataSource dataSource = new HikariDataSource(hikariConfig);
        //生成配置
        EngineConfig engineConfig = EngineConfig.builder()
                //生成文件路径
                .fileOutputDir(FILE_OUTPUT_DIR)
                //生成完成后是否打开目录
                .openOutputDir(true)

                //指定生成数据库文档的文件类型 //html格式
                .fileType(EngineFileType.HTML)
                //.fileType(EngineFileType.WORD)//word格式
                //.fileType(EngineFileType.MD)//md格式

                //生成模板实现
                .produceType(EngineTemplateType.freemarker)

                //自定义文件名称
                .fileName("Test 数据库设计文档").build();

        //忽略表：这些表不生成
        ArrayList<String> ignoreTableName = new ArrayList<>();
        ignoreTableName.add("test_user");
        ignoreTableName.add("test_group");
        //忽略表前缀：这些表不生成
        ArrayList<String> ignorePrefix = new ArrayList<>();
        ignorePrefix.add("test_");
        //忽略表后缀：这些表不生成
        ArrayList<String> ignoreSuffix = new ArrayList<>();
        ignoreSuffix.add("_test");

        ProcessConfig processConfig = ProcessConfig.builder()
                //指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
                //根据名称指定表生成
                .designatedTableName(new ArrayList<>())
                //根据表前缀生成
                .designatedTablePrefix(new ArrayList<>())
                //根据表后缀生成
                .designatedTableSuffix(new ArrayList<>())
                //忽略表名
                .ignoreTableName(ignoreTableName)
                //忽略表前缀
                .ignoreTablePrefix(ignorePrefix)
                //忽略表后缀
                .ignoreTableSuffix(ignoreSuffix).build();
        //配置
        Configuration config = Configuration.builder()
                //数据库文档的版本
                .version("1.1.1")
                //文档标题
                .title("Test系统数据库设计文档")
                //机构信息
                .organization("Test")
                //文档描述
                .description("Test 数据库设计文档")
                //数据源
                .dataSource(dataSource)
                //生成配置
                .engineConfig(engineConfig)
                //生成配置
                .produceConfig(processConfig)
                .build();

        //执行生成
        new DocumentationExecute(config).execute();
        System.out.println("**********************数据库文档生成完成**********************");
    }
}

