<template>
  <a-card :bordered="false" class="card-area">
    <div style="background:#ECECEC; padding:30px;margin-top: 30px;margin-bottom: 30px">
      <a-row :gutter="30">
        <a-col :span="6" v-for="(item, index) in roomList" :key="index">
          <a-card :bordered="false">
             <a-carousel autoplay style="height: 250px;" v-if="item.images !== undefined && item.images !== ''">
                <div style="width: 100%;height: 150px" v-for="(item, index) in item.images.split(',')" :key="index">
                  <img :src="'http://127.0.0.1:9527/imagesWeb/'+item" style="width: 100%;height: 250px">
                </div>
              </a-carousel>
            <span slot="title">
              <a-badge status="processing"/>
              <span style="font-size: 14px;font-family: SimHei">
                {{ item.name }} | {{ item.typeName }}
                <span style="margin-left: 15px;color: green" v-if="item.checkStatus == true">已入住</span>
                <span style="margin-left: 15px;color: red" v-if="item.checkStatus == false">未入住</span>
              </span>
            </span>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </a-card>
</template>

<script>
export default {
  name: 'Work',
  data () {
    return {
      roomList: [],
      loading: false
    }
  },
  mounted () {
    this.getWorkStatusList()
  },
  methods: {
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
