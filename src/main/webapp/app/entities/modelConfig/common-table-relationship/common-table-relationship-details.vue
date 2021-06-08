<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="commonTableRelationship">
        <h2 class="jh-entity-heading">
          <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.detail.title')">CommonTableRelationship</span>
          {{ commonTableRelationship.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.id')">ID</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.id }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.name')">Name</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.relationshipType')">Relationship Type</span>
          </dt>
          <dd>
            <span v-text="$t('jhipsterApplicationApp.RelationshipType.' + commonTableRelationship.relationshipType)">{{
              commonTableRelationship.relationshipType
            }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.sourceType')">Source Type</span>
          </dt>
          <dd>
            <span v-text="$t('jhipsterApplicationApp.SourceType.' + commonTableRelationship.sourceType)">{{
              commonTableRelationship.sourceType
            }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.otherEntityField')">Other Entity Field</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.otherEntityField }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.otherEntityName')">Other Entity Name</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.otherEntityName }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.relationshipName')">Relationship Name</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.relationshipName }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.otherEntityRelationshipName')"
              >Other Entity Relationship Name</span
            >
          </dt>
          <dd>
            <span>{{ commonTableRelationship.otherEntityRelationshipName }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.columnWidth')">Column Width</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.columnWidth }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.order')">Order</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.order }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.fixed')">Fixed</span>
          </dt>
          <dd>
            <span v-text="$t('jhipsterApplicationApp.FixedType.' + commonTableRelationship.fixed)">{{
              commonTableRelationship.fixed
            }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.editInList')">Edit In List</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.editInList }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.enableFilter')">Enable Filter</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.enableFilter }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.hideInList')">Hide In List</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.hideInList }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.hideInForm')">Hide In Form</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.hideInForm }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.system')">System</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.system }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.fontColor')">Font Color</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.fontColor }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.backgroundColor')">Background Color</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.backgroundColor }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.help')">Help</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.help }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.ownerSide')">Owner Side</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.ownerSide }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.dataName')">Data Name</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.dataName }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.webComponentType')">Web Component Type</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.webComponentType }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.otherEntityIsTree')">Other Entity Is Tree</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.otherEntityIsTree }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.showInFilterTree')">Show In Filter Tree</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.showInFilterTree }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.dataDictionaryCode')">Data Dictionary Code</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.dataDictionaryCode }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.clientReadOnly')">Client Read Only</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.clientReadOnly }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.endUsed')">End Used</span>
          </dt>
          <dd>
            <span v-text="$t('jhipsterApplicationApp.EndUsedType.' + commonTableRelationship.endUsed)">{{
              commonTableRelationship.endUsed
            }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.options')">Options</span>
          </dt>
          <dd>
            <span>{{ commonTableRelationship.options }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.relationEntity')">关联实体</span>
          </dt>
          <dd>
            <div v-if="commonTableRelationship.relationEntityId">
              <router-link :to="{ name: 'CommonTableView', params: { commonTableId: commonTableRelationship.relationEntityId } }">{{
                commonTableRelationship.relationEntityName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.dataDictionaryNode')">字典表节点</span>
          </dt>
          <dd>
            <div v-if="commonTableRelationship.dataDictionaryNodeId">
              <router-link
                :to="{ name: 'DataDictionaryView', params: { dataDictionaryId: commonTableRelationship.dataDictionaryNodeId } }"
                >{{ commonTableRelationship.dataDictionaryNodeName }}</router-link
              >
            </div>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.metaModel')">元模型</span>
          </dt>
          <dd>
            <div v-if="commonTableRelationship.metaModelId">
              <router-link :to="{ name: 'CommonTableView', params: { commonTableId: commonTableRelationship.metaModelId } }">{{
                commonTableRelationship.metaModelName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.modelConfigCommonTableRelationship.commonTable')">所属表</span>
          </dt>
          <dd>
            <div v-if="commonTableRelationship.commonTableId">
              <router-link :to="{ name: 'CommonTableView', params: { commonTableId: commonTableRelationship.commonTableId } }">{{
                commonTableRelationship.commonTableName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="commonTableRelationship.id"
          :to="{ name: 'CommonTableRelationshipEdit', params: { commonTableRelationshipId: commonTableRelationship.id } }"
          tag="button"
          class="btn btn-primary"
        >
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./common-table-relationship-details.component.ts"></script>
