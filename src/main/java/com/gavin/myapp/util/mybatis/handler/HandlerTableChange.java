package com.gavin.myapp.util.mybatis.handler;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gavin.myapp.SpringBootUtil;
import com.gavin.myapp.domain.enumeration.CommonFieldType;
import com.gavin.myapp.domain.enumeration.RelationshipType;
import com.gavin.myapp.service.dto.CommonTableFieldDTO;
import com.gavin.myapp.service.dto.CommonTableRelationshipDTO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;

public class HandlerTableChange {

    private static final ObjectMapper objMap = new ObjectMapper();

    private static void handlerChangelog(JsonNode databaseChangeLog) {
        File changelogPath = new File(SpringBootUtil.getApplicationPathEndWithSeparator() + "extDataChangelog");
        if (!changelogPath.exists()) {
            changelogPath.mkdir();
        }
        String fileName = SpringBootUtil.getApplicationPathEndWithSeparator() + "extDataChangelog/" + System.nanoTime() + ".json";
        File changelogFile = new File(fileName);
        try {
            if (changelogFile.exists()) {
                JsonNode root = objMap.readTree(changelogFile);
                //                ArrayNode databaseChangeLog = (ArrayNode) root.path("databaseChangeLog");
                //                int size = databaseChangeLog.size();
                //                databaseChangeLog.add(node);
                OutputStream outputStream = new FileOutputStream(changelogFile);
                objMap.writeValue(outputStream, databaseChangeLog);
                outputStream.flush();
                outputStream.close();
            } else {
                OutputStream outputStream = new FileOutputStream(changelogFile);
                objMap.writeValue(outputStream, databaseChangeLog);
                outputStream.flush();
                outputStream.close();
            }
            LiquibaseProperties liquibaseProperties = SpringBootUtil.getBean(LiquibaseProperties.class);
            Database database = DatabaseFactory
                .getInstance()
                .findCorrectDatabaseImplementation(
                    new JdbcConnection(SpringBootUtil.getBean("secondDatasource", DataSource.class).getConnection())
                );
            Liquibase liquibasex = new Liquibase(fileName, new FileSystemResourceAccessor(), database);
            liquibasex.update(liquibaseProperties.getContexts());
        } catch (IOException | SQLException | LiquibaseException exception) {
            exception.printStackTrace();
        }
    }

    private static JsonNode generateAddColumn(String tableName, List<Map<String, String>> columns) {
        ObjectNode root = objMap.createObjectNode();
        root.put("tableName", tableName);
        ArrayNode columnsNode = objMap.createArrayNode();
        columns.forEach(
            columnMap -> {
                ObjectNode columnRoot = objMap.createObjectNode();
                ObjectNode columnNode = objMap.createObjectNode();
                columnNode.put("name", columnMap.get("name"));
                columnNode.put("type", columnMap.get("type"));
                columnRoot.put("column", columnNode);
                columnsNode.add(columnRoot);
            }
        );
        root.put("columns", columnsNode);
        ObjectNode result = objMap.createObjectNode();
        result.put("addColumn", root);
        return result;
    }

    private static JsonNode generateExtCreateTable(String tableName, String referencedTableName) {
        ObjectNode createTableNode = objMap.createObjectNode();
        ObjectNode createTableObject = objMap.createObjectNode();
        createTableObject.put("tableName", tableName);
        ArrayNode columnsArray = objMap.createArrayNode();
        ObjectNode columnRoot = objMap.createObjectNode();
        ObjectNode columnNode = objMap.createObjectNode();
        columnNode.put("name", "id");
        columnNode.put("type", "bigint");
        columnNode.put("autoIncrement", false);
        ObjectNode constraintsNode = objMap.createObjectNode();
        constraintsNode.put("nullable", false);
        constraintsNode.put("primaryKey", true);
        columnNode.put("constraints", constraintsNode);
        columnRoot.put("column", columnNode);
        columnsArray.add(columnRoot);
        createTableObject.put("columns", columnsArray);
        createTableNode.put("createTable", createTableObject);
        createTableNode.put(
            "addForeignKeyConstraint",
            generateAddForeignKeyConstraint("id", tableName, "fk_" + tableName + "_id", "id", referencedTableName)
                .get("addForeignKeyConstraint")
        );
        return createTableNode;
    }

