import { RouteConfig } from 'vue-router';
import { RouteView } from '@/layouts';
export const uploadFileRoute: RouteConfig = {
  path: 'upload-file',
  component: RouteView,
  meta: { authorities: ['ROLE_USER'], title: '上传文件' },
  children: [
    {
      path: '',
      name: 'files-upload-file-list',
      component: () => import('./upload-file.vue'),
      meta: { authorities: ['ROLE_USER'], title: '列表' },
    },
    {
      path: 'new',
      name: 'files-upload-file-new',
      component: () => import('./upload-file-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '新建' },
    },
    {
      path: ':uploadFileId/edit',
      name: 'files-upload-file-edit',
      component: () => import('./upload-file-update.vue'),
      meta: { authorities: ['ROLE_USER'], title: '编辑' },
    },
    {
      path: ':uploadFileId/detail',
      name: 'files-upload-file-detail',
      component: () => import('./upload-file-details.vue'),
      meta: { authorities: ['ROLE_USER'], title: '查看' },
    },
  ],
};
