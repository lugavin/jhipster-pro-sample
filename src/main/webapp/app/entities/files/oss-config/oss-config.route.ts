import { RouteConfig } from 'vue-router';
import { RouteView } from '@/layouts';
export const ossConfigRoute: RouteConfig = {
  path: 'oss-config',
  component: RouteView,
  meta: { authorities: ['ROLE_USER'], title: '对象存储配置' },
  children: [
    {
      path: '',
      name: 'files-oss-config-list',
      component: () => import('./oss-config.vue'),
      meta: { authorities: ['ROLE_USER'], title: '列表' },
    },
    {
      path: 'new',
      name: 'files-oss-config-new',
      component: () => import('./oss-config-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '新建' },
    },
    {
      path: ':ossConfigId/edit',
      name: 'files-oss-config-edit',
      component: () => import('./oss-config-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '编辑' },
    },
    {
      path: ':ossConfigId/detail',
      name: 'files-oss-config-detail',
      component: () => import('./oss-config-details.vue'),
      meta: { authorities: ['ROLE_USER'], title: '查看' },
    },
  ],
};
