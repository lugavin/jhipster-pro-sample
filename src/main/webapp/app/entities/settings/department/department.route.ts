import { RouteConfig } from 'vue-router';
import { RouteView } from '@/layouts';
export const departmentRoute: RouteConfig = {
  path: 'department',
  component: RouteView,
  meta: { authorities: ['ROLE_USER'], title: '部门' },
  children: [
    {
      path: '',
      name: 'settings-department-list',
      component: () => import('./department.vue'),
      meta: { authorities: ['ROLE_USER'], title: '列表' },
    },
    {
      path: 'new',
      name: 'settings-department-new',
      component: () => import('./department-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '新建' },
    },
    {
      path: ':departmentId/edit',
      name: 'settings-department-edit',
      component: () => import('./department-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '编辑' },
    },
    {
      path: ':departmentId/detail',
      name: 'settings-department-detail',
      component: () => import('./department-details.vue'),
      meta: { authorities: ['ROLE_USER'], title: '查看' },
    },
  ],
};
