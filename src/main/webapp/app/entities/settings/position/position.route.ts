import { RouteConfig } from 'vue-router';
import { RouteView } from '@/layouts';
export const positionRoute: RouteConfig = {
  path: 'position',
  component: RouteView,
  meta: { authorities: ['ROLE_USER'], title: '岗位' },
  children: [
    {
      path: '',
      name: 'settings-position-list',
      component: () => import('./position.vue'),
      meta: { authorities: ['ROLE_USER'], title: '列表' },
    },
    {
      path: 'new',
      name: 'settings-position-new',
      component: () => import('./position-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '新建' },
    },
    {
      path: ':positionId/edit',
      name: 'settings-position-edit',
      component: () => import('./position-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '编辑' },
    },
    {
      path: ':positionId/detail',
      name: 'settings-position-detail',
      component: () => import('./position-details.vue'),
      meta: { authorities: ['ROLE_USER'], title: '查看' },
    },
  ],
};
