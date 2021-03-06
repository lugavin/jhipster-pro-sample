import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import JhiFooter from '@/core/jhi-footer/jhi-footer.vue';
import JhiFooterClass from '@/core/jhi-footer/jhi-footer.component';

import * as config from '@/shared/config/config';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);

describe('JhiFooter', () => {
  let jhiFooter: JhiFooterClass;
  let wrapper: Wrapper<JhiFooterClass>;

  beforeEach(() => {
    wrapper = shallowMount<JhiFooterClass>(JhiFooter, {
      i18n,
      localVue,
    });
    jhiFooter = wrapper.vm;
  });

  it('should be a Vue instance', async () => {
    expect(wrapper.isVueInstance()).toBeTruthy();
  });
});
