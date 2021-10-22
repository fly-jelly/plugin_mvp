import com.android.aapt.Resources;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.messages.MessageDialog;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryImpl;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiUtil;
import com.intellij.ui.JBColor;
import com.jetbrains.rd.util.string.StingUtilKt;
import kotlin.text.Charsets;
import org.apache.commons.compress.utils.IOUtils;
import org.codehaus.groovy.runtime.StringGroovyMethods;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.io.ByteArrayInStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sun.nio.ch.IOUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.function.Predicate;

/**
 * @author jelly
 * @date 2021/10/18
 * @time 15:07
 * @desc
 */

public class MenuAction extends AnAction {

    //基类目录
    private String mBasePath;
    private Project mProject;
    @Override
    public void actionPerformed(AnActionEvent e) {
        //获取当前项目
        mProject = e.getProject();

//        Editor editor = e.getData(CommonDataKeys.EDITOR);

//        String txt = editor.getDocument().getText();
//        PsiFile file = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
        VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE);

        //获取当前的编辑器对象
//        Editor editor = e.getData(PlatformDataKeys.EDITOR);
//        //获取当前编辑的文件
//        PsiFile editorFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
//
//        buffer.append("\n ---------------- VirtualFile -----------------------");
//        buffer.append("\n editorFile : ").append(editorFile.getVirtualFile().getPath());
//        if (file.getFileType().getName().equals("UNKNOWN")) {
        String currEditPath = file.getPath();
        if (currEditPath.contains("main/java")) {
            mBasePath = currEditPath.substring(0, file.getPath().indexOf("main/java") + 4);
            String pack = parseManifest(mBasePath);
            showInputDialog(pack);
        }
//        MessageDialogBuilder.yesNo("请输入账号", file+"\n"+file.getPath() + " -> " + file.getFileType().getName()).show();
//            MessageDialogBuilder.yesNo("请输入账号", base+"\n"+pack+"\n"+file.getPath() + " -> " + file.getFileType().getName()).show();
//        }
        /*StringBuffer buffer = new StringBuffer();
        //获取当前的编辑器对象
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        //获取当前编辑的文件
        PsiFile editorFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());

        buffer.append("\n ---------------- VirtualFile -----------------------");
        buffer.append("\n editorFile : ").append(editorFile.getVirtualFile().getPath());

        //
        VirtualFile vFile = CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());

        buffer.append("---------------- project -----------------------");
        String base = project.getBasePath();
        buffer.append("\nbase : ").append(base);
        String pfp = project.getProjectFilePath();
        buffer.append("\nFilePath : ").append(pfp);
        String pu = project.getPresentableUrl();
        buffer.append("\nPresentableUrl : ").append(pu);

        buffer.append("\n ---------------- VirtualFile -----------------------");

        buffer.append("\n VirtualFile : ").append(vFile.getPath());
        String cp = vFile.getCanonicalPath();
        buffer.append("\n CanonicalPath : ").append(cp);
        String exten = vFile.getExtension();
        buffer.append("\n Extension : ").append(exten);

        PsiFile file = e.getData(CommonDataKeys.PSI_FILE);
        buffer.append("\n PsiFile : ").append(file.getParent().getVirtualFile().getPath());

//        PsiClass mainfest =  JavaPsiFacade.getInstance(project).findClass("AndroidManifest.xml", GlobalSearchScope.allScope(project));

//        PsiManager.getInstance(project).findFile(VirtualFile.PROP_NAME.equals("AndroidManifest.xml"));
        File model = new File(file.getParent().getVirtualFile().getPath() , "model");
        if (!model.exists()){
            model.mkdirs();
        }

//        buffer.append("\n ---------------  AllFilenames ------------------- ");
//        String strs[] = Arrays.stream(FilenameIndex.getAllFilenames(project)).filter(s -> s.endsWith(".java") || s.endsWith(".xml")).toArray(String[]::new);
//
//        for (String str : strs){
//            buffer.append("\n ").append(str);
//        }

        buffer.append("\n ---------------  FilenameIndex ------------------- ");
        PsiFile psiFiles[] = Arrays.stream(FilenameIndex.getFilesByName(project,"AndroidManifest.xml",GlobalSearchScope.allScope(project))).filter(p->p.getVirtualFile().getPath().startsWith(project.getBasePath())).toArray(PsiFile[]::new);
        for (PsiFile pf : psiFiles){
            buffer.append("\n "+ pf.getName()).append(pf.getVirtualFile().getPath());
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(project.getProjectFile().getPath()+"");
            NodeList nodeList = doc.getElementsByTagName("manifest");
            for (int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                Element element = (Element) node;
                String package_name = element.getAttribute("package");
            }

        } catch (ParserConfigurationException | SAXException | IOException parserConfigurationException) {
            parserConfigurationException.printStackTrace();
        }*/




//        PsiElement element = e.getData(CommonDataKeys.PSI_ELEMENT);
//        buffer.append("\n PsiElement : ").append(element.getManager().toString());

//        PsiManager.getInstance(project).findFile();
//        PsiDocumentManager.getInstance(project).getPsiFile();
//        PsiElement.getContainingFile()

//        String pk = ((PsiJavaFile)vFile.getCanonicalFile()).getPackageName();
//        buffer.append("\n PackageName : ").append(pk);
        // 获取JavaFile中的Class：

//        if (editorFile != null) {
//            buffer.append("\n PsiFile : " + (editorFile == null ? "true" : "false"));
//            if (editorFile instanceof PsiJavaFile) {
//                PsiJavaFile javaFile = (PsiJavaFile) editorFile;
//                // 一个文件中可能会定义有多个Class，因此返回的是一个数组
//                PsiClass[] classes = javaFile.getClasses();
                // PsiClass的常用方法：
                //
                // 获取所有属性：getFields、getAllFields
                // 查找属性：findFieldByName()，其中第二个参数是是否查找父类的属性；
                // 获取所有方法：getMethods/getAllMethods
                // 查找方法：findMethodsByName
                // Import某个类：由于引入类操作是在File中进行的，因此在Class类上是没有办法导入的，如果已经获取到了javaFile对象，那么可以这样导入，否则需要先获取到Class所在的File后再进行导入：

