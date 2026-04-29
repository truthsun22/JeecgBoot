<template>
  <div>
    <BasicTable @register="registerTable">
      <template #tableTitle>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleCreate"> 新增</a-button>
        <a-button type="primary" preIcon="ant-design:export-outlined" @click="onExportXls"> 导出</a-button>
        <j-upload-button type="primary" preIcon="ant-design:import-outlined" @click="onImportXls">导入</j-upload-button>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="batchHandleDelete">
                <Icon icon="ant-design:delete-outlined"></Icon>
                删除
              </a-menu-item>
            </a-menu>
          </template>
          <a-button>
            批量操作
            <Icon icon="ant-design:down-outlined"></Icon>
          </a-button>
        </a-dropdown>
      </template>
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" />
      </template>
    </BasicTable>
    <AnimalModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts" name="system-animal" setup>
  import { useMethods } from '/@/hooks/system/useMethods';
  import { useListPage } from '/@/hooks/system/useListPage';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { useModal } from '/@/components/Modal';
  import AnimalModal from './AnimalModal.vue';
  import { columns, searchFormSchema } from './animal.data';
  import { list, deleteAnimal, batchDeleteAnimal, getExportUrl, getImportUrl, saveOrUpdate } from './animal.api';

  const { handleExportXls, handleImportXls } = useMethods();
  const [registerModal, { openModal }] = useModal();

  const { prefixCls, onExportXls, onImportXls, tableContext } = useListPage({
    designScope: 'animal-template',
    tableProps: {
      title: '野生动物管理',
      api: list,
      columns: columns,
      actionColumn: {
        width: 180,
      },
      formConfig: {
        schemas: searchFormSchema,
      },
    },
    exportConfig: {
      name: '野生动物列表',
      url: getExportUrl,
    },
    importConfig: {
      url: getImportUrl,
    },
  });

  const [registerTable, { reload }, { rowSelection, selectedRowKeys }] = tableContext;

  function handleCreate() {
    openModal(true, {
      isUpdate: false,
    });
  }

  function handleEdit(record) {
    openModal(true, {
      record,
      isUpdate: true,
    });
  }

  function handleDetail(record) {
    openModal(true, {
      record,
      isUpdate: true,
      hideFooter: true,
    });
  }

  async function handleDelete(record) {
    await deleteAnimal({ id: record.id }, handleSuccess);
  }

  async function batchHandleDelete() {
    await batchDeleteAnimal({ ids: selectedRowKeys.value }, handleSuccess);
  }

  function handleSuccess() {
    selectedRowKeys.value = [];
    reload();
  }

  function getTableAction(record) {
    return [
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
      },
      {
        label: '详情',
        onClick: handleDetail.bind(null, record),
      },
      {
        label: '删除',
        popConfirm: {
          title: '确定删除吗?',
          confirm: handleDelete.bind(null, record),
        },
      },
    ];
  }
</script>

<style scoped></style>
