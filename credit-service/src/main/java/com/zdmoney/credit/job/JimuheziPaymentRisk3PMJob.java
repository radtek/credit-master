package com.zdmoney.credit.job;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ToolUtils;
import com.zdmoney.credit.common.util.exceldata.JimuData;
import com.zdmoney.credit.framework.dao.pub.IJobFreeSqlDao;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISendMailService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zdmoney.credit.system.service.pub.ISysSpecialDayService;

@Service
public class JimuheziPaymentRisk3PMJob {
	
	private static final Logger logger = Logger.getLogger(JimuheziPaymentRisk3PMJob.class);

	@Autowired
	private IJobFreeSqlDao jobFreeSqlDao;
	
	@Autowired
	private IAfterLoanService afterLoanService;
	
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	
	@Autowired
	private ISysSpecialDayService sysSpecialDayService;
	@Autowired
	private ISendMailService sendMailService;
	
	@Autowired
	private ILoanLogService loanLogService;
    private SimpleDateFormat format =new SimpleDateFormat("yyyy年MM月dd日");
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:00");
    private SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd 00:00");
    private SimpleDateFormat df3 =new SimpleDateFormat("yyyy-MM-dd");
	
	public void execute() {
    	String isJimuheziPaymentRisk3PM = sysParamDefineService.getSysParamValue("sysJob", "isJimuheziPaymentRisk3PM");
		if(!Const.isClosing.equals(isJimuheziPaymentRisk3PM)){
			Date today = Dates.getCurrDate();
			if (sysSpecialDayService.isWorkDay(today) && ToolUtils.isRepayDay(today)) {
				doFillRiskBatchJimuBatch(today);
				doNoticeNormal(today);
			}
		}else{
			loanLogService.createLog("JimuheziPaymentRisk3PMJob", "info", "定时开关isJimuheziPaymentRisk3PM关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isJimuheziPaymentRisk3PM关闭，此次不执行");
		}
		

	}
	
	private void doFillRiskBatchJimuBatch(Date date) {
		logger.info("积木提前结清的风险金预付发送邮件处理开始........");
		loanLogService.createLog("JimuheziPaymentRisk3PMJob", "info", "积木提前结清的风险金预付发送邮件处理开始........", "SYSTEM");
		BigDecimal principalBalance = BigDecimal.ZERO;
//		BigDecimal returneterm = BigDecimal.ZERO;  //没有用到？？
		BigDecimal currentAccrual = BigDecimal.ZERO;
		BigDecimal count = BigDecimal.ZERO;
		
		Calendar calendar = Calendar.getInstance();//获得日历对象
        calendar.setTime(date);//设置时间为传递过来的时间
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.DATE, 1);
        calendar.add(Calendar.MONTH, -1);
        
        String lastTime=df2.format(calendar.getTime());
        String currentTime=df.format(new Date());
        String startDate = df3.format(calendar.getTime());
        String endDate = df3.format(date);
        
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("day", day);
        param.put("lastTime", lastTime);
        param.put("currentTime", currentTime);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        
        List<Map<String, Object>> results = jobFreeSqlDao.jimuMailData(param);
        
        if (results.size() > 0) {
        	Map<String, Object> result = results.get(0);
        	
        	principalBalance = new BigDecimal((String)result.get("PRINCIPAL"));
//    		returneterm = new BigDecimal((String)result.get("returneterm"));
    		currentAccrual = new BigDecimal((String)result.get("ACCRUAL"));
    		count = new BigDecimal((String)result.get("COUNT"));
        }
        
        String now = format.format(new Date());
        String title = now+"预付积木盒子的提前结清划拨金额";
        StringBuffer stringBuffer= new StringBuffer();
        stringBuffer.append("截止至"+now+"，由提前结清引起的划拨给积木盒子风险金的金额为："+
        		(principalBalance.add(currentAccrual)).toPlainString()+"\n");
        stringBuffer.append("提前结清笔数："+count.toPlainString()+"\n");
        stringBuffer.append("提前结清本金："+principalBalance.toPlainString()+"\n");
        stringBuffer.append("提前结清利息："+currentAccrual.toPlainString());
		
