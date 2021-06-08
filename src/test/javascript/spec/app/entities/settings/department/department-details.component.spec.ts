/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import DepartmentDetailComponent from '@/entities/settings/department/department-details.vue';
import DepartmentClass from '@/entities/settings/department/department-details.component';
import DepartmentService from '@/entities/settings/department/department.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Department Management Detail Component', () => {
    let wrapper: Wrapper<DepartmentClass>;
    let comp: DepartmentClass;
    let departmentServiceStub: SinonStubbedInstance<DepartmentService>;

    beforeEach(() => {
      departmentServiceStub = sinon.createStubInstance<DepartmentService>(DepartmentService);

      wrapper = shallowMount<DepartmentClass>(DepartmentDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { departmentService: () => departmentServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDepartment = { id: 123 };
        // @ts-ignore
        departmentServiceStub.find.resolves(foundDepartment);

        // WHEN
        comp.retrieveDepartment(123);
        await comp.$nextTick();

        // THEN
        expect(comp.department).toBe(foundDepartment);
      });
    });
  });
});
