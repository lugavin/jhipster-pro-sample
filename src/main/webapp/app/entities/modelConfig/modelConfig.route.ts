import { RouteConfig } from 'vue-router';
import { PageView } from '@/layouts';
import { commonTableFieldRoute } from '@/entities/modelConfig/common-table-field/common-table-field.route';
import { commonTableRelationshipRoute } from '@/entities/modelConfig/common-table-relationship/common-table-relationship.route';
import { commonTableRoute } from '@/entities/modelConfig/common-table/common-table.route';
// jhipster-needle-add-entity-to-client-root-folder-router-import - JHipster will import entities to the client root folder router here

export const modelConfigRoute: RouteConfig = {
  path: 'modelConfig',
  component: PageView,
  name: 'modelConfig',
  meta: { authorities: ['ROLE_USER'], title: '模型管理' },
  children: [
    commonTableFieldRoute,
    commonTableRelationshipRoute,
    commonTableRoute,
    // jhipster-needle-add-entity-to-client-root-folder-router - JHipster will add entities to the client root folder router here
  ],
};
