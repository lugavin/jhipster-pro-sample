<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="总销售额" total="￥126,560">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <trend flag="up" style="margin-right: 16px">
              <span slot="term">周同比</span>
              12%
            </trend>
            <trend flag="down">
              <span slot="term">日同比</span>
              11%
            </trend>
          </div>
          <template slot="footer">日均销售额<span>￥ 234.56</span></template>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="访问量" :total="8846 | NumberFormat">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div class="chart-card-content-analysis">
            <v-chart :options="accessStatics" :autoresize="true" />
          </div>
          <template slot="footer"
            >日访问量<span> {{ '1234' | NumberFormat }}</span></template
          >
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="支付笔数" :total="6560 | NumberFormat">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div class="chart-card-content-analysis">
            <v-chart :options="searchDataSmooth" :autoresize="true" />
          </div>
          <template slot="footer">转化率 <span>60%</span></template>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="运营活动效果" total="78%">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-progress color="rgb(19, 194, 194)" :target="80" :percentage="78" height="8px" />
          </div>
          <template slot="footer">
            <trend flag="down" style="margin-right: 16px">
              <span slot="term">同周比</span>
              12%
            </trend>
            <trend flag="up">
              <span slot="term">日环比</span>
              80%
            </trend>
          </template>
        </chart-card>
      </a-col>
    </a-row>

    <a-card :loading="loading" :bordered="false" :body-style="{ padding: '0' }">
      <div class="salesCard">
        <a-tabs default-active-key="1" size="large" :tab-bar-style="{ marginBottom: '24px', paddingLeft: '16px' }">
          <div class="extra-wrapper" slot="tabBarExtraContent">
            <div class="extra-item">
              <a>今日</a>
              <a>本周</a>
              <a>本月</a>
              <a>本年</a>
            </div>
            <a-range-picker :style="{ width: '256px' }" />
          </div>
          <a-tab-pane loading="true" tab="销售额" key="1">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <div :style="{ padding: '0 0 32px 32px' }">
                  <h4 :style="{ marginBottom: '20px' }">销售额排行</h4>
                  <v-chart :options="polar" :autoresize="true" />
                </div>
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <rank-list title="门店销售排行榜" :list="rankList" />
              </a-col>
            </a-row>
          </a-tab-pane>
          <a-tab-pane tab="访问量" key="2">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <div :style="{ padding: '0 0 32px 32px' }">
                  <h4 :style="{ marginBottom: '20px' }">销售额趋势</h4>
                  <v-chart :options="polar2" :autoresize="true" />
                </div>
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <rank-list title="门店销售排行榜" :list="rankList" />
              </a-col>
            </a-row>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-card>

    <div class="antd-pro-pages-dashboard-analysis-twoColLayout" :class="isDesktop() ? 'desktop' : ''">
      <a-row :gutter="24" type="flex" :style="{ marginTop: '24px' }">
        <a-col :xl="12" :lg="24" :md="24" :sm="24" :xs="24">
          <a-card :loading="loading" :bordered="false" title="线上热门搜索" :style="{ height: '100%' }">
            <a-dropdown :trigger="['click']" placement="bottomLeft" slot="extra">
              <a class="ant-dropdown-link" href="#">
                <a-icon type="ellipsis" />
              </a>
              <a-menu slot="overlay">
                <a-menu-item>
                  <a href="javascript:;">操作一</a>
                </a-menu-item>
                <a-menu-item>
                  <a href="javascript:;">操作二</a>
                </a-menu-item>
              </a-menu>
            </a-dropdown>
            <a-row :gutter="68">
              <a-col :xs="24" :sm="12" :style="{ marginBottom: ' 24px' }">
                <number-info :total="12321" :sub-total="17.1">
                  <span slot="subtitle">
                    <span>搜索用户数</span>
                    <a-tooltip title="指标说明" slot="action">
                      <a-icon type="info-circle-o" :style="{ marginLeft: '8px' }" />
                    </a-tooltip>
                  </span>
                </number-info>
                <!-- miniChart -->
                <div style="height: 45px">
                  <div class="chart-card-content-search">
                    <v-chart :options="searchDataSmooth" :autoresize="true" />
                  </div>
                </div>
              </a-col>
              <a-col :xs="24" :sm="12" :style="{ marginBottom: ' 24px' }">
                <number-info :total="2.7" :sub-total="26.2" status="down">
                  <span slot="subtitle">
                    <span>人均搜索次数</span>
                    <a-tooltip title="指标说明" slot="action">
                      <a-icon type="info-circle-o" :style="{ marginLeft: '8px' }" />
                    </a-tooltip>
                  </span>
                </number-info>
                <!-- miniChart -->
                <div style="height: 45px">
                  <div class="chart-card-content-search">
                    <v-chart :options="searchDataSmooth" :autoresize="true" />
                  </div>
                </div>
              </a-col>
            </a-row>
            <div class="ant-table-wrapper">
              <a-table row-key="index" size="small" :columns="searchTableColumns" :dataSource="searchData" :pagination="{ pageSize: 5 }">
                <span slot="range" slot-scope="text, record">
                  <trend :flag="record.status === 0 ? 'up' : 'down'"> {{ text }}% </trend>
                </span>
              </a-table>
            </div>
          </a-card>
        </a-col>
        <a-col :xl="12" :lg="24" :md="24" :sm="24" :xs="24">
          <a-card
            class="antd-pro-pages-dashboard-analysis-salesCard"
            :loading="loading"
            :bordered="false"
            title="销售额类别占比"
            :style="{ height: '100%' }"
          >
            <div slot="extra" style="height: inherit">
              <!-- style="bottom: 12px;display: inline-block;" -->
              <span class="dashboard-analysis-iconGroup">
                <a-dropdown :trigger="['click']" placement="bottomLeft">
                  <a-icon type="ellipsis" class="ant-dropdown-link" />
                  <a-menu slot="overlay">
                    <a-menu-item>
                      <a href="javascript:;">操作一</a>
                    </a-menu-item>
                    <a-menu-item>
                      <a href="javascript:;">操作二</a>
                    </a-menu-item>
                  </a-menu>
                </a-dropdown>
              </span>
              <div class="analysis-salesTypeRadio">
                <a-radio-group defaultValue="a">
                  <a-radio-button value="a">全部渠道</a-radio-button>
                  <a-radio-button value="b">线上</a-radio-button>
                  <a-radio-button value="c">门店</a-radio-button>
                </a-radio-group>
              </div>
            </div>
            <h4>销售额</h4>
            <div>
              <!-- style="width: calc(100% - 240px);" -->
              <div>
                <v-chart :options="pie" :autoresize="true" />
              </div>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script>
