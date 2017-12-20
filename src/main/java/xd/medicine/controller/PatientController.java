package xd.medicine.controller;

import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xd.medicine.entity.bo.Patient;
import xd.medicine.entity.bo.TrustAttr;
import xd.medicine.entity.dto.FrontResult;
import xd.medicine.service.PatientService;
import xd.medicine.service.TrustAttrService;

import javax.jnlp.IntegrationService;
import java.util.Date;
import java.util.List;

/**
 * created by xdCao on 2017/12/19
 */
@RestController
@RequestMapping("/patient")
public class PatientController {

    private static final Logger LOGGER= LoggerFactory.getLogger(PatientController.class);

    @Autowired
    private PatientService patientService;

    @Autowired
    private TrustAttrService trustAttrService;

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public FrontResult login(@RequestParam String account,@RequestParam String password){
        List<Patient> patientByAccount = patientService.getPatientByAccount(account);
        if (patientByAccount!=null&&(patientByAccount.size()==1)){
            if (password.equals(patientByAccount.get(0).getPassword())){
                return new FrontResult(200,patientByAccount.get(0),null);
            }else {
                return new FrontResult(500,null,"密码错误");
            }
        }else {
            return new FrontResult(500,null,"用户名错误");
        }
    }

    @RequestMapping(value = "/single",method = RequestMethod.GET)
    public FrontResult getSinglePatient(@RequestParam int patientId){
        Patient patient = patientService.getPatientById(patientId);
        if (patient!=null){
            return new FrontResult(200,patient,null);
        }else {
            return new FrontResult(500,null,"找不到该患者");
        }
    }

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public FrontResult getAllPatients(){
        List<Patient> allPatients = patientService.getAllPatients();
        if (allPatients!=null&&allPatients.size()>0){
            return new FrontResult(200,allPatients,null);
        }else {
            return new FrontResult(500,null,"患者列表为空");
        }
    }

    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public FrontResult getPatientsByPage(@RequestParam int page,@RequestParam int rows){
        PageInfo<Patient> patientByPage = patientService.getPatientByPage(page, rows);
        return new FrontResult(200,patientByPage,null);
    }

    @RequestMapping(value = "/doctor",method = RequestMethod.GET)
    public FrontResult getPatientByDoctor(@RequestParam int doctorId){
        List<Patient> patientsByDoctor = patientService.getPatientsByDoctor(doctorId);
        if (patientsByDoctor!=null&&patientsByDoctor.size()>0){
            return new FrontResult(200,patientsByDoctor,null);
        }else {
            return new FrontResult(500,null,"该医生没有病患");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public FrontResult deletePatient(@RequestParam int patientId){
        Patient patientById = patientService.getPatientById(patientId);
        trustAttrService.deleteById(patientById.getTrustattrId());
        patientService.deletePatient(patientId);
        return new FrontResult(200,null,null);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public FrontResult updatePatient(
                                 @RequestParam Integer id,
                                 @RequestParam Integer doctorId,
                                 @RequestParam String phone,
                                 @RequestParam Boolean senseAware,
                                 @RequestParam String illnessCondition){
        Patient patient=new Patient();
        patient.setDoctorId(doctorId);
        patient.setPhone(phone);
        patient.setSenseAware(senseAware);
        patient.setIllnessCondition(illnessCondition);
        Integer integer = patientService.updatePatient(patient);
        return new FrontResult(200,integer,null);
    }

    @RequestMapping(value = "/trust/update",method = RequestMethod.POST)
    public FrontResult updateTrustAttr(@RequestParam Integer id,
                                       @RequestParam Byte department,
                                       @RequestParam Byte demandTitle,
                                       @RequestParam Byte demandWorkage,
                                       @RequestParam Byte demandDegree){
        TrustAttr trustAttr=new TrustAttr();
        trustAttr.setDepartment(department);
        trustAttr.setDemandTitle(demandTitle);
        trustAttr.setDemandWorkage(demandWorkage);
        trustAttr.setDemandDegree(demandDegree);
        trustAttrService.insertTrustAttr(trustAttr);
        return new FrontResult(200,trustAttr,null);
    }

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public FrontResult addNewPatient(@RequestParam String name,
                                 @RequestParam String account,
                                 @RequestParam String password,
                                 @RequestParam Integer doctorId,
                                 @RequestParam String phone,
                                 @RequestParam boolean senseAware,
                                 @RequestParam String illnessCondition,
                                 @RequestParam byte department,
                                 @RequestParam byte demandTitle,
                                 @RequestParam byte demandWorkage,
                                 @RequestParam byte demandDegree){

        Integer countPatientByAccount = patientService.countPatientByAccount(account);
        if (countPatientByAccount>0){
            return new FrontResult(500,null,"该账户名已存在");
        }

        /*
        病人注册时将其信任信息与个人信息分两部分插入
        因此在注册时先提交信任信息并将trustAttrId返回给前端，再作为参数进行病人注册
         */
        TrustAttr trustAttr=new TrustAttr();
        trustAttr.setDepartment(department);
        trustAttr.setDemandTitle(demandTitle);
        trustAttr.setDemandWorkage(demandWorkage);
        trustAttr.setDemandDegree(demandDegree);
        trustAttrService.insertTrustAttr(trustAttr);


        Patient patient=new Patient();
        patient.setName(name);
        patient.setAccount(account);
        patient.setPassword(password);
        patient.setTrustattrId(trustAttr.getId());
        patient.setDoctorId(doctorId);
        patient.setPhone(phone);
        patient.setSenseAware(senseAware);
        patient.setIllnessCondition(illnessCondition);
        patient.setRegistryDate(new Date());
        patientService.insertPatient(patient);
        return new FrontResult(200,patient,null);
    }

}