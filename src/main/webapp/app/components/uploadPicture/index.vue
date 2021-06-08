<template>
  <div>
    <a-row :gutter="30">
      <a-col v-bind="grid">
        <div class="Nav">
          <div class="input">
            <a-input-search style="margin-bottom: 8px" placeholder="选择分类" v-model="filterText" clearable />
          </div>
          <div class="trees-coadd">
            <div class="scollhide">
              <div class="trees">
                <a-tree
                  ref="tree"
                  :tree-data="treeData2"
                  :filter-node-method="filterNode"
                  :replace-fields="defaultProps"
                  highlight-current
                  @select="onSelectTree"
                >
                </a-tree>
              </div>
            </div>
          </div>
        </div>
      </a-col>
      <a-col v-bind="grid2" class="colLeft">
        <div class="conter mb15">
          <div class="bnt">
            <a-button size="small" type="primary" class="mb10 mr10" @click="checkPics">使用选中图片</a-button>
            <a-upload
              class="upload-demo mr10 mb15"
              name="image"
              :action="uploadImageUrl"
              :headers="authHeader"
              :show-upload-list="false"
              :data="getUploadParam"
              @change="handleChange"
            >
              <a-button size="small" type="primary">点击上传</a-button>
            </a-upload>
            <a-button type="success" size="small" @click.stop="onAdd">添加分类</a-button>
            <a-button type="error" size="small" class="mr10" :disabled="checkPicList.length === 0" @click.stop="deletePicList('图片')"
              >删除图片
            </a-button>
            <a-select v-model="sleOptions.attachment_category_name" placeholder="图片移动至" class="mb15" size="small" style="width: 120px">
              <a-select-option
                :value="sleOptions.attachment_category_id"
                style="max-width: 560px; height: 200px; overflow: auto; background-color: #fff"
              >
                <a-tree
                  ref="tree2"
                  :tree-data="treeData2"
                  :filter-node-method="filterNode"
                  :props="defaultProps"
                  highlight-current
                  @node-click="handleSelClick"
                />
              </a-select-option>
            </a-select>
          </div>
          <div class="pictrueList acea-row">
            <div v-show="isShowPic" class="imagesNo">
              <i class="el-icon-picture" style="font-size: 60px; color: rgb(219, 219, 219)" />
              <span class="imagesNo_sp">图片库为空</span>
            </div>
            <div class="conters">
              <div v-for="(item, index) in pictrueList.list" :key="index" class="gridPic">
                <img v-lazy="item.url" :class="item.isSelect ? 'on' : ''" @click="changImage(item, index, pictrueList.list)" />
                <!--<img v-if="index === 0" v-lazy="item.localImg?item.localImg:item.sattDir" :class="item.isSelect ? 'on': '' " @click="changImage(item, index, pictrueList.list)" />-->
                <!--<img v-else v-lazy="item.sattDir?item.sattDir:localImg" :class="item.isSelect ? 'on': '' " @click="changImage(item, index, pictrueList.list)" />-->
              </div>
            </div>
          </div>
        </div>
        <div class="block">
          <a-pagination
            :page-sizes="[12, 24, 48, 60]"
            :page-size="tableData.limit"
            :current-page="tableData.page"
            :pager-count="5"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pictrueList.total"
            @size-change="handleSizeChange"
            @current-change="pageChange"
          />
        </div>
      </a-col>
    </a-row>
    <a-modal
      destroy-on-close
      :title="bizTitle"
      :visible.sync="visible"
      :close-on-click-modal="false"
      :modal="modals"
      :footer="null"
      @cancel="closeModel"
    >
      <a-form-model ref="editPram" :model="editPram" label-width="100px" v-loading="loading">
        <a-form-model-item label="上级分类">
          <a-tree-select v-model="editPram.parentId" style="width: 100%" :tree-data="treeData2" :replaceFields="categoryProps">
          </a-tree-select>
        </a-form-model-item>
        <a-form-model-item
          label="分类名称"
          prop="title"
          :rules="[{ required: true, message: '请输入分类名称', trigger: ['blur', 'change'] }]"
        >
          <a-input v-model="editPram.title" placeholder="分类名称" />
        </a-form-model-item>
        <a-form-model-item label="排序">
          <a-input-number v-model="editPram.sort" />
        </a-form-model-item>
        <a-form-model-item>
          <a-button type="primary" @click="handlerSubmit('editPram')">确定</a-button>
        </a-form-model-item>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
