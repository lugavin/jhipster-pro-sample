<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="dataDictionary">
        <h2 class="jh-entity-heading">
          <span v-text="$t('jhipsterApplicationApp.settingsDataDictionary.detail.title')">DataDictionary</span> {{ dataDictionary.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsDataDictionary.id')">ID</span>
          </dt>
          <dd>
            <span>{{ dataDictionary.id }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsDataDictionary.name')">Name</span>
          </dt>
          <dd>
            <span>{{ dataDictionary.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsDataDictionary.code')">Code</span>
          </dt>
          <dd>
            <span>{{ dataDictionary.code }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsDataDictionary.description')">Description</span>
          </dt>
          <dd>
            <span>{{ dataDictionary.description }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsDataDictionary.fontColor')">Font Color</span>
          </dt>
          <dd>
            <span>{{ dataDictionary.fontColor }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsDataDictionary.backgroundColor')">Background Color</span>
          </dt>
          <dd>
            <span>{{ dataDictionary.backgroundColor }}</span>
          </dd>
          <dt>
            <span v-text="$t('jhipsterApplicationApp.settingsDataDictionary.parent')">上级节点</span>
          </dt>
          <dd>
            <div v-if="dataDictionary.parentId">
              <router-link :to="{ name: 'DataDictionaryView', params: { dataDictionaryId: dataDictionary.parentId } }">{{
                dataDictionary.parentName
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="dataDictionary.id"
          :to="{ name: 'DataDictionaryEdit', params: { dataDictionaryId: dataDictionary.id } }"
          tag="button"
          class="btn btn-primary"
        >
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./data-dictionary-details.component.ts"></script>
