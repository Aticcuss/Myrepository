package com.example.usermanage.controller;
import com.example.usermanage.entity.CarWash;
import com.example.usermanage.mapper.CWMapper;
import com.example.usermanage.server.CWServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/carWash")
public class CWController {
    @Autowired
    private CWMapper cwMapper;
    @Autowired
    private CWServer cwServer;
    @DeleteMapping("/del/{cw_id}")
    public int delCW(@PathVariable int cw_id){
        return cwMapper.delCW(cw_id);
    }
    @GetMapping("/page")
    public Map<String, Object> findPage(@RequestParam int pageNum,
                                        @RequestParam int pageSize,
                                        @RequestParam String cw_name,
                                        @RequestParam String location,
                                        @RequestParam String cw_id
                                        ){
        return cwServer.findPage(pageNum,pageSize,cw_name,location,cw_id);
    }
    @PostMapping("/save")
    public int save(@RequestBody CarWash carWash){
        return cwServer.save(carWash);
    }
}
