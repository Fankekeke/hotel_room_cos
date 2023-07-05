<template>
  <a-modal v-model="show" title="订单评价" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        提交
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="orderData !== null">
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
    </div>
    <a-form :form="form" layout="vertical" style="padding-left: 24px;padding-right: 24px;">
      <a-row :gutter="20">
        <a-col :span="6">
          <a-form-item label='准时得分' v-bind="formItemLayout">
            <a-input-number style="width: 100%" v-decorator="[
            'scheduleScore',
            { rules: [{ required: true, message: '请输入准时得分!' }] }
            ]" :min="1" :max="100" :step="0.1"/>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='服务得分' v-bind="formItemLayout">
            <a-input-number style="width: 100%" v-decorator="[
            'serviceScore',
            { rules: [{ required: true, message: '请输入服务得分!' }] }
            ]" :min="1" :max="100" :step="0.1"/>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='价格得分' v-bind="formItemLayout">
            <a-input-number style="width: 100%" v-decorator="[
            'priceScore',
            { rules: [{ required: true, message: '请输入价格得分!' }] }
            ]" :min="1" :max="100" :step="0.1"/>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='质量得分' v-bind="formItemLayout">
            <a-input-number style="width: 100%" v-decorator="[
            'qualityScore',
            { rules: [{ required: true, message: '请输入质量得分!' }] }
            ]" :min="1" :max="100" :step="0.1"/>
          </a-form-item>
        </a-col>
         <a-col :span="6">
          <a-form-item label='交付得分' v-bind="formItemLayout">
            <a-input-number style="width: 100%" v-decorator="[
            'deliverScore',
            { rules: [{ required: true, message: '请输入交付得分!' }] }
            ]" :min="1" :max="100" :step="0.1"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='评价内容' v-bind="formItemLayout">
            <a-textarea :rows="6" v-decorator="[
            'content',
             { rules: [{ required: true, message: '请输入评价内容!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='图册' v-bind="formItemLayout">
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
  </a-modal>
</template>

<script>
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
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'orderEvaluate',
  props: {
    orderEvaluateVisiable: {
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
        return this.orderEvaluateVisiable
      },
      set: function () {
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
      previewImage: ''
    }
  },
  methods: {
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
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        images.push(image.response)
      })
      this.form.validateFields((err, values) => {
        values.images = images.length > 0 ? images.join(',') : null
        values.orderCode = this.orderData.code
        if (!err) {
          this.loading = true
          this.$post('/cos/order-info/orderEvaluate', {
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