    private static JsonNode generateAddForeignKeyConstraint(
        String baseColumnNames,
        String baseTableName,
        String constraintName,
        String referencedColumnNames,
        String referencedTableName
    ) {
        ObjectNode addForeignKeyConstraintNode = objMap.createObjectNode();
        ObjectNode addForeignKeyConstraintObject = objMap.createObjectNode();
        addForeignKeyConstraintObject.put("baseColumnNames", baseColumnNames);
        addForeignKeyConstraintObject.put("baseTableName", baseTableName);
        addForeignKeyConstraintObject.put("constraintName", constraintName);
        addForeignKeyConstraintObject.put("referencedColumnNames", referencedColumnNames);
        addForeignKeyConstraintObject.put("referencedTableName", referencedTableName);
        addForeignKeyConstraintObject.put("onDelete", "CASCADE");
        addForeignKeyConstraintNode.put("addForeignKeyConstraint", addForeignKeyConstraintObject);
        return addForeignKeyConstraintNode;
    }

    private static JsonNode generateCreateTable(String tableName, List<Map<String, String>> columns) {
        ObjectNode createTableNode = objMap.createObjectNode();
        ObjectNode createTableObject = objMap.createObjectNode();
        createTableObject.put("tableName", tableName);
        ArrayNode columnsArray = objMap.createArrayNode();
        columns.forEach(
            columnMap -> {
                ObjectNode columnRoot = objMap.createObjectNode();
                ObjectNode columnNode = objMap.createObjectNode();
                columnNode.put("name", columnMap.get("name"));
                columnNode.put("type", columnMap.get("type"));
                columnRoot.put("column", columnNode);
                columnsArray.add(columnRoot);
            }
        );
        createTableObject.put("columns", columnsArray);
        createTableNode.put("createTable", createTableObject);
        return createTableNode;
    }

    /**
     * 生成删除表JsonNode
     *
     * @param tableName 表名
     * @return JsonNode
     */
    private static JsonNode generateDropTable(String tableName) {
        ObjectNode dropTableNode = objMap.createObjectNode();
        ObjectNode dropTableObject = objMap.createObjectNode();
        dropTableObject.put("tableName", tableName);
        dropTableNode.put("dropTable", dropTableObject);
        return dropTableNode;
    }

    /**
     * 生成删除表JsonNode
     *
     * @param tableName   表名
     * @param columnNames 列名list
     * @return JsonNode
     */
    private static JsonNode generateDropColumn(String tableName, List<String> columnNames) {
        ObjectNode dropColumnsNode = objMap.createObjectNode();
        ObjectNode dropColumnsObject = objMap.createObjectNode();
        dropColumnsObject.put("tableName", tableName);
        ArrayNode columnsArray = objMap.createArrayNode();
        columnNames.forEach(
            column -> {
                ObjectNode columnObject = objMap.createObjectNode();
                ObjectNode columnNameObject = objMap.createObjectNode();
                columnNameObject.put("name", column);
                columnObject.put("column", columnNameObject);
                columnsArray.add(columnObject);
            }
        );
        dropColumnsObject.put("columns", columnsArray);
        dropColumnsNode.put("dropColumn", dropColumnsObject);
        return dropColumnsNode;
    }

    private static JsonNode generatePreConditions(String tableName) {
        ArrayNode preConditionsArray = objMap.createArrayNode();
        ObjectNode preConditionNode = objMap.createObjectNode();
        ObjectNode onFailNode = objMap.createObjectNode();
        onFailNode.put("onFail", "MARK_RAN");
        ObjectNode tableExistsNode = objMap.createObjectNode();
        tableExistsNode.put("tableName", tableName);
        tableExistsNode.put("schemaName", "gatewaytest08");
        ObjectNode notNode = objMap.createObjectNode();
        notNode.put("tableExists", tableExistsNode);
        preConditionNode.put("not", notNode);
        preConditionsArray.add(preConditionNode);
        preConditionsArray.add(onFailNode);
        return preConditionsArray;
    }

    /**
     * 重命名列
     *
     * @param tableName     表名
     * @param newColumnName 新列名
     * @param oldColumnName 新列名
     * @param dataType      数据类型
     * @return JsonNode
     */
    private static JsonNode generateRenameColumn(String tableName, String newColumnName, String oldColumnName, String dataType) {
        ObjectNode renameColumnNode = objMap.createObjectNode();
        ObjectNode renameColumnObject = objMap.createObjectNode();
        renameColumnObject.put("columnDataType", dataType);
        renameColumnObject.put("newColumnName", newColumnName);
        renameColumnObject.put("oldColumnName", oldColumnName);
        renameColumnNode.put("renameColumn", renameColumnObject);
        return renameColumnNode;
    }

