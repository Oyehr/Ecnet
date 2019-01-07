package com.ecm.service;

import com.ecm.vo.BundleDocumentEvaluateResult;
import com.ecm.vo.SingleDocumentBasicInfo;
import com.ecm.vo.SingleDocumentEvaluateResult;

import java.util.List;

public interface EvaluateService {

    /**获取单个文书中的案件基本信息
     *
     * @param fileName 要解析的文书对应的文件名
     * @return 文书对应的案件基本信息
     */
    public SingleDocumentBasicInfo getSingleCaseBasicInfo(String fileName);

    /**评估单个文书中某二者之间的相关性
     *
     * @param fileName 要解析的文书对应的文件名
     * @param evaluateType 评估类型：事实到法条；法条到事实；法条到结论；结论到法条；
     * @return 文书中此二者相关性评估结果列表
     */
    public List<SingleDocumentEvaluateResult> singleEvaluate(String fileName, String evaluateType);

    /**评估批量文书中某二者之间的相关性
     *
     * @param directoryPath 批量文书所在文件夹的路径
     * @param evaluateType 评估类型：事实到法条；法条到事实；法条到结论；结论到法条；
     * @return 批量文书中此二者相关性评估结果列表
     */
    public List<BundleDocumentEvaluateResult> bundleEvaluate(String directoryPath, String evaluateType);

    /**导出文书评估的结果到Excel文件中去
     *
     * @param filePath 导出文件的路径
     * @param evaluateType 文书的评估类型
     * @param basicInfo 文书的基本信息（如果是单篇文书的评估，则此参数有意义；如果是批量文书的评估，则此参数无意义）
     * @param evaluateResult 文书的评估结果
     * @return 导出Excel是否成功：若成功，返回导出的Excel的文件路径和文件名；若不成功，返回fail
     */
    public String exportToExcel(String filePath, String evaluateType, String basicInfo, String evaluateResult);

}
