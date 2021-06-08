import { RouteConfig } from 'vue-router';
import { RouteView } from '@/layouts';
export const businessTypeRoute: RouteConfig = {
  path: 'business-type',
  component: RouteView,
  meta: { authorities: ['ROLE_USER'], title: '业务类型' },
  children: [
    {
      path: '',
      name: 'settings-business-type-list',
      component: () => import('./business-type.vue'),
      meta: { authorities: ['ROLE_USER'], title: '列表' },
    },
    {
      path: 'new',
      name: 'settings-business-type-new',
      component: () => import('./business-type-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '新建' },
    },
    {
      path: ':businessTypeId/edit',
      name: 'settings-business-type-edit',
      component: () => import('./business-type-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '编辑' },
    },
    {
      path: ':businessTypeId/detail',
      name: 'settings-business-type-detail',
      component: () => import('./business-type-details.vue'),
      meta: { authorities: ['ROLE_USER'], title: '查看' },
    },
  ],
};
