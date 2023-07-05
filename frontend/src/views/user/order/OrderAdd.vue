<template>
  <a-drawer
    title="支付"
    :maskClosable="false"
    width=1350
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="orderAddShow"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <a-form :form="form" layout="vertical" v-if="nextFlag == 1">
      <a-row :gutter="10">
        <a-divider orientation="left">
          <span style="font-size: 13px">始发地</span>
        </a-divider>
        <a-col :span="12">
          <a-form-item label='始发地'>
            <a-input-search
              v-decorator="[
              'startAddress',
              { rules: [{ required: true, message: '请选择始发地!' }] }
              ]"
              enter-button="选择"
              @search="showChildrenDrawer(1)"
            />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='始发经度'>
            <a-input v-decorator="[
            'startLongitude',
            { rules: [{ required: true, message: '请输入始发经度!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='始发纬度'>
            <a-input v-decorator="[
            'startLatitude',
            { rules: [{ required: true, message: '请输入始发纬度!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-divider orientation="left">
          <span style="font-size: 13px">运输地</span>
        </a-divider>
        <a-col :span="12">
          <a-form-item label='运输地'>
            <a-input-search
              v-decorator="[
              'endAddress',
               { rules: [{ required: true, message: '请输入运输地!' }] }
              ]"
              enter-button="选择"
              @search="showChildrenDrawer(2)"
            />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='运输经度'>
            <a-input v-decorator="[
            'endLongitude',
            { rules: [{ required: true, message: '请输入运输经度!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='运输纬度'>
            <a-input v-decorator="[
            'endLatitude',
            { rules: [{ required: true, message: '请输入运输纬度!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-divider orientation="left">
          <span style="font-size: 13px">运输配置</span>
        </a-divider>
        <a-col :span="24">
          <a-form-item label='车辆配置'>
            <a-radio-group button-style="solid" v-decorator="['vehicleOptions', { rules: [{ required: true, message: '请输入车辆配置!' }] }]">
              <a-radio-button value="1">
                大型车
              </a-radio-button>
              <a-radio-button value="2">
                中型车
              </a-radio-button>
              <a-radio-button value="3">
                小型车
              </a-radio-button>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='是否需要搬运工'>
            <a-radio-group button-style="solid" v-decorator="['staffOptions', { rules: [{ required: true, message: '请输入是否需要搬运工!' }] }]">
              <a-radio-button value="0">
                不需要
              </a-radio-button>
              <a-radio-button value="1">
                一个
              </a-radio-button>
              <a-radio-button value="2">
                两个
              </a-radio-button>
              <a-radio-button value="3">
                三个
              </a-radio-button>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='是否有电梯'>
            <a-radio-group button-style="solid" v-decorator="['hasElevator', { rules: [{ required: true, message: '请输入是否有电梯!' }] }]">
              <a-radio-button value="0">
                无
              </a-radio-button>
              <a-radio-button value="1">
                有
              </a-radio-button>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :span="16">
          <a-form-item label='备注'>
            <a-textarea v-decorator="[
            'remark'
            ]" :rows="4"/>
          </a-form-item>
        </a-col>
        <a-divider orientation="left">
          <span style="font-size: 13px">图片</span>
        </a-divider>
        <a-col :span="24">
          <a-form-item label='' v-bind="formItemLayout">
            <a-upload
              name="avatar"
              action="http://127.0.0.1:9527/file/fileUpload/"
              list-type="picture-card"
              :file-list="fileList"
              @preview="handlePreview"
              @change="picHandleChange"
            >
              <div v-if="fileList.length < 8">
                <a-icon type="plus" />
                <div class="ant-upload-text">
                  Upload
                </div>
              </div>
            </a-upload>
            <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
              <img alt="example" style="width: 100%" :src="previewImage" />
            </a-modal>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
    <div v-if="nextFlag == 2" style="font-size: 13px;font-family: SimHei">
      <a-row :gutter="10">
        <a-divider orientation="left">
          <span style="font-size: 13px">价格规则</span>
        </a-divider>
        <a-col :span="24">
          【基础金额 + (距离 * 距离单价) + 配送车辆金额 + (配送员数量 * 配送员金额) + 无电梯费用】
        </a-col>
      </a-row>
      <br/>
      <a-row :gutter="10">
        <a-col :span="6"><b>基础金额：</b>
           {{ orderInfo.base }}元
        </a-col>
        <a-col :span="6"><b>距离：</b>
          {{ orderInfo.distanceLength }}千米
        </a-col>
        <a-col :span="6"><b>距离单价：</b>
          {{ orderInfo.distancePrice }}元
        </a-col>
      </a-row>
      <br/>
      <a-row :gutter="10">
        <a-col :span="6"><b>配送车辆金额：</b>
          {{ orderInfo.vehiclePrice }}元
        </a-col>
        <a-col :span="6"><b>配送员数量：</b>
          {{ orderInfo.staffNum }}个
        </a-col>
        <a-col :span="6"><b>配送员金额：</b>
          {{ orderInfo.staffPrice }}元
        </a-col>
        <a-col :span="6"><b>无电梯费用：</b>
          {{ orderInfo.elevator }}元
        </a-col>
      </a-row>
      <br/>
      <br/>
      <div style="text-align: center;margin-top: 180px">
        <a-icon type="smile" theme="twoTone" style="font-size: 130px"/>
        <p style="margin-top: 20px;font-size: 28px">您需要缴纳{{ orderInfo.amount }}元</p>
      </div>
    </div>
    <drawerMap :childrenDrawerShow="childrenDrawer" @handlerClosed="handlerClosed"></drawerMap>
    <div class="drawer-bootom-button">
      <a-popconfirm title="确定放弃编辑？" @confirm="onClose" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
      <a-button @click="next" type="primary" v-if="nextFlag == 1">下一步</a-button>
      <a-button @click="pay" type="primary" v-if="nextFlag == 2">支付</a-button>
    </div>
  </a-drawer>
</template>

<script>
import baiduMap from '@/utils/map/baiduMap'
import drawerMap from '@/utils/map/searchmap/drawerMap'
import {mapState} from 'vuex'

function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}

const formItemLayout = {
  labelCol: {span: 24},
  wrapperCol: {span: 24}
}
export default {
  name: 'inventoryAdd',
  props: {
    orderAddShow: {
      default: false
    }
  },
  components: {
    drawerMap
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.orderAddShow
      },
      set: function () {
      }
    }
  },
  watch: {
    'orderAddShow': function (value) {
      if (value) {
        this.dataList = []
      }
    }
  },
  data () {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      localPoint: {},
      stayAddress: '',
      childrenDrawer: false,
      flagType: 0,
      nextFlag: 1,
      orderInfo: null
    }
  },
  mounted () {
  },
  methods: {
    pay (row) {
      let data = { outTradeNo: this.orderInfo.orderCode, subject: this.orderInfo.orderCode, totalAmount: this.orderInfo.amount, body: '' }
      this.$post('/cos/pay/alipay', data).then((r) => {
        // console.log(r.data.msg)
        // 添加之前先删除一下，如果单页面，页面不刷新，添加进去的内容会一直保留在页面中，二次调用form表单会出错
        const divForm = document.getElementsByTagName('div')
        if (divForm.length) {
          document.body.removeChild(divForm[0])
        }
        const div = document.createElement('div')
        div.innerHTML = r.data.msg // data就是接口返回的form 表单字符串
        // console.log(div.innerHTML)
        document.body.appendChild(div)
        document.forms[0].setAttribute('target', '_self') // 新开窗口跳转
        document.forms[0].submit()
      })
    },
    next () {
      // 获取图片List
      let images = []
      if (this.fileList.length === 0) {
        this.$message.warn('请上传图片')
        return false
      }
      this.fileList.forEach(image => {
        images.push(image.response)
      })
      this.form.validateFields((err, values) => {
        if (!err) {
          this.loading = true
          values.userId = this.currentUser.userId
          values.images = images.length > 0 ? images.join(',') : null
          this.$post('/cos/order-info', {
            ...values
          }).then((r) => {
            this.orderInfo = r.data
            this.nextFlag = 2
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    handleChange (value, record) {
      console.log(value)
      if (value) {
        this.drugList.forEach(e => {
          if (e.drugId === value) {
            record.brand = e.brand
            record.classification = e.classification
            record.dosageForm = e.dosageForm
            record.unitPrice = e.unitPrice
            record.reserve = e.reserve
            record.drugId = e.drugId
            console.log(record)
          }
        })
      }
    },
    handlerClosed (localPoint) {
      this.childrenDrawer = false
      if (localPoint !== null && localPoint !== undefined) {
        this.localPoint = localPoint
        console.log(this.localPoint)
        let address = baiduMap.getAddress(localPoint)
        address.getLocation(localPoint, (rs) => {
          if (rs != null) {
            if (rs.address !== undefined && rs.address.length !== 0) {
              this.stayAddress = rs.address
            }
            if (rs.surroundingPois !== undefined) {
              if (rs.surroundingPois.address !== undefined && rs.surroundingPois.address.length !== 0) {
                this.stayAddress = rs.surroundingPois.address
              }
            }
            let obj = {}
            console.log(this.flagType)
            if (this.flagType === 1) {
              obj['startAddress'] = this.stayAddress
              obj['startLongitude'] = localPoint.lng
              obj['startLatitude'] = localPoint.lat
            } else {
              obj['endAddress'] = this.stayAddress
              obj['endLongitude'] = localPoint.lng
              obj['endLatitude'] = localPoint.lat
            }
            this.form.setFieldsValue(obj)
          }
        })
      }
    },
    showChildrenDrawer (value) {
      this.flagType = value
      this.childrenDrawer = true
    },
    onChildrenDrawerClos () {
      this.childrenDrawer = false
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
    picHandleChange ({fileList}) {
      this.fileList = fileList
    },
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        values.orderDetailList = JSON.stringify(this.dataList)
        if (!err) {
          this.loading = true
          values.userId = this.currentUser.userId
          this.$post('/cos/order-info', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
