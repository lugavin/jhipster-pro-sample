import { RouteConfig } from 'vue-router';
import { RouteView } from '@/layouts';
export const resourceCategoryRoute: RouteConfig = {
  path: 'resource-category',
  component: RouteView,
  meta: { authorities: ['ROLE_USER'], title: '资源分类' },
  children: [
    {
      path: '',
      name: 'files-resource-category-list',
      component: () => import('./resource-category.vue'),
      meta: { authorities: ['ROLE_USER'], title: '列表' },
    },
    {
      path: 'new',
      name: 'files-resource-category-new',
      component: () => import('./resource-category-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '新建' },
    },
    {
      path: ':resourceCategoryId/edit',
      name: 'files-resource-category-edit',
      component: () => import('./resource-category-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '编辑' },
    },
    {
      path: ':resourceCategoryId/detail',
      name: 'files-resource-category-detail',
      component: () => import('./resource-category-details.vue'),
      meta: { authorities: ['ROLE_USER'], title: '查看' },
    },
  ],
};
