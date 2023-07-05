<template>
  <a-modal v-model="show" title="订单处理" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="submit" type="primary">
        分配
      </a-button>
      <a-button @click="onClose">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="orderData !== null">
      <div style="padding-left: 24px;padding-right: 24px;margin-bottom: 50px;margin-top: 50px">
        <a-steps :current="current" progress-dot size="small">
          <a-step title="待付款" />
          <a-step title="正在分配" />
          <a-step title="运输中" />
          <a-step title="运输完成" />
        </a-steps>
      </div>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>订单编号：</b>
          {{ orderData.code }}
        </a-col>
        <a-col :span="8"><b>客户名称：</b>
          {{ orderData.userName ? orderData.userName : '- -' }}
        </a-col>
        <a-col :span="8"><b>联系方式：</b>
          {{ orderData.phone ? orderData.phone : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>当前状态：</b>
          <span v-if="orderData.orderStatus == 0">待付款</span>
          <span v-if="orderData.orderStatus == 1">正在分配</span>
          <span v-if="orderData.orderStatus == 2">运输中</span>
          <span v-if="orderData.orderStatus == 3">运输完成</span>
        </a-col>
        <a-col :span="8"><b>订单金额：</b>
          {{ orderData.amount }} 元
        </a-col>
        <a-col :span="8"><b>下单时间：</b>
          {{ orderData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">运输配置</span></a-col>
        <a-col :span="8"><b>是否需要车辆：</b>
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
          <span v-if="orderData.hasElevator == 1" style="color: red">有电梯</span>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>起始地址：</b>
          {{ orderData.startAddress }}
        </a-col>
        <a-col :span="8"><b>运输地址：</b>
          {{ orderData.endAddress }}
        </a-col>
        <a-col :span="8"><b>总距离：</b>
          {{ orderData.distanceLength }} 千米
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
      <br/>
      <a-row :gutter="15" style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="12">
          <a-form-item label='选择司机'>
            <a-select style="width: 100%" v-model="driverCheck">
              <a-select-option :value="item.code" v-for="(item, index) in driverList" :key="index">{{ item.name }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <br/>
      <a-row :gutter="15" style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="12">
          <a-form-item label='选择搬运工'>
            <a-select style="width: 100%" mode="multiple" v-model="staffCheck">
              <a-select-option :value="item.code" v-for="(item, index) in staffList" :key="index">{{ item.name }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
    </div>
  </a-modal>
</template>

<script>
import moment from 'moment'
import {mapState} from 'vuex'
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
moment.locale('zh-cn')
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'OrderAudit',
  props: {
    orderShow: {
      type: Boolean,
      default: false
    },
    orderData: {
      type: Object
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.orderShow
      },
      set: function () {
      }
    }
  },
  watch: {
    'orderShow': function (value) {
      if (value) {
        this.current = this.orderData.status
      }
    }
  },
  data () {
    return {
      formItemLayout,
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      auditData: {
        remark: ''
      },
      staffList: [],
      driverCheck: [],
      staffCheck: [],
      driverList: [],
      current: 0
    }
  },
  mounted () {
    this.selectStaff()
  },
  methods: {
    moment,
    selectStaff () {
      this.$get(`/cos/staff-info/staff/type`).then((r) => {
        console.log(JSON.stringify(r.data))
        this.staffList = r.data.staff
        this.driverList = r.data.driver
      })
    },
    onDateChange (date) {
      this.auditData.reserveDate = moment(date).format('YYYY-MM-DD')
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
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    submit () {
      if (this.driverCheck.length !== 0) {
        this.$get(`/cos/order-info/checkOrder`, {
          'orderCode': this.orderData.code,
          'driverCode': this.driverCheck,
          'staffCodeStr': this.staffCheck.join(',')
        }).then((r) => {
          this.cleanData()
          this.$emit('success')
        })
      } else {
        this.$message.warn('请选择司机')
      }
    },
    onClose () {
      this.cleanData()
      this.$emit('close')
    },
    cleanData () {
      this.staffCheck = []
      this.driverCheck = []
    }
  }
}
</script>

<style scoped>

</style>
