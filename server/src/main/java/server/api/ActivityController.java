package server.api;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import server.database.ActivitiesRepository;

import java.util.List;
import java.util.Random;

import commons.Activity;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    @Autowired
    private ActivitiesRepository repository;

    @GetMapping("/getActivityByWh")
    @ResponseBody
    public Activity getActivityByWh(@RequestParam Long energyConsumption) {
        List<Activity> act = repository.findByEnergyConsumptionDesc(energyConsumption);
        return act.get(0);
    }

    @GetMapping("/getRandomActivity")
    @ResponseBody
    public Activity getRandomActivity() {
        List<Activity> allAct = repository.findAll();
        Random ran = new Random();
        return allAct.get(ran.nextInt(allAct.size()));
    }
}
