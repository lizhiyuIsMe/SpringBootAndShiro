package jvm.hotimport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//查看文件最后修改时间,如果时间变了则重新加载class
public class HotClassExecute implements Runnable {

    public static Map<String, Long> map = new HashMap<String, Long>();
    public static String projectPath="";
    public static String targetPath = "";
    public static String comPath = "";
    public static MyHotClassLoader myHotClassLoader;

    public HotClassExecute() throws IOException {
        File file = new File("");
        this.projectPath=file.getCanonicalPath();
        this.targetPath =projectPath+File.separator +"src"+File.separator +"main"+File.separator+"java"+File.separator;
        this.myHotClassLoader=new MyHotClassLoader(projectPath+File.separator +"src"+File.separator +"main"+File.separator+"java");
    }

    @Override
    public void run() {
        while (true) {
            File parent = new File(this.targetPath);
            try {
                execute(parent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void execute(File parent) throws ClassNotFoundException {
        if (parent.isDirectory()) {
            File[] files = parent.listFiles();
            for (File file : files) {
                execute(file);
            }
        } else {
            //获得文件的修改时间
            long lastModifiedTime = parent.lastModified();
            String path = parent.getPath();
            String absPath = path.replace(projectPath+File.separator +"src"+File.separator +"main"+File.separator+"java"+File.separator, "");
            absPath= absPath.replace(File.separator, ".");
            if (absPath.endsWith(".java")) {
                absPath = absPath.replace(".java", "");
                boolean flag = map.containsKey(absPath);
                System.out.println(lastModifiedTime);
                if(flag && absPath.endsWith("People")){
                    System.out.println(lastModifiedTime);
                    Long aLong = map.get(absPath);
                    if(!aLong.equals(lastModifiedTime)){
                        myHotClassLoader.loadClass(absPath);
                        map.put(absPath,lastModifiedTime);
                    }
                }else{
                    myHotClassLoader.loadClass(absPath);
                    map.put(absPath,lastModifiedTime);
                }
            }

            //如果不包含什么都不做,类加载器按需加载节省空间
        }
    }

}
