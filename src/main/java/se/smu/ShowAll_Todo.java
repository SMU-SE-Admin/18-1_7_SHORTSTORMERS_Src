package se.smu;

import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import java.io.File;
import java.io.FileInputStream;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ShowAll_Todo extends JFrame {
	String Todo,Done,Sub,VALUE;
	String Dead_day,Actual_day;
	XSSFRow row;
	XSSFCell cell;
    Boolean Is_import;
    File file;

    ToDoList_Main win;
    
    //////////맴버 필드
    
    private JLabel Title_Label;
    private JTable tableArea;
    private JScrollPane scrollArea;
    
    //////////UI맴버 필드
    
    public ShowAll_Todo(ToDoList_Main win) {//생성자매소드
    	this.win = win;
    	setSize(750,650);
    	    
    setLayout(null);
    	
    	JPanel Label = new JPanel();
    	JPanel Table = new JPanel();
    	
    	////////////////////////////////////////////////////////////Title Label
    	Title_Label=new JLabel("전체 TO do LIST");
    	Title_Label.setFont(new Font("HY견고딕",Font.BOLD,30));
    	Title_Label.setForeground(new Color(0,32,96));
    	Label.add(Title_Label);
    	add(Label);
    	Label.setBounds(220,70,300,50);
    	
    	////////////////////////////////////////////////////////////TableArea
    	String ColumnNames[] = {"과목","TO do","마감 기한","실제 마감일","완료 여부"};
    	
    	
    	DefaultTableModel model = new DefaultTableModel(ColumnNames,0) { // table 
    		 public boolean isCellEditable(int rowIndex, int mColIndex) {
    	        	return false;
    	        }
    	};
    	tableArea = new JTable(model);
    	scrollArea = new JScrollPane(tableArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); //占쏙옙크占쏙옙 占쌩곤옙
    	
    	
    	scrollArea.setPreferredSize(new Dimension(650,400)); // table 크기설정
    	Table.setBounds(0,150,735,500); 
    	
        tableArea.getTableHeader().setReorderingAllowed(false); // table 속성들 이동 금지
        
        tableArea.setAutoCreateRowSorter(true); // 컴럼 정렬하기 기능
        
        JTableHeader header = tableArea.getTableHeader(); // 헤더 색상 지정
        header.setBackground(new Color(0,32,96));
        header.setForeground(Color.white); 
        
        header.setFont(new Font("맑은고딕",Font.BOLD,15));
        tableArea.setFont(new Font("맑은고딕",Font.PLAIN,15)); //헤더와 각 셀 폰트와 크기설정
        
        header.setPreferredSize(new Dimension(100,30)); // 헤더높이조절
        tableArea.setRowHeight(30); // 셀높이조절
        
        tableArea.getColumnModel().getColumn(0).setPreferredWidth(100); // 셀너비조절
        tableArea.getColumnModel().getColumn(1).setPreferredWidth(150);
        tableArea.getColumnModel().getColumn(2).setPreferredWidth(50);
        tableArea.getColumnModel().getColumn(3).setPreferredWidth(50);
        tableArea.getColumnModel().getColumn(4).setPreferredWidth(50); 
        
       
        
        DefaultTableCellRenderer CR = new DefaultTableCellRenderer(); // 셀 가운데 정렬
        CR.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel CM = tableArea.getColumnModel();
        for(int i =0; i<CM.getColumnCount(); i++) {
        	CM.getColumn(i).setCellRenderer(CR);
        }
        
        Table.add(scrollArea);
        add(Table);
    	
        /////////////////////////////////////////////////////////////////엑셀파일입출력
		String folder="./Subject_Dir/ToDolist_Dir/"; //읽을 폴더명 세팅
		String file_name; // 읽을 파일이름
		File file=new File(folder);
		
		if(!file.isDirectory()) {
			System.out.println("해당디렉토리가 없습니다");
			System.exit(1);
		}
		
		File []list=file.listFiles();
		
		for(File f:list) {
			
			if(f.isFile() && !f.getName().equals("Trashcan.xlsx")) { // 휴지통은 제외
				
				file_name=f.getName();
			
				System.out.println(folder + file_name);
		        try {

					FileInputStream inputStream = new FileInputStream(folder + file_name);

					XSSFWorkbook wb = new XSSFWorkbook(inputStream);
					

					//sheet수 취득

					int sheetCn = wb.getNumberOfSheets();

					

					

					for(int cn = 0; cn < sheetCn; cn++){


						//0번째 sheet 정보 취득

						XSSFSheet sheet = wb.getSheetAt(cn);

						

						//취득한 sheet에서 rows수 취득

						int rows = sheet.getPhysicalNumberOfRows();

						

						//취득한 row에서 취득대상 cell수 취득

						int cells = sheet.getRow(cn).getPhysicalNumberOfCells(); 
					
						

						for (int r = 1; r < rows; r++) { // 엑셀파일 두번째 줄부터 출력

							row = sheet.getRow(r); // row 가져오기

							if (row != null) {

								for (int c = 0; c < cells; c++) {

									cell = row.getCell(c);

									if (cell != null) {

										String value = null;

										switch (cell.getCellType()) {

										case XSSFCell.CELL_TYPE_FORMULA:

											value = cell.getCellFormula();

											break;

										case XSSFCell.CELL_TYPE_NUMERIC:
											if(!HSSFDateUtil.isCellDateFormatted(cell)) {
												SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
												value = "" + formatter.format(cell.getDateCellValue());
												
											}else {
												DecimalFormat df = new DecimalFormat();
												value = "" + df.format(cell.getNumericCellValue());
												
											}
											break;

										case XSSFCell.CELL_TYPE_STRING:

											value = "" + cell.getStringCellValue();

											break;

										case XSSFCell.CELL_TYPE_BLANK:

											value = "[null 아닌 공백]";

											break;

										case XSSFCell.CELL_TYPE_ERROR:

											value = "" + cell.getErrorCellValue();

											break;

										default:

										}

										System.out.print(value + "\t");
										VALUE = value;

									} else {

										System.out.print("[null]\t");
										VALUE = null;
									}
									
									switch(c) {
									
									case 0 : Sub=VALUE;
									break;
									case 1 : Todo=VALUE;
									break;
									case 2 : Dead_day=VALUE;
									break;
									case 3 : Actual_day=VALUE;
									break;
									case 4 : Done = VALUE;
									model.addRow(new Object[] {Sub,Todo,Dead_day,Actual_day,Done});
									
									break;
									}

								} // for(c) 문

								
								System.out.print("\n");

							}

						} // for(r) 문

					}

				} catch (Exception e) {

					e.printStackTrace();

				}
			} // if(파일일 경우)
		} // for(f)

		setVisible(false);	
    

        this.addWindowListener(new WindowAdapter(){ //도구차의 x표시누른후 메인페이지 이동
            public void windowClosing(WindowEvent e) { 
            	Back test = new Back();
            	test.back();
            	
            }
        });
    }
}

