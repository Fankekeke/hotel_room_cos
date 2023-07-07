<template>
  <a-modal v-model="show" title="修改员工" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        修改
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label='员工姓名' v-bind="formItemLayout">
            <a-input v-decorator="[
            'name',
            { rules: [{ required: true, message: '请输入名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='员工性别' v-bind="formItemLayout">
            <a-select v-decorator="[
              'sex',
              { rules: [{ required: true, message: '请输入员工性别!' }] }
              ]">
              <a-select-option value="1">男</a-select-option>
              <a-select-option value="2">女</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='出生日期' v-bind="formItemLayout">
            <a-date-picker style="width: 100%" v-decorator="[
            'birthday',
            { rules: [{ required: true, message: '请输入出生日期!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='籍 贯' v-bind="formItemLayout">
            <a-input v-decorator="[
            'nativeAddress'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='身份证号码' v-bind="formItemLayout">
            <a-input v-decorator="[
            'idCard'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='学历' v-bind="formItemLayout">
            <a-select v-decorator="[
              'diploma'
              ]">
              <a-select-option value="1">高中</a-select-option>
              <a-select-option value="2">专科</a-select-option>
              <a-select-option value="3">本科</a-select-option>
              <a-select-option value="4">硕士</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='学校名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'schoolName'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='家庭住址' v-bind="formItemLayout">
            <a-input v-decorator="[
            'address'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='邮 箱' v-bind="formItemLayout">
            <a-input v-decorator="[
            'mail'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='联系方式' v-bind="formItemLayout">
            <a-input v-decorator="[
            'phone'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='所属部门' v-bind="formItemLayout">
            <a-select v-decorator="[
              'deptId',
              { rules: [{ required: true, message: '请输入所属部门!' }] }
              ]">
              <a-select-option :value="item.id" v-for="(item, index) in deptList" :key="index">{{ item.deptName }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='所属岗位' v-bind="formItemLayout">
            <a-select v-decorator="[
              'positionId',
              { rules: [{ required: true, message: '请输入所属岗位!' }] }
              ]">
              <a-select-option :value="item.id" v-for="(item, index) in positionList" :key="index">{{ item.name }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='照片' v-bind="formItemLayout">
            <a-upload
              name="avatar"
              action="http://127.0.0.1:9527/file/fileUpload/"
              list-type="picture-card"
              :file-list="fileList"
              @preview="handlePreview"
              @change="picHandleChange"
            >
              <div v-if="fileList.length < 2">
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
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'staffEdit',
  props: {
    staffEditVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.staffEditVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      rowId: null,
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      formLoading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      deptList: [],
      positionList: []
    }
  },
  mounted () {
    this.selectDeptList()
    this.selectPositionList()
  },
  methods: {
    selectDeptList () {
      this.$get(`/cos/dept-info/list`).then((r) => {
        this.deptList = r.data.data
      })
    },
    selectPositionList () {
      this.$get(`/cos/position-info/list`).then((r) => {
        this.positionList = r.data.data
      })
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
    responsibleInit (responsible) {
      this.formLoading = false
      if (responsible !== null && responsible !== '') {
        let responsibleList = []
        responsible.split(',').forEach((id, index) => {
          responsibleList.push(parseInt(id))
        })
        this.checkedList = responsibleList
        console.log(JSON.stringify(this.checkedList))
        this.onChange(this.checkedList)
      }
      setTimeout(() => {
        this.formLoading = true
      }, 200)
    },
    setFormValues ({...staff}) {
      this.rowId = staff.id
      let fields = ['name', 'sex', 'birthday', 'nativeAddress', 'idCard', 'diploma', 'schoolName', 'address', 'mail', 'phone', 'deptId', 'positionId']
      let obj = {}
      Object.keys(staff).forEach((key) => {
        if (key === 'sex') {
          staff[key] = staff[key].toString()
        }
        if (key === 'birthday' && staff[key] != null) {
          staff[key] = moment(staff[key])
        }
        if (key === 'images') {
          this.fileList = []
          this.imagesInit(staff['images'])
        }
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = staff[key]
        }
      })
      this.form.setFieldsValue(obj)
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
        if (image.response !== undefined) {
          images.push(image.response)
        } else {
          images.push(image.name)
        }
      })
      this.form.validateFields((err, values) => {
        if (values.birthday) {
          values.birthday = moment(values.birthday).format('YYYY-MM-DD')
        }
        values.id = this.rowId
        values.images = images.length > 0 ? images.join(',') : null
        if (!err) {
          this.loading = true
          this.$put('/cos/staff-info', {
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
