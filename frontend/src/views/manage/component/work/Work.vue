<template>
  <div style="background:#ECECEC; padding:30px;margin-top: 30px;margin-bottom: 30px">
    <div style="height: 500px;">
      <div style="height: 350px;background-image: url(../static/img/living-room-1853203_1920.jpg);padding: 50px">
        <div style="font-size: 35px;font-weight: 500;color: white;font-family: SimHei">你好 朋友</div>
        <div style="font-size: 22px;font-weight: 500;color: white;font-family: SimHei">让您入住更轻松</div>
        <div style="height: 250px;margin-top: 100px">
          <a-card :bordered="false" hoverable style="height: 100%;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);color:#fff">
            <a-row style="padding: 50px;margin: 0 auto">
              <a-col :span="16">
                <a-row>
                  <a-col :span="18">
                    <a-range-picker @change="onChange" style="width: 100%" size="large"/>
                  </a-col>
                  <a-col :span="4" :offset="2">
                    <a-button type="primary" size="large">
                      查找
                    </a-button>
                  </a-col>
                  <a-col :span="24"></a-col>
                  <a-col :span="24" style="font-size: 15px;font-family: SimHei">
                    <div style="margin-top: 10px">
                      <a style="margin-right: 15px" v-for="(item, index) in roomTypeList" :key="index">{{ item.typeName }}</a>
                    </div>
                    <a-col :span="24" style="font-size: 15px;font-family: SimHei">
                      <div style="margin-top: 10px">

                      </div>
                    </a-col>
                  </a-col>
                </a-row>
              </a-col>
              <a-col :span="6" :offset="2">
              </a-col>
            </a-row>
          </a-card>
        </div>
      </div>
    </div>
    <a-row :gutter="30" style="padding: 35px;margin: 0 auto">
      <a-col :span="6" v-for="(item, index) in roomList" :key="index">
        <div style="background: #e8e8e8">
          <a-carousel autoplay style="height: 250px;" v-if="item.images !== undefined && item.images !== ''">
            <div style="width: 100%;height: 150px" v-for="(item, index) in item.images.split(',')" :key="index">
              <img :src="'http://127.0.0.1:9527/imagesWeb/'+item" style="width: 100%;height: 250px">
            </div>
          </a-carousel>
          <a-card :bordered="false">
            <div slot="title">
              <div style="font-size: 14px;font-family: SimHei">
                {{ item.name }} | {{ item.typeName }} | {{ item.floor }} | {{ item.roomSize }}
              </div>
            </div>
            <template slot="actions" class="ant-card-actions">
              <a-icon key="heart" type="heart" style="color: red"/>
              <a-icon key="ellipsis" type="ellipsis" @click="view(item)"/>
            </template>
          </a-card>
        </div>
      </a-col>
    </a-row>
    <rent-view :rentShow="rentView.visiable" :rentData="rentView.data" @close="rentView.visiable = false"></rent-view>
  </div>
</template>

<script>

import RentView from './RentView.vue'

export default {
  name: 'Work',
  components: {RentView},
  data () {
    return {
      roomList: [],
      roomTypeList: [],
      loading: false,
      rentView: {
        visiable: false,
        data: null
      }
    }
  },
  mounted () {
    this.getWorkStatusList()
    this.getRoomType()
  },
  methods: {
    view (record) {
      this.rentView.visiable = true
      this.rentView.data = record
    },
    getRoomType () {
      this.$get(`/cos/room-type/list`).then((r) => {
        this.roomTypeList = r.data.data
      })
    },
    getWorkStatusList () {
      this.$get(`/cos/order-info/room/status`).then((r) => {
        this.roomList = r.data.data
      })
    }
  }
}
</script>

<style scoped>
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
.ant-carousel >>> .slick-slide {
  text-align: center;
  height: 250px;
  line-height: 250px;
  overflow: hidden;
}

</style>
