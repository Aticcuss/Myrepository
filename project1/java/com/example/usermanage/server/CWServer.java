package com.example.usermanage.server;
import com.example.usermanage.entity.CarWash;
import com.example.usermanage.mapper.CWMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class CWServer {
    @Autowired
    private CWMapper cwMapper;
    public int save(CarWash carWash){

            if (carWash.getCw_id() > 0) {
                return cwMapper.updateCW(carWash);
            } else {
                return cwMapper.insertCW(carWash);
            }

    }
    public Map<String, Object> findPage(int pageNum, int pageSize,String cw_name,String location,String cw_id){
        //计算页开始的编号
        Map<String, Object> res = new HashMap<>();
        List<CarWash> data=new ArrayList<>();
        int total=0;
        try{
            if(!cw_id.equals("")){
                int id = Integer.parseInt(cw_id);
                data=cwMapper.findPageById(id);
                if(!data.isEmpty()){
                    total=1;
                }
            }else {
                //计算开始行编号
                pageNum=(pageNum-1)*pageSize;
                cw_name= "%"+cw_name+"%";
                location= "%"+location+"%";
                total=cwMapper.countCW(cw_name,location);
                data = cwMapper.findPage(pageNum,pageSize,cw_name,location);
            }
        }catch (NumberFormatException e){
            //userId不能转换为整形
            System.out.println("cw_id is not a integer");
        }
        res.put("data",data);
        res.put("total",total);
        return res;
    }
}