import moment from 'moment';
import { ChartCard, MiniProgress, RankList, Trend, NumberInfo } from '@/components';
import { mixinDevice } from '@/utils/mixin';

const barData = [];
const barData2 = [];
for (let i = 0; i < 12; i += 1) {
  barData.push({
    x: `${i + 1}月`,
    y: Math.floor(Math.random() * 1000) + 200,
  });
  barData2.push({
    x: `${i + 1}月`,
    y: Math.floor(Math.random() * 1000) + 200,
  });
}

const rankList = [];
for (let i = 0; i < 7; i++) {
  rankList.push({
    name: '白鹭岛 ' + (i + 1) + ' 号店',
    total: 1234.56 - i * 100,
  });
}

const searchUserData = [];
for (let i = 0; i < 7; i++) {
  searchUserData.push({
    x: moment().add(i, 'days').format('YYYY-MM-DD'),
    y: Math.ceil(Math.random() * 10),
  });
}
const searchUserScale = [
  {
    dataKey: 'x',
    alias: '时间',
  },
  {
    dataKey: 'y',
    alias: '用户数',
    min: 0,
    max: 10,
  },
];

const searchTableColumns = [
  {
    dataIndex: 'index',
    title: '排名',
    width: 90,
  },
  {
    dataIndex: 'keyword',
    title: '搜索关键词',
  },
  {
    dataIndex: 'count',
    title: '用户数',
  },
  {
    dataIndex: 'range',
    title: '周涨幅',
    align: 'right',
    sorter: (a, b) => a.range - b.range,
    scopedSlots: { customRender: 'range' },
  },
];
const searchData = [];
for (let i = 0; i < 50; i += 1) {
  searchData.push({
    index: i + 1,
    keyword: `搜索关键词-${i}`,
    count: Math.floor(Math.random() * 1000),
    range: Math.floor(Math.random() * 100),
    status: Math.floor((Math.random() * 10) % 2),
  });
}