import UploadImageService from '@/entities/files/upload-image/upload-image.service';
import ResourceCategoryService from '@/entities/files/resource-category/resource-category.service';
import { getFilter } from '@/utils/entity-list-utils';
import { UPLOAD_IMAGE_URL } from '@/constants';
import { ResourceCategory } from '@/shared/model/files/resource-category.model';

export default {
  name: 'Upload',
  props: {
    isMore: {
      type: Boolean,
      default: false,
    },
    modelName: {
      type: String,
      default: '',
    },
    checkedMore: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      uploadImageUrl: UPLOAD_IMAGE_URL,
      loading: false,
      modals: false,
      allTreeList: [],
      categoryProps: {
        value: 'id',
        label: 'title',
        children: 'children',
        key: 'id',
      },
      editPram: {
        parentId: null,
        title: '',
        sort: 1,
        id: null,
      },
      visible: false,
      bizTitle: '',
      sleOptions: {
        attrId: '',
        pid: null,
      },
      list: [],
      grid: {
        xl: 8,
        lg: 8,
        md: 8,
        sm: 8,
        xs: 24,
      },
      grid2: {
        xl: 16,
        lg: 16,
        md: 16,
        sm: 16,
        xs: 24,
      },
      filterText: '',
      treeData: [],
      treeData2: [],
      defaultProps: {
        children: 'children',
        title: 'title',
        key: 'id',
      },
      tableData: {
        page: 1,
        limit: 12,
        pid: null,
      },
      classifyId: 0,
      // myHeaders: { 'X-Token': getToken() },
      treeFrom: {
        status: -1,
        type: 2,
      },
      pictrueList: {
        list: [],
        total: 0,
      },
      isShowPic: false,
      checkPicList: [],
      ids: [],
      listPram: {
        pid: 0,
        type: 2,
        status: 0,
        name: '',
        page: 1,
        limit: 9999,
      },
      localImg: '',
      authHeader: { Authorization: 'Bearer ' },
    };
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    },
  },
  mounted() {
    // if (this.$route && this.$route.query.field === 'dialog') import('../../../public/UEditor/dialogs/internal.js')
    const token = localStorage.getItem('jhi-authenticationToken') || sessionStorage.getItem('jhi-authenticationToken');
    this.authHeader.Authorization = 'Bearer ' + token;
    this.getList();
    this.getFileList();
    // this.handlerGetList()
  },
  methods: {
    // 选取图片后自动回调，里面可以获取到文件
    imgSaveToUrl(event) {
      // 也可以用file
      this.localFile = event.raw; // 或者 this.localFile=file.raw

      // 转换操作可以不放到这个函数里面，
      // 因为这个函数会被多次触发，上传时触发，上传成功也触发
      let reader = new FileReader();
      reader.readAsDataURL(this.localFile); // 这里也可以直接写参数event.raw

      // 转换成功后的操作，reader.result即为转换后的DataURL ，
      // 它不需要自己定义，你可以console.integralLog(reader.result)看一下
      reader.onload = () => {
        // console.integralLog(reader.result)
      };

      /* 另外一种本地预览方法 */
      let URL = window.URL || window.webkitURL;
      this.localImg = URL.createObjectURL(event.raw);
      // 转换后的地址为 blob:http://xxx/7bf54338-74bb-47b9-9a7f-7a7093c716b5
    },
    closeModel() {
      // this.$refs['editPram'].resetFields();
      this.$refs.editPram.resetFields();
      this.visible = false;
    },
    handlerSubmit(formName) {
      this.$refs.editPram.validate(valid => {
        if (valid) {
          let resourceCategoryService = new ResourceCategoryService();
          let resourceCategory = new ResourceCategory();
          console.log('this.editPram', this.editPram);
          resourceCategory.id = this.editPram.id;
          resourceCategory.parentId = this.editPram.parentId;
          resourceCategory.title = this.editPram.title;
          if (resourceCategory.id) {
            resourceCategoryService.update(resourceCategory).subscribe(res => {
              this.$message.success('创建成功');
              this.visible = false;
            });
          } else {
            resourceCategoryService.create(resourceCategory).subscribe(res => {
              this.$message.success('更新成功');
              this.visible = false;
            });
          }
          this.getList();
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    // 表单分类
    handlerGetList() {
      /*let datas = {
                name: '全部分类',
                id: ''
            }
            treeCategroy(this.treeFrom).then(data => {
                this.allTreeList = data
                this.allTreeLis.unshift(datas)
            })*/
    },
    // 搜索分类
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    // 所有分类
    getList() {
      const data = {
        name: '全部分类',
        id: 10000,
      };
      const resourceCategoryService = new ResourceCategoryService();
      resourceCategoryService.tree().subscribe(res => {
        this.treeData2 = res.data;
      });
      /*treeCategroy(this.treeFrom).then(res => {
                this.treeData = res
                this.treeData.unshift(data)
                this.treeData2 = [...this.treeData]
            })*/
    },
    // 添加分类
    onAdd(id) {
      this.tableData.pid = id;
      // if (this.tableData.pid === 10000) this.tableData.pid = 0
      this.bizTitle = '添加分类';
      this.visible = true;
      if (id)
        this.editPram = {
          pid: id,
          name: '',
          type: 2,
          sort: 1,
          status: 0,
          url: 'url',
          id: null,
        };
    },
    // 编辑
    onEdit(id) {
      if (id === 10000) id = 0;
      this.bizTitle = '编辑分类';
      this.loading = true;
      /*infoCategroy({id: id}).then((res) => {
                this.editPram = res
                this.loading = false
            })*/
      this.visible = true;
    },
    // 删除
    handleDelete(id) {
      this.$confirm({
        title: '删除图片',
        content: '是否删除选中的图片？',
        onOk() {
          return new Promise((resolve, reject) => {
            const resourceCategoryService = new ResourceCategoryService();
            resourceCategoryService.delete(id).subscribe(() => {
              this.$message.success('删除成功');
              this.getList();
            });
          }).catch(() => console.log('Oops errors!'));
        },
        onCancel() {},
      });
    },
    handleNodeClick(data) {
      this.tableData.pid = data.id;
      this.getFileList();
    },

    // 上传
    handleUploadForm(param) {
      const { fileList } = this;
      const formData = new FormData();
      /*fileList.forEach(file => {
                formData.append('files[]', file);
            });*/
      const data = {
        model: this.modelName ? this.modelName : this.$route.path.split('/')[1],
        pid: this.tableData.pid,
      };
      formData.append('image', param.file);
      let loading = this.$loading({
        lock: true,
        text: '上传中，请稍候...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)',
      });
      const uploadImageService = new UploadImageService();
      uploadImageService
        .upload(formData)
        .then(res => {
          loading.close();
          this.$message.success('上传成功');
          this.getFileList();
        })
        .catch(res => {
          loading.close();
          this.$message.error('上传失败');
        });
    },
    // 文件列表
    getFileList() {
      const paginationQuery = {
        listModelName: 'UploadImage',
        page: this.tableData.page - 1,
        size: this.tableData.limit,
      };
      if (this.tableData.pid) {
        paginationQuery.filter = { 'categoryId.equals': this.tableData.pid };
      }
      const uploadImageService = new UploadImageService();
      uploadImageService.retrieve(paginationQuery).subscribe(res => {
        this.pictrueList.list = res.data;
        if (this.tableData.page === 1 && this.pictrueList.list.length > 0) this.pictrueList.list[0].localImg = this.localImg;
        this.isShowPic = !this.pictrueList.list.length;
        this.pictrueList.total = Number(res.headers['x-total-count']);
      });
    },
    pageChange(page) {
      this.tableData.page = page;
      this.getFileList();
    },
    handleSizeChange(val) {
      this.tableData.limit = val;
      this.getFileList();
    },
    // 选中图片
    changImage(item, index, row) {
      let selectItem = '';
      this.$set(item, 'isSelect', item.isSelect === undefined ? true : !item.isSelect);
      selectItem = this.pictrueList.list.filter(item => {
        return item.isSelect === true;
      });
      this.checkPicList = selectItem;
      this.ids = [];
      this.checkPicList.map((item, index) => {
        this.ids.push(item.id);
      });
    },
    // 点击使用选中图片
    checkPics() {
      if (!this.checkPicList.length) return this.$message.warning('请先选择图片');
      // if (this.$route && this.$route.query.field === 'dialog') {
      //   let str = '';
      //   for (let i = 0; i < this.checkPicList.length; i++) {
      //     str += '<img src="' + this.checkPicList[i].sattDir + '">'
      //   }
      //   /* eslint-disable */
      //   nowEditor.dialog.close(true);
      //   nowEditor.editor.setContent(str,true)
      // }else{
      if (!this.isMore && this.checkPicList.length > 1) {
        return this.$warning({ content: '最多只能选一张图片' });
      }
      console.log('checkMore', this.checkedMore);
      this.$emit('getImage', [...this.checkedMore, ...this.checkPicList]);
      // }
    },
    // 删除图片
    deletePicList(tit) {
      this.$confirm('是否删除选中的图片？', '删除图片')
        .then(() => {
          const uploadImageService = new UploadImageService();
          uploadImageService.deleteByIds(this.ids).subscribe(() => {
            this.$message.success('刪除成功');
            this.getFileList();
            this.checkPicList = [];
          });
        })
        .catch(err => {
          console.log('失败', err);
        });
    },
    // 移动分类点击
    handleSelClick(node) {
      if (this.ids.length) {
        this.sleOptions = {
          attrId: this.ids.join(','),
          pid: node.id,
        };
        this.getMove();
      } else {
        this.$message.warning('请先选择图片');
      }
    },
    getMove() {
      /*attachmentMoveApi(this.sleOptions).then(async res => {
                this.$message.success('操作成功')
                this.clearBoth()
                this.getFileList()
            }).catch(res => {
                this.clearBoth()
            })*/
    },
    clearBoth() {
      this.sleOptions = {
        attrId: '',
        pid: null,
      };
      this.checkPicList = [];
      this.ids = [];
    },
    onSelectTree(selectedKeys, info) {
      this.tableData.pid = selectedKeys;
      this.getFileList();
    },
    getUploadParam() {
      return { categoryId: Number(this.tableData.pid) };
    },
    handleChange(info) {
      if (info.file.status === 'uploading') {
        this.loading = true;
        return;
      }
      if (info.file.status === 'done') {
        // Get this url from response in real world.
        this.getFileList();
      }
    },
  },
};
</script>

<style scoped lang="scss">
.temp {
  height: 0;
  margin-bottom: 0;
  border: none;
  padding: 0;
}

.selectTreeClass {
  background: #d5e8fc;
}

.treeBox {
  width: 100%;
  height: 100%;
}

.upload-demo {
  display: inline-block !important;
}

.tree_w {
  padding: 20px 30px;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
  color: #4386c6;
}

.custom-tree-node-label {
  display: block;
  width: 125px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.el-ic {
  display: none;

  i,
  span {
    /*padding: 0 14px;*/
    font-size: 18px;
    font-weight: 600;
  }

  .svg-icon {
    color: #4386c6;
  }
}

.el-tree-node__content {
  height: 38px;
}

.el-tree-node__expand-icon {
  color: #428bca;
  /*padding: 10px 10px 0px 10px !important;*/
}

.custom-tree-node:hover .el-ic {
  color: #428bca !important;
  display: inline-block;
}

.custom-tree-node:hover {
  font-weight: bold;
}

.el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content :hover {
  .el-tree-node__expand-icon.is-leaf {
    color: transparent;
    cursor: default;
  }

  /*background-color: #3998d9;*/
  .custom-tree-node {
    font-weight: bold;
  }

  .el-tree-node__expand-icon {
    font-weight: bold;
  }
}

.el-dialog__body {
  .upload-container .image-preview .image-preview-wrapper img {
    height: 100px;
  }

  .el-dialog .el-collapse-item__wrap {
    padding-top: 0;
  }

  .spatial_img {
    .el-collapse-item__wrap {
      margin-bottom: 0;
      padding-top: 0;
    }
  }

  .upload-container .image-preview .image-preview-wrapper {
    width: 120px;
  }

  .upload-container .image-preview .image-preview-action {
    line-height: 100px;
    height: 100px;
  }
}

.trees-coadd {
  width: 100%;
  border-radius: 4px;
  overflow: hidden;
  position: relative;

  .scollhide {
    overflow-x: hidden;
    overflow-y: scroll;
    padding: 10px 0 10px 0;
    box-sizing: border-box;

    .trees {
      width: 100%;
      max-height: 374px;
    }
  }

  .scollhide::-webkit-scrollbar {
    display: none;
  }
}

.conters {
  display: flex;
  flex-wrap: wrap;
}

.conters:after {
  content: '';
  width: 410px !important;
}

.gridPic {
  margin-right: 20px;
  margin-bottom: 10px;
  width: 110px;
  height: 110px;
  cursor: pointer;

  img {
    width: 100%;
    height: 100%;
    display: block;
  }
}

.conter {
  width: 99%;
  height: 100%;

  .bnt {
    width: 100%;
    padding: 0 13px 10px 15px;
    box-sizing: border-box;
  }

  .pictrueList {
    padding-left: 15px;
    width: 100%;

    el-image {
      width: 100%;
      border: 2px solid #fff;
    }

    .on {
      border: 2px solid #5fb878;
    }
  }

  .el-image {
    width: 110px;
    height: 110px;
    cursor: pointer;
  }

  .imagesNo {
    width: 100%;
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;
    margin: 65px 0;

    .imagesNo_sp {
      font-size: 13px;
      color: #dbdbdb;
      line-height: 3;
    }
  }
}
</style>
