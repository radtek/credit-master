package com.zdmoney.credit.core.calculator.fundssource.wm3;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.constant.RepaymentStateEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.core.calculator.fundssource.common.ExcelGetInfo;
import com.zdmoney.credit.excel.vo.VExcelInfo;
import com.zdmoney.credit.system.service.pub.IProdCreditProductTermService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zdmoney.credit.core.calculator.fundssource.wm.WMCalculatorBase;
import com.zdmoney.credit.core.calculator.pub.ICalculator;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailDao;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanProduct;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;

/**
 * 外贸信托3 计算器 版本 v2.0
 * @author 10098  2017年3月28日 下午5:49:23
 */

@Component("FS_00019_v2")
public class WM3CalculatorVTwo extends WMCalculatorBase implements ICalculator {

    private static final Logger logger = Logger.getLogger(WM3CalculatorVTwo.class);

    @Autowired
    private ILoanRepaymentDetailDao loanRepaymentDetailDao;
    @Autowired
    ISysParamDefineService sysParamDefineService;
    @Autowired
    IProdCreditProductTermService prodCreditProductTermServiceImpl;
    @Autowired
    ISequencesService sequencesService;

    @Override
    public BigDecimal getOnetimeRepaymentAmount(Long loanId, Date currDate, List<LoanRepaymentDetail> repayList) {
        System.out.println("外贸3 计算器 版本 v2.0");
        return super.getOnetimeRepaymentAmountV1(loanId, currDate, repayList);
    }

    @Override
    public BigDecimal getPenalty(Long loanId, List<LoanRepaymentDetail> repayList, VLoanInfo vLoanInfo) {
        System.out.println("外贸3  计算器 版本 v2.0");
        return super.getPenaltyV1(loanId, repayList, vLoanInfo);
    }

    @Override
    public void updateRate(LoanBase loanBase, LoanInitialInfo loanInitialInfo,LoanProduct loanProduct,ProdCreditProductTerm prodCreditProductTerm) {
        super.updateRate(loanBase, loanInitialInfo, loanProduct, prodCreditProductTerm);
        try {
            //读取excel文件获取数据
            String excelPath = sysParamDefineService.getSysParamValue("system.thirdparty", "excelPathWM3");
            ExcelGetInfo t = new ExcelGetInfo(excelPath, loanInitialInfo, prodCreditProductTerm,loanProduct,loanBase);
            VExcelInfo excelInfo = t.getInfo();
            loanProduct.setRate(prodCreditProductTerm.getRate());
            loanProduct.setPenaltyRate(prodCreditProductTerm.getPenaltyRate());//逾期罚息
            loanProduct.setAccrualem(prodCreditProductTerm.getAccrualem());
            loanProduct.setPactMoney(excelInfo.getPactMoney().setScale(2, BigDecimal.ROUND_HALF_UP));//合同金额
            loanProduct.setGrantMoney(loanInitialInfo.getMoney());
            loanProduct.setRateem(excelInfo.getRateem());//月利率
            loanProduct.setRateey(excelInfo.getRateey());//年利率
            loanProduct.setRateed(excelInfo.getRateed());//日利率
            loanProduct.setResidualPactMoney(loanProduct.getPactMoney());

            loanProduct.setRisk(excelInfo.getRisk());// 风险金
            loanProduct.setRateSum(excelInfo.getPactMoney().subtract(loanInitialInfo.getMoney()).subtract(excelInfo.getRisk()));// 收入总和
            loanProduct.setManageRateForPartyC(BigDecimal.ZERO);// 丙方管理费
            loanProduct.setReferRate(excelInfo.getReferRate());//咨询费
            loanProduct.setEvalRate(excelInfo.getEvalRate());// 评估费
            loanProduct.setManageRate(excelInfo.getManageRate());// 管理费
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆外贸3updateRate错误",e);
        }
    }

