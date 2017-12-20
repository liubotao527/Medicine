package xd.medicine.service;

import com.github.pagehelper.PageInfo;
import xd.medicine.entity.bo.Others;

import javax.jnlp.IntegrationService;
import javax.print.attribute.standard.MediaSize;
import java.util.List;

/**
 * created by xdCao on 2017/12/20
 */

public interface OthersService {

    Integer insert(Others others);

    Integer update(Others others);

    Integer delete(int id);

    Others getOthersById(int id);

    List<Others> getAllOthers();

    PageInfo<Others> getOthersByPage(int page,int rows);

    List<Others> getOthersByAccount(String account);

}