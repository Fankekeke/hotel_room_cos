<template>
  <a-drawer
    placement="right"
    width="100%"
    :closable="false"
    :visible="orderShow"
    destroyOnClose
    wrapClassName="aa"
    :getContainer="false"
  >
    <div style="width: 100%">
      <a-icon type="arrow-left" style="position: absolute;z-index: 999;color: red;font-size: 20px;margin: 15px" @click="home"/>
      <a-row style="height:100vh;font-family: SimHei">
        <a-col :span="18" style="height: 100%;">
          <div id="areas" style="width: 100%;height: 100%;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);background:#ec9e3c;color:#fff"></div>
        </a-col>
        <a-col :span="6" style="height: 100%;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);color:#fff">
          <div>
            <div class="scenicInfo" style="height: 100vh; overflow-y: auto;padding-left: 5px;overflow-x: hidden">
              <a-carousel autoplay style="height: 250px;" v-if="orderData.images !== undefined && orderData.images !== ''">
                <div style="width: 100%;height: 250px" v-for="(item, index) in orderData.images.split(',')" :key="index">
                  <img :src="'http://127.0.0.1:9527/imagesWeb/'+item" style="width: 100%;height: 100%">
                </div>
              </a-carousel>
              <a-card :title="orderData.startAddress +' ~ '+ orderData.endAddress" :bordered="false">
              </a-card>
              <div style="font-size: 12px;font-family: SimHei;color: #404040;margin-top: 15px">
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="24"><b>订单编号：</b>
                    {{ orderData.code !== null ? orderData.code : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="userInfo != null">
                  <a-col :span="8"><b>客户编号：</b>
                    {{ userInfo.code !== null ? userInfo.code : '- -' }}
                  </a-col>
                  <a-col :span="8"><b>客户名称：</b>
                    {{ userInfo.name !== null ? userInfo.name : '- -' }}
                  </a-col>
                  <a-col :span="8"><b>联系方式：</b>
                    {{ userInfo.phone !== null ? userInfo.phone : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="8"><b>订单金额：</b>
                    {{ orderData.amount !== null ? orderData.amount : '- -' }}元
                  </a-col>
                  <a-col :span="8"><b>订单状态：</b>
                    <span v-if="orderData.status == 0">未支付</span>
                    <span v-if="orderData.status == 1">正在分配</span>
                    <span v-if="orderData.status == 2">运输中</span>
                    <span v-if="orderData.status == 3">运输完成</span>
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="8"><b>车辆配置：</b>
                    <span v-if="orderData.vehicleOptions == null">不需要车辆</span>
                    <span v-if="orderData.vehicleOptions == 1">大型车</span>
                    <span v-if="orderData.vehicleOptions == 2">中型车</span>
                    <span v-if="orderData.vehicleOptions == 3">小型车</span>
                  </a-col>
                  <a-col :span="8"><b>搬运工：</b>
                    {{ orderData.staffOptions }} 个
                  </a-col>
                  <a-col :span="8"><b>是否有电梯：</b>
                    <span v-if="orderData.hasElevator == 0" style="color: red">无电梯</span>
                    <span v-if="orderData.hasElevator == 1" style="color: green">有电梯</span>
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="12"><b>下单时间：</b>
                    {{ orderData.createDate !== null ? orderData.createDate : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col style="margin-bottom: 15px"><span style="font-size: 14px;font-weight: 650;color: #000c17">周边设施</span></a-col>
                  <a-col :span="24">
                    <a-radio-group button-style="solid" style="width: 100%" @change="gisOnChange">
                      <a-radio-button value="1" style="width: 25%;text-align: center">
                        交通
                      </a-radio-button>
                      <a-radio-button value="2" style="width: 25%;text-align: center">
                        餐饮
                      </a-radio-button>
                      <a-radio-button value="3" style="width: 25%;text-align: center">
                        教育
                      </a-radio-button>
                      <a-radio-button value="4" style="width: 25%;text-align: center">
                        医疗
                      </a-radio-button>
                    </a-radio-group>
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col style="margin-bottom: 15px"><span style="font-size: 14px;font-weight: 650;color: #000c17">起始地址</span></a-col>
                  {{ orderData.startAddress !== null ? orderData.startAddress : '- -' }}
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col style="margin-bottom: 15px"><span style="font-size: 14px;font-weight: 650;color: #000c17">运输地址</span></a-col>
                  {{ orderData.endAddress !== null ? orderData.endAddress : '- -' }}
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col style="margin-bottom: 15px"><span style="font-size: 14px;font-weight: 650;color: #000c17">总距离</span></a-col>
                  <a-col :span="12">
                    {{ orderData.distanceLength !== null ? (orderData.distanceLength + '千米') : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="12"><b>起始经度：</b>
                    {{ orderData.startLongitude !== null ? orderData.startLongitude : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>起始纬度：</b>
                    {{ orderData.startLatitude !== null ? orderData.startLatitude : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="12"><b>运输经度：</b>
                    {{ orderData.endLongitude !== null ? orderData.endLongitude : '- -' }}
                  </a-col>
                  <a-col :span="12"><b>运输纬度：</b>
                    {{ orderData.endLatitude !== null ? orderData.endLatitude : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="evaluateInfo != null">
                  <a-col><span style="font-size: 14px;font-weight: 650;color: #000c17">订单评价</span></a-col>
                </a-row>
                <a-card v-if="evaluateInfo != null" hoverable :bordered="false" style="width: 100%">
                  <a-skeleton active v-if="checkLoading" />
                  <apexchart v-if="!checkLoading" type="radar" height="300" :options="chartOptions" :series="series"></apexchart>
                </a-card>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="staffList.length !== 0">
                  <a-col><span style="font-size: 14px;font-weight: 650;color: #000c17">员工信息</span></a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="staffList.length !== 0">
                  <a-col :span="6" v-for="(item, index) in staffList" :key="index">
                    <a-avatar shape="square" :size="95" :src="'http://127.0.0.1:9527/imagesWeb/' + item.images.split(',')[0]" icon="user" />
                    <p>{{ item.name }}【{{ item.type == 1 ? '搬运工' : '驾驶员' }}】</p>
                  </a-col>
                </a-row>
                <br/>
                <br/>
                <div style="text-align: center">
                  <a-icon type="smile" theme="twoTone" style="font-size: 75px"/>
                  <p style="margin-top: 25px;font-size: 20px;font-family: SimHei" v-if="orderData.status == 0">未支付</p>
                  <p style="margin-top: 25px;font-size: 20px;font-family: SimHei" v-if="orderData.status == 1">正在分配</p>
                  <p style="margin-top: 25px;font-size: 20px;font-family: SimHei" v-if="orderData.status == 2">运输中</p>
                  <p style="margin-top: 25px;font-size: 20px;font-family: SimHei" v-if="orderData.status == 3">运输完成</p>
                </div>
              </div>
            </div>
          </div>
        </a-col>
      </a-row>
    </div>
  </a-drawer>
</template>

<script>
import baiduMap from '@/utils/map/baiduMap'
export default {
  name: 'Map',
  props: {
    orderShow: {
      type: Boolean,
      default: false
    },
    orderData: {
      type: Object
    }
  },
  data () {
    return {
      staffList: [],
      evaluateInfo: null,
      userInfo: null,
      communityRent: 0,
      rentShow: false,
      communityShow: false,
      communityData: null,
      checkedList: ['Apple', 'Orange'],
      indeterminate: true,
      checkAll: false,
      plainOptions: ['Apple', 'Pear', 'Orange'],
      visible: false,
      rentList: [],
      communityList: [],
      community: null,
      nowPoint: null,
      roadData: [],
      checkLoading: false,
      echartsShow: false,
      series: [{
        name: '得分',
        data: []
      }],
      chartOptions: {
        chart: {
          height: 380,
          type: 'radar'
        },
        title: {
          text: ''
        },
        xaxis: {
          categories: ['交付得分', '价格得分', '质量得分', '准时得分', '服务得分', '综合得分']
        }
      }
    }
  },
  watch: {
    'orderShow': function (value) {
      if (value) {
        this.selectOrderDetail(this.orderData.code)
        setTimeout(() => {
          baiduMap.initMap('areas')
          setTimeout(() => {
            this.navigation(this.orderData)
          }, 200)
          this.getLocal()
        }, 200)
      }
    }
  },
  methods: {
    navigation (data) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions: {map: baiduMap.rMap(), autoViewport: true}})
      // eslint-disable-next-line no-undef
      driving.search(new BMap.Point(data.startLongitude, data.startLatitude), new BMap.Point(data.endLongitude, data.endLatitude))
      // this.getRoadData()
    },
    getRoadData () {
      let options = {
        onSearchComplete: results => {
          // eslint-disable-next-line eqeqeq,no-undef
          if (driving.getStatus() == BMAP_STATUS_SUCCESS) {
            // 获取第一条方案
            let plan = results.getPlan(0)
            // 获取方案的驾车线路
            // eslint-disable-next-line no-unused-vars
            let route = plan.getRoute(0)
            // 获取每个关键步骤,并输出到页面
            let s = []
            for (let j = 0; j < plan.getNumRoutes(); j++) {
              let route = plan.getRoute(j)
              for (let i = 0; i < route.getNumSteps(); i++) {
                let step = route.getStep(i)
                s.push((i + 1) + '. ' + step.getDescription())
              }
            }
            this.roadData = s
          }
        }
      }
      // eslint-disable-next-line no-undef
      let driving = new BMap.DrivingRoute(baiduMap.rMap(), options)
      // eslint-disable-next-line no-undef
      let start = new BMap.Point(this.nowPoint.lng, this.nowPoint.lat)
      let end = null
      if (this.rentShow) {
        end = new BMap.Point(this.rentData.longitude, this.rentData.latitude)
      } else {
        end = new BMap.Point(this.communityData.longitude, this.communityData.latitude)
      }
      // eslint-disable-next-line no-undef
      driving.search(start, end)
    },
    checkEvaluate (score) {
      let data = [score.deliverScore, score.priceScore, score.qualityScore, score.scheduleScore, score.serviceScore, score.overScore, 100]
      this.series[0].data = data
    },
    home () {
      this.$emit('close')
    },
    gisOnChange (e) {
      let key = ''
      switch (e.target.value) {
        case '1':
          key = '公交站'
          break
        case '2':
          key = '餐饮'
          break
        case '3':
          key = '教育'
          break
        case '4':
          key = '医疗'
          break
      }
      baiduMap.searchNear(this.orderData.startLongitude, this.orderData.endLatitude, key)
    },
    getLocal () {
      // eslint-disable-next-line no-undef
      let geolocation = new BMap.Geolocation()
      geolocation.getCurrentPosition(r => {
        this.nowPoint = r.point
      }, {enableHighAccuracy: true})
    },
    selectOrderDetail (orderCode) {
      this.checkLoading = true
      this.evaluateInfo = null
      this.staffList = []
      this.$get(`/cos/order-info/detail/${orderCode}`).then((r) => {
        this.userInfo = r.data.user
        this.evaluateInfo = r.data.evaluate
        if (r.data.evaluate != null) {
          this.checkEvaluate(r.data.evaluate)
        }
        this.staffList = r.data.staff
        this.checkLoading = false
      })
    }
  }
}
</script>

<style scoped>
  >>> .ant-drawer-body {
    padding: 0 !important;
  }
  >>> .ant-card-meta-title {
    font-size: 13px;
    font-family: SimHei;
  }
  >>> .ant-card-meta-description {
    font-size: 12px;
    font-family: SimHei;
  }
  >>> .ant-divider-with-text-left {
    margin: 0;
  }

  >>> .ant-card-head-title {
    font-size: 13px;
    font-family: SimHei;
  }
  >>> .ant-card-extra {
    font-size: 13px;
    font-family: SimHei;
  }
  >>> .ant-radio-button-wrapper {
    border-radius: 0;
  }
</style>
