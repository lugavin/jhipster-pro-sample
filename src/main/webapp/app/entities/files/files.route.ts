import { RouteConfig } from 'vue-router';
import { PageView } from '@/layouts';
import { ossConfigRoute } from '@/entities/files/oss-config/oss-config.route';
import { uploadImageRoute } from '@/entities/files/upload-image/upload-image.route';
import { smsConfigRoute } from '@/entities/files/sms-config/sms-config.route';
import { resourceCategoryRoute } from '@/entities/files/resource-category/resource-category.route';
import { uploadFileRoute } from '@/entities/files/upload-file/upload-file.route';
// jhipster-needle-add-entity-to-client-root-folder-router-import - JHipster will import entities to the client root folder router here

export const filesRoute: RouteConfig = {
  path: 'files',
  component: PageView,
  name: 'files',
  meta: { authorities: ['ROLE_USER'], title: '文件管理' },
  children: [
    ossConfigRoute,
    uploadImageRoute,
    smsConfigRoute,
    resourceCategoryRoute,
    uploadFileRoute,
    // jhipster-needle-add-entity-to-client-root-folder-router - JHipster will add entities to the client root folder router here
  ],
};
