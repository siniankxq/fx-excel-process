package com.ypc.fx.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lxj
 * 读取excel
 */
public class ReadExcelUtil {
	//总行数
	private int totalRows = 0;
	//总条数
	private int totalCells = 0;
	//错误信息接收器
	private String errorMsg;

	//构造方法
	public ReadExcelUtil() {
	}

	//获取总行数
	public int getTotalRows() {
		return totalRows;
	}

	//获取总列数
	public int getTotalCells() {
		return totalCells;
	}

	//获取错误信息
	public String getErrorInfo() {
		return errorMsg;
	}

	/**
	 * 验证EXCEL文件
	 *
	 * @param filePath
	 * @return
	 */
	public boolean validateExcel(String filePath) {
		if (filePath == null || !(WDWUtil.isExcel2003(filePath) || WDWUtil.isExcel2007(filePath))) {
			errorMsg = "文件名不是excel格式";
			return false;
		}
		return true;
	}

    public static void main(String[] args) {
        ReadExcelUtil readExcelUtil = new ReadExcelUtil();
        File file = new File("C:\\Users\\sinia\\Desktop\\备份\\source.xls");
        List<Map<String, Object>> excelInfo = readExcelUtil.getExcelInfo(file, 0);
        System.out.println(excelInfo);
    }
	/**
	 * 读EXCEL文件，获取Excel信息集合
	 *
	 * @param fileName
	 * @param Mfile
	 * @param sheetNo  读取第几个sheet
	 * @return
	 */
	public List<Map<String, Object>> getExcelInfo(File readFile,  int sheetNo) {



		//初始化客户信息的集合
		List<Map<String, Object>> pds = new ArrayList<Map<String, Object>>();
		//初始化输入流
		InputStream is = null;
		try {
            String name = readFile.getName();
            //根据文件名判断文件是2003版本还是2007版本
			boolean isExcel2003 = true;
			if (WDWUtil.isExcel2007(name)) {
				isExcel2003 = false;
			}
			//根据新建的文件实例化输入流
			is = new FileInputStream(readFile);
			//根据excel里面的内容读取信息
			pds = getExcelInfo(is, isExcel2003, sheetNo);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					is = null;
					e.printStackTrace();
				}
			}
		}
		return pds;
	}

	/**
	 * 根据excel里面的内容读取客户信息
	 *
	 * @param is          输入流
	 * @param isExcel2003 excel是2003还是2007版本
	 * @return
	 * @throws IOException
	 */
	public List<Map<String, Object>> getExcelInfo(InputStream is, boolean isExcel2003, int sheetNo) {
		List<Map<String, Object>> pds = null;
		try {
			/** 根据版本选择创建Workbook的方式 */
			Workbook wb = null;
			//当excel是2003时
			if (isExcel2003) {
				wb = new HSSFWorkbook(is);
			} else {//当excel是2007时
				wb = new XSSFWorkbook(is);
			}
			//读取Excel里面的信息
			pds = readExcelValue(wb, sheetNo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pds;
	}


	/**
	 * 读取Excel里面的信息
	 *
	 * @param wb
	 * @return
	 */
	private List<Map<String, Object>> readExcelValue(Workbook wb, int sheetNo) {
		//得到第几个shell
		Sheet sheet = wb.getSheetAt(sheetNo );

		//得到Excel的行数
		this.totalRows = sheet.getPhysicalNumberOfRows();

		//得到Excel的列数(前提是有行数)
		if (totalRows >= 1 && sheet.getRow(0) != null) {
			this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
		}
		//第一行的数据
		ArrayList<String> keys = new ArrayList<>();
		//获取第一行的数据
		Row row1 = sheet.getRow(0);
		for (int i = 0; i < totalCells; i++) {
			Cell cell = row1.getCell(i);
			String value = cell.getStringCellValue();
			keys.add(value);
		}
		List<Map<String, Object>> pds = new ArrayList<Map<String, Object>>();
		Map<String, Object> pd;
		//循环Excel行数,从第二行开始。标题不入库
		for (int r = 1; r < totalRows; r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
                continue;
            }
			pd = new HashMap<>();

			//循环Excel的列
			for (int c = 0; c < this.totalCells; c++) {

				Cell cell = row.getCell(c);
				//将列的信息添加到pageData
				pd.put(keys.get(c), PoiExcelUtil.getCallValue(cell));
			}
			//添加客户
			pds.add(pd);
		}
		return pds;
	}

//	/**
//	 * 读入excel的内容转换成字符串
//	 *
//	 * @param cell
//	 * @return
//	 */
//	private String getStringValueFromCell(Cell cell) {
//		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy/MM/dd");
//		DecimalFormat decimalFormat = new DecimalFormat("#.#");
//		String cellValue = "";
//		if (cell == null) {
//			return cellValue;
//		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
//			cellValue = cell.getStringCellValue();
//		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
//			if (HSSFDateUtil.isCellDateFormatted(cell)) {
//				double d = cell.getNumericCellValue();
//				Date date = HSSFDateUtil.getJavaDate(d);
//				cellValue = sFormat.format(date);
//			} else {
//				cellValue = decimalFormat.format((cell.getNumericCellValue()));
//			}
//		} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
//			cellValue = "";
//		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
//			cellValue = String.valueOf(cell.getBooleanCellValue());
//		} else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
//			cellValue = "";
//		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
//			cellValue = cell.getCellFormula().toString();
//		}
//		return cellValue;
//	}

}

class WDWUtil {

	// @描述：是否是2003的excel，返回true是2003
	public static boolean isExcel2003(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}

	//@描述：是否是2007的excel，返回true是2007
	public static boolean isExcel2007(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}

}
