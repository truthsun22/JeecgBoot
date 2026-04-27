package org.jeecg.modules.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.ImportExcelUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.product.entity.Product;
import org.jeecg.modules.product.service.IProductService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 商品表
 * @Author: jeecg-boot
 * @Date: 2026-04-27
 * @Version:V1.0
 */
@RestController
@RequestMapping("/product")
@Slf4j
@Api(tags = "商品维护接口")
public class ProductController {
    @Autowired
    private IProductService productService;

    /**
     * 分页列表查询
     *
     * @param product
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<?> queryPageList(Product product, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<Product> queryWrapper = QueryGenerator.initQueryWrapper(product, req.getParameterMap());
        Page<Product> page = new Page<Product>(pageNo, pageSize);
        IPage<Product> pageList = productService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加商品
     *
     * @param product
     * @return
     */
    @RequiresPermissions("product:product:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<?> add(@RequestBody Product product) {
        product.setDelFlag(CommonConstant.DEL_FLAG_0);
        productService.save(product);
        return Result.ok("添加成功！");
    }

    /**
     * 更新商品
     *
     * @param product
     * @return
     */
    @RequiresPermissions("product:product:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> eidt(@RequestBody Product product) {
        productService.updateById(product);
        return Result.ok("更新成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @RequiresPermissions("product:product:delete")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Result.error("未找到对应实体");
        }
        productService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequiresPermissions("product:product:deleteBatch")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        if (ids == null || "".equals(ids.trim())) {
            return Result.error("参数不识别！");
        }
        productService.removeByIds(Arrays.asList(ids.split(SymbolConstant.COMMA)));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Product product = productService.getById(id);
        return Result.ok(product);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param product
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Product product) {
        QueryWrapper<Product> queryWrapper = QueryGenerator.initQueryWrapper(product, request.getParameterMap());
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.in("id", selectionList);
        }
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<Product> pageList = productService.list(queryWrapper);
        mv.addObject(NormalExcelConstants.FILE_NAME, "商品列表");
        mv.addObject(NormalExcelConstants.CLASS, Product.class);
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("商品列表数据", "导出人:" + user.getRealname(), "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        List<String> errorMessage = new ArrayList<>();
        int successLines = 0, errorLines = 0;
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<Product> listProducts = ExcelImportUtil.importExcel(file.getInputStream(), Product.class, params);
                for (Product product : listProducts) {
                    product.setDelFlag(CommonConstant.DEL_FLAG_0);
                }
                List<String> list = ImportExcelUtil.importDateSave(listProducts, IProductService.class, errorMessage);
                errorLines += list.size();
                successLines += (listProducts.size() - errorLines);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败！");
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ImportExcelUtil.imporReturnRes(errorLines, successLines, errorMessage);
    }

}
