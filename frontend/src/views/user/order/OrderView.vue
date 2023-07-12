<template>
  <a-modal v-model="show" title="订单详情" @cancel="onClose" :width="900">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei">
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="orderInfo !== null">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>订单编号：</b>
          {{ orderInfo.code }}
        </a-col>
        <a-col :span="8"><b>入住时间：</b>
          {{ orderInfo.startDate ? orderInfo.startDate : '- -' }}
        </a-col>
        <a-col :span="8"><b>结束时间：</b>
          {{ orderInfo.endDate ? orderInfo.endDate : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="orderInfo !== null">
        <a-col :span="8"><b>入住天数：</b>
          {{ orderInfo.days }} 天
        </a-col>
        <a-col :span="8"><b>每日租金：</b>
          {{ orderInfo.rentDay }} 元
        </a-col>
        <a-col :span="8"><b>总金额：</b>
          {{ orderInfo.totalPrice }} 元
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="orderInfo !== null">
        <a-col :span="8"><b>支付状态：</b>
          <span v-if="orderInfo.orderStatus == 0" style="color: red">未支付</span>
          <span v-if="orderInfo.orderStatus == 1" style="color: green">已支付</span>
        </a-col>
        <a-col :span="8"><b>是否退房：</b>
          <span v-if="orderInfo.recedeFlag == 0" style="color: green">否</span>
          <span v-if="orderInfo.recedeFlag == 1" style="color: red">是</span>
        </a-col>
        <a-col :span="8"><b>下单时间：</b>
          {{ orderInfo.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="roomInfo != null">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">房间信息</span></a-col>
        <a-col :span="8"><b>房间名称：</b>
          {{ roomInfo.name !== null ? roomInfo.name : '- -' }}
        </a-col>
        <a-col :span="8"><b>楼层：</b>
          {{ roomInfo.floor !== null ? roomInfo.floor : '- -' }}
        </a-col>
        <a-col :span="8"><b>价格/日：</b>
          {{ roomInfo.rentPrice !== null ? roomInfo.rentPrice : '- -' }}元
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="roomInfo != null">
        <a-col :span="8"><b>发布时间：</b>
          {{ roomInfo.createDate !== null ? roomInfo.createDate : '- -' }}
        </a-col>
        <a-col :span="8"><b>负责人：</b>
          {{ roomInfo.contact !== null ? roomInfo.contact : '- -' }}
        </a-col>
        <a-col :span="8"><b>联系方式：</b>
          {{ roomInfo.phone !== null ? roomInfo.phone : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="roomInfo != null">
        <a-col :span="8"><b>房间大小：</b>
          {{ roomInfo.roomSize !== null ? roomInfo.roomSize : '- -' }}
        </a-col>
        <a-col :span="8"><b>房间编号：</b>
          {{ roomInfo.code !== null ? roomInfo.code : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row :gutter="10" style="padding-left: 24px;padding-right: 24px;" v-if="roomInfo != null">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">包含设施</span></a-col>
        <a-col :span="3" style="text-align: center">
          <div :style="{color: roomInfo.hasBed == 1 ? 'green' : 'red'}">床</div>
          <img src="http://127.0.0.1:9527/imagesWeb/chuang.png">
        </a-col>
        <a-col :span="3" style="text-align: center">
          <div :style="{color: roomInfo.hasRefrigerator == 1 ? 'green' : 'red'}">冰箱</div>
          <img src="http://127.0.0.1:9527/imagesWeb/dankaimenbingxiang.png">
        </a-col>
        <a-col :span="3" style="text-align: center">
          <div :style="{color: roomInfo.hasAirConditioner == 1 ? 'green' : 'red'}">空调</div>
          <img src="http://127.0.0.1:9527/imagesWeb/kongtiao.png">
        </a-col>
        <a-col :span="3" style="text-align: center">
          <div :style="{color: roomInfo.hasBroadband == 1 ? 'green' : 'red'}">宽带</div>
          <img src="http://127.0.0.1:9527/imagesWeb/kuandai.png">
        </a-col>
        <a-col :span="3" style="text-align: center">
          <div :style="{color: roomInfo.hasBalcony == 1 ? 'green' : 'red'}">阳台</div>
          <img src="http://127.0.0.1:9527/imagesWeb/loutianyangtai.png">
        </a-col>
        <a-col :span="3" style="text-align: center">
          <div :style="{color: roomInfo.hasGasStoves == 1 ? 'green' : 'red'}">燃气灶</div>
          <img src="http://127.0.0.1:9527/imagesWeb/ranqizao.png">
        </a-col>
        <a-col :span="3" style="text-align: center">
          <div :style="{color: roomInfo.hasWaterHeater == 1 ? 'green' : 'red'}">热水器</div>
          <img src="http://127.0.0.1:9527/imagesWeb/reshuiqi.png">
        </a-col>
        <a-col :span="3" style="text-align: center">
          <div :style="{color: roomInfo.hasSofa == 1 ? 'green' : 'red'}">沙发</div>
          <img src="http://127.0.0.1:9527/imagesWeb/shafa_1.png">
        </a-col>
        <a-col :span="24" style="margin: 20px 0px"></a-col>
        <a-col :span="3" style="text-align: center">
          <div :style="{color: roomInfo.hasCook == 1 ? 'green' : 'red'}">做饭</div>
          <img src="http://127.0.0.1:9527/imagesWeb/weibolu.png">
        </a-col>
        <a-col :span="3" style="text-align: center">
          <div :style="{color: roomInfo.hasBathroom == 1 ? 'green' : 'red'}">卫生间</div>
          <img src="http://127.0.0.1:9527/imagesWeb/weiyu.png">
        </a-col>
        <a-col :span="3" style="text-align: center">
          <div :style="{color: roomInfo.hasTelevision == 1 ? 'green' : 'red'}">电视</div>
          <img src="http://127.0.0.1:9527/imagesWeb/dianshi.png">
        </a-col>
        <a-col :span="3" style="text-align: center">
          <div :style="{color: roomInfo.hasWashingMachine == 1 ? 'green' : 'red'}">洗衣机</div>
          <img src="http://127.0.0.1:9527/imagesWeb/xiyiji.png">
        </a-col>
        <a-col :span="3" style="text-align: center">
          <div :style="{color: roomInfo.hasWardrobe == 1 ? 'green' : 'red'}">衣柜</div>
          <img src="http://127.0.0.1:9527/imagesWeb/yigui.png">
        </a-col>
        <a-col :span="3" style="text-align: center">
          <div :style="{color: roomInfo.hasRangeHood == 1 ? 'green' : 'red'}">油烟机</div>
          <img src="http://127.0.0.1:9527/imagesWeb/youyanji.png">
        </a-col>
        <a-col :span="3" style="text-align: center">
          <div :style="{color: roomInfo.hasHeating == 1 ? 'green' : 'red'}">暖气</div>
          <img src="http://127.0.0.1:9527/imagesWeb/zaotai.png">
        </a-col>
        <a-col :span="3" style="text-align: center">
          <div :style="{color: roomInfo.hasDoorLock == 1 ? 'green' : 'red'}">智能门锁</div>
          <img src="http://127.0.0.1:9527/imagesWeb/zhinengmensuo.png">
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">房间图片</span></a-col>
        <span v-if="fileList.length === 0">暂无图片</span>
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
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">房间备注</span></a-col>
        {{ roomInfo.content !== null ? roomInfo.content : '- -' }}
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="typeInfo != null">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">房间类型</span></a-col>
        <a-col :span="8"><b>类型编号：</b>
          {{ typeInfo.code !== null ? typeInfo.code : '- -' }}
        </a-col>
        <a-col :span="8"><b>房间类型：</b>
          {{ typeInfo.typeName !== null ? typeInfo.typeName : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="typeInfo != null">
        <a-col :span="24"><b>备注：</b>
          {{ typeInfo.remark !== null ? typeInfo.remark : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="userInfo !== null">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">用户信息</span></a-col>
        <a-col :span="8"><b>用户编号：</b>
          {{ userInfo.code }}
        </a-col>
        <a-col :span="8"><b>用户名称：</b>
          {{ userInfo.name }}
        </a-col>
        <a-col :span="8"><b>联系方式：</b>
          {{ userInfo.phone }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>身份证号码：</b>
          {{ userInfo.idCard }}
        </a-col>
        <a-col :span="8"><b>性别：</b>
          <span v-if="userInfo.sex == 1">男</span>
          <span v-if="userInfo.sex == 2">女</span>
        </a-col>
      </a-row>
    </div>
  </a-modal>
</template>

<script>
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
  name: 'orderView',
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
    show: {
      get: function () {
        return this.orderShow
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
      current: 0,
      userInfo: null,
      orderInfo: null,
      roomInfo: null,
      typeInfo: null
    }
  },
  watch: {
    orderShow: function (value) {
      if (value) {
        this.dataInit(this.orderData.id)
        this.imagesInit(this.orderData.images)
      }
    }
  },
  methods: {
    dataInit (id) {
      this.$get(`/cos/order-info/detail/${id}`).then((r) => {
        this.userInfo = r.data.user
        this.orderInfo = r.data.order
        this.roomInfo = r.data.room
        this.typeInfo = r.data.type
      })
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
