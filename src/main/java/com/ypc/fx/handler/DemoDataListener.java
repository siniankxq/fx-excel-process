package com.ypc.fx.handler;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.ypc.fx.util.PoiExcelUtil;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.model.InternalSheet;
import org.apache.poi.hssf.record.DVRecord;
import org.apache.poi.hssf.record.aggregates.DataValidityTable;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;


public class DemoDataListener {

	public static final Map<String, String> MAPPING = new HashMap<>();
	public static final Map<String, Object> DEFAULT_MAP = new HashMap<>();

	static {
		MAPPING.put("*姓名", "*姓名");
		MAPPING.put("*证件类型", "*身份证件类型");
		MAPPING.put("*证件号码", "*身份证件号码");
		MAPPING.put("本期收入", "*本期收入");

		DEFAULT_MAP.put("*国籍（地区）", "__156__中华人民共和国");
		DEFAULT_MAP.put("*所得项目", "正常工资薪金");
//		DEFAULT_MAP.put("*本期收入", 4800.00);
		DEFAULT_MAP.put("本期费用", 0.00);
		DEFAULT_MAP.put("本期免税收入", 0.00);
		DEFAULT_MAP.put("月减除费用", 5000.00);
	}


//	public static void main(String[] args) throws Exception {
//		System.out.println("请输入要读取的excel文件目录：");
//		//2.构造一个“标准输入流”System.in关联的Scanner对象。
//		Scanner scanner = new Scanner(System.in);
//		//3.读取输入
//		String readExcelPath = scanner.nextLine();
//		System.out.println("您输入的要读取的excel文件目录是：" + readExcelPath);
//		ExcelReader reader = ExcelUtil.getReader(readExcelPath);
//		List<Map<String, Object>> readAll = reader.readAll();
//		reader.close();
//		System.out.println("请输入要写入的excel文件目录：");
//		String writeExcelPath = scanner.nextLine();
//		System.out.println("您输入的要写入的excel文件目录是：" + writeExcelPath);
////		for (DVRecord dvRecord : init) {
////			System.out.println(dvRecord);
////		}
//		System.out.println("请输入开始写入的行号：");
//		int beginRow = scanner.nextInt();
//		System.out.println("您输入的开始写入的行号是：" + beginRow);
//		Map<String, String> mapping = new HashMap<>();
//		mapping.put("*姓名", "*姓名");
//		mapping.put("*证件类型", "*身份证件类型");
//		mapping.put("*证件号码", "*身份证件号码");
//
//		Map<String, Object> defaultMap = new HashMap<>();
//		defaultMap.put("*姓名", "张三");
//		writeInExcel(new File(writeExcelPath), readAll, mapping, defaultMap, beginRow - 1, 1);
//	}

	public static void writeInExcel(File readFile, File writeFile, Map<String, String> mapping, Map<String, Object> defaultMap, int beginRow, int beginCell) throws Exception {
		ExcelReader reader = ExcelUtil.getReader(readFile);
		List<Map<String, Object>> mapList = reader.readAll();
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(writeFile);
			Workbook workbook = new HSSFWorkbook(fis);
			// 执行公式
			workbook.setForceFormulaRecalculation(true);
			// 获取指定名字的sheet
			Sheet sheet = workbook.getSheetAt(0);
//		Row row = sheet.getRow(61);
			Row headRow = sheet.getRow(5);
//		List<? extends DataValidation> dataValidations = sheet.getDataValidations();
//		HSSFFormulaEvaluator eva= new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
//		CellValue cellVal = eva.evaluate(row.getCell(2));//获取单元格的值//根据值的类型获取公式的计算结果


			short colCount = headRow.getLastCellNum();
			System.out.println(colCount);
			List<String> targetHeadNameList = new ArrayList<>();
			for (int j = 0; j < colCount; j++) {
				Cell cell = headRow.getCell(j);
				Object callValue = PoiExcelUtil.getCallValue(cell);
				System.out.println("index: " + cell.getColumnIndex() + "  value: " + callValue);
				Object cellValue1 = PoiExcelUtil.getCellValue(headRow, j);
				targetHeadNameList.add((String) cellValue1);
//			String cellValue = cell.getStringCellValue().trim();
//			set.add(cellValue);
			}
//		for (DataValidation dataValidation : dataValidations) {
//			System.out.println(dataValidation);
//
//			DataValidationConstraint validationConstraint = dataValidation.getValidationConstraint();
//			String[] explicitListValues = validationConstraint.getExplicitListValues();
//			System.out.println(explicitListValues);
//			DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(new String[]{"JAVA", "C#", "C++"});
//			CellRangeAddressList regions = dataValidation.getRegions();
//			System.out.println(regions);
//
//		}
			Set<String> sourceHeadSet = mapping.keySet();
			Map<String, Integer> targetHeadNameIndexMap = new HashMap<>();
			for (int i = 0; i < targetHeadNameList.size(); i++) {
				targetHeadNameIndexMap.put(targetHeadNameList.get(i), i);
			}

			for (Map<String, Object> map : mapList) {
				Row sheetRow = sheet.getRow(beginRow);
				for (String sourceHead : sourceHeadSet) {
					Object value = map.get(sourceHead);
					String targetHeadName = mapping.get(sourceHead);
					Integer index = targetHeadNameIndexMap.get(targetHeadName);
					Cell cell = sheetRow.getCell(index);
					if (value instanceof String) {
						String castValue = (String) value;
						if (castValue.contains("身份证")) {
							cell.setCellValue("201|居民身份证");
						} else {
							cell.setCellValue(castValue);
						}
					} else if (value instanceof Number) {
						cell.setCellValue(((Number) value).doubleValue());
					}

				}
				// TODO 处理defaultMap

				Set<String> defaultKeySet = defaultMap.keySet();
				for (String defaultKey : defaultKeySet) {
					Object defaultValue = defaultMap.get(defaultKey);
					Integer index = targetHeadNameIndexMap.get(defaultKey);
					Cell cell = sheetRow.getCell(index);
					PoiExcelUtil.setCallValue(cell, defaultValue);
				}
				beginRow++;
			}

			// 写入回原来的表格
			fos = new FileOutputStream(writeFile);
			workbook.write(fos);
		} catch (Exception e) {
			System.out.println("writeInExcel error");
		} finally {
			IOUtils.close(fos);
			IOUtils.close(fis);
		}


	}

	private static ArrayList<DVRecord> init(String filePath) throws InvalidFormatException, IOException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		FileInputStream fis = new FileInputStream(filePath);
		HSSFWorkbook hWorkbook = (HSSFWorkbook) WorkbookFactory.create(fis);
		HSSFSheet hSheet = hWorkbook.getSheetAt(0); // sheet on which you want to read data validation
		Class c = HSSFSheet.class;
		Field field = c.getDeclaredField("_sheet");
		field.setAccessible(true);
		Object internalSheet = field.get(hSheet);
		InternalSheet is = (InternalSheet) internalSheet;
		DataValidityTable dvTable = is.getOrCreateDataValidityTable();
		Class c2 = DataValidityTable.class;
		Field field2 = c2.getDeclaredField("_validationList");
		field2.setAccessible(true);
		Object records = field2.get(dvTable);
		ArrayList<DVRecord> dvRecords = (ArrayList<DVRecord>) records;
		return dvRecords;
	}
}