                // javaFile.importClass(classes[0]);
                // 根据文件名查找路径
                //  FilenameIndex.getFilesByName();

                // 根据class获取所在文件：
                // (PsiJavaFile) classes[0].getContainingFile();

                //  获取类所在包：

                // 先获取到文件后再获取文件所在包


//                buffer.append("\n ---------------- File -----------------------");
//                PsiElement element = javaFile.getContext();
//                if (javaFile != null) {
//                    String pk = ((PsiJavaFile) classes[0].getContainingFile()).getPackageName();
//                    buffer.append("\nPackageName : ").append(pk);
//
//                }
//            }
//        }

//        PsiJavaFile javaFile = editorFile.getFileType().getName();

        //获取项目的包名

//        MessageDialogBuilder.yesNo("请输入账号",buffer.toString()).show();
//        MessageDialogBuilder.yesNo("请输入账号",project.getBasePath()).show();

//        showPopupBalloon(editor,"Plugin Demo",5);
    }

    private void showInputDialog(String pack) {
        MvpControllerDialog dialog = new MvpControllerDialog(pack, new MvpControllerDialog.OnOwnerInputListener() {
            @Override
            public void submit(String activity, String act_layout, String pack) {
                String jpath = mBasePath+"/java";
                createActivity(jpath,pack ,activity,act_layout);
                createIview(jpath,pack,activity);
                createModule(jpath,pack,activity);
                createPresenter(jpath,pack,activity);
                createLayout(mBasePath+"/res/layout",act_layout);
                VirtualFileManager.getInstance().syncRefresh();
//                MessageDialogBuilder.yesNo("提示","创建完成").yesText("确定").show();
//                DialogBuilder builder = new DialogBuilder();
//                builder.setTitle("提示");
//                MessageDialog message = new MessageDialog("创建成功","提示",new String[]{"确定"},MessageDialog.OK_EXIT_CODE,null);
//                message.show();
                Messages.showMessageDialog("创建成功","提示",Messages.getInformationIcon());
            }
        });
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void createActivity(String path, String pack, String actName,String layid) {
        File dirs = createDirs(path+ "/" +pack.replace(".","/") + "/ui/activities");
        String content = readContent("activity.template");
        content = content.replaceAll("#PACKAGE_NAME#",pack);
        content = content.replaceAll("#ACTIVITY_CLASS_NAME#",actName);
        content = content.replaceAll("#LAYOUT_ID_NAME#",layid);

//        MessageDialogBuilder.yesNo("test",content).show();
        writeFile(dirs,actName+"Activity.java",content);
    }


    /**
     * 创建IView类
     * @param path 模块路径
     * @param pack 包名
     * @param actName 类名
     */
    private void createIview(String path,String pack,String actName){
        File dirs = createDirs(path+ "/" +pack.replace(".","/") + "/iviews");
        String content = readContent("iview.template");
        content = content.replaceAll("#PACKAGE_NAME#",pack);
        content = content.replaceAll("#ACTIVITY_CLASS_NAME#",actName);
        writeFile(dirs,"I"+actName+"View.java",content);
    }

    /**
     * 创建module类
     * @param path 模块路径
     * @param pack 包名
     * @param actName 类名
     */
    private void createModule(String path,String pack,String actName){
        File dirs = createDirs(path+ "/" +pack.replace(".","/") + "/module");
        String content = readContent("module.template");
        content = content.replaceAll("#PACKAGE_NAME#",pack);
        content = content.replaceAll("#ACTIVITY_CLASS_NAME#",actName);
        writeFile(dirs,actName+"Module.java",content);
    }


    /**
     * 创建Presenter类
     * @param path 模块路径
     * @param pack 包名
     * @param actName 类名
     */
    private void createPresenter(String path,String pack,String actName){
        File dirs = createDirs(path+ "/" +pack.replace(".","/") + "/presenter");
        String content = readContent("presenter.template");
        content = content.replaceAll("#PACKAGE_NAME#",pack);
        content = content.replaceAll("#ACTIVITY_CLASS_NAME#",actName);
        writeFile(dirs,actName+"Presenter.java",content);
    }


    private String parseManifest(String base) {
        File manifest = new File(base,"AndroidManifest.xml");

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(manifest);

            NodeList nodes = document.getElementsByTagName("manifest");
            if (nodes.item(0)!=null){
                Element element = (Element) nodes.item(0);
                return element.getAttribute("package");
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createLayout(String layoutPath,String layName){
        File dirs = createDirs(layoutPath);
        String content = readContent("layout.template");
        writeFile(dirs,layName+".xml",content);
    }

    private void writeFile(File dirs, String name, String content) {
        File dest = new File(dirs,name);
        try {
            PrintWriter writer = new PrintWriter(dest);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private String readContent(String tname){
        InputStream is = getClass().getResourceAsStream("/templates/" + tname);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte [] temp = new byte[1024];
        int len = -1;
        try {
            while (((len = is.read(temp)) != -1)) {
                baos.write(temp,0,len);
            }
            byte[] contents = baos.toByteArray();

            return new String(contents, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                baos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private File createDirs(String dirs) {
        File file = new File(dirs);
        if (!file.exists()){
            file.mkdirs();
        }
        return file;

    }
}