        try {
        	sendMailService.sendPaymentRiskMail(sysParamDefineService.getSysParamValue("jimu", "jimu.risk.to"), 
					sysParamDefineService.getSysParamValue("jimu", "jimu.risk.cc"), title, stringBuffer.toString());
		} catch (UnknownHostException e) {
			logger.error("积木提前结清的风险金预付发送邮件出错：" + e.getMessage());
			int length = e.getMessage().length();
			loanLogService.createLog("JimuheziPaymentRisk3PMJob", "error", "积木提前结清的风险金预付发送邮件出错：" + (length > 1500 ? e.getMessage().substring(0, 1500) : e.getMessage()), "SYSTEM");
		}
        logger.info("积木提前结清的风险金预付发送邮件结束");
        loanLogService.createLog("JimuheziPaymentRisk3PMJob", "info", "积木提前结清的风险金预付发送邮件结束", "SYSTEM");
	}
	
	private void doNoticeNormal(Date date) {
		try {
            List<JimuData> datas = new ArrayList<JimuData>();

            Calendar calendar = Calendar.getInstance();//获得日历对象
            calendar.setTime(date);//设置时间为传递过来的时间
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            calendar.add(Calendar.DATE, 1);    //得到后一天
            calendar.add(Calendar.MONTH, -1);    //得到前一个月

            String lastTime=df2.format(calendar.getTime());
            String currentTime=df.format(new Date());

            String startDate = df3.format(calendar.getTime());
            String endDate = df3.format(date);
            
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("day", day);
            param.put("lastTime", lastTime);
            param.put("currentTime", currentTime);
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            
            datas = jobFreeSqlDao.jimuNotifyData(param);
            
            for (JimuData data : datas) {
            	try {
            		afterLoanService.generateFlowForRepayAllJimuPay(data.getLoanId(), data.getCurrentTerm(), Dates.getCurrDate());
            	} catch (Exception e) {
            		logger.error(data.getLoanId()+"&&"+data.getCurrentTerm()+"&&"+e.getMessage());
    				int length = e.getMessage().length();
            		loanLogService.createLog("JimuheziPaymentRisk3PMJob", "error", data.getLoanId()+"&&"+data.getCurrentTerm()+"&&"+(length > 1500 ? e.getMessage().substring(0, 1500) : e.getMessage()), "SYSTEM");
            	}
            }
            
            if (datas.size() > 0) {
                String now = format.format(new Date());
                String title = now+"积木盒子提前结清通知";
                StringBuffer stringBuffer= new StringBuffer();
                stringBuffer.append(now+"，积木盒子债权提前结清的名单，见附件。");
                logger.info("积木提前结清通知发送邮件处理开始........");
                loanLogService.createLog("JimuheziPaymentRisk3PMJob", "info", "积木提前结清通知发送邮件处理开始........", "SYSTEM");
                sendMailService.sendPaymentRiskAttachMail(
                		sysParamDefineService.getSysParamValue("jimu", "jimu.notify.to"), 
    					sysParamDefineService.getSysParamValue("jimu", "jimu.notify.cc"),
                		title, stringBuffer.toString(),datas);
                logger.info("积木提前通知发送邮件结束");
                loanLogService.createLog("JimuheziPaymentRisk3PMJob", "info", "积木提前通知发送邮件结束", "SYSTEM");
            }
		} catch (Exception e) {
			logger.error("jimuhezi提前结清通知，数据附件处理出错:" + e.getMessage());
			int length = e.getMessage().length();
			loanLogService.createLog("JimuheziPaymentRisk3PMJob", "error", "jimuhezi提前结清通知，数据附件处理出错:" + (length > 1500 ? e.getMessage().substring(0, 1500) : e.getMessage()), "SYSTEM");
		}
	}
}
