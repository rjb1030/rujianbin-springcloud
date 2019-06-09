package rujianbin.app.web.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import rujianbin.autoconfiguration.qlexpress.promotion.ExpressConfig;
import rujianbin.autoconfiguration.qlexpress.promotion.Product;
import rujianbin.autoconfiguration.qlexpress.promotion.PromotionRule;
import rujianbin.autoconfiguration.qlexpress.promotion.RuleCalculate;
import rujianbin.common.utils.RjbStringUtils;
import rujianbin.security.principal.author.RjbSecurityUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 汝建斌 on 2017/4/10.
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    @RequestMapping("")
    public String login(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
        Object obj = request.getSession().getAttribute(RjbSecurityUser.sessionKey);
        if(obj!=null){
            RjbSecurityUser rjbSecurityUser  = (RjbSecurityUser)obj;
            model.put("user",rjbSecurityUser.getUsername()+"("+rjbSecurityUser.getName()+")");
            model.put("authority",rjbSecurityUser.getAuthorities());
        }

        System.out.println("sessionId----->"+request.getSession().getId());
        return "home/home";
    }


    @RequestMapping("/hello")
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response) {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        return "hello world! you are authorization,your token = "+request.getSession().getId();
    }

    @RequestMapping("/currentUser")
    @ResponseBody
    public RjbSecurityUser currentUser(){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        return (RjbSecurityUser)user.getPrincipal();
    }

    @RequestMapping("/token")
    @ResponseBody
    public Map<String,String> token(HttpServletRequest request, HttpServletResponse response) {
        Map<String,String> map = new HashMap<>();
        map.put("success","true");
        map.put("message","");
        map.put("data",request.getSession().getId());
        return map;
    }




    @RequestMapping("/chat-room")
    public String chatRoom(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
        Object obj = request.getSession().getAttribute(RjbSecurityUser.sessionKey);
        if(obj!=null){
            RjbSecurityUser rjbSecurityUser  = (RjbSecurityUser)obj;
            model.put("token","xxxxx");
            model.put("nickName",rjbSecurityUser.getName());
            model.put("userName",rjbSecurityUser.getUsername());
            model.put("authority",rjbSecurityUser.getAuthorities());
        }
        return "chat/chat-room";
    }


    @RequestMapping("/rule-engine")
    public String ruleEngine(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
        return "ruleEngine/rule-engine";
    }

    @RequestMapping("/ruleEngineSubmit")
    @ResponseBody
    public String ruleEngineSubmit(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
        String[] ruleName = request.getParameterValues("ruleName");
        String[] mylabel = request.getParameterValues("mylabel");
        String[] my_dimension = request.getParameterValues("my_dimension");
        String[] my_judge = request.getParameterValues("my_judge");
        String[] my_condition = request.getParameterValues("my_condition");
        String[] promotion_behave = request.getParameterValues("promotion_behave");
        String[] promotion_result = request.getParameterValues("promotion_result");


        String[] productName = request.getParameterValues("productName");
        String[] productLabel = request.getParameterValues("productLabel");
        String[] productNum = request.getParameterValues("productNum");
        String[] productPrice = request.getParameterValues("productPrice");

        int ruleLenth = mylabel.length;
        List<PromotionRule> ruleList = new ArrayList<>();
        for(int i=0;i<ruleLenth;i++){
            if(StringUtils.isEmpty(mylabel[i])){
                continue;
            }
            PromotionRule rule = new PromotionRule();
            rule.setRuleName(ruleName[i]);
            rule.setLabel(mylabel[i]);
            rule.setDimension(my_dimension[i]);
            rule.setJudge(my_judge[i]);
            rule.setCondition(my_condition[i]);
            rule.setBehave(promotion_behave[i]);
            rule.setResult(promotion_result[i]);
            ruleList.add(rule);
        }

        int productLenth = productLabel.length;
        List<Product> productList = new ArrayList<>();
        BigDecimal totalAmount = new BigDecimal(0);
        for(int i=0;i<productLenth;i++){
            if(StringUtils.isEmpty(productLabel[i])){
                continue;
            }
            Product p = new Product(productName[i],productLabel[i],Integer.valueOf(productNum[i]),BigDecimal.valueOf(Double.valueOf(productPrice[i])));
            productList.add(p);
            totalAmount = totalAmount.add(p.getPromotionTotalPrice());
        }

        try {
            BigDecimal realPay = totalAmount;
            boolean isRuleEffect = false;
            String message = "计算结果--->";
            for(PromotionRule rule : ruleList){
                String express = rule.getExpress();
                System.out.println("表达式--->"+express);
                RuleCalculate.CalculateResult result = ExpressConfig.execute(express,productList,rule);
                if(result.isSuccess()){
                    isRuleEffect = true;
                    if(result.getData() instanceof BigDecimal){
                        realPay = realPay.subtract((BigDecimal)result.getData());
                    }
                }
                message+=rule.getRuleName()+":"+result.isSuccess()+"("+result.getData()+")";
            }

            Map<String,Object> rsMap  =new LinkedHashMap<>();
            rsMap.put("message",message);
            if(isRuleEffect){
                rsMap.put("实付金额",realPay);
                rsMap.put("cal_products",productList);
            }

            return RjbStringUtils.ObjectToString(rsMap);
        } catch (Exception e) {
            e.printStackTrace();
            return "计算错误";
        }
    }




}
