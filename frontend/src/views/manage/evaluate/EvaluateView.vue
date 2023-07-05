<template>
  <a-modal v-model="show" title="评价详情" @cancel="onClose" :width="1200">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="evaluateInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">评价信息</span></a-col>
        <a-col :span="6"><b>评价得分：</b>
          {{ evaluateInfo.score }}
        </a-col>
        <a-col :span="18"><b>评价备注：</b>
          {{ evaluateInfo.remark ? evaluateInfo.remark : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">图册</span></a-col>
        <a-col :span="24">
          <a-upload
            name="avatar"
            action="http://127.0.0.1:9527/file/fileUpload/"
            list-type="picture-card"
            :file-list="fileList"
            @preview="handlePreview"
            @change="picHandleChange"
          >
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-col>
      </a-row>
    </div>
    <div style="font-size: 13px;font-family: SimHei" v-if="userInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">评价用户</span></a-col>
        <a-col :span="6"><b>用户姓名：</b>
          {{ userInfo.name }}
        </a-col>
        <a-col :span="6"><b>联系电话：</b>
          {{ userInfo.phone ? userInfo.phone : '- -' }}
        </a-col>
        <a-col :span="6"><b>会员编号：</b>
          {{ userInfo.code ? userInfo.code : '- -' }}
        </a-col>
      </a-row>
    </div>
    <div style="font-size: 13px;font-family: SimHei" v-if="vehicleInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">车辆信息</span></a-col>
        <a-col :span="6"><b>车辆编号：</b>
          {{ vehicleInfo.vehicleNo }}
        </a-col>
        <a-col :span="6"><b>车牌号：</b>
          {{ vehicleInfo.vehicleNumber ? vehicleInfo.vehicleNumber : '- -' }}
        </a-col>
        <a-col :span="6"><b>车辆颜色：</b>
          {{ vehicleInfo.vehicleColor ? vehicleInfo.vehicleColor : '- -' }}
        </a-col>
        <a-col :span="6"><b>车辆名称：</b>
          {{ vehicleInfo.name }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="6"><b>载客数量：</b>
          {{ vehicleInfo.carryPassengers }}
        </a-col>
        <a-col :span="6"><b>发动机号码：</b>
          {{ vehicleInfo.engineNo }}
        </a-col>
        <a-col :span="6"><b>车辆状态：</b>
          <span v-if="vehicleInfo.status == 0" style="color: red">空闲</span>
          <span v-if="vehicleInfo.status == 1" style="color: green">使用中</span>
          <span v-if="vehicleInfo.status == 2" style="color: green">维修中</span>
          <span v-if="vehicleInfo.status == 3" style="color: green">已报废</span>
        </a-col>
        <a-col :span="6"><b>负责人：</b>
          {{ vehicleInfo.principal }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">  
        <a-col :span="6"><b>联系电话：</b>
          {{ vehicleInfo.phone }}
        </a-col>
        <a-col :span="6"><b>出厂日期：</b>
          {{ vehicleInfo.factoryDate }}
        </a-col>
        <a-col :span="6"><b>排放标准：</b>
          {{ vehicleInfo.emissionStandard }}
        </a-col>
        <a-col :span="6"><b>燃料类型：</b>
          <span v-if="vehicleInfo.fuelType == 1" style="color: green">燃油</span>
          <span v-if="vehicleInfo.fuelType == 2" style="color: green">柴油</span>
          <span v-if="vehicleInfo.fuelType == 3" style="color: green">油电混动</span>
          <span v-if="vehicleInfo.fuelType == 4" style="color: green">电能</span>
        </a-col>
        <a-col :span="24"><b>备注：</b>
          {{ vehicleInfo.content }}
        </a-col>
      </a-row>
      <br/>
    </div>
  </a-modal>
</template>

<script>
import baiduMap from '@/utils/map/baiduMap'
import moment from 'moment'
moment.locale('zh-cn')
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'ShopView',
  props: {
    evaluateShow: {
      type: Boolean,
      default: false
    },
    evaluateData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.evaluateShow
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      repairInfo: null,
      reserveInfo: null,
      durgList: [],
      logisticsList: [],
      userInfo: null,
      vehicleInfo: null,
      orderInfo: null,
      evaluateInfo: null
    }
  },
  watch: {
    evaluateShow: function (value) {
      if (value) {
        this.dataInit(this.evaluateData.id)
      }
    }
  },
  methods: {
    dataInit (id) {
      this.$get(`/cos/vehicle-info/evaluate/detail/${id}`).then((r) => {
        this.userInfo = r.data.user
        this.orderInfo = r.data.order
        this.vehicleInfo = r.data.vehicle
        this.evaluateInfo = r.data.evaluate
        this.imagesInit(this.evaluateInfo.images)
      })
    },
    local (evaluateData) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let point = new BMap.Point(evaluateData.longitude, evaluateData.latitude)
      baiduMap.pointAdd(point)
      baiduMap.findPoint(point, 16)
      // let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions:{map: baiduMap.rMap(), autoViewport: true}});
      // driving.search(new BMap.Point(this.nowPoint.lng,this.nowPoint.lat), new BMap.Point(scenic.point.split(",")[0],scenic.point.split(",")[1]));
    },
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