    /**
     * 重命名表
     *
     * @param newTableName 新表名
     * @param oldTableName 旧表名
     * @return JsonNode
     */
    private static JsonNode generateRenameTable(String newTableName, String oldTableName) {
        ObjectNode renameTableNode = objMap.createObjectNode();
        ObjectNode renameTableObject = objMap.createObjectNode();
        renameTableObject.put("newTableName", newTableName);
        renameTableObject.put("oldTableName", oldTableName);
        renameTableNode.put("renameTable", renameTableObject);
        return renameTableNode;
    }

    /**
     * 修改列数据类型
     *
     * @param tableName   表名
     * @param columnName  列名
     * @param newDataType 新数据类型
     * @return JsonNode
     */
    private static JsonNode generateModifyDataType(String tableName, String columnName, String newDataType) {
        ObjectNode result = objMap.createObjectNode();
        ObjectNode root = objMap.createObjectNode();
        root.put("tableName", tableName);
        root.put("columnName", columnName);
        root.put("newDataType", newDataType);
        result.put("modifyDataType", root);
        return result;
    }

    /**
     * @param id     Id标识
     * @param change 节点
     * @return JsonNode
     */
    private static JsonNode generateChangeSetAndChanges(String id, JsonNode change, Map<String, JsonNode> changeSetOthers) {
        ObjectNode changeSetNode = objMap.createObjectNode();
        changeSetNode.put("id", id);
        changeSetNode.put("author", "jhi");
        changeSetNode.put("changes", change);
        ObjectNode changeSetRoot = objMap.createObjectNode();
        if (changeSetOthers != null && !changeSetOthers.isEmpty()) {
            changeSetOthers.forEach(changeSetNode::put);
        }
        changeSetRoot.put("changeSet", changeSetNode);
        return changeSetRoot;
    }

    /**
     * 生成databaseChangeLog节点
     *
     * @return JsonNode
     */
    private static JsonNode generatedatabaseChangeLog() {
        ObjectNode databaseChangeLogRoot = objMap.createObjectNode();
        ArrayNode databaseChangeLogArray = objMap.createArrayNode();
        databaseChangeLogRoot.put("databaseChangeLog", databaseChangeLogArray);
        return databaseChangeLogRoot;
    }

    /**
     * @param newField 新字段描述
     * @param oldField 旧字段描述
     */
    public static void compareCommonTableField(CommonTableFieldDTO newField, CommonTableFieldDTO oldField) {
        String extTableName = newField.getCommonTable().getTableName() + "_" + newField.getCommonTable().getId() + "_ext";
        if (oldField == null) {
            //1、 检查表是否存在
            //2. 表不存在新增表
            //3. 表存在，新增列
            // 增加新列
            List<Map<String, String>> columns = new ArrayList<>();
            Map<String, String> column = new HashMap<>();
            column.put("name", newField.getTableColumnName());
            column.put("type", getFieldTypeString(newField.getType()));
            columns.add(column);
            JsonNode databaseChangeLog = generatedatabaseChangeLog();
            ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
            Map<String, JsonNode> changeSetOthers = new HashMap<>();
            changeSetOthers.put("preConditions", generatePreConditions(extTableName));
            databaseChangeLogArray.add(
                generateChangeSetAndChanges(
                    System.nanoTime() + "",
                    generateExtCreateTable(extTableName, newField.getCommonTable().getTableName()),
                    changeSetOthers
                )
            );
            databaseChangeLogArray.add(generateChangeSetAndChanges(System.nanoTime() + "", generateAddColumn(extTableName, columns), null));
            handlerChangelog(databaseChangeLog);
        } else {
            if (!newField.getType().equals(oldField.getType())) {
                JsonNode databaseChangeLog = generatedatabaseChangeLog();
                ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                databaseChangeLogArray.add(
                    generateModifyDataType(extTableName, newField.getTableColumnName(), getFieldTypeString(newField.getType()))
                );
                handlerChangelog(databaseChangeLog);
            } else if (!newField.getEntityFieldName().equals(oldField.getEntityFieldName())) {
                JsonNode databaseChangeLog = generatedatabaseChangeLog();
                ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                String oldColumnName = oldField.getEntityFieldName();
                String newColumnName = newField.getEntityFieldName();
                databaseChangeLogArray.add(
                    generateRenameColumn(extTableName, newColumnName, oldColumnName, getFieldTypeString(CommonFieldType.LONG))
                );
                handlerChangelog(databaseChangeLog);
            }
        }
        /*if (!newField.getTableColumnName().equals(oldField.getTableColumnName())) {

        }*/
    }

