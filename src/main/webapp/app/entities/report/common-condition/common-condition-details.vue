<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="commonCondition">
        <h2 class="jh-entity-heading">
          <span v-text="$t('jhipsterApplicationApp.reportCommonCondition.detail.title')">CommonCondition</span> {{ commonCondition.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportCommonCondition.id')">ID</span>
          </dt>
          <dd>
            <span>{{ commonCondition.id }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportCommonCondition.name')">Name</span>
          </dt>
          <dd>
            <span>{{ commonCondition.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportCommonCondition.description')">Description</span>
          </dt>
          <dd>
            <span>{{ commonCondition.description }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportCommonCondition.lastModifiedTime')">Last Modified Time</span>
          </dt>
          <dd>
            <span v-if="commonCondition.lastModifiedTime">{{ $d(Date.parse(commonCondition.lastModifiedTime), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.reportCommonCondition.commonTable')">所属模型</span>
          </dt>
          <dd>
            <div v-if="commonCondition.commonTableId">
              <router-link :to="{ name: 'CommonTableView', params: { commonTableId: commonCondition.commonTableId } }">{{
                commonCondition.commonTableName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="commonCondition.id"
          :to="{ name: 'CommonConditionEdit', params: { commonConditionId: commonCondition.id } }"
          tag="button"
          class="btn btn-primary"
        >
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./common-condition-details.component.ts"></script>
