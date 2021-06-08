<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="authority">
        <h2 class="jh-entity-heading"><span v-text="$t('test037App.systemAuthority.detail.title')">Authority</span> {{ authority.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('test037App.systemAuthority.name')">Name</span>
          </dt>
          <dd>
            <span>{{ authority.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('test037App.systemAuthority.code')">Code</span>
          </dt>
          <dd>
            <span>{{ authority.code }}</span>
          </dd>
          <dt>
            <span v-text="$t('test037App.systemAuthority.info')">Info</span>
          </dt>
          <dd>
            <span>{{ authority.info }}</span>
          </dd>
          <dt>
            <span v-text="$t('test037App.systemAuthority.order')">Order</span>
          </dt>
          <dd>
            <span>{{ authority.order }}</span>
          </dd>
          <dt>
            <span v-text="$t('test037App.systemAuthority.display')">Display</span>
          </dt>
          <dd>
            <span>{{ authority.display }}</span>
          </dd>
          <dt>
            <span v-text="$t('test037App.systemAuthority.users')">用户</span>
          </dt>
          <dd>
            <span v-for="(users, i) in authority.users" :key="users.id"
              >{{ i > 0 ? ', ' : '' }}
              {{ users.login }}
            </span>
          </dd>
          <dt>
            <span v-text="$t('test037App.systemAuthority.viewPermission')">可视权限</span>
          </dt>
          <dd>
            <span v-for="(viewPermission, i) in authority.viewPermissions" :key="viewPermission.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'ViewPermissionView', params: { viewPermissionId: viewPermission.id } }">{{
                viewPermission.text
              }}</router-link>
            </span>
          </dd>
          <dt>
            <span v-text="$t('test037App.systemAuthority.parent')">上级</span>
          </dt>
          <dd>
            <div v-if="authority.parentId">
              <router-link :to="{ name: 'AuthorityView', params: { authorityId: authority.parentId } }">{{
                authority.parentName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="authority.id"
          :to="{ name: 'AuthorityEdit', params: { authorityId: authority.id } }"
          tag="button"
          class="btn btn-primary"
        >
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./authority-details.component.ts"></script>