    /**
     * @param newRelationship 新关系描述
     * @param oldRelationship 旧关系描述
     */
    public static void compareCommonTableRelationship(
        CommonTableRelationshipDTO newRelationship,
        CommonTableRelationshipDTO oldRelationship
    ) {
        String extTableName = newRelationship.getCommonTable().getTableName() + "_" + newRelationship.getCommonTable().getId() + "_ext";
        if (oldRelationship == null) { // 增加新关联关系
            switch (newRelationship.getRelationshipType()) {
                case MANY_TO_ONE:
                case ONE_TO_ONE:
                    {
                        List<Map<String, String>> columns = new ArrayList<>();
                        Map<String, String> column = new HashMap<>();
                        // 关联ID字段
                        column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName() + "Id"));
                        column.put("type", getFieldTypeString(CommonFieldType.LONG));
                        columns.add(column);
                        JsonNode databaseChangeLog = generatedatabaseChangeLog();
                        ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                        Map<String, JsonNode> changeSetOthers = new HashMap<>();
                        changeSetOthers.put("preConditions", generatePreConditions(extTableName));
                        // 如果表不存在则创建表
                        databaseChangeLogArray.add(
                            generateChangeSetAndChanges(
                                System.nanoTime() + "",
                                generateExtCreateTable(extTableName, newRelationship.getCommonTable().getTableName()),
                                changeSetOthers
                            )
                        );
                        databaseChangeLogArray.add(
                            generateChangeSetAndChanges(System.nanoTime() + "", generateAddColumn(extTableName, columns), null)
                        );
                        handlerChangelog(databaseChangeLog);
                        break;
                    }
                case ONE_TO_MANY:
                case MANY_TO_MANY:
                    {
                        // 首先增加一个单独的表
                        String joinTableName =
                            newRelationship.getCommonTable().getTableName() +
                            "_" +
                            StrUtil.toUnderlineCase(newRelationship.getRelationshipName());
                        List<Map<String, String>> columns = new ArrayList<>();
                        Map<String, String> column = new HashMap<>();
                        // 关联ID主键
                        column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                        column.put("type", getFieldTypeString(CommonFieldType.LONG));
                        columns.add(column);
                        column = new HashMap<>();
                        // 关联其他外键
                        column.put("name", StrUtil.toUnderlineCase(newRelationship.getOtherEntityName()) + "_id");
                        column.put("type", getFieldTypeString(CommonFieldType.LONG));
                        columns.add(column);
                        JsonNode databaseChangeLog = generatedatabaseChangeLog();
                        ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                        Map<String, JsonNode> changeSetOthers = new HashMap<>();
                        changeSetOthers.put("preConditions", generatePreConditions(extTableName));
                        // 如果表不存在则创建表
                        databaseChangeLogArray.add(
                            generateChangeSetAndChanges(
                                System.nanoTime() + "",
                                generateExtCreateTable(extTableName, newRelationship.getCommonTable().getTableName()),
                                changeSetOthers
                            )
                        );
                        databaseChangeLogArray.add(
                            generateChangeSetAndChanges(System.nanoTime() + "", generateCreateTable(joinTableName, columns), null)
                        );
                        handlerChangelog(databaseChangeLog);
                        break;
                    }
            }
        } else { // 修改关联关系
            // 关联关系变化
            if (!newRelationship.getRelationshipType().equals(oldRelationship.getRelationshipType())) {
                if (
                    oldRelationship.getRelationshipType().equals(RelationshipType.MANY_TO_ONE) ||
                    oldRelationship.getRelationshipType().equals(RelationshipType.ONE_TO_ONE)
                ) {
                    if (
                        newRelationship.getRelationshipType().equals(RelationshipType.MANY_TO_ONE) ||
                        newRelationship.getRelationshipType().equals(RelationshipType.ONE_TO_ONE)
                    ) {
                        // 不发生变化,many-to-one与one-to-one基本一定的设置。
                        // 1.relationshipName发生变化
                        if (!newRelationship.getRelationshipName().equals(oldRelationship.getRelationshipName())) {
                            // 修改对应的列字段名。此时即使对应的关联表发生变化，也没有关系。
                            JsonNode databaseChangeLog = generatedatabaseChangeLog();
                            ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                            if (!newRelationship.getCommonTable().getId().equals(oldRelationship.getCommonTable().getId())) {
                                // 删除列，
                                List<String> columnNames = new ArrayList<>();
                                columnNames.add(oldRelationship.getRelationshipName() + "_id");
                                databaseChangeLogArray.add(
                                    generateChangeSetAndChanges(System.nanoTime() + "", generateDropColumn(extTableName, columnNames), null)
                                );
                                // 增加新列
                                List<Map<String, String>> columns = new ArrayList<>();
                                Map<String, String> column = new HashMap<>();
                                // 关联ID主键
                                column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                                column.put("type", getFieldTypeString(CommonFieldType.LONG));
                                columns.add(column);
                                databaseChangeLogArray.add(
                                    generateChangeSetAndChanges(System.nanoTime() + "", generateAddColumn(extTableName, columns), null)
                                );
                                handlerChangelog(databaseChangeLog);
                            } else {
                                // 修改列名
                                String newColumnName = newRelationship.getRelationshipName() + "_id";
                                String oldColumnName = oldRelationship.getRelationshipName() + "_id";
                                databaseChangeLogArray.add(
                                    generateChangeSetAndChanges(
                                        System.nanoTime() + "",
                                        generateRenameColumn(
                                            extTableName,
                                            newColumnName,
                                            oldColumnName,
                                            getFieldTypeString(CommonFieldType.LONG)
                                        ),
                                        null
                                    )
                                );
                                handlerChangelog(databaseChangeLog);
                            }
                        } else {
                            //2.relationshipName没有发生变化
                            // 对应关联表发生了变化
                            if (!newRelationship.getCommonTable().getId().equals(oldRelationship.getCommonTable().getId())) {
                                //1. 删除旧列，因为可能数据已经无用了
                                JsonNode databaseChangeLog = generatedatabaseChangeLog();
                                ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                                List<String> columnNames = new ArrayList<>();
                                columnNames.add(oldRelationship.getRelationshipName() + "_id");
                                databaseChangeLogArray.add(
                                    generateChangeSetAndChanges(System.nanoTime() + "", generateDropColumn(extTableName, columnNames), null)
                                );
                                //2. todo 增加新的列，重新命名。
                                List<Map<String, String>> columns = new ArrayList<>();
                                Map<String, String> column = new HashMap<>();
                                // 关联ID主键
                                column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                                column.put("type", getFieldTypeString(CommonFieldType.LONG));
                                columns.add(column);
                                databaseChangeLogArray.add(
                                    generateChangeSetAndChanges(System.nanoTime() + "", generateAddColumn(extTableName, columns), null)
                                );
                                handlerChangelog(databaseChangeLog);
                            }
                        }
                    } else { // to-many关联
                        // 1.删除原来的列
                        JsonNode databaseChangeLog = generatedatabaseChangeLog();
                        ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                        List<String> columnNames = new ArrayList<>();
                        columnNames.add(oldRelationship.getRelationshipName() + "_id");
                        databaseChangeLogArray.add(
                            generateChangeSetAndChanges(System.nanoTime() + "", generateDropColumn(extTableName, columnNames), null)
                        );
                        // 2.增加新的中间关联表
                        // 首先增加一个单独的表
                        String joinTableName =
                            newRelationship.getCommonTable().getTableName() +
                            "_" +
                            StrUtil.toUnderlineCase(newRelationship.getRelationshipName());

                        List<Map<String, String>> columns = new ArrayList<>();
                        Map<String, String> column = new HashMap<>();
                        // 关联ID主键
                        column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                        column.put("type", getFieldTypeString(CommonFieldType.LONG));
                        columns.add(column);
                        column = new HashMap<>();
                        // 关联其他外键
                        column.put("name", StrUtil.toUnderlineCase(newRelationship.getOtherEntityName()) + "_id");
                        column.put("type", getFieldTypeString(CommonFieldType.LONG));
                        columns.add(column);
                        databaseChangeLogArray.add(
                            generateChangeSetAndChanges(System.nanoTime() + "", generateCreateTable(joinTableName, columns), null)
                        );
                        handlerChangelog(databaseChangeLog);
                    }
                } else { // 原来为-to-many关系
                    // 关联关系没有变化
                    if (
                        newRelationship.getRelationshipType().equals(RelationshipType.ONE_TO_MANY) ||
                        newRelationship.getRelationshipType().equals(RelationshipType.MANY_TO_MANY)
                    ) {
                        // 不发生变化,one-to-many与many-to-many基本一定的设置。
                        // 1.relationshipName发生变化
                        if (!newRelationship.getRelationshipName().equals(oldRelationship.getRelationshipName())) {
                            if (!newRelationship.getCommonTable().getId().equals(oldRelationship.getCommonTable().getId())) {
                                //1. 删除旧表，因为可能数据已经无用了
                                String oldJoinTableName =
                                    oldRelationship.getCommonTable().getTableName() +
                                    "_" +
                                    StrUtil.toUnderlineCase(oldRelationship.getRelationshipName());
                                JsonNode databaseChangeLog = generatedatabaseChangeLog();
                                ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                                databaseChangeLogArray.add(
                                    generateChangeSetAndChanges(System.nanoTime() + "", generateDropTable(oldJoinTableName), null)
                                );
                                //2. 增加新表和新的列，重新命名。
                                String newJoinTableName =
                                    newRelationship.getCommonTable().getTableName() +
                                    "_" +
                                    StrUtil.toUnderlineCase(newRelationship.getRelationshipName());
                                List<Map<String, String>> columns = new ArrayList<>();
                                Map<String, String> column = new HashMap<>();
                                // 关联ID主键
                                column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                                column.put("type", getFieldTypeString(CommonFieldType.LONG));
                                columns.add(column);
                                column = new HashMap<>();
                                // 关联其他外键
                                column.put("name", StrUtil.toUnderlineCase(newRelationship.getOtherEntityName()) + "_id");
                                column.put("type", getFieldTypeString(CommonFieldType.LONG));
                                columns.add(column);
                                databaseChangeLogArray.add(
                                    generateChangeSetAndChanges(
                                        System.nanoTime() + "",
                                        generateCreateTable(newJoinTableName, columns),
                                        null
                                    )
                                );
                                handlerChangelog(databaseChangeLog);
                            } else {
                                // 仅修改中间关联表的列名，其他没有变化。
                                String joinTableName =
                                    newRelationship.getCommonTable().getTableName() +
                                    "_" +
                                    StrUtil.toUnderlineCase(newRelationship.getRelationshipName());
                                String oldColumnName = StrUtil.toUnderlineCase(oldRelationship.getRelationshipName()) + "_id";
                                String newColumnName = StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id";
                                JsonNode databaseChangeLog = generatedatabaseChangeLog();
                                ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                                databaseChangeLogArray.add(
                                    generateChangeSetAndChanges(
                                        System.nanoTime() + "",
                                        generateRenameColumn(
                                            joinTableName,
                                            newColumnName,
                                            oldColumnName,
                                            getFieldTypeString(CommonFieldType.LONG)
                                        ),
                                        null
                                    )
                                );
                                handlerChangelog(databaseChangeLog);
                            }
                        } else {
                            //2.relationshipName没有发生变化
                            // 对应关联表发生了变化
                            if (!newRelationship.getCommonTable().getId().equals(oldRelationship.getCommonTable().getId())) {
                                //1. 删除旧表，因为可能数据已经无用了
                                String oldJoinTableName =
                                    oldRelationship.getCommonTable().getTableName() +
                                    "_" +
                                    StrUtil.toUnderlineCase(oldRelationship.getRelationshipName());
                                JsonNode databaseChangeLog = generatedatabaseChangeLog();
                                ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                                databaseChangeLogArray.add(
                                    generateChangeSetAndChanges(System.nanoTime() + "", generateDropTable(oldJoinTableName), null)
                                );
                                //2. 增加新表和新的列，重新命名。
                                String newJoinTableName =
                                    newRelationship.getCommonTable().getTableName() +
                                    "_" +
                                    StrUtil.toUnderlineCase(newRelationship.getRelationshipName());
                                List<Map<String, String>> columns = new ArrayList<>();
                                Map<String, String> column = new HashMap<>();
                                // 关联ID主键
                                column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                                column.put("type", getFieldTypeString(CommonFieldType.LONG));
                                columns.add(column);
                                column = new HashMap<>();
                                // 关联其他外键
                                column.put("name", StrUtil.toUnderlineCase(newRelationship.getOtherEntityName()) + "_id");
                                column.put("type", getFieldTypeString(CommonFieldType.LONG));
                                columns.add(column);
                                databaseChangeLogArray.add(
                                    generateChangeSetAndChanges(
                                        System.nanoTime() + "",
                                        generateCreateTable(newJoinTableName, columns),
                                        null
                                    )
                                );
                                handlerChangelog(databaseChangeLog);
                            }
                        }
                    } else { // 变为to-one关系
                        // 改变表或新建表
                        // 1. 删除原来的关联中间表
                        //1. 删除旧表，因为可能数据已经无用了
                        String oldJoinTableName =
                            oldRelationship.getCommonTable().getTableName() +
                            "_" +
                            StrUtil.toUnderlineCase(oldRelationship.getRelationshipName());
                        JsonNode databaseChangeLog = generatedatabaseChangeLog();
                        ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                        databaseChangeLogArray.add(
                            generateChangeSetAndChanges(System.nanoTime() + "", generateDropTable(oldJoinTableName), null)
                        );
                        // 2. 新建many-to-one或oen-to-one的字段名。
                        List<Map<String, String>> columns = new ArrayList<>();
                        Map<String, String> column = new HashMap<>();
                        // 关联ID主键
                        column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                        column.put("type", getFieldTypeString(CommonFieldType.LONG));
                        columns.add(column);
                        databaseChangeLogArray.add(
                            generateChangeSetAndChanges(System.nanoTime() + "", generateAddColumn(extTableName, columns), null)
                        );
                        handlerChangelog(databaseChangeLog);
                    }
                }
            } else {
                // 关联关系未变化，此时变化的可能是关联的另一实体变化，或者关联的relationshipName发生变化。
                // 1.relationshipName发生变化
                if (!newRelationship.getRelationshipName().equals(oldRelationship.getRelationshipName())) {
                    // 修改对应的列字段名。此时即使对应的关联表发生变化，也没有关系。
                    if (
                        newRelationship.getRelationshipType().equals(RelationshipType.MANY_TO_ONE) ||
                        newRelationship.getRelationshipType().equals(RelationshipType.ONE_TO_ONE)
                    ) {
                        // 关联表发生变化
                        if (!newRelationship.getCommonTable().getId().equals(oldRelationship.getCommonTable().getId())) {
                            // 1. 删除列
                            JsonNode databaseChangeLog = generatedatabaseChangeLog();
                            ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                            List<String> columnNames = new ArrayList<>();
                            columnNames.add(oldRelationship.getRelationshipName() + "_id");
                            databaseChangeLogArray.add(
                                generateChangeSetAndChanges(System.nanoTime() + "", generateDropColumn(extTableName, columnNames), null)
                            );
                            // 2. 增加新列
                            List<Map<String, String>> columns = new ArrayList<>();
                            Map<String, String> column = new HashMap<>();
                            // 关联ID主键
                            column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                            column.put("type", getFieldTypeString(CommonFieldType.LONG));
                            columns.add(column);
                            databaseChangeLogArray.add(
                                generateChangeSetAndChanges(System.nanoTime() + "", generateAddColumn(extTableName, columns), null)
                            );
                            handlerChangelog(databaseChangeLog);
                        } else {
                            // 仅修改列名
                            String joinTableName =
                                newRelationship.getCommonTable().getTableName() +
                                "_" +
                                StrUtil.toUnderlineCase(newRelationship.getRelationshipName());
                            String oldColumnName = StrUtil.toUnderlineCase(oldRelationship.getRelationshipName()) + "_id";
                            String newColumnName = StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id";
                            JsonNode databaseChangeLog = generatedatabaseChangeLog();
                            ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                            databaseChangeLogArray.add(
                                generateChangeSetAndChanges(
                                    System.nanoTime() + "",
                                    generateRenameColumn(
                                        joinTableName,
                                        newColumnName,
                                        oldColumnName,
                                        getFieldTypeString(CommonFieldType.LONG)
                                    ),
                                    null
                                )
                            );
                            handlerChangelog(databaseChangeLog);
                        }
                    } else { // to-many关系
                        if (!newRelationship.getCommonTable().getId().equals(oldRelationship.getCommonTable().getId())) {
                            // 1.删除原来的中间关联表
                            String oldJoinTableName =
                                oldRelationship.getCommonTable().getTableName() +
                                "_" +
                                StrUtil.toUnderlineCase(oldRelationship.getRelationshipName());
                            JsonNode databaseChangeLog = generatedatabaseChangeLog();
                            ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                            databaseChangeLogArray.add(
                                generateChangeSetAndChanges(System.nanoTime() + "", generateDropTable(oldJoinTableName), null)
                            );
                            // 2.增加新的关联表
                            String newJoinTableName =
                                newRelationship.getCommonTable().getTableName() +
                                "_" +
                                StrUtil.toUnderlineCase(newRelationship.getRelationshipName());
                            List<Map<String, String>> columns = new ArrayList<>();
                            Map<String, String> column = new HashMap<>();
                            // 关联ID主键
                            column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                            column.put("type", getFieldTypeString(CommonFieldType.LONG));
                            columns.add(column);
                            column = new HashMap<>();
                            // 关联其他外键
                            column.put("name", StrUtil.toUnderlineCase(newRelationship.getOtherEntityName()) + "_id");
                            column.put("type", getFieldTypeString(CommonFieldType.LONG));
                            columns.add(column);
                            databaseChangeLogArray.add(
                                generateChangeSetAndChanges(System.nanoTime() + "", generateCreateTable(newJoinTableName, columns), null)
                            );
                            handlerChangelog(databaseChangeLog);
                        } else { // 关联表未变化
                            // 仅修改列名
                            String joinTableName =
                                newRelationship.getCommonTable().getTableName() +
                                "_" +
                                StrUtil.toUnderlineCase(newRelationship.getRelationshipName());
                            String oldColumnName = StrUtil.toUnderlineCase(oldRelationship.getRelationshipName()) + "_id";
                            String newColumnName = StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id";
                            JsonNode databaseChangeLog = generatedatabaseChangeLog();
                            ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                            databaseChangeLogArray.add(
                                generateChangeSetAndChanges(
                                    System.nanoTime() + "",
                                    generateRenameColumn(
                                        joinTableName,
                                        newColumnName,
                                        oldColumnName,
                                        getFieldTypeString(CommonFieldType.LONG)
                                    ),
                                    null
                                )
                            );
                            handlerChangelog(databaseChangeLog);
                        }
                    }
                } else {
                    //2.relationshipName没有发生变化
                    // 对应关联表发生了变化
                    if (!newRelationship.getCommonTable().getId().equals(oldRelationship.getCommonTable().getId())) {
                        if (
                            newRelationship.getRelationshipType().equals(RelationshipType.ONE_TO_ONE) ||
                            newRelationship.getRelationshipType().equals(RelationshipType.MANY_TO_ONE)
                        ) {
                            //1. 删除相关列，因为可能数据已经无用了
                            JsonNode databaseChangeLog = generatedatabaseChangeLog();
                            ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                            List<String> columnNames = new ArrayList<>();
                            columnNames.add(oldRelationship.getRelationshipName() + "_id");
                            databaseChangeLogArray.add(
                                generateChangeSetAndChanges(System.nanoTime() + "", generateDropColumn(extTableName, columnNames), null)
                            );
                            //2. 增加新的列，重新命名。
                            List<Map<String, String>> columns = new ArrayList<>();
                            Map<String, String> column = new HashMap<>();
                            // 关联ID主键
                            column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                            column.put("type", getFieldTypeString(CommonFieldType.LONG));
                            columns.add(column);
                            databaseChangeLogArray.add(
                                generateChangeSetAndChanges(System.nanoTime() + "", generateAddColumn(extTableName, columns), null)
                            );
                            handlerChangelog(databaseChangeLog);
                        } else {
                            //1. 删除整个表，因为可能数据已经无用了
                            String oldJoinTableName =
                                oldRelationship.getCommonTable().getTableName() +
                                "_" +
                                StrUtil.toUnderlineCase(oldRelationship.getRelationshipName());
                            JsonNode databaseChangeLog = generatedatabaseChangeLog();
                            ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                            databaseChangeLogArray.add(
                                generateChangeSetAndChanges(System.nanoTime() + "", generateDropTable(oldJoinTableName), null)
                            );
                            //2. 增加新的中间表。
                            String newJoinTableName =
                                newRelationship.getCommonTable().getTableName() +
                                "_" +
                                StrUtil.toUnderlineCase(newRelationship.getRelationshipName());
                            List<Map<String, String>> columns = new ArrayList<>();
                            Map<String, String> column = new HashMap<>();
                            // 关联ID主键
                            column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                            column.put("type", getFieldTypeString(CommonFieldType.LONG));
                            columns.add(column);
                            column = new HashMap<>();
                            // 关联其他外键
                            column.put("name", StrUtil.toUnderlineCase(newRelationship.getOtherEntityName()) + "_id");
                            column.put("type", getFieldTypeString(CommonFieldType.LONG));
                            columns.add(column);
                            databaseChangeLogArray.add(
                                generateChangeSetAndChanges(System.nanoTime() + "", generateCreateTable(newJoinTableName, columns), null)
                            );
                            handlerChangelog(databaseChangeLog);
                        }
                    }
                }
            }
        }
        /*if (!newField.getTableColumnName().equals(oldField.getTableColumnName())) {

        }*/
    }

    private static String getFieldTypeString(CommonFieldType type) {
        switch (type) {
            case ZONED_DATE_TIME:
                return "datetime";
            case TEXTBLOB:
                return "longtext";
            case INTEGER:
                return "integer";
            case BOOLEAN:
                return "boolean";
            case STRING:
                return "varchar(255)";
            case DOUBLE:
                return "double";
            case FLOAT:
                return "float";
            case LONG:
                return "bigint";
            default:
                return null;
        }
    }
}
