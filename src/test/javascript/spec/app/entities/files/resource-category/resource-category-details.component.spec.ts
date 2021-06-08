/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import ResourceCategoryDetailComponent from '@/entities/files/resource-category/resource-category-details.vue';
import ResourceCategoryClass from '@/entities/files/resource-category/resource-category-details.component';
import ResourceCategoryService from '@/entities/files/resource-category/resource-category.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('ResourceCategory Management Detail Component', () => {
    let wrapper: Wrapper<ResourceCategoryClass>;
    let comp: ResourceCategoryClass;
    let resourceCategoryServiceStub: SinonStubbedInstance<ResourceCategoryService>;

    beforeEach(() => {
      resourceCategoryServiceStub = sinon.createStubInstance<ResourceCategoryService>(ResourceCategoryService);

      wrapper = shallowMount<ResourceCategoryClass>(ResourceCategoryDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { resourceCategoryService: () => resourceCategoryServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundResourceCategory = { id: 123 };
        // @ts-ignore
        resourceCategoryServiceStub.find.resolves(foundResourceCategory);

        // WHEN
        comp.retrieveResourceCategory(123);
        await comp.$nextTick();

        // THEN
        expect(comp.resourceCategory).toBe(foundResourceCategory);
      });
    });
  });
});
