package com.fengwuj.testdemo.word;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengwuj.testdemo.utils.ReflectProxyUtil;
import com.fengwuj.testdemo.word.anno.WordBookMark;
import com.fengwuj.testdemo.word.anno.WordTableCell;
import com.fengwuj.testdemo.word.wordmodel.SingleModel;
import com.fengwuj.testdemo.word.wordmodel.TableModel;
import com.fengwuj.testdemo.word.wordmodel.WordStyle;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.w3c.dom.Node;

import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class WordDocxUtil {
    public static final String RUN_NODE_NAME = "w:r";
    public static final String TEXT_NODE_NAME = "w:t";
    public static final String BOOKMARK_START_TAG = "w:bookmarkStart";
    public static final String BOOKMARK_END_TAG = "w:bookmarkEnd";
    public static final String BOOKMARK_ID_ATTR_NAME = "w:id";
    public static final String STYLE_NODE_NAME = "w:rPr";

    private WordDocxUtil() {
        //静态类不要初始化
        throw new IllegalStateException("instantiate not available");
    }

    public static XWPFDocument loadWordDocument(String templatePath){

        if (!(templatePath.endsWith("docx")
                || templatePath.endsWith("DOCX"))){
            throw new RuntimeException("仅支持docx文件解析");
        }
        XWPFDocument document = null;
        try (InputStream is = WordDocxUtil.class.getClassLoader().getResourceAsStream(templatePath)){
            document = new XWPFDocument(is);
        } catch (IOException e) {
            log.error("装载Document失败",e);
            throw new RuntimeException(e);
        }
        return document;
    }

    public static File exportDocumentToFile(XWPFDocument document){
        File file = new File(UUID.randomUUID()+".docx");
        try (OutputStream outputStream = new FileOutputStream(file)){
            document.write(outputStream);
        } catch (FileNotFoundException e) {
            log.error("未找到docx文件",e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("输出docx文件失败",e);
            throw new RuntimeException(e);
        }finally {
            try {
				if(document!=null){
					document.close();
				}
            } catch (IOException e) {
                log.error("关闭文件流失败",e);
            }
        }
        return file;
    }


    public static <T extends SingleModel> XWPFDocument replaceHashedText(XWPFDocument document, T dataModel){

        Map<String,String> dataMap = new HashMap<>();
        Map<String, WordBookMark> styleMap = new HashMap<>();
        //获取书签,建立书签和待替换值的关系
        Field[] fields = dataModel.getClass().getDeclaredFields();
        for (Field field : fields){
            if (field.isAnnotationPresent(WordBookMark.class)){
                WordBookMark wordBookMark = field.getAnnotation(WordBookMark.class);
                String bookMarkKey = wordBookMark.value();
                Object val = ReflectProxyUtil.getValueByPropertyName(dataModel, field.getName());
                String replaceValue = transferValueToString(field,val);
                dataMap.put(bookMarkKey,replaceValue);
                styleMap.put(bookMarkKey,wordBookMark);
            }
        }

        //加载docx文件数据
        try {
            //读取段落
            List<XWPFParagraph> paragraphList =  document.getParagraphs();
            for(XWPFParagraph xwpfParagraph:paragraphList){
                CTP ctp = xwpfParagraph.getCTP();

                //读取每个段落的书签
                for(int dwI = 0;dwI < ctp.sizeOfBookmarkStartArray();dwI++){
                    CTBookmark bookmark = ctp.getBookmarkStartArray(dwI);
                    processBookmark(dataMap, styleMap, xwpfParagraph, ctp, bookmark);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return document;
    }

    private static void processBookmark(Map<String, String> dataMap, Map<String, WordBookMark> styleMap, XWPFParagraph xwpfParagraph, CTP ctp, CTBookmark bookmark) {
        if(dataMap.containsKey(bookmark.getName())){

            XWPFRun run = xwpfParagraph.createRun();
            run.setText(dataMap.get(bookmark.getName()));
            if (styleMap.get(bookmark.getName()).fontSize() != -1){
                run.setFontSize(styleMap.get(bookmark.getName()).fontSize());
            }
            Node firstNode = bookmark.getDomNode();
            Node nextNode = firstNode.getNextSibling();
            while(nextNode != null){
                // 循环查找结束符
                String nodeName = nextNode.getNodeName();
                if(nodeName.equals(BOOKMARK_END_TAG)){
                    break;
                }

                // 删除中间的非结束节点，即删除原书签内容
                Node delNode = nextNode;
                nextNode = nextNode.getNextSibling();

                ctp.getDomNode().removeChild(delNode);
            }

            if(nextNode == null){
                // 始终找不到结束标识的，就在书签前面添加
                ctp.getDomNode().insertBefore(run.getCTR().getDomNode(),firstNode);
            }else{
                // 找到结束符，将新内容添加到结束符之前，即内容写入bookmark中间
                ctp.getDomNode().insertBefore(run.getCTR().getDomNode(),nextNode);
            }
        }
    }

    private static String transferValueToString(Field field, Object val) {
        String result = "";
        if (Objects.isNull(val)){
            return result;
        }
        if (field.getType() == Date.class){
            Date date = (Date) val;
            String pattern = "yyyy-MM-dd HH:mm:ss";
            if (field.isAnnotationPresent(JsonFormat.class)){
                JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
                if (null != jsonFormat.pattern() && jsonFormat.pattern().length() > 0){
                    pattern = jsonFormat.pattern();
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            result = sdf.format(date);
        }else if (field.getType() == String.class && val.getClass() == String.class){
            result = (String) val;
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                result = objectMapper.writeValueAsString(val);
            } catch (JsonProcessingException e) {
                log.error("object转json异常",e);
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static XWPFDocument clearAndAppendTableRows(XWPFDocument document, List<List<? extends TableModel>> nTableDatas){

        //加载docx文件数据
        List<XWPFTable> tables = document.getTables();
        for (int i = 0; i < tables.size(); i++) {
            XWPFTable table = tables.get(i);

            //删除多余行
            int numberOfRows = table.getNumberOfRows();
            try {
                while (numberOfRows > 1){
                    table.removeRow(numberOfRows-1);
                    numberOfRows -= 1;
                }
            }catch (Exception e){
                log.error("删除第{}行失败",numberOfRows-1);
            }

            List<? extends TableModel> tableData = nTableDatas.get(i);
            insertRows(table,tableData);
        }

        return document;
    }

    /**
     * 将tableModel数据写入到表格
     * */
    public static void insertRows(XWPFTable table, List<? extends TableModel> tableModels) {
        // 在表格中指定的位置新增一行
        XWPFTableRow row = table.getRows().get(0);
        for (TableModel t : tableModels){
            CTRow ctrow = null;
            try {
                ctrow = CTRow.Factory.parse(row.getCtRow().newInputStream());//重点行
            } catch (Exception e) {
                log.error("构建新行样式失败");
                throw new RuntimeException(e);
            }

            //创建新行
            XWPFTableRow newRow = new XWPFTableRow(ctrow, table);
            //填充一行
            for (Field field : t.getClass().getDeclaredFields()){
                //判断注解是否正常
                if (!field.isAnnotationPresent(WordTableCell.class)){
                    log.error("属性:{}未添加注解",field.getName());
                    throw new RuntimeException("属性:"+field.getName()+"+未添加注解");
                }

                //填充cell数据
                WordTableCell wordTableCell = field.getAnnotation(WordTableCell.class);
                Object val = ReflectProxyUtil.getValueByPropertyName(t, field.getName());
                String replaceValue = transferValueToString(field,val);
                // 在新增的行上面创建cell
                XWPFTableCell cell = newRow.getTableCells().get(wordTableCell.cellIndex());
                //替换文本
                WordStyle wordStyle = new WordStyle();
                if (wordTableCell.fontSize() != -1){
                    wordStyle.setFontSize(wordTableCell.fontSize());
                }
                replaceCellValue(cell,replaceValue,wordStyle);
            }
            table.addRow(newRow);
        }

    }

    private static void replaceCellValue(XWPFTableCell cell, String replaceValue,WordStyle wordStyle) {
        for (int i = 0; i < cell.getParagraphs().size(); i++) {
            cell.removeParagraph(i);
        }
        //单个
        if (replaceValue.indexOf("\n") == -1){
            XWPFParagraph newPara = new XWPFParagraph(cell.getCTTc().addNewP(), cell);
            XWPFRun run = newPara.createRun();

            if (wordStyle.getFontSize() != null){
                run.setFontSize(wordStyle.getFontSize());
            }

            run.setText(replaceValue);
            cell.setParagraph(newPara);
        }else {
            //多段落
            String[] split = replaceValue.split("\n");
            XWPFParagraph newPara = new XWPFParagraph(cell.getCTTc().addNewP(), cell);
            for (String s : split) {
                XWPFRun run = newPara.createRun();//对某个段落设置格式

                if (wordStyle.getFontSize() != null){
                    run.setFontSize(wordStyle.getFontSize());
                }

                run.setText(s.trim());
                run.addBreak();//换行
            }
        }

    }

    public static void replaceTableValue(XWPFTable table, TableModel tableModel){
        Field[] fields = tableModel.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(WordTableCell.class)){
                continue;
            }

            WordTableCell wordTableCell = field.getAnnotation(WordTableCell.class);
            int rowIndex = wordTableCell.rowIndex();
            int cellIndex = wordTableCell.cellIndex();
            //替换变量
            if (field.getType() == List.class){
                //处理Table中的小table
                List<Object> list = (List<Object>) ReflectProxyUtil.getValueByPropertyName(tableModel, field.getName());
                for (Object o : list) {
                    Field[] innerFs = o.getClass().getDeclaredFields();
                    processField(table, wordTableCell, rowIndex, o, innerFs);
                    rowIndex += 1;
                }
            }else {
                //处理Table中的数据替换
                WordStyle wordStyle = new WordStyle();
                if (wordTableCell.fontSize() != -1){
                    wordStyle.setFontSize(wordTableCell.fontSize());
                }
                String replaceV = transferValueToString(field, ReflectProxyUtil.getValueByPropertyName(tableModel,field.getName()));
                XWPFTableCell cell =  table.getRow(rowIndex).getCell(cellIndex);
                if (cell == null){
                    continue;
                }
                replaceCellValue(cell,replaceV,wordStyle);
            }
        }
    }

    private static void processField(XWPFTable table, WordTableCell wordTableCell, int rowIndex, Object o, Field[] innerFs) {
        for (Field innerF : innerFs) {
            if (!innerF.isAnnotationPresent(WordTableCell.class)){
                continue;
            }
            WordTableCell innerWordTableCell = innerF.getAnnotation(WordTableCell.class);
            String replaceV = transferValueToString(innerF, ReflectProxyUtil.getValueByPropertyName(o,innerF.getName()));
            WordStyle wordStyle = new WordStyle();
            if (wordTableCell.fontSize() != -1){
                wordStyle.setFontSize(wordTableCell.fontSize());
            }
            XWPFTableCell cell =  table.getRow(rowIndex).getCell(innerWordTableCell.cellIndex());
            if (cell == null){
                break;
            }
            replaceCellValue(cell,replaceV,wordStyle);
        }
    }
}