// const DataSet = require('@antv/data-set')

const sourceData = [
  { item: '家用电器', count: 32.2 },
  { item: '食用酒水', count: 21 },
  { item: '个护健康', count: 17 },
  { item: '服饰箱包', count: 13 },
  { item: '母婴产品', count: 9 },
  { item: '其他', count: 7.8 },
];

const pieScale = [
  {
    dataKey: 'percent',
    min: 0,
    formatter: '.0%',
  },
];

// const dv = new DataSet.View().source(sourceData)
// dv.transform({
//     type: 'percent',
//     field: 'count',
//     dimension: 'item',
//     as: 'percent'
// })
// const pieData = dv.rows
const datax = [];
const beginDay = new Date().getTime();

for (let i = 0; i < 10; i++) {
  datax.push({
    x: moment(new Date(beginDay + 1000 * 60 * 60 * 24 * i)).format('YYYY-MM-DD'),
    y: Math.round(Math.random() * 10),
  });
}
const echartsColor = [
  '#3FA1FF',
  '#3ECBCB',
  '#50CB74',
  '#FBD444',
  '#F2637B',
  '#9860E5',
  '#ca8622',
  '#bda29a',
  '#6e7074',
  '#546570',
  '#c4ccd3',
];
export default {
  name: 'Analysis',
  mixins: [mixinDevice],
  components: {
    ChartCard,
    MiniProgress,
    RankList,
    Trend,
    NumberInfo,
  },
  data() {
    let data = [];

    for (let i = 0; i <= 360; i++) {
      let t = (i / 180) * Math.PI;
      let r = Math.sin(2 * t) * Math.cos(2 * t);
      data.push([r, i]);
    }
    return {
      loading: true,
      rankList,

      // 搜索用户数
      searchUserData,
      searchUserScale,
      searchTableColumns,
      searchData,

      barData,
      barData2,

      //
      pieScale,
      // pieData,
      sourceData,
      pieStyle: {
        stroke: '#fff',
        lineWidth: 1,
      },
      polar: {
        color: echartsColor,
        title: {
          text: '极坐标双数值轴',
          show: false,
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: barData.map(item => item.x),
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: barData.map(item => item.y),
            type: 'bar',
          },
        ],
        itemStyle: {
          color: '#379EFC',
          shadowBlur: 5,
          shadowColor: 'rgba(0, 0, 0, 0.5)',
        },
      },
      polar2: {
        color: echartsColor,
        title: {
          text: '极坐标双数值轴',
          show: false,
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: barData2.map(item => item.x),
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: barData2.map(item => item.y),
            type: 'bar',
          },
        ],
        itemStyle: {
          color: '#379EFC',
          shadowBlur: 5,
          shadowColor: 'rgba(0, 0, 0, 0.5)',
        },
      },
      accessStatics: {
        color: echartsColor,
        title: {
          text: '极坐标双数值轴',
          show: false,
        },
        grid: {
          show: false,
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: datax.map(item => item.x),
          show: false,
        },
        yAxis: {
          type: 'value',
          show: false,
        },
        series: [
          {
            data: datax.map(item => item.y),
            type: 'line',
            smooth: true,
            areaStyle: {},
          },
        ],
        /*itemStyle: {
                    color: '#379EFC',
                    shadowBlur: 5,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }*/
      },
      searchDataSmooth: {
        color: echartsColor,
        title: {
          text: '极坐标双数值轴',
          show: false,
        },
        grid: {
          show: false,
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: searchUserData.map(item => item.x),
          show: false,
        },
        yAxis: {
          type: 'value',
          show: false,
        },
        series: [
          {
            data: searchUserData.map(item => item.y),
            type: 'line',
            smooth: true,
            areaStyle: {},
          },
        ],
        /*itemStyle: {
                    color: '#379EFC',
                    shadowBlur: 5,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }*/
      },
      searchUserDataSmooth: {
        color: echartsColor,
        title: {
          text: '极坐标双数值轴',
          show: false,
        },
        grid: {
          show: false,
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: searchUserData.map(item => item.x),
          show: false,
        },
        yAxis: {
          type: 'value',
          show: false,
        },
        series: [
          {
            data: searchUserData.map(item => item.y),
            type: 'line',
            smooth: true,
            areaStyle: {},
          },
        ],
        /*itemStyle: {
                    color: '#379EFC',
                    shadowBlur: 5,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }*/
      },
      accessStaticsBar: {
        color: echartsColor,
        title: {
          text: '极坐标双数值轴',
          show: false,
        },
        grid: {
          show: false,
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: datax.map(item => item.x),
          show: false,
        },
        yAxis: {
          type: 'value',
          show: false,
        },
        series: [
          {
            data: datax.map(item => item.y),
            type: 'bar',
          },
        ],
        /*itemStyle: {
                    color: '#379EFC',
                    shadowBlur: 5,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }*/
      },
      pie: {
        color: echartsColor,
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)',
        },
        legend: {
          orient: 'horizontal',
          bottom: 10,
          data: ['家用电器', '食用酒水', '个护健康', '服饰箱包', '母婴产品', '其他'],
        },
        series: [
          {
            name: '访问来源',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center',
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '30',
                fontWeight: 'bold',
              },
            },
            labelLine: {
              show: false,
            },
            data: [
              { value: 32.2, name: '家用电器' },
              { value: 21, name: '食用酒水' },
              { value: 17, name: '个护健康' },
              { value: 13, name: '服饰箱包' },
              { value: 9, name: '母婴产品' },
              { value: 7.8, name: '其他' },
            ],
          },
        ],
      },
      chartData: {
        columns: ['date', 'user', 'orderUser', 'rate'],
        rows: [
          { date: '1/1', user: 1393, orderUser: 1093, rate: 0.32 },
          { date: '1/2', user: 3530, orderUser: 3230, rate: 0.26 },
          { date: '1/3', user: 2923, orderUser: 2623, rate: 0.76 },
          { date: '1/4', user: 1723, orderUser: 1423, rate: 0.49 },
          { date: '1/5', user: 3792, orderUser: 3492, rate: 0.323 },
          { date: '1/6', user: 4593, orderUser: 4293, rate: 0.78 },
        ],
      },
      chartxData: {
        dimensions: {
          name: 'Week',
          data: ['Mon.', 'Tue.', 'Wed.', 'Thu.', 'Fir.', 'Sat.', 'Sun.'],
        },
        measures: [
          {
            name: 'Vue',
            data: [30, 40, 35, 50, 49, 70, 90],
          },
        ],
      },
    };
  },
  created() {
    setTimeout(() => {
      this.loading = !this.loading;
    }, 1000);
  },
};
</script>

