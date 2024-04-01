<template>
  <div style="width: 100%">
    <a-row :gutter="8" class="count-info">
      <a-col :span="8" class="visit-count-wrapper">
        <a-card>
          <div v-if="scenicShow" class="scenicInfo" style="height: 670px; overflow-y: auto">
            <img :src="scenicData.webImg" alt="" width="100%"
                 style="height: 180px;object-fit: cover">
            <a-card :title="scenicData.name">
              <a slot="extra" @click="scenicBack">返回</a>
              <a-tabs default-active-key="1">
                <a-tab-pane key="1" tab="基础信息">
                  <ul>
                    <li>地址：{{ scenicData.address }}</li>
                    <br/>
                    <li>等级：{{ scenicData.level }}</li>
                    <br/>
                    <li>门票：{{ scenicData.price }} 元</li>
                    <br/>
                    <li>景区特色：{{ scenicData.feature }}</li>
                  </ul>
                </a-tab-pane>
                <a-tab-pane key="2" tab="路线规划">
                  <a-timeline>
                    <a-timeline-item v-for="(item,index) in roadData" :key="index">
                      <div v-html="item"></div>
                    </a-timeline-item>
                  </a-timeline>
                </a-tab-pane>
              </a-tabs>
            </a-card>
          </div>
          <a-tabs default-active-key="1" v-if="!scenicShow && !hotelShow">
            <a-tab-pane key="1" tab="景点" style="height: 600px; overflow-y: auto">
              <a-card @click="scenicDetail(item)" hoverable style="width: 100%;margin-bottom: 15px"
                      v-for="(item, index) in scenicList" :key="index">
                <a-popover v-if="item.webImg !== null">
                  <template slot="content">
                    <a-avatar shape="square" :size="132" icon="user" :src="item.webImg"/>
                  </template>
                  <a-avatar shape="square" :size="70" icon="user" style="margin-bottom: 15px;margin-right: 10px" :src="item.webImg"/>
                </a-popover>
                <a-card-meta :title="item.scenicName" :description="item.history.slice(0, 40)+'...'">
                </a-card-meta>
              </a-card>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </a-col>
      <a-col :span="16" class="visit-count-wrapper">
        <div id="areas" style="width: 100%;height: 710px;box-shadow: 0 0 0 10px white;"></div>
      </a-col>
      <a-col :span="24" class="visit-count-wrapper" style="margin-top: 20px">
        <a-card class="visit-count" hoverable :bordered="false">
          <apexchart ref="count" type=bar height=300 :options="chartOptions" :series="series"/>
        </a-card>
      </a-col>
      <a-col :span="12" class="project-wrapper">
      </a-col>
    </a-row>
  </div>
</template>
<script>
import HeadInfo from '@/views/common/HeadInfo'
import {mapState} from 'vuex'
import moment from 'moment'
import baiduMap from '@/utils/map/baiduMap'

moment.locale('zh-cn')

export default {
  name: 'HomePage',
  components: {HeadInfo},
  data () {
    return {
      todayIp: '',
      todayVisitCount: '',
      totalVisitCount: '',
      userRole: '',
      userDept: '',
      lastLoginTime: '',
      welcomeMessage: '',
      orderInfoByHotel: null,
      orderTypeByHotelId: [],
      orderNum: [],
      loading: false,
      scenicList: [],
      hotelList: [],
      scenicShow: false,
      scenicData: null,
      hotelShow: false,
      hotelData: null,
      roadData: [],
      nowPoint: null
    }
  },
  computed: {
    ...mapState({
      multipage: state => state.setting.multipage,
      user: state => state.account.user
    }),
    avatar () {
      return `static/avatar/${this.user.avatar}`
    }
  },
  methods: {
    scenicBack () {
      this.scenicShow = false
    },
    scenicDetail (row) {
      this.scenicData = row
      this.scenicShow = true
      this.local(row)
    },
    hotelBack () {
      this.hotelShow = false
    },
    hotelDetail (row) {
      this.hotelData = row
      this.hotelShow = true
      this.local(row)
    },
    getLocal () {
      // eslint-disable-next-line no-undef
      let geolocation = new BMap.Geolocation()
      geolocation.getCurrentPosition(r => {
        this.nowPoint = r.point
      }, {enableHighAccuracy: true})
    },
    local (scenic) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions: {map: baiduMap.rMap(), autoViewport: true}})
      // eslint-disable-next-line no-undef
      driving.search(new BMap.Point(this.nowPoint.lng, this.nowPoint.lat), new BMap.Point(scenic.point.split(',')[0], scenic.point.split(',')[1]))
      this.getRoadData()
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
      if (this.scenicShow) {
        end = new BMap.Point(this.scenicData.point.split(',')[0], this.scenicData.point.split(',')[1])
      } else {
        end = new BMap.Point(this.hotelData.point.split(',')[0], this.hotelData.point.split(',')[1])
      }
      // eslint-disable-next-line no-undef
      driving.search(start, end)
    },
    home () {
      this.loading = true
      this.$get('/cos/scenic-info/list').then((r) => {
        this.scenicList = r.data.data
        setTimeout(() => {
          this.loading = false
        }, 800)
      })
    }
  },
  mounted () {
    baiduMap.initMap('areas')
    this.getLocal()
    this.home()
  }
}
</script>
<style lang="less">

</style>
