package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.PageDTO;
import com.sky.entity.Employee;
import com.sky.query.PageQuery;

public interface EmployeeService extends IService<Employee> {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    Integer updatePassword(Integer empId, String newPassword, String oldPassword);

    PageDTO<Employee> queryEmpByPage(PageQuery pageQuery);
}
