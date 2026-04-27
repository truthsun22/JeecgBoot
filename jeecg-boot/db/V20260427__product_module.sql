-- =============================================
-- 商品维护子模块数据库脚本
-- 执行日期：2026-04-27
-- =============================================

-- =============================================
-- 1. 创建商品表(product)
-- =============================================
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `sys_org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  `del_flag` int(11) NULL DEFAULT 0 COMMENT '删除状态(0正常 1已删除)',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_type` int(11) NULL DEFAULT NULL COMMENT '商品类型(1：家电，2：饮食，3：其它)',
  `remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品表' ROW_FORMAT = DYNAMIC;

-- =============================================
-- 2. 插入商品类型字典数据
-- =============================================
-- 先插入字典主表
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`, `tenant_id`, `low_app_id`)
VALUES ('product_type_dict', '商品类型', 'product_type', '商品类型字典', 0, 'admin', NOW(), NULL, NULL, 0, 0, NULL);

-- 插入字典项数据
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `item_color`)
VALUES ('product_type_1', 'product_type_dict', '家电', '1', '家电类商品', 1, 1, 'admin', NOW(), NULL, NULL, NULL);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `item_color`)
VALUES ('product_type_2', 'product_type_dict', '饮食', '2', '饮食类商品', 2, 1, 'admin', NOW(), NULL, NULL, NULL);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `item_color`)
VALUES ('product_type_3', 'product_type_dict', '其它', '3', '其它类商品', 3, 1, 'admin', NOW(), NULL, NULL, NULL);

-- =============================================
-- 3. 插入菜单数据
-- =============================================
-- 一级菜单：商品维护
SET @menu_id = 'product_menu_001';
SET @now = NOW();

INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `status`, `del_flag`, `rule_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `internal_or_external`)
VALUES (@menu_id, NULL, '商品维护', '/product/index', 'product/index', NULL, NULL, 0, NULL, '1', 0.00, 0, 'shop', 1, 0, 0, 0, 0, '商品维护管理菜单', '1', 0, 0, 'admin', @now, NULL, NULL, 0);

-- 按钮权限：新增
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`)
VALUES ('product_menu_002', @menu_id, '新增商品', NULL, NULL, 0, NULL, NULL, 2, 'product:product:add', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', @now, NULL, NULL, 0, 0, '1', 0);

-- 按钮权限：编辑
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`)
VALUES ('product_menu_003', @menu_id, '编辑商品', NULL, NULL, 0, NULL, NULL, 2, 'product:product:edit', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', @now, NULL, NULL, 0, 0, '1', 0);

-- 按钮权限：删除
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`)
VALUES ('product_menu_004', @menu_id, '删除商品', NULL, NULL, 0, NULL, NULL, 2, 'product:product:delete', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', @now, NULL, NULL, 0, 0, '1', 0);

-- 按钮权限：批量删除
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`)
VALUES ('product_menu_005', @menu_id, '批量删除商品', NULL, NULL, 0, NULL, NULL, 2, 'product:product:deleteBatch', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', @now, NULL, NULL, 0, 0, '1', 0);

-- 按钮权限：导出excel
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`)
VALUES ('product_menu_006', @menu_id, '导出excel', NULL, NULL, 0, NULL, NULL, 2, 'product:product:exportXls', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', @now, NULL, NULL, 0, 0, '1', 0);

-- 按钮权限：导入excel
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`)
VALUES ('product_menu_007', @menu_id, '导入excel', NULL, NULL, 0, NULL, NULL, 2, 'product:product:importExcel', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', @now, NULL, NULL, 0, 0, '1', 0);
