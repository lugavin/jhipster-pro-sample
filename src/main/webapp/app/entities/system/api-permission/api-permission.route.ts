import { RouteConfig } from 'vue-router';
import { RouteView } from '@/layouts';
export const apiPermissionRoute: RouteConfig = {
  path: 'api-permission',
  component: RouteView,
  meta: { authorities: ['ROLE_USER'], title: 'API权限' },
  children: [
    {
      path: '',
      name: 'system-api-permission-list',
      component: () => import('./api-permission.vue'),
      meta: { authorities: ['ROLE_USER'], title: '列表' },
    },
    {
      path: 'new',
      name: 'system-api-permission-new',
      component: () => import('./api-permission-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '新建' },
    },
    {
      path: ':apiPermissionId/edit',
      name: 'system-api-permission-edit',
      component: () => import('./api-permission-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '编辑' },
    },
    {
      path: ':apiPermissionId/detail',
      name: 'system-api-permission-detail',
      component: () => import('./api-permission-details.vue'),
      meta: { authorities: ['ROLE_USER'], title: '查看' },
    },
  ],
};
