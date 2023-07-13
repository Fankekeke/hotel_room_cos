<template>
  <a-modal v-model="show" title="商品购买详情" @cancel="onClose" :width="1200">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="evaluateData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基本信息</span></a-col>
        <a-col :span="8"><b>采购单号：</b>
          {{ evaluateData.recordCode }}
        </a-col>
        <a-col :span="8"><b>总金额：</b>
          {{ evaluateData.totalPrice ? evaluateData.totalPrice + '元': '- -' }}
        </a-col>
        <a-col :span="8"><b>订单状态：</b>
          <span v-if="evaluateData.status == 1">已支付</span>
          <span v-if="evaluateData.status == 2">退款中</span>
          <span v-if="evaluateData.status == 3">已退货</span>
          <span v-if="evaluateData.status == 4">驳回</span>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>购买用户：</b>
          {{ evaluateData.name }}
        </a-col>
        <a-col :span="8"><b>身份证号码：</b>
          {{ evaluateData.idCard ? evaluateData.idCard : '- -' }}
        </a-col>
        <a-col :span="8"><b>联系方式：</b>
          {{ evaluateData.phone ? evaluateData.phone : '- -' }}
        </a-col>
      </a-row>
      <br/>
    </div>
    <a-row style="padding-left: 24px;padding-right: 24px;font-family: SimHei">
      <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">购买信息</span></a-col>
      <a-col :span="24">
        <a-table :columns="columns" :data-source="goodsList">
        </a-table>
      </a-col>
    </a-row>
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
    },
    columns () {
      return [{
        title: '商品名称',
        dataIndex: 'name'
      }, {
        title: '规格',
        dataIndex: 'model'
      }, {
        title: '数量',
        dataIndex: 'outNum'
      }, {
        title: '商品图片',
        dataIndex: 'images',
        customRender: (text, record, index) => {
          if (!record.images) return <a-avatar shape="square" icon="user" />
          return <a-popover>
            <template slot="content">
              <a-avatar shape="square" size={132} icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images.split(',')[0] } />
            </template>
            <a-avatar shape="square" icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images.split(',')[0] } />
          </a-popover>
        }
      }, {
        title: '价格',
        dataIndex: 'itemPrice',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text + '元'
          } else {
            return '- -'
          }
        }
      }]
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
      goodsList: [],
      logisticsList: [],
      recordInfo: null,
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
      this.$get(`/cos/order-info/record/detail/${id}`).then((r) => {
        this.goodsList = r.data.goods
        this.recordInfo = r.data.record
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
