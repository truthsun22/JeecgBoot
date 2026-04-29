-- ----------------------------
-- 野生动物管理菜单配置
-- ----------------------------

-- 顶级菜单：业务管理（如果不存在）
-- INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) 
-- VALUES ('2200000000000000001', '', '业务管理', '/business', 'layouts/default/index', 1, '', NULL, 0, NULL, '0', 1.00, 0, 'ant-design:appstore-outlined', 0, 0, 0, 0, NULL, 'admin', NOW(), 'admin', NOW(), 0, 0, NULL, 0);

-- 野生动物管理菜单
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) 
VALUES ('2200000000000000002', '', '野生动物管理', '/system/animal', 'system/animal/index', 1, '', NULL, 1, NULL, '0', 1.00, 0, 'ant-design:bug-outlined', 1, 0, 0, 0, NULL, 'admin', NOW(), 'admin', NOW(), 0, 0, NULL, 0);

-- 按钮权限：分页列表查询
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) 
VALUES ('2200000000000000003', '2200000000000000002', '分页列表查询', NULL, NULL, 0, NULL, NULL, 2, 'animal:animal:list', '1', 1.00, 0, NULL, 1, 0, 0, 0, NULL, 'admin', NOW(), NULL, NULL, 0, 0, '1', 0);

-- 按钮权限：添加
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) 
VALUES ('2200000000000000004', '2200000000000000002', '添加', NULL, NULL, 0, NULL, NULL, 2, 'animal:animal:add', '1', 2.00, 0, NULL, 1, 0, 0, 0, NULL, 'admin', NOW(), NULL, NULL, 0, 0, '1', 0);

-- 按钮权限：编辑
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) 
VALUES ('2200000000000000005', '2200000000000000002', '编辑', NULL, NULL, 0, NULL, NULL, 2, 'animal:animal:edit', '1', 3.00, 0, NULL, 1, 0, 0, 0, NULL, 'admin', NOW(), NULL, NULL, 0, 0, '1', 0);

-- 按钮权限：删除
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) 
VALUES ('2200000000000000006', '2200000000000000002', '删除', NULL, NULL, 0, NULL, NULL, 2, 'animal:animal:delete', '1', 4.00, 0, NULL, 1, 0, 0, 0, NULL, 'admin', NOW(), NULL, NULL, 0, 0, '1', 0);

-- 按钮权限：批量删除
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) 
VALUES ('2200000000000000007', '2200000000000000002', '批量删除', NULL, NULL, 0, NULL, NULL, 2, 'animal:animal:deleteBatch', '1', 5.00, 0, NULL, 1, 0, 0, 0, NULL, 'admin', NOW(), NULL, NULL, 0, 0, '1', 0);

-- 按钮权限：导出
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) 
VALUES ('2200000000000000008', '2200000000000000002', '导出', NULL, NULL, 0, NULL, NULL, 2, 'animal:animal:exportXls', '1', 6.00, 0, NULL, 1, 0, 0, 0, NULL, 'admin', NOW(), NULL, NULL, 0, 0, '1', 0);

-- 按钮权限：导入
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) 
VALUES ('2200000000000000009', '2200000000000000002', '导入', NULL, NULL, 0, NULL, NULL, 2, 'animal:animal:importExcel', '1', 7.00, 0, NULL, 1, 0, 0, 0, NULL, 'admin', NOW(), NULL, NULL, 0, 0, '1', 0);
