import { RouteConfig } from 'vue-router';
import { RouteView } from '@/layouts';
export const authorityRoute: RouteConfig = {
  path: 'authority',
  component: RouteView,
  meta: { authorities: ['ROLE_USER'], title: '角色' },
  children: [
    {
      path: '',
      name: 'system-authority-list',
      component: () => import('./authority.vue'),
      meta: { authorities: ['ROLE_USER'], title: '列表' },
    },
    {
      path: 'new',
      name: 'system-authority-new',
      component: () => import('./authority-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '新建' },
    },
    {
      path: ':authorityId/edit',
      name: 'system-authority-edit',
      component: () => import('./authority-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '编辑' },
    },
    {
      path: ':authorityId/detail',
      name: 'system-authority-detail',
      component: () => import('./authority-details.vue'),
      meta: { authorities: ['ROLE_USER'], title: '查看' },
    },
  ],
};
