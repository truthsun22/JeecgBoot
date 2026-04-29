import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '动物名称',
    dataIndex: 'animalName',
    width: 200,
  },
  {
    title: '动物类别',
    dataIndex: 'animalType',
    width: 150,
  },
  {
    title: '备注',
    dataIndex: 'remark',
    width: 200,
  },
  {
    title: '创建人',
    dataIndex: 'createBy',
    width: 120,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
  },
  {
    title: '更新人',
    dataIndex: 'updateBy',
    width: 120,
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    width: 180,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    label: '动物名称',
    field: 'animalName',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '动物类别',
    field: 'animalType',
    component: 'Input',
    colProps: { span: 6 },
  },
];

export const formSchema: FormSchema[] = [
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    label: '动物名称',
    field: 'animalName',
    required: true,
    component: 'Input',
  },
  {
    label: '动物类别',
    field: 'animalType',
    required: true,
    component: 'Input',
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
  },
];
