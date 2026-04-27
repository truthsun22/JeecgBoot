import { BasicColumn, FormSchema } from '/@/components/Table';
import { render } from '/@/utils/common/renderUtils';

export const columns: BasicColumn[] = [
  {
    title: '商品名称',
    dataIndex: 'name',
    width: 200,
    align: 'left',
  },
  {
    title: '商品类型',
    dataIndex: 'productType',
    width: 150,
    customRender: ({ text }) => {
      const color = text == 1 ? 'blue' : text == 2 ? 'green' : 'orange';
      return render.renderTag(render.renderDict(text, 'product_type'), color);
    },
  },
  {
    title: '备注',
    dataIndex: 'remarks',
    width: 300,
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
    customRender: ({ text }) => {
      return text ? text : '';
    },
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'name',
    label: '商品名称',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'productType',
    label: '商品类型',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'product_type',
      stringToNumber: true,
    },
    colProps: { span: 8 },
  },
];

export const formSchema: FormSchema[] = [
  {
    field: 'id',
    label: 'id',
    component: 'Input',
    show: false,
  },
  {
    field: 'name',
    label: '商品名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'productType',
    label: '商品类型',
    component: 'JDictSelectTag',
    required: true,
    componentProps: {
      dictCode: 'product_type',
      type: 'radioButton',
      stringToNumber: true,
      dropdownStyle: {
        maxHeight: '6vh',
      },
    },
  },
  {
    field: 'remarks',
    label: '备注',
    component: 'InputTextArea',
  },
];