<style lang="less" scoped>
.extra-wrapper {
  line-height: 55px;
  padding-right: 24px;

  .extra-item {
    display: inline-block;
    margin-right: 24px;

    a {
      margin-left: 24px;
    }
  }
}

.antd-pro-pages-dashboard-analysis-twoColLayout {
  position: relative;
  //display: flex;
  display: block;
  flex-flow: row wrap;
}

.antd-pro-pages-dashboard-analysis-salesCard {
  height: calc(100% - 24px);

  /deep/ .ant-card-head {
    position: relative;
  }
}

.dashboard-analysis-iconGroup {
  i {
    margin-left: 16px;
    color: rgba(0, 0, 0, 0.45);
    cursor: pointer;
    transition: color 0.32s;
    color: black;
  }
}

.analysis-salesTypeRadio {
  position: absolute;
  right: 54px;
  bottom: 12px;
}
.chart-card-content-analysis {
  position: absolute;
  left: 0;
  bottom: -70px;
  width: 100%;
  height: 180px;
  .echarts {
    width: 100%;
    height: 100%;
  }
}

.chart-card-content-search {
  position: absolute;
  left: 0;
  bottom: -70px;
  width: 100%;
  height: 170px;
  .echarts {
    width: 100%;
    height: 100%;
  }
}
</style>
