package com.zdmoney.credit.blackList.service.pub;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * 
 */
@WebService(name = "RiskControlServiceSoap", targetNamespace = "http://www.zendai.com/")
public interface RiskControlServiceSoap {

	/**
	 * ��ӿͻ�����XML
	 * 
	 * @param blackListXml
	 * @return returns boolean
	 */
	@WebMethod(operationName = "AddCustomerBlackListXml", action = "http://www.zendai.com/AddCustomerBlackListXml")
	@WebResult(name = "AddCustomerBlackListXmlResult", targetNamespace = "http://www.zendai.com/")
	@RequestWrapper(localName = "AddCustomerBlackListXml", targetNamespace = "http://www.zendai.com/", className = "com.zdmoney.credit.blackList.service.xml.AddCustomerBlackListXml")
	@ResponseWrapper(localName = "AddCustomerBlackListXmlResponse", targetNamespace = "http://www.zendai.com/", className = "com.zdmoney.credit.blackList.service.xml.AddCustomerBlackListXmlResponse")
	public boolean addCustomerBlackListXml(
            @WebParam(name = "blackListXml", targetNamespace = "http://www.zendai.com/") String blackListXml);

	/**
	 * �����ҵ����XML
	 * 
	 * @param blackListXml
	 * @return returns boolean
	 */
	@WebMethod(operationName = "AddEnterpriseBlackListXml", action = "http://www.zendai.com/AddEnterpriseBlackListXml")
	@WebResult(name = "AddEnterpriseBlackListXmlResult", targetNamespace = "http://www.zendai.com/")
	@RequestWrapper(localName = "AddEnterpriseBlackListXml", targetNamespace = "http://www.zendai.com/", className = "com.zdmoney.credit.blackList.service.xml.AddEnterpriseBlackListXml")
	@ResponseWrapper(localName = "AddEnterpriseBlackListXmlResponse", targetNamespace = "http://www.zendai.com/", className = "com.zdmoney.credit.blackList.service.xml.AddEnterpriseBlackListXmlResponse")
	public boolean addEnterpriseBlackListXml(
            @WebParam(name = "blackListXml", targetNamespace = "http://www.zendai.com/") String blackListXml);

	/**
	 * �޸Ŀͻ�����XML
	 * 
	 * @param blackListXml
	 * @return returns boolean
	 */
	@WebMethod(operationName = "UpdateCustomerBlackListXml", action = "http://www.zendai.com/UpdateCustomerBlackListXml")
	@WebResult(name = "UpdateCustomerBlackListXmlResult", targetNamespace = "http://www.zendai.com/")
	@RequestWrapper(localName = "UpdateCustomerBlackListXml", targetNamespace = "http://www.zendai.com/", className = "com.zdmoney.credit.blackList.service.xml.UpdateCustomerBlackListXml")
	@ResponseWrapper(localName = "UpdateCustomerBlackListXmlResponse", targetNamespace = "http://www.zendai.com/", className = "com.zdmoney.credit.blackList.service.xml.UpdateCustomerBlackListXmlResponse")
	public boolean updateCustomerBlackListXml(
            @WebParam(name = "blackListXml", targetNamespace = "http://www.zendai.com/") String blackListXml);

	/**
	 * �޸���ҵ����XML
	 * 
	 * @param blackListXml
	 * @return returns boolean
	 */
	@WebMethod(operationName = "UpdateEnterpriseBlackListXml", action = "http://www.zendai.com/UpdateEnterpriseBlackListXml")
	@WebResult(name = "UpdateEnterpriseBlackListXmlResult", targetNamespace = "http://www.zendai.com/")
	@RequestWrapper(localName = "UpdateEnterpriseBlackListXml", targetNamespace = "http://www.zendai.com/", className = "com.zdmoney.credit.blackList.service.xml.UpdateEnterpriseBlackListXml")
	@ResponseWrapper(localName = "UpdateEnterpriseBlackListXmlResponse", targetNamespace = "http://www.zendai.com/", className = "com.zdmoney.credit.blackList.service.xml.UpdateEnterpriseBlackListXmlResponse")
	public boolean updateEnterpriseBlackListXml(
            @WebParam(name = "blackListXml", targetNamespace = "http://www.zendai.com/") String blackListXml);

	/**
	 * ��ÿͻ�����XML
	 * 
	 * @param riskInfo
	 * @param idCard
	 * @param mobilePhone
	 * @param workUnit
	 * @param custName
	 * @param telephone
	 * @return returns java.lang.String
	 */
	@WebMethod(operationName = "GetCustomerBlackListXml", action = "http://www.zendai.com/GetCustomerBlackListXml")
	@WebResult(name = "GetCustomerBlackListXmlResult", targetNamespace = "http://www.zendai.com/")
	@RequestWrapper(localName = "GetCustomerBlackListXml", targetNamespace = "http://www.zendai.com/", className = "com.zdmoney.credit.blackList.service.xml.GetCustomerBlackListXml")
	@ResponseWrapper(localName = "GetCustomerBlackListXmlResponse", targetNamespace = "http://www.zendai.com/", className = "com.zdmoney.credit.blackList.service.xml.GetCustomerBlackListXmlResponse")
	public String getCustomerBlackListXml(
            @WebParam(name = "custName", targetNamespace = "http://www.zendai.com/") String custName,
            @WebParam(name = "idCard", targetNamespace = "http://www.zendai.com/") String idCard,
            @WebParam(name = "mobilePhone", targetNamespace = "http://www.zendai.com/") String mobilePhone,
            @WebParam(name = "telephone", targetNamespace = "http://www.zendai.com/") String telephone,
            @WebParam(name = "workUnit", targetNamespace = "http://www.zendai.com/") String workUnit,
            @WebParam(name = "riskInfo", targetNamespace = "http://www.zendai.com/") String riskInfo);

	/**
	 * �����ҵ����XML
	 * 
	 * @param riskInfo
	 * @param legalName
	 * @param licenseCode
	 * @param enterpriseName
	 * @param legalIdcard
	 * @param telephone
	 * @return returns java.lang.String
	 */
	@WebMethod(operationName = "GetEnterpriseBlackListXml", action = "http://www.zendai.com/GetEnterpriseBlackListXml")
	@WebResult(name = "GetEnterpriseBlackListXmlResult", targetNamespace = "http://www.zendai.com/")
	@RequestWrapper(localName = "GetEnterpriseBlackListXml", targetNamespace = "http://www.zendai.com/", className = "com.zdmoney.credit.blackList.service.xml.GetEnterpriseBlackListXml")
	@ResponseWrapper(localName = "GetEnterpriseBlackListXmlResponse", targetNamespace = "http://www.zendai.com/", className = "com.zdmoney.credit.blackList.service.xml.GetEnterpriseBlackListXmlResponse")
	public String getEnterpriseBlackListXml(
            @WebParam(name = "enterpriseName", targetNamespace = "http://www.zendai.com/") String enterpriseName,
            @WebParam(name = "legalName", targetNamespace = "http://www.zendai.com/") String legalName,
            @WebParam(name = "legalIdcard", targetNamespace = "http://www.zendai.com/") String legalIdcard,
            @WebParam(name = "licenseCode", targetNamespace = "http://www.zendai.com/") String licenseCode,
            @WebParam(name = "telephone", targetNamespace = "http://www.zendai.com/") String telephone,
            @WebParam(name = "riskInfo", targetNamespace = "http://www.zendai.com/") String riskInfo);

}