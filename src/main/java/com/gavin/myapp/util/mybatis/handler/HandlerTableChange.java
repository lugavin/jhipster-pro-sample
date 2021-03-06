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
     * ???????????????JsonNode
     *
     * @param tableName ??????
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
     * ???????????????JsonNode
     *
     * @param tableName   ??????
     * @param columnNames ??????list
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
     * ????????????
     *
     * @param tableName     ??????
     * @param newColumnName ?????????
     * @param oldColumnName ?????????
     * @param dataType      ????????????
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
     * ????????????
     *
     * @param newTableName ?????????
     * @param oldTableName ?????????
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
     * ?????????????????????
     *
     * @param tableName   ??????
     * @param columnName  ??????
     * @param newDataType ???????????????
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
     * @param id     Id??????
     * @param change ??????
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
     * ??????databaseChangeLog??????
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
     * @param newField ???????????????
     * @param oldField ???????????????
     */
    public static void compareCommonTableField(CommonTableFieldDTO newField, CommonTableFieldDTO oldField) {
        String extTableName = newField.getCommonTable().getTableName() + "_" + newField.getCommonTable().getId() + "_ext";
        if (oldField == null) {
            //1??? ?????????????????????
            //2. ?????????????????????
            //3. ?????????????????????
            // ????????????
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
     * @param newRelationship ???????????????
     * @param oldRelationship ???????????????
     */
    public static void compareCommonTableRelationship(
        CommonTableRelationshipDTO newRelationship,
        CommonTableRelationshipDTO oldRelationship
    ) {
        String extTableName = newRelationship.getCommonTable().getTableName() + "_" + newRelationship.getCommonTable().getId() + "_ext";
        if (oldRelationship == null) { // ?????????????????????
            switch (newRelationship.getRelationshipType()) {
                case MANY_TO_ONE:
                case ONE_TO_ONE:
                    {
                        List<Map<String, String>> columns = new ArrayList<>();
                        Map<String, String> column = new HashMap<>();
                        // ??????ID??????
                        column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName() + "Id"));
                        column.put("type", getFieldTypeString(CommonFieldType.LONG));
                        columns.add(column);
                        JsonNode databaseChangeLog = generatedatabaseChangeLog();
                        ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                        Map<String, JsonNode> changeSetOthers = new HashMap<>();
                        changeSetOthers.put("preConditions", generatePreConditions(extTableName));
                        // ??????????????????????????????
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
                        // ??????????????????????????????
                        String joinTableName =
                            newRelationship.getCommonTable().getTableName() +
                            "_" +
                            StrUtil.toUnderlineCase(newRelationship.getRelationshipName());
                        List<Map<String, String>> columns = new ArrayList<>();
                        Map<String, String> column = new HashMap<>();
                        // ??????ID??????
                        column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                        column.put("type", getFieldTypeString(CommonFieldType.LONG));
                        columns.add(column);
                        column = new HashMap<>();
                        // ??????????????????
                        column.put("name", StrUtil.toUnderlineCase(newRelationship.getOtherEntityName()) + "_id");
                        column.put("type", getFieldTypeString(CommonFieldType.LONG));
                        columns.add(column);
                        JsonNode databaseChangeLog = generatedatabaseChangeLog();
                        ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                        Map<String, JsonNode> changeSetOthers = new HashMap<>();
                        changeSetOthers.put("preConditions", generatePreConditions(extTableName));
                        // ??????????????????????????????
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
        } else { // ??????????????????
            // ??????????????????
            if (!newRelationship.getRelationshipType().equals(oldRelationship.getRelationshipType())) {
                if (
                    oldRelationship.getRelationshipType().equals(RelationshipType.MANY_TO_ONE) ||
                    oldRelationship.getRelationshipType().equals(RelationshipType.ONE_TO_ONE)
                ) {
                    if (
                        newRelationship.getRelationshipType().equals(RelationshipType.MANY_TO_ONE) ||
                        newRelationship.getRelationshipType().equals(RelationshipType.ONE_TO_ONE)
                    ) {
                        // ???????????????,many-to-one???one-to-one????????????????????????
                        // 1.relationshipName????????????
                        if (!newRelationship.getRelationshipName().equals(oldRelationship.getRelationshipName())) {
                            // ?????????????????????????????????????????????????????????????????????????????????????????????
                            JsonNode databaseChangeLog = generatedatabaseChangeLog();
                            ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                            if (!newRelationship.getCommonTable().getId().equals(oldRelationship.getCommonTable().getId())) {
                                // ????????????
                                List<String> columnNames = new ArrayList<>();
                                columnNames.add(oldRelationship.getRelationshipName() + "_id");
                                databaseChangeLogArray.add(
                                    generateChangeSetAndChanges(System.nanoTime() + "", generateDropColumn(extTableName, columnNames), null)
                                );
                                // ????????????
                                List<Map<String, String>> columns = new ArrayList<>();
                                Map<String, String> column = new HashMap<>();
                                // ??????ID??????
                                column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                                column.put("type", getFieldTypeString(CommonFieldType.LONG));
                                columns.add(column);
                                databaseChangeLogArray.add(
                                    generateChangeSetAndChanges(System.nanoTime() + "", generateAddColumn(extTableName, columns), null)
                                );
                                handlerChangelog(databaseChangeLog);
                            } else {
                                // ????????????
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
                            //2.relationshipName??????????????????
                            // ??????????????????????????????
                            if (!newRelationship.getCommonTable().getId().equals(oldRelationship.getCommonTable().getId())) {
                                //1. ????????????????????????????????????????????????
                                JsonNode databaseChangeLog = generatedatabaseChangeLog();
                                ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                                List<String> columnNames = new ArrayList<>();
                                columnNames.add(oldRelationship.getRelationshipName() + "_id");
                                databaseChangeLogArray.add(
                                    generateChangeSetAndChanges(System.nanoTime() + "", generateDropColumn(extTableName, columnNames), null)
                                );
                                //2. todo ?????????????????????????????????
                                List<Map<String, String>> columns = new ArrayList<>();
                                Map<String, String> column = new HashMap<>();
                                // ??????ID??????
                                column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                                column.put("type", getFieldTypeString(CommonFieldType.LONG));
                                columns.add(column);
                                databaseChangeLogArray.add(
                                    generateChangeSetAndChanges(System.nanoTime() + "", generateAddColumn(extTableName, columns), null)
                                );
                                handlerChangelog(databaseChangeLog);
                            }
                        }
                    } else { // to-many??????
                        // 1.??????????????????
                        JsonNode databaseChangeLog = generatedatabaseChangeLog();
                        ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                        List<String> columnNames = new ArrayList<>();
                        columnNames.add(oldRelationship.getRelationshipName() + "_id");
                        databaseChangeLogArray.add(
                            generateChangeSetAndChanges(System.nanoTime() + "", generateDropColumn(extTableName, columnNames), null)
                        );
                        // 2.???????????????????????????
                        // ??????????????????????????????
                        String joinTableName =
                            newRelationship.getCommonTable().getTableName() +
                            "_" +
                            StrUtil.toUnderlineCase(newRelationship.getRelationshipName());

                        List<Map<String, String>> columns = new ArrayList<>();
                        Map<String, String> column = new HashMap<>();
                        // ??????ID??????
                        column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                        column.put("type", getFieldTypeString(CommonFieldType.LONG));
                        columns.add(column);
                        column = new HashMap<>();
                        // ??????????????????
                        column.put("name", StrUtil.toUnderlineCase(newRelationship.getOtherEntityName()) + "_id");
                        column.put("type", getFieldTypeString(CommonFieldType.LONG));
                        columns.add(column);
                        databaseChangeLogArray.add(
                            generateChangeSetAndChanges(System.nanoTime() + "", generateCreateTable(joinTableName, columns), null)
                        );
                        handlerChangelog(databaseChangeLog);
                    }
                } else { // ?????????-to-many??????
                    // ????????????????????????
                    if (
                        newRelationship.getRelationshipType().equals(RelationshipType.ONE_TO_MANY) ||
                        newRelationship.getRelationshipType().equals(RelationshipType.MANY_TO_MANY)
                    ) {
                        // ???????????????,one-to-many???many-to-many????????????????????????
                        // 1.relationshipName????????????
                        if (!newRelationship.getRelationshipName().equals(oldRelationship.getRelationshipName())) {
                            if (!newRelationship.getCommonTable().getId().equals(oldRelationship.getCommonTable().getId())) {
                                //1. ????????????????????????????????????????????????
                                String oldJoinTableName =
                                    oldRelationship.getCommonTable().getTableName() +
                                    "_" +
                                    StrUtil.toUnderlineCase(oldRelationship.getRelationshipName());
                                JsonNode databaseChangeLog = generatedatabaseChangeLog();
                                ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                                databaseChangeLogArray.add(
                                    generateChangeSetAndChanges(System.nanoTime() + "", generateDropTable(oldJoinTableName), null)
                                );
                                //2. ??????????????????????????????????????????
                                String newJoinTableName =
                                    newRelationship.getCommonTable().getTableName() +
                                    "_" +
                                    StrUtil.toUnderlineCase(newRelationship.getRelationshipName());
                                List<Map<String, String>> columns = new ArrayList<>();
                                Map<String, String> column = new HashMap<>();
                                // ??????ID??????
                                column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                                column.put("type", getFieldTypeString(CommonFieldType.LONG));
                                columns.add(column);
                                column = new HashMap<>();
                                // ??????????????????
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
                                // ?????????????????????????????????????????????????????????
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
                            //2.relationshipName??????????????????
                            // ??????????????????????????????
                            if (!newRelationship.getCommonTable().getId().equals(oldRelationship.getCommonTable().getId())) {
                                //1. ????????????????????????????????????????????????
                                String oldJoinTableName =
                                    oldRelationship.getCommonTable().getTableName() +
                                    "_" +
                                    StrUtil.toUnderlineCase(oldRelationship.getRelationshipName());
                                JsonNode databaseChangeLog = generatedatabaseChangeLog();
                                ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                                databaseChangeLogArray.add(
                                    generateChangeSetAndChanges(System.nanoTime() + "", generateDropTable(oldJoinTableName), null)
                                );
                                //2. ??????????????????????????????????????????
                                String newJoinTableName =
                                    newRelationship.getCommonTable().getTableName() +
                                    "_" +
                                    StrUtil.toUnderlineCase(newRelationship.getRelationshipName());
                                List<Map<String, String>> columns = new ArrayList<>();
                                Map<String, String> column = new HashMap<>();
                                // ??????ID??????
                                column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                                column.put("type", getFieldTypeString(CommonFieldType.LONG));
                                columns.add(column);
                                column = new HashMap<>();
                                // ??????????????????
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
                    } else { // ??????to-one??????
                        // ?????????????????????
                        // 1. ??????????????????????????????
                        //1. ????????????????????????????????????????????????
                        String oldJoinTableName =
                            oldRelationship.getCommonTable().getTableName() +
                            "_" +
                            StrUtil.toUnderlineCase(oldRelationship.getRelationshipName());
                        JsonNode databaseChangeLog = generatedatabaseChangeLog();
                        ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                        databaseChangeLogArray.add(
                            generateChangeSetAndChanges(System.nanoTime() + "", generateDropTable(oldJoinTableName), null)
                        );
                        // 2. ??????many-to-one???oen-to-one???????????????
                        List<Map<String, String>> columns = new ArrayList<>();
                        Map<String, String> column = new HashMap<>();
                        // ??????ID??????
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
                // ?????????????????????????????????????????????????????????????????????????????????????????????relationshipName???????????????
                // 1.relationshipName????????????
                if (!newRelationship.getRelationshipName().equals(oldRelationship.getRelationshipName())) {
                    // ?????????????????????????????????????????????????????????????????????????????????????????????
                    if (
                        newRelationship.getRelationshipType().equals(RelationshipType.MANY_TO_ONE) ||
                        newRelationship.getRelationshipType().equals(RelationshipType.ONE_TO_ONE)
                    ) {
                        // ?????????????????????
                        if (!newRelationship.getCommonTable().getId().equals(oldRelationship.getCommonTable().getId())) {
                            // 1. ?????????
                            JsonNode databaseChangeLog = generatedatabaseChangeLog();
                            ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                            List<String> columnNames = new ArrayList<>();
                            columnNames.add(oldRelationship.getRelationshipName() + "_id");
                            databaseChangeLogArray.add(
                                generateChangeSetAndChanges(System.nanoTime() + "", generateDropColumn(extTableName, columnNames), null)
                            );
                            // 2. ????????????
                            List<Map<String, String>> columns = new ArrayList<>();
                            Map<String, String> column = new HashMap<>();
                            // ??????ID??????
                            column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                            column.put("type", getFieldTypeString(CommonFieldType.LONG));
                            columns.add(column);
                            databaseChangeLogArray.add(
                                generateChangeSetAndChanges(System.nanoTime() + "", generateAddColumn(extTableName, columns), null)
                            );
                            handlerChangelog(databaseChangeLog);
                        } else {
                            // ???????????????
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
                    } else { // to-many??????
                        if (!newRelationship.getCommonTable().getId().equals(oldRelationship.getCommonTable().getId())) {
                            // 1.??????????????????????????????
                            String oldJoinTableName =
                                oldRelationship.getCommonTable().getTableName() +
                                "_" +
                                StrUtil.toUnderlineCase(oldRelationship.getRelationshipName());
                            JsonNode databaseChangeLog = generatedatabaseChangeLog();
                            ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                            databaseChangeLogArray.add(
                                generateChangeSetAndChanges(System.nanoTime() + "", generateDropTable(oldJoinTableName), null)
                            );
                            // 2.?????????????????????
                            String newJoinTableName =
                                newRelationship.getCommonTable().getTableName() +
                                "_" +
                                StrUtil.toUnderlineCase(newRelationship.getRelationshipName());
                            List<Map<String, String>> columns = new ArrayList<>();
                            Map<String, String> column = new HashMap<>();
                            // ??????ID??????
                            column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                            column.put("type", getFieldTypeString(CommonFieldType.LONG));
                            columns.add(column);
                            column = new HashMap<>();
                            // ??????????????????
                            column.put("name", StrUtil.toUnderlineCase(newRelationship.getOtherEntityName()) + "_id");
                            column.put("type", getFieldTypeString(CommonFieldType.LONG));
                            columns.add(column);
                            databaseChangeLogArray.add(
                                generateChangeSetAndChanges(System.nanoTime() + "", generateCreateTable(newJoinTableName, columns), null)
                            );
                            handlerChangelog(databaseChangeLog);
                        } else { // ??????????????????
                            // ???????????????
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
                    //2.relationshipName??????????????????
                    // ??????????????????????????????
                    if (!newRelationship.getCommonTable().getId().equals(oldRelationship.getCommonTable().getId())) {
                        if (
                            newRelationship.getRelationshipType().equals(RelationshipType.ONE_TO_ONE) ||
                            newRelationship.getRelationshipType().equals(RelationshipType.MANY_TO_ONE)
                        ) {
                            //1. ???????????????????????????????????????????????????
                            JsonNode databaseChangeLog = generatedatabaseChangeLog();
                            ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                            List<String> columnNames = new ArrayList<>();
                            columnNames.add(oldRelationship.getRelationshipName() + "_id");
                            databaseChangeLogArray.add(
                                generateChangeSetAndChanges(System.nanoTime() + "", generateDropColumn(extTableName, columnNames), null)
                            );
                            //2. ?????????????????????????????????
                            List<Map<String, String>> columns = new ArrayList<>();
                            Map<String, String> column = new HashMap<>();
                            // ??????ID??????
                            column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                            column.put("type", getFieldTypeString(CommonFieldType.LONG));
                            columns.add(column);
                            databaseChangeLogArray.add(
                                generateChangeSetAndChanges(System.nanoTime() + "", generateAddColumn(extTableName, columns), null)
                            );
                            handlerChangelog(databaseChangeLog);
                        } else {
                            //1. ???????????????????????????????????????????????????
                            String oldJoinTableName =
                                oldRelationship.getCommonTable().getTableName() +
                                "_" +
                                StrUtil.toUnderlineCase(oldRelationship.getRelationshipName());
                            JsonNode databaseChangeLog = generatedatabaseChangeLog();
                            ArrayNode databaseChangeLogArray = (ArrayNode) databaseChangeLog.get("databaseChangeLog");
                            databaseChangeLogArray.add(
                                generateChangeSetAndChanges(System.nanoTime() + "", generateDropTable(oldJoinTableName), null)
                            );
                            //2. ????????????????????????
                            String newJoinTableName =
                                newRelationship.getCommonTable().getTableName() +
                                "_" +
                                StrUtil.toUnderlineCase(newRelationship.getRelationshipName());
                            List<Map<String, String>> columns = new ArrayList<>();
                            Map<String, String> column = new HashMap<>();
                            // ??????ID??????
                            column.put("name", StrUtil.toUnderlineCase(newRelationship.getRelationshipName()) + "_id");
                            column.put("type", getFieldTypeString(CommonFieldType.LONG));
                            columns.add(column);
                            column = new HashMap<>();
                            // ??????????????????
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