    @Override
    public Map<String, Object> createLoanRepaymentDetail(LoanBase loanBase, LoanInitialInfo loanInitialInfo, LoanProduct loanProduct) {
        try {
            //读取excel文件获取数据
            String excelPath = sysParamDefineService.getSysParamValue("system.thirdparty", "excelPathWM3");//获取excel路径
            logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆外贸3计算器excel路径："+excelPath);
            ProdCreditProductTerm prodCreditProductTerm = prodCreditProductTermServiceImpl.findBymap(loanProduct.getTime(),loanInitialInfo.getLoanType(),loanBase.getFundsSources());
            ExcelGetInfo excelGetInfo = new ExcelGetInfo(excelPath, loanInitialInfo,prodCreditProductTerm,loanProduct,loanBase);
            VExcelInfo excelInfo = excelGetInfo.getInfo();

            for (int i = 0; i < excelInfo.getExcelDetailList().size(); i++) {
                LoanRepaymentDetail c = excelInfo.getExcelDetailList().get(i);

                c.setId(sequencesService.getSequences(SequencesEnum.LOAN_REPAYMENT_DETAIL));
                c.setLoanId(loanBase.getId());
                c.setCurrentTerm(i + 1L);//当前期数
                c.setRepaymentState(RepaymentStateEnum.未还款.name());
                Date dx = (Date) loanProduct.getStartrdate().clone();
                dx.setMonth(dx.getMonth() + i);
                c.setReturnDate(dx);//固定还款日期
                c.setPenaltyDate(dx);//罚息起算日期
                double accrualRevise = 0;
                c.setAccrualRevise(BigDecimal.valueOf(accrualRevise));
                loanRepaymentDetailDao.insert(c);
            }

        } catch (Exception e) {
            logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆外贸3生成还款计划错误",e);
            throw new PlatformException("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆外贸3生成还款计划错误");
        }
        return null;//不用调整精度
    }

    /**
     * 贷前试算
     *
     * @param loanBase
     * @param loanInitialInfo
     * @param loanProduct
     * @return
     */
    @Override
    public List<LoanRepaymentDetail> createLoanTrial(LoanBase loanBase, LoanInitialInfo loanInitialInfo, LoanProduct loanProduct) {
        try {
            //读取excel文件获取数据
            String excelPath = sysParamDefineService.getSysParamValue("system.thirdparty", "excelPathWM3");//获取excel路径
            logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆外贸3计算器excel路径："+excelPath);
            ProdCreditProductTerm prodCreditProductTerm = prodCreditProductTermServiceImpl.findBymap(loanProduct.getTime(),loanInitialInfo.getLoanType(),loanBase.getFundsSources());
            ExcelGetInfo excelGetInfo = new ExcelGetInfo(excelPath, loanInitialInfo,prodCreditProductTerm,loanProduct,loanBase);
            VExcelInfo excelInfo = excelGetInfo.getInfo();
            List<LoanRepaymentDetail> detailList = excelInfo.getExcelDetailList();
            for (int i = 0; i < detailList.size(); i++) {
                LoanRepaymentDetail c = excelInfo.getExcelDetailList().get(i);

                c.setId(sequencesService.getSequences(SequencesEnum.LOAN_REPAYMENT_DETAIL));
                c.setLoanId(loanBase.getId());
                c.setCurrentTerm(i + 1L);//当前期数
                c.setRepaymentState(RepaymentStateEnum.未还款.name());
                Date dx = (Date) loanProduct.getStartrdate().clone();
                dx.setMonth(dx.getMonth() + i);
                c.setReturnDate(dx);//固定还款日期
                c.setPenaltyDate(dx);//罚息起算日期
                double accrualRevise = 0;
                c.setAccrualRevise(BigDecimal.valueOf(accrualRevise));
            }
            return detailList;
        } catch (Exception e) {
            logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆外贸3生成还款计划错误",e);
            throw new PlatformException("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆外贸3生成还款计划错误");
        }
    }

	@Override
	public boolean enterAccountAfter(OfferRepayInfo offerRepayInfo) {
		return true;
	}

	@Override
	public boolean enterAccountBefore(OfferRepayInfo offerRepayInfo) {
		return true;
	}
}
