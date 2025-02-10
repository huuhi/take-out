package com.sky.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmpChangePwd;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.PageDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.query.PageQuery;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

//    新增员工
    @PostMapping
    public Result addEmp(@RequestBody EmployeeDTO emp){
//        新增
        Employee employee = BeanUtil.copyProperties(emp, Employee.class);
        employee.setCreateTime(LocalDateTime.now());
        //获取当前登录用户的id
        employee.setCreateUser(emp.getId());
        employeeService.save(employee);
        return Result.success();
    }

//    修改密码
    @PutMapping("editPassword")
    public Result updatePassword(@RequestBody EmpChangePwd ep, @RequestHeader("token") String token){
        log.info("修改密码：{}", ep);
//        获取前端的token
        Claims claims = JwtUtil.parseJWT(token);
        ep.setEmpId((Integer) claims.get(JwtClaimsConstant.EMP_ID));
        Integer result = employeeService.updatePassword(ep.getEmpId(), ep.getNewPassword(), ep.getOldPassword());
        return result==1?Result.success():Result.error("修改密码失败");
    }
//    启用、禁用员工账号
    @PostMapping("status/{status}")
    public Result updateStatus(@PathVariable("status") Integer status, @RequestParam("id") Long id
    ,@RequestHeader("token") String token){
        log.info("修改员工账号状态：{}", id);
        Employee employee = employeeService.getById(id);
        employee.setStatus(status);
//        Claims claims = JwtUtil.parseJWT(token);
//        Long o = Long.valueOf((Integer) claims.get(JwtClaimsConstant.EMP_ID));
        employee.setUpdateUser(JwtUtil.getIdFromToken(token));
        employeeService.updateById(employee);
        return Result.success();
    }
//  分页查询
    @GetMapping("page")
    public Result<PageDTO<Employee>> page(Integer page,Integer pageSize,String name){
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPage(page);
        pageQuery.setPageSize(pageSize);
        pageQuery.setName(name);
        PageDTO<Employee> resilt=employeeService.queryEmpByPage(pageQuery);
        return Result.success(resilt);
    }
//    根据id查询员工
    @GetMapping("{id}")
    public Result<Employee> getEmpById(@PathVariable("id") Long id){
        return Result.success(employeeService.getById(id));
    }
//    修改员工
    @PutMapping
    public Result updateEmp(@RequestBody EmployeeDTO emp,@RequestHeader("token") String token){
        Employee employee = BeanUtil.copyProperties(emp, Employee.class);
//        Claims claims = JwtUtil.parseJWT(token);
//        Long o = Long.valueOf((Integer) claims.get(JwtClaimsConstant.EMP_ID));
//        employee.setUpdateUser(o);
        employee.setUpdateUser(JwtUtil.getIdFromToken(token));
        employeeService.updateById(employee);
        return Result.success();
    }
}
