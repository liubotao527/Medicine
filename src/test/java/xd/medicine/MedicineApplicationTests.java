package xd.medicine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xd.medicine.entity.bo.ProDutyLog;
import xd.medicine.service.ProDutyLogService;
import xd.medicine.service.ServiceImpl.ProDutyLogServiceImpl;

import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MedicineApplicationTests {


	@Autowired
    ProDutyLogService proDutyLogService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void test(){
		System.out.println((int)9.67);
	}


	@Test
	public void test1(){
		HashMap<Integer,Integer> hashMap=new HashMap<>();
		hashMap.put(1,1);
		hashMap.put(1,2);
		System.out.println(hashMap.get(1));
	}

	@Test
	public void testSql(){
		List<ProDutyLog> proDutyLogsBySub = proDutyLogService.getProDutyLogsBySub((byte) 1, 1);
		System.out.println(proDutyLogsBySub.size());

	}

	@Test
	public void insert(){
		for (int i = 0; i < 10000; i++) {
			ProDutyLog proDutyLog=new ProDutyLog();
			proDutyLog.setDutyId(i);
			proDutyLog.setObjId(i);
			proDutyLog.setState((byte) i);
			proDutyLog.setSubId(i);
			proDutyLog.setSubType((byte) i);
			proDutyLogService.insertNewProDutyLog(proDutyLog);
		}
	}


}
