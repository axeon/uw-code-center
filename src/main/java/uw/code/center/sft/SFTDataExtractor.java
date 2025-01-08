package uw.code.center.sft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * SFT数据提取器。
 * 适配ali-pai qwen2.5-coder-7b-instruct sft训练集格式。
 */
public class SFTDataExtractor {


    private static final Logger logger = LoggerFactory.getLogger( SFTDataExtractor.class );


    /**
     * 项目代码路径。
     * 需要修改为要提取数据的项目路径。
     */
    private static final String PROJECT_PATH = "/Users/axeon/work-uw/uw-code-center/src";


    /**
     * 输出文件路径。
     */
    private static final String OUTPUT_FILE = "/Users/axeon/work-uw/uw-code-center/sft_training_data.json";

    public static void main(String[] args) {
        // 定义项目路径
        // 创建一个列表来存储数据对
        List<DataPair> dataPairs = new ArrayList<>();

        // 遍历项目中的所有Java文件
        traverseDirectory( new File( PROJECT_PATH ), dataPairs );

        // 输出数据对到文件
        try (FileWriter writer = new FileWriter( OUTPUT_FILE )) {
            writer.write( "[\n" );
            for (int i = 0; i < dataPairs.size(); i++) {
                DataPair pair = dataPairs.get( i );
                writer.write( "  {\n" );
                writer.write( "    \"instruction\": \"" + escapeJson( pair.getInput() ) + "\",\n" );
                writer.write( "    \"output\": \"" + escapeJson( pair.getOutput() ) + "\"\n" );
                writer.write( "  }" );
                if (i < dataPairs.size() - 1) {
                    writer.write( ",\n" );
                }
            }
            writer.write( "\n]" );
        } catch (IOException e) {
            logger.error( e.getMessage(), e );
        }

        // 调试信息：打印提取的数据对数量
        logger.info( "Extracted {} data pairs.", dataPairs.size() );
    }

    /**
     * 递归遍历目录中的所有Java文件
     *
     * @param directory 当前目录
     * @param dataPairs 存储数据对的列表
     */
    private static void traverseDirectory(File directory, List<DataPair> dataPairs) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 如果是目录，递归遍历
                    traverseDirectory( file, dataPairs );
                } else if (file.getName().endsWith( ".java" )) {
                    // 如果是Java文件，提取数据
                    extractDataFromFile( file, dataPairs );
                }
            }
        } else {
            // 调试信息：打印目录为空的情况
            logger.warn( "Directory is empty or not accessible: {}", directory.getAbsolutePath() );
        }
    }

    /**
     * 从Java文件中提取注释和代码
     *
     * @param file      当前Java文件
     * @param dataPairs 存储数据对的列表
     */
    private static void extractDataFromFile(File file, List<DataPair> dataPairs) {
        List<String> lines;
        try {
            lines = Files.readAllLines( Paths.get( file.getAbsolutePath() ) );
        } catch (IOException e) {
            logger.error( e.getMessage(), e );
            return;
        }

        // 用于存储代码的StringBuilder
        StringBuilder codeBuilder = new StringBuilder();
        // 用于存储注释的StringBuilder
        StringBuilder commentBuilder = new StringBuilder();
        // 标记是否在多行注释中
        boolean inComment = false;

        for (String line : lines) {
            if (line.trim().startsWith( "/*" )) {
                line = line.trim();
                addDataPair( dataPairs, commentBuilder, codeBuilder );
                inComment = true;
                commentBuilder.append( line.substring( 2 ).replaceFirst( "\\*", "" ).trim() ).append( "\n" );
            } else if (line.trim().endsWith( "*/" )) {
                line = line.trim();
                inComment = false;
                commentBuilder.append( line.substring( 0, line.trim().length() - 2 ).trim() ).append( "\n" );
            } else if (inComment) {
                line = line.trim();
                commentBuilder.append( line.replaceFirst( "\\*", "" ).trim() ).append( "\n" );
            } else {
                codeBuilder.append( line ).append( "\n" );
            }
        }

        // 处理最后一段代码和注释
        addDataPair( dataPairs, commentBuilder, codeBuilder );
        logger.info( "Processed file: {}", file.getAbsolutePath() );
    }

    private static void addDataPair(List<DataPair> dataPairs, StringBuilder commentBuilder, StringBuilder codeBuilder) {
        if (!commentBuilder.toString().trim().isEmpty() && !codeBuilder.toString().trim().isEmpty()) {
            dataPairs.add( new DataPair( commentBuilder.toString().trim(), codeBuilder.toString().trim() ) );
        }
        codeBuilder.setLength( 0 );
        commentBuilder.setLength( 0 );
    }

    /**
     * 转义JSON字符串。
     *
     * @param str 输入字符串
     * @return 转义后的字符串
     */
    private static String escapeJson(String str) {
        return str.replace( "\\", "\\\\" ).replace( "\"", "\\\"" ).replace( "\b", "\\b" ).replace( "\f", "\\f" ).replace( "\n", "\\n" ).replace( "\r", "\\r" ).replace( "\t",
                "\\t" );
    }

    /**
     * 数据对类，包含输入和输出
     */
    static class DataPair {
        private final String input;
        private final String output;

        /**
         * 构造函数
         *
         * @param input  输入字符串
         * @param output 输出字符串
         */
        public DataPair(String input, String output) {
            this.input = input;
            this.output = output;
        }

        /**
         * 获取输入字符串
         *
         * @return 输入字符串
         */
        public String getInput() {
            return input;
        }

        /**
         * 获取输出字符串
         *
         * @return 输出字符串
         */
        public String getOutput() {
            return output;
        }
    }
}
