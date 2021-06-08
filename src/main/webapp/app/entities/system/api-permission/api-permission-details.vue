<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="apiPermission">
        <h2 class="jh-entity-heading">
          <span v-text="$t('jhipsterApplicationApp.systemApiPermission.detail.title')">ApiPermission</span> {{ apiPermission.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jhipsterApplicationApp.systemApiPermission.id')">ID</span>
          </dt>
          <dd>
            <span>{{ apiPermission.id }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.systemApiPermission.serviceName')">Service Name</span>
          </dt>
          <dd>
            <span>{{ apiPermission.serviceName }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.systemApiPermission.name')">Name</span>
          </dt>
          <dd>
            <span>{{ apiPermission.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.systemApiPermission.code')">Code</span>
          </dt>
          <dd>
            <span>{{ apiPermission.code }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.systemApiPermission.description')">Description</span>
          </dt>
          <dd>
            <span>{{ apiPermission.description }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.systemApiPermission.type')">Type</span>
          </dt>
          <dd>
            <span v-text="$t('jhipsterApplicationApp.ApiPermissionType.' + apiPermission.type)">{{ apiPermission.type }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.systemApiPermission.method')">Method</span>
          </dt>
          <dd>
            <span>{{ apiPermission.method }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.systemApiPermission.url')">Url</span>
          </dt>
          <dd>
            <span>{{ apiPermission.url }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.systemApiPermission.parent')">上级</span>
          </dt>
          <dd>
            <div v-if="apiPermission.parentId">
              <router-link :to="{ name: 'ApiPermissionView', params: { apiPermissionId: apiPermission.parentId } }">{{
                apiPermission.parentName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="apiPermission.id"
          :to="{ name: 'ApiPermissionEdit', params: { apiPermissionId: apiPermission.id } }"
          tag="button"
          class="btn btn-primary"
        >
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./api-permission-details.component.ts"></script>
