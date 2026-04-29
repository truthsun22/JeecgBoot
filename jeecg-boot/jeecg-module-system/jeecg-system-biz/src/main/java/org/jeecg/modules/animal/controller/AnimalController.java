package org.jeecg.modules.animal.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.animal.entity.Animal;
import org.jeecg.modules.animal.service.IAnimalService;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "野生动物管理")
@RestController
@RequestMapping("/animal/animal")
public class AnimalController {

    @Autowired
    private IAnimalService animalService;

    @Operation(summary = "野生动物-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<Animal>> queryPageList(Animal animal,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                 HttpServletRequest req) {
        Result<IPage<Animal>> result = new Result<>();
        QueryWrapper<Animal> queryWrapper = QueryGenerator.initQueryWrapper(animal, req.getParameterMap());
        Page<Animal> page = new Page<>(pageNo, pageSize);
        IPage<Animal> pageList = animalService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @Operation(summary = "野生动物-添加")
    @PostMapping(value = "/add")
    public Result<Animal> add(@RequestBody Animal animal) {
        Result<Animal> result = new Result<>();
        try {
            animalService.save(animal);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    @Operation(summary = "野生动物-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<Animal> edit(@RequestBody Animal animal) {
        Result<Animal> result = new Result<>();
        Animal animalEntity = animalService.getById(animal.getId());
        if (animalEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = animalService.updateById(animal);
            if (ok) {
                result.success("修改成功!");
            }
        }
        return result;
    }

    @Operation(summary = "野生动物-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<Animal> delete(@RequestParam(name = "id", required = true) String id) {
        Result<Animal> result = new Result<>();
        Animal animal = animalService.getById(id);
        if (animal == null) {
            result.error500("未找到对应实体");
        } else {
            animal.setDelFlag(1);
            animalService.updateById(animal);
            result.success("删除成功!");
        }
        return result;
    }

    @Operation(summary = "野生动物-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<Animal> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<Animal> result = new Result<>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            List<String> idList = Arrays.asList(ids.split(","));
            for (String id : idList) {
                Animal animal = animalService.getById(id);
                if (animal != null) {
                    animal.setDelFlag(1);
                    animalService.updateById(animal);
                }
            }
            result.success("删除成功!");
        }
        return result;
    }

    @Operation(summary = "野生动物-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<Animal> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<Animal> result = new Result<>();
        Animal animal = animalService.getById(id);
        if (animal == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(animal);
            result.setSuccess(true);
        }
        return result;
    }

    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Animal animal) {
        QueryWrapper<Animal> queryWrapper = QueryGenerator.initQueryWrapper(animal, request.getParameterMap());
        List<Animal> pageList = animalService.list(queryWrapper);
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        mv.addObject(NormalExcelConstants.FILE_NAME, "野生动物列表");
        mv.addObject(NormalExcelConstants.CLASS, Animal.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("野生动物列表数据", "导出人:" + user.getRealname(), "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<Animal> listAnimals = ExcelImportUtil.importExcel(file.getInputStream(), Animal.class, params);
                for (Animal animal : listAnimals) {
                    animalService.save(animal);
                }
                return Result.OK("文件导入成功！数据行数：" + listAnimals.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK("文件导入失败！");
    }
}
