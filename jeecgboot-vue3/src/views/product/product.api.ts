import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/product/list',
  save = '/product/add',
  edit = '/product/edit',
  get = '/product/queryById',
  delete = '/product/delete',
  exportXlsUrl = '/product/exportXls',
  importExcelUrl = '/product/importExcel',
  deleteBatch = '/product/deleteBatch',
}

/**
 * 导出api
 */
export const getExportUrl = Api.exportXlsUrl;
/**
 * 导入api
 */
export const getImportUrl = Api.importExcelUrl;
/**
 * 查询商品列表
 * @param params
 */
export const getProductList = (params) => {
  return defHttp.get({ url: Api.list, params });
};

/**
 * 保存或者更新商品
 * @param params
 */
export const saveOrUpdateProduct = (params, isUpdate) => {
  let url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url: url, params });
};

/**
 * 查询商品详情
 * @param params
 */
export const getProductById = (params) => {
  return defHttp.get({ url: Api.get, params });
};

/**
 * 删除商品
 * @param params
 */
export const deleteProduct = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.delete, data: params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};

/**
 * 批量删除商品
 * @param params
 */
export const batchDeleteProduct = (params, handleSuccess) => {
  Modal.confirm({
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({ url: Api.deleteBatch, data: params }, { joinParamsToUrl: true }).then(() => {
        handleSuccess();
      });
    },
  });
};
