import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/animal/animal/list',
  save = '/animal/animal/add',
  edit = '/animal/animal/edit',
  deleteAnimal = '/animal/animal/delete',
  deleteBatch = '/animal/animal/deleteBatch',
  importExcel = '/animal/animal/importExcel',
  exportXls = '/animal/animal/exportXls',
}

export const getExportUrl = Api.exportXls;
export const getImportUrl = Api.importExcel;

export const list = (params) => defHttp.get({ url: Api.list, params });

export const deleteAnimal = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteAnimal, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};

export const batchDeleteAnimal = (params, handleSuccess) => {
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

export const saveOrUpdate = (params, isUpdate) => {
  let url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url: url, params });
};
