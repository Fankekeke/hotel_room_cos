<template>
  <a-card :bordered="false" class="card-area">
    <div style="background:#ECECEC; padding:30px;margin-top: 30px;margin-bottom: 30px">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="选择时间"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.roomName"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="房间类型"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.typeName"/>
              </a-form-item>
            </a-col>
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
          </span>
        </a-row>
      </a-form>
    </div>
    <a-row :gutter="20">
      <a-col :span="24">
        <a-card hoverable :bordered="false" style="width: 100%">
          <a-skeleton active v-if="loading" />
          <apexchart v-if="!loading" type="bar" height="300" :options="chartOptions1" :series="series1"></apexchart>
        </a-card>
      </a-col>
      <br/>
      <a-col :span="24">
        <a-card hoverable :bordered="false" style="width: 100%">
          <a-skeleton active v-if="loading" />
          <apexchart v-if="!loading" type="bar" height="300" :options="chartOptions2" :series="series2"></apexchart>
        </a-card>
      </a-col>
    </a-row>
    <a-row :gutter="20">
      <a-col :span="12">
        <a-card hoverable :bordered="false" style="width: 100%">
          <a-skeleton active v-if="loading" />
          <apexchart v-if="!loading" type="donut" height="270" :options="chartOptions3" :series="series3"></apexchart>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card hoverable :bordered="false" style="width: 100%">
          <a-skeleton active v-if="loading" />
          <apexchart v-if="!loading" type="donut" height="270" :options="chartOptions4" :series="series4"></apexchart>
        </a-card>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
export default {
  name: 'Work',
  data () {
    return {
      queryParams: {},
      loading: false,
      series1: [],
      chartOptions1: {
        chart: {
          type: 'bar',
          height: 300
        },
        title: {
          text: '订单数量统计',
          align: 'left'
        },
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: '55%'
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          show: true,
          width: 2,
          colors: ['transparent']
        },
        xaxis: {
          categories: []
        },
        yaxis: {
          title: {
            text: ''
          }
        },
        fill: {
          opacity: 1
        },
        tooltip: {
          y: {
            formatter: function (val) {
              return val + ' 单'
            }
          }
        }
      },
      series2: [],
      chartOptions2: {
        chart: {
          type: 'bar',
          height: 300
        },
        title: {
          text: '订单收益统计',
          align: 'left'
        },
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: '55%'
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          show: true,
          width: 2,
          colors: ['transparent']
        },
        xaxis: {
          categories: []
        },
        yaxis: {
          title: {
            text: ''
          }
        },
        fill: {
          opacity: 1
        },
        tooltip: {
          y: {
            formatter: function (val) {
              return val + ' 单'
            }
          }
        }
      },
      series3: [],
      chartOptions3: {
        chart: {
          type: 'donut',
          height: 300
        },
        labels: [],
        title: {
          text: '销量统计',
          align: 'left'
        },
        responsive: [{
          breakpoint: 380,
          options: {
            chart: {
              width: 200
            },
            legend: {
              position: 'bottom'
            }
          }
        }]
      },
      series4: [],
      chartOptions4: {
        chart: {
          type: 'donut',
          height: 300
        },
        labels: [],
        title: {
          text: '销售统计',
          align: 'left'
        },
        responsive: [{
          breakpoint: 380,
          options: {
            chart: {
              width: 200
            },
            legend: {
              position: 'bottom'
            }
          }
        }]
      },
    }
  },
  mounted () {
    this.getWorkStatusList()
    this.selectOrderDays()
  },
  methods: {
    getStatusList () {
      this.$get(`/cos/order-info/statistics`, this.queryParams).then((r) => {
        this.priceByMonth = r.data.priceByMonth
        this.orderNumByMonth = r.data.orderNumByMonth
        this.typeOrderNumRateByMonth = r.data.typeOrderNumRateByMonth
        this.typePriceRateByMonth = r.data.typePriceRateByMonth

        if (r.data.orderNumByMonth !== null && r.data.orderNumByMonth.length !== 0) {
          if (this.chartOptions1.xaxis.categories.length === 0) {
            this.chartOptions1.xaxis.categories = Array.from(r.data.orderNumByMonth, ({days}) => days)
          }
          let itemData = { name: '订单数', data: Array.from(r.data.orderNumByMonth, ({count}) => count) }
          values.push(itemData)
          this.series1 = values
        }
        this.series[0].data = Array.from(r.data.priceByMonth, ({price}) => price)
        this.chartOptions.xaxis.categories = Array.from(r.data.priceByMonth, ({days}) => days)

        if (r.data.typeOrderNumRateByMonth !== null && r.data.typeOrderNumRateByMonth.length !== 0) {
          this.chartOptions3.labels = Array.from(r.data.typeOrderNumRateByMonth, ({typeName}) => typeName)
          this.series3 = Array.from(r.data.typeOrderNumRateByMonth, ({count}) => count)
        }
        if (r.data.typePriceRateByMonth !== null && r.data.typePriceRateByMonth.length !== 0) {
          this.chartOptions3.labels = Array.from(r.data.typePriceRateByMonth, ({typeName}) => typeName)
          this.series3 = Array.from(r.data.typePriceRateByMonth, ({count}) => count)
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
