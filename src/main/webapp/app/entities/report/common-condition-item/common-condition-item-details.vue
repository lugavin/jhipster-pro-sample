<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="commonConditionItem">
        <h2 class="jh-entity-heading">
          <span v-text="$t('jhipsterApplicationApp.reportCommonConditionItem.detail.title')">CommonConditionItem</span>
          {{ commonConditionItem.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportCommonConditionItem.id')">ID</span>
          </dt>
          <dd>
            <span>{{ commonConditionItem.id }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportCommonConditionItem.prefix')">Prefix</span>
          </dt>
          <dd>
            <span>{{ commonConditionItem.prefix }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportCommonConditionItem.fieldName')">Field Name</span>
          </dt>
          <dd>
            <span>{{ commonConditionItem.fieldName }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportCommonConditionItem.fieldType')">Field Type</span>
          </dt>
          <dd>
            <span>{{ commonConditionItem.fieldType }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportCommonConditionItem.operator')">Operator</span>
          </dt>
          <dd>
            <span>{{ commonConditionItem.operator }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportCommonConditionItem.value')">Value</span>
          </dt>
          <dd>
            <span>{{ commonConditionItem.value }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportCommonConditionItem.suffix')">Suffix</span>
          </dt>
          <dd>
            <span>{{ commonConditionItem.suffix }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportCommonConditionItem.order')">Order</span>
          </dt>
          <dd>
            <span>{{ commonConditionItem.order }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportCommonConditionItem.commonCondition')">查询</span>
          </dt>
          <dd>
            <div v-if="commonConditionItem.commonConditionId">
              <router-link :to="{ name: 'CommonConditionView', params: { commonConditionId: commonConditionItem.commonConditionId } }">{{
                commonConditionItem.commonConditionName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="commonConditionItem.id"
          :to="{ name: 'CommonConditionItemEdit', params: { commonConditionItemId: commonConditionItem.id } }"
          tag="button"
          class="btn btn-primary"
        >
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./common-condition-item-details.component.ts"></script>
