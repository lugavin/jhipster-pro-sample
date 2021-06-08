/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import AdministrativeDivisionComponent from '@/entities/settings/administrative-division/administrative-division.vue';
import AdministrativeDivisionClass from '@/entities/settings/administrative-division/administrative-division.component';
import AdministrativeDivisionService from '@/entities/settings/administrative-division/administrative-division.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-alert', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
  },
};

describe('Component Tests', () => {
  describe('AdministrativeDivision Management Component', () => {
    let wrapper: Wrapper<AdministrativeDivisionClass>;
    let comp: AdministrativeDivisionClass;
    let administrativeDivisionServiceStub: SinonStubbedInstance<AdministrativeDivisionService>;

    beforeEach(() => {
      administrativeDivisionServiceStub = sinon.createStubInstance<AdministrativeDivisionService>(AdministrativeDivisionService);
      // @ts-ignore
      administrativeDivisionServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<AdministrativeDivisionClass>(AdministrativeDivisionComponent, {
        store,
        i18n,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          alertService: () => new AlertService(store),
          administrativeDivisionService: () => administrativeDivisionServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    it('should be a Vue instance', () => {
      expect(wrapper.isVueInstance()).toBeTruthy();
    });

    it('Should call load all on init', async () => {
      // GIVEN
      // @ts-ignore
      administrativeDivisionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadAll();
      await comp.$nextTick();

      // THEN
      expect(administrativeDivisionServiceStub.retrieve.called).toBeTruthy();
      expect(comp.administrativeDivisions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      // @ts-ignore
      administrativeDivisionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(administrativeDivisionServiceStub.retrieve.called).toBeTruthy();
      expect(comp.administrativeDivisions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      administrativeDivisionServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(administrativeDivisionServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      administrativeDivisionServiceStub.retrieve.reset();
      // @ts-ignore
      administrativeDivisionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(administrativeDivisionServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.administrativeDivisions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // GIVEN
      comp.propOrder = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      // @ts-ignore
      administrativeDivisionServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeById(123);
      await comp.$nextTick();

      // THEN
      expect(administrativeDivisionServiceStub.delete.called).toBeTruthy();
      expect(administrativeDivisionServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
