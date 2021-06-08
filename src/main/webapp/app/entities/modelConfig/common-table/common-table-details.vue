<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="commonTable">
        <h2 class="jh-entity-heading">
          <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.detail.title')">CommonTable</span> {{ commonTable.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.id')">ID</span>
          </dt>
          <dd>
            <span>{{ commonTable.id }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.name')">Name</span>
          </dt>
          <dd>
            <span>{{ commonTable.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.entityName')">Entity Name</span>
          </dt>
          <dd>
            <span>{{ commonTable.entityName }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.tableName')">Table Name</span>
          </dt>
          <dd>
            <span>{{ commonTable.tableName }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.system')">System</span>
          </dt>
          <dd>
            <span>{{ commonTable.system }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.clazzName')">Clazz Name</span>
          </dt>
          <dd>
            <span>{{ commonTable.clazzName }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.generated')">Generated</span>
          </dt>
          <dd>
            <span>{{ commonTable.generated }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.creatAt')">Creat At</span>
          </dt>
          <dd>
            <span v-if="commonTable.creatAt">{{ $d(Date.parse(commonTable.creatAt), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.generateAt')">Generate At</span>
          </dt>
          <dd>
            <span v-if="commonTable.generateAt">{{ $d(Date.parse(commonTable.generateAt), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.generateClassAt')">Generate Class At</span>
          </dt>
          <dd>
            <span v-if="commonTable.generateClassAt">{{ $d(Date.parse(commonTable.generateClassAt), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.description')">Description</span>
          </dt>
          <dd>
            <span>{{ commonTable.description }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.treeTable')">Tree Table</span>
          </dt>
          <dd>
            <span>{{ commonTable.treeTable }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.baseTableId')">Base Table Id</span>
          </dt>
          <dd>
            <span>{{ commonTable.baseTableId }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.recordActionWidth')">Record Action Width</span>
          </dt>
          <dd>
            <span>{{ commonTable.recordActionWidth }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.listConfig')">List Config</span>
          </dt>
          <dd>
            <span>{{ commonTable.listConfig }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.formConfig')">Form Config</span>
          </dt>
          <dd>
            <span>{{ commonTable.formConfig }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.editInModal')">Edit In Modal</span>
          </dt>
          <dd>
            <span>{{ commonTable.editInModal }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.metaModel')">元模型</span>
          </dt>
          <dd>
            <div v-if="commonTable.metaModelId">
              <router-link :to="{ name: 'CommonTableView', params: { commonTableId: commonTable.metaModelId } }">{{
                commonTable.metaModelName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.creator')">创建人</span>
          </dt>
          <dd>
            {{ commonTable.creatorLogin }}
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTable.businessType')">业务类型</span>
          </dt>
          <dd>
            <div v-if="commonTable.businessTypeId">
              <router-link :to="{ name: 'BusinessTypeView', params: { businessTypeId: commonTable.businessTypeId } }">{{
                commonTable.businessTypeName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="commonTable.id"
          :to="{ name: 'CommonTableEdit', params: { commonTableId: commonTable.id } }"
          tag="button"
          class="btn btn-primary"
        >
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./common-table-details.component.ts"></script>
