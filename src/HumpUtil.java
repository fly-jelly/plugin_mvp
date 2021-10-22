import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jelly
 * @date 2021/10/21
 * @time 9:50
 * @desc 驼峰命名法
 */

public class HumpUtil {
    //转变的依赖字符
    public static final char UNDERLINE='_';

    /**
     * 将驼峰转换成"_"(userId:user_id)
     * @param param
     * @return
     */

    public static String camelToUnderline(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        int len=param.length();
        StringBuilder sb=new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c=param.charAt(i);
            if (Character.isUpperCase(c) && i == 0) {
                sb.append(Character.toLowerCase(c));
            }else if (Character.isUpperCase(c)){
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String underlineToCamel(String param,Boolean isHeader){
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else if (i == 0 && isHeader) {
                sb.append(Character.toUpperCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    /**
     * 将"_"转成驼峰(user_id:userId)
     * @param param
     * @return
     */
    public static String underlineToCamel(String param) {
        return underlineToCamel(param,false);
    }
    /**
     * 将"_"转成驼峰(user_id:userId)
     * @param param
     * @return
     */
    public static String underlineToCamel2(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        StringBuilder sb = new StringBuilder(param);
        Matcher mc = Pattern.compile(UNDERLINE + "").matcher(param);
        int i = 0;
        while (mc.find()) {
            int position = mc.end() - (i++);
            String.valueOf(Character.toUpperCase(sb.charAt(position)));
            sb.replace(position - 1, position + 1, sb.substring(position, position + 1).toUpperCase());
        }
        return sb.toString();
    }


//    public static void main(String[] args) {
//        System.out.println(underlineToCamel("jellyan_test_file"));
//        System.out.println(underlineToCamel("jellyan_test_file",true));
//        System.out.println(camelToUnderline("JellyanTestFile"));
//    }
}
