import { RouteConfig } from 'vue-router';
import { PageView } from '@/layouts';
import { uReportFileRoute } from '@/entities/report/u-report-file/u-report-file.route';
import { statisticsApiRoute } from '@/entities/report/statistics-api/statistics-api.route';
import { commonConditionItemRoute } from '@/entities/report/common-condition-item/common-condition-item.route';
import { commonConditionRoute } from '@/entities/report/common-condition/common-condition.route';
// jhipster-needle-add-entity-to-client-root-folder-router-import - JHipster will import entities to the client root folder router here

export const reportRoute: RouteConfig = {
  path: 'report',
  component: PageView,
  name: 'report',
  meta: { authorities: ['ROLE_USER'], title: '数据可视' },
  children: [
    uReportFileRoute,
    statisticsApiRoute,
    commonConditionItemRoute,
    commonConditionRoute,
    // jhipster-needle-add-entity-to-client-root-folder-router - JHipster will add entities to the client root folder router here
  ],
};
