package xd.medicine.service;

import com.github.pagehelper.PageInfo;

import xd.medicine.entity.bo.Doctor;
import xd.medicine.entity.dto.AvaDoctor;

import java.util.List;


/**
 * created by xdCao on 2017/12/19
 */
public interface DoctorService {

    Integer insertDoctor(Doctor doctor);

    Integer updateDoctor(Doctor doctor);

    Integer deleteDoctorById(int id);

    Doctor getDoctorById(int id);

    List<Doctor> getAllDoctors();

    PageInfo<AvaDoctor> getDoctorsByPage(int page, int rows);

    Integer countDoctorsByAccount(String account);

    List<Doctor> getDoctorByAccount(String account);
}
